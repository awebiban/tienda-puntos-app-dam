import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { LoyaltyCard } from '../../models/LoyaltyCard';
import { Store } from '../../models/Store';
import { AuthService } from '../../services/auth.service';
import { LoyaltycardsService } from '../../services/loyaltycards.service';
import { StoresService } from '../../services/stores.service';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [RouterLink, CommonModule],
  templateUrl: './landing.component.html',
  styleUrl: './landing.component.scss'
})
export class LandingComponent implements OnInit {

  @ViewChild('cardContainer') cardContainer!: ElementRef;

  isLoadingStores: boolean = true;

  userName: string | null = null;
  currentUserId: number | null = null;

  loyaltyCards: LoyaltyCard[] = [];
  stores: Store[] = [];

  constructor(
    private loyaltyCardService: LoyaltycardsService,
    private storesService: StoresService,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadStores();

    if (this.authService.isLoggedIn()) {
      const storedId = localStorage.getItem('userId');
      const storedName = localStorage.getItem('userName');

      if (storedId) {
        this.currentUserId = Number(storedId);
        this.userName = storedName;
        this.loadMyCards();
      }
    }
  }

  loadStores(): void {
    this.isLoadingStores = true;

    this.storesService.getAllStores().subscribe({
      next: (data: any[]) => {
        this.stores = data;
        this.isLoadingStores = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        this.isLoadingStores = false;
      }
    });
  }

  loadMyCards(): void {
    if (!this.currentUserId) return;

    this.loyaltyCardService.getAllLoyaltyCardsByUserId(this.currentUserId).subscribe({
      next: (data: any) => {
        this.loyaltyCards = data;
        this.cdr.detectChanges();
      },
      error: (err: any) => console.error(err)
    });
  }

  joinStore(storeId?: number): void {
    if (!this.currentUserId) {
      this.router.navigate(['/login']);
      return;
    }

    if (!storeId) {
      return;
    }

    if (this.loyaltyCards.some(card => card.storeDTO.id === storeId)) {
      alert('Ya tienes la tarjeta de esta tienda. ¡Explora tus puntos en el dashboard!');
      return;
    }

    this.loyaltyCardService.joinStore(this.currentUserId, storeId).subscribe({
      next: () => {
        this.router.navigate(['/customer/dashboard']);
      },
      error: (err) => {
        alert('Hubo un error o ya tienes la tarjeta de esta tienda.');
      }
    });
  }

  scrollCards(direction: number): void {
    if (this.cardContainer) {
      const scrollAmount = 320 * direction;
      this.cardContainer.nativeElement.scrollBy({
        left: scrollAmount,
        behavior: 'smooth'
      });
    }
  }

  selectCard(card: LoyaltyCard) {
    let trimedName = card.storeDTO.name?.trim().toLowerCase().replace(/\s+/g, '-');
    this.router.navigate(['/customer/store', trimedName], { queryParams: { cardId: card.id } });
  }
}

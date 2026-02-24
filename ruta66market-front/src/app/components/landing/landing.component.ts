import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { RouterLink, Router } from '@angular/router';
import { LoyaltyCard } from '../../models/LoyaltyCard';
import { Store } from '../../models/Store';
import { LoyaltycardsService } from '../../services/loyaltycards.service';
import { StoresService } from '../../services/stores.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './landing.component.html',
  styleUrl: './landing.component.scss'
})
export class LandingComponent implements OnInit {

  @ViewChild('cardContainer') cardContainer!: ElementRef;

  userName: string | null = null;
  currentUserId: number | null = null;

  loyaltyCards: LoyaltyCard[] = [];
  stores: Store[] = [];

  constructor(
    private loyaltyCardService: LoyaltycardsService,
    private storesService: StoresService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadStores();

    // Comprobamos si el usuario está logueado leyendo la memoria
    if (this.authService.isLoggedIn()) {
      const storedId = localStorage.getItem('userId');
      const storedName = localStorage.getItem('userName');

      if (storedId) {
        this.currentUserId = Number(storedId);
        this.userName = storedName;
        this.loadMyCards(); // Cargamos las tarjetas para el carrusel
      }
    }
  }

  loadStores(): void {
    this.storesService.getAllStores().subscribe({
      next: (data: any[]) => {
        this.stores = data.filter(store => store.isVisible);
      },
      error: (error) => console.error('Error al cargar las tiendas:', error)
    });
  }

  loadMyCards(): void {
    if (!this.currentUserId) return;

    this.loyaltyCardService.getAllLoyaltyCardsByUserId(this.currentUserId).subscribe({
      next: (data: any) => this.loyaltyCards = data,
      error: (err: any) => console.error('Error al cargar tarjetas en la landing', err)
    });
  }

  joinStore(storeId?: number): void {
    // 1. Si el usuario no ha iniciado sesión, lo mandamos al login
    if (!this.currentUserId) {
      this.router.navigate(['/login']);
      return;
    }

    // 2. Si por algún motivo la tienda no tiene ID, cancelamos
    if (!storeId) {
      console.error('La tienda no tiene un ID válido.');
      return;
    }

    // 3. Llamamos al servicio para crear la tarjeta
    this.loyaltyCardService.joinStore(this.currentUserId, storeId).subscribe({
      next: () => {
        console.log('%c[Éxito] %c¡Bienvenido a la tienda!', 'color: #10b981; font-weight: bold', 'color: gray');
        this.router.navigate(['/customer/dashboard']);
      },
      error: (err) => {
        console.error('Error al unirse', err);
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
}

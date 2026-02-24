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
  imports: [RouterLink],
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
    console.log('1. Iniciando carga de tiendas...');
    this.isLoadingStores = true;

    this.storesService.getAllStores().subscribe({
      next: (data: any[]) => {
        this.stores = data; // Prueba sin filtrar primero
        this.isLoadingStores = false;
        this.cdr.detectChanges(); // <--- Esto fuerza a la vista a refrescarse
      },
      error: (error) => {
        console.error('ERROR CRÍTICO:', error);
        this.isLoadingStores = false; // Si falla, también debemos quitar el skeleton
      }
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

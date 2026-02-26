<<<<<<< Updated upstream
import { Component } from '@angular/core';
=======
import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { LoyaltyCard } from '../../../models/LoyaltyCard';
import { Store } from '../../../models/Store';
import { AuthService } from '../../../services/auth.service';
import { LoyaltycardsService } from '../../../services/loyaltycards.service';
import { StoresService } from '../../../services/stores.service';
<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes

@Component({
  selector: 'app-dashboard',
  imports: [],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent {

<<<<<<< Updated upstream
=======
  loyaltyCards: LoyaltyCard[] = [];
  isLoading: boolean = true;

  userName: string = 'Cliente';
  userRole: string = '';
  currentUserId: number = 0;

  hasStore: boolean = false;
  store: Store | null = null;

  constructor(
    private loyaltycardsService: LoyaltycardsService,
    private storesService: StoresService,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef // 👈 La herramienta para forzar el redibujado
  ) { }

  ngOnInit(): void {
    const storedId = localStorage.getItem('userId');
    const storedName = localStorage.getItem('userName');
    const storedRole = localStorage.getItem('userRole');

    if (storedId) {
      this.currentUserId = Number(storedId);
      this.userName = storedName || 'Cliente';
      this.userRole = storedRole || '';
      this.loadMyCards();
    } else {
      this.logout();
    }
  }

  loadMyCards(): void {
    this.isLoading = true;
    console.log('--- Iniciando carga de tarjetas para ID:', this.currentUserId);

    this.loyaltycardsService.getAllLoyaltyCardsByUserId(this.currentUserId).subscribe({
      next: (data: any) => {
        console.log('Tarjetas recibidas:', data);
        this.loyaltyCards = data;

        // SEGUNDA PETICIÓN: Verificar si tiene tienda
        this.storesService.getStoreByOwnerId(this.currentUserId).subscribe({
          next: (storeData: Store) => {
            console.log('Datos de tienda recibidos:', storeData);

            // Verificamos si realmente hay un objeto y si tiene ID
            if (storeData && Object.keys(storeData).length > 0) {
              this.hasStore = true;
              this.store = storeData; // Guardamos los datos si existen
            } else {
              console.warn('La petición volvió OK pero el objeto está vacío (undefined). El usuario no tiene tienda.');
              this.hasStore = false;
            }

            this.finalizeLoad();
          },
          error: (err: any) => {
            // Si llega aquí es un error de red o un 404/500
            this.hasStore = false;
            this.finalizeLoad();
          }
        });
      },
      error: (err: any) => {
        console.error('ERROR CRÍTICO cargando tarjetas:', err);
        this.finalizeLoad(); // ¡Importante! Si esto falla, el spinner debe morir también
      }
    });
  }

  // Método auxiliar para limpiar el estado
  private finalizeLoad(): void {
    this.isLoading = false;
    this.cdr.detectChanges();
    console.log('Carga finalizada. hasStore:', this.hasStore);
  }

  logout(): void {
    localStorage.clear();
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  goToMerchantDashboard() {
    if (this.currentUserId) {
      // Navegamos al dashboard del cliente, pasando el ID del cliente seleccionado
      console.log('Redirigiendo al dashboard del cliente con ID:', this.currentUserId);
      localStorage.setItem('userRole', 'ADMIN_NEGOCIO');

      setTimeout(() => {
        this.router.navigate(['/business/dashboard']);
      }, 500);
    }
    else {
      console.warn('No se encontró el ID del usuario en localStorage. Redirigiendo al login.', this.currentUserId);
      // this.router.navigate(['/login']);
    }
  }

  goToStore(store: Store | null, card: LoyaltyCard) {
    let trimedName = store?.name?.trim().toLowerCase().replace(/\s+/g, '-');

    // Navegamos sin queryParams, usamos 'state'
    this.router.navigate(['/customer/store', trimedName], {
      state: { cardId: card.id }
    });
  }
>>>>>>> Stashed changes
}

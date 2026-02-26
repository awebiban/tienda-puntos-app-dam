import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { LoyaltyCard } from '../../../models/LoyaltyCard';
import { Store } from '../../../models/Store';
import { LoyaltycardsService } from '../../../services/loyaltycards.service';
import { StoresService } from '../../../services/stores.service';

@Component({
  selector: 'app-merchant-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './merchant-dashboard.html',
  styleUrl: './merchant-dashboard.scss'
})
export class MerchantDashboardComponent implements OnInit {

  ownerId: number | null = null;

  store: Store | null = null;
  customers: LoyaltyCard[] = [];
  filteredCustomers: LoyaltyCard[] = [];

  isLoading: boolean = true;
  searchTerm: string = '';

  selectedCard: LoyaltyCard | null = null;
  pointsToAdd: number = 0;
  isProcessing: boolean = false;

  constructor(
    private storesService: StoresService,
    private loyaltyService: LoyaltycardsService,
    private cdr: ChangeDetectorRef,
    private router: Router
  ) { }

  ngOnInit(): void {
    const ownerId = localStorage.getItem('userId');
    if (ownerId) {
      this.ownerId = Number(ownerId);
      this.loadMerchantData(Number(ownerId));
    } else {
      console.error('No se encontró el ID del usuario en el localStorage');
      this.isLoading = false;
    }
  }

  loadMerchantData(ownerId: number): void {
    this.isLoading = true;
    this.storesService.getStoreByOwnerId(ownerId).subscribe({
      next: (storeData) => {
        if (storeData) {
          this.store = storeData;
          this.loadStoreCustomers(storeData.id!);
        } else {
          console.warn('El usuario es admin pero no tiene tiendas asociadas');
          this.isLoading = false;
          this.cdr.detectChanges();
          this.createFirtStore();
        }
      },
      error: (err) => {
        console.error('Error crítico al cargar datos del comerciante:', err);
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  loadStoreCustomers(storeId: number): void {
    this.loyaltyService.getCardsByStoreId(storeId).subscribe({
      next: (cards) => {
        this.customers = cards;
        this.filteredCustomers = cards;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al cargar la lista de clientes:', err);
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  filterCustomers(): void {
    const term = this.searchTerm.toLowerCase();
    this.filteredCustomers = this.customers.filter(c =>
      c.id.toString().includes(term) ||
      c.storeDTO.name.toLowerCase().includes(term)
    );
  }

  openPointsModal(card: LoyaltyCard): void {
    this.selectedCard = card;
    this.pointsToAdd = 0;
  }

  confirmAddPoints(): void {
    if (!this.selectedCard || this.pointsToAdd <= 0) return;
    this.isProcessing = true;
    this.loyaltyService.addPoints(this.selectedCard.id, this.pointsToAdd).subscribe({
      next: (updatedCard) => {
        const index = this.customers.findIndex(c => c.id === updatedCard.id);
        if (index !== -1) {
          this.customers[index] = updatedCard;
          this.filterCustomers();
        }
        this.selectedCard = null;
        this.isProcessing = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al sumar puntos:', err);
        this.isProcessing = false;
        this.cdr.detectChanges();
      }
    });
  }

  goToCustomerDashboard() {
    if (this.ownerId) {
      // Navegamos al dashboard del cliente, pasando el ID del cliente seleccionado
      console.log('Redirigiendo al dashboard del cliente con ID:', this.ownerId);
      localStorage.setItem('userRole', 'CLIENTE');

      setTimeout(() => {
        this.router.navigate(['/customer/dashboard']);
      }, 500);
    }
    else {
      console.warn('No se encontró el ID del usuario en localStorage. Redirigiendo al login.', this.ownerId);
      // this.router.navigate(['/login']);
    }
  }

  createFirtStore(): void {

  }
}

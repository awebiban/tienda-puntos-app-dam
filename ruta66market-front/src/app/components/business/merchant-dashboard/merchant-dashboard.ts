import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { StoresService } from '../../../services/stores.service';
import { LoyaltycardsService } from '../../../services/loyaltycards.service';
import { Store } from '../../../models/Store';
import { LoyaltyCard } from '../../../models/LoyaltyCard';

@Component({
  selector: 'app-merchant-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './merchant-dashboard.html',
  styleUrl: './merchant-dashboard.scss'
})
export class MerchantDashboardComponent implements OnInit {

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
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    const ownerId = localStorage.getItem('userId');
    if (ownerId) {
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
}

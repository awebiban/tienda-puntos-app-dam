import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { StoresService } from '../../../services/stores.service';
import { Store } from '../../../models/Store';

@Component({
  selector: 'app-store-config',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './store-config.component.html',
  styleUrl: './store-config.component.scss'
})
export class StoreConfigComponent implements OnInit {

  store: Store | null = null;
  isLoading: boolean = true;
  isSaving: boolean = false;
  successMessage: boolean = false;

  constructor(
    private storesService: StoresService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    const ownerId = localStorage.getItem('userId');
    if (ownerId) {
      this.loadStoreData(Number(ownerId));
    }
  }

  loadStoreData(ownerId: number): void {
    this.isLoading = true;
    this.storesService.getStoreByOwnerId(ownerId).subscribe({
      next: (data: Store) => {
        this.store = data;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err: Error) => {
        console.error('Error al cargar configuración de la tienda', err);
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  updateStore(): void {
    if (!this.store || !this.store.id) return;

    this.isSaving = true;
    this.storesService.updateStore(this.store.id, this.store).subscribe({
      next: (updated: Store) => {
        this.store = updated;
        this.isSaving = false;
        this.successMessage = true;
        setTimeout(() => this.successMessage = false, 3000);
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('Error al actualizar la tienda', err);
        this.isSaving = false;
        this.cdr.detectChanges();
      }
    });
  }
}

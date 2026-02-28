import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Store } from '../../../models/Store';
import { User } from '../../../models/User';
import { StoresService } from '../../../services/stores.service';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-store-config',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './store-config.component.html',
  styleUrl: './store-config.component.scss'
})
export class StoreConfigComponent implements OnInit {

  store: Store | null = null;
  owner: User | null = null;
  isLoading: boolean = true;
  isSaving: boolean = false;
  successMessage: boolean = false;

  constructor(
    private storesService: StoresService,
    private cdr: ChangeDetectorRef,
    private userService: UserService,
  ) { }

  ngOnInit(): void {
    const storeId = history.state?.storeId;
    const userId = history.state?.userId;
    if (storeId || userId) {
      this.loadStoreData(Number(storeId), Number(userId));
    }
    console.warn('No se recibió el ID de la tienda para configurar');
    this.isLoading = false;
    this.cdr.detectChanges();
  }

  loadStoreData(storeId: number, userId: number): void {
    this.isLoading = true;
    this.storesService.getStoreById(storeId).subscribe({
      next: (data) => {
        this.store = data;
        this.isLoading = false;
        this.cdr.detectChanges();
        this.loadOwnerData(userId);
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
    if (this.store && this.store.companyDTO) {
      //this.store!.companyDTO!.ownerDTO = this.owner;
    }
    console.log("tienda a actualiozar", this.store)
    this.isSaving = true;
    this.storesService.updateStore(this.store!.id, this.store).subscribe({
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
  loadOwnerData(userId: number) {
    this.userService.getUserById(userId).subscribe({
      next: (data) => {
        this.owner = data;
        console.log("Recibimos los datos del owner de la tienda", data);
      },
      error(err) {
        console.error("Error en la solicitud de datos del usuario")
      },
    })
  }
}

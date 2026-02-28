import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Company } from '../../../models/Company';
import { LoyaltyCard } from '../../../models/LoyaltyCard';
import { Store } from '../../../models/Store';
import { CompaniesService } from '../../../services/companies.service';
import { LoyaltycardsService } from '../../../services/loyaltycards.service';
import { StoresService } from '../../../services/stores.service';

@Component({
  selector: 'app-merchant-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './merchant-dashboard.html',
  styleUrl: './merchant-dashboard.scss'
})
export class MerchantDashboardComponent implements OnInit {

  company: Company | null = null;

  // Estados para el nuevo modal
  showCreateStoreModal = false;
  storeForm: FormGroup;
  isCreatingStore = false;

  stores: Store[] = [];
  customers: LoyaltyCard[] = [];
  filteredCustomers: LoyaltyCard[] = [];

  showCardsModal = false;
  selectedStoreName = '';

  isLoading: boolean = true;
  searchTerm: string = '';

  selectedCard: LoyaltyCard | null = null;
  pointsToAdd: number = 0;
  isProcessing: boolean = false;

  constructor(
    private storesService: StoresService,
    private loyaltyService: LoyaltycardsService,
    private companiesService: CompaniesService,
    private cdr: ChangeDetectorRef,
    private router: Router,
    private fb: FormBuilder,
  ) {
    // Inicializamos el formulario con los campos del modelo Store
    this.storeForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      category: ['', Validators.required],
      address: ['', Validators.required],
      pointsRatio: [1, [Validators.required, Validators.min(1)]],
      isVisible: [true]
    });
  }

  ngOnInit(): void {
    const ownerId = localStorage.getItem('userId');
    if (ownerId) {
      this.loadCompanyData(Number(ownerId));
    } else {
      console.error('No se encontró el ID del usuario en el localStorage');
      this.isLoading = false;
    }
  }
  loadCompanyData(arg0: number) {
    this.isLoading = true;
    this.companiesService.getCompanyByOwnerId(arg0).subscribe({
      next: (companyData) => {
        this.company = companyData;
        this.isLoading = false;
        this.cdr.detectChanges();

        if (this.company) {
          this.loadMerchantData(Number(this.company.id));
        }
      },
      error: (err) => {
        console.error('Error crítico al cargar datos de la empresa:', err);
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  loadMerchantData(companyId: number): void {
    this.isLoading = true;
    console.log('Cargando tiendas para la empresa con ID:', companyId);
    this.storesService.getStoresByCompanyId(companyId).subscribe({
      next: (storeData) => {
        if (storeData && storeData.length > 0) {
          this.stores = storeData;
          console.log('Tiendas cargadas para la empresa:', this.stores);
          this.isLoading = false;
          this.cdr.detectChanges();
          // this.loadStoreCustomers(storeData[0].id!);  IMPLEMENTAR EN MODAL POPUP - Línea 220
        } else {
          console.warn('El usuario es admin pero no tiene tiendas asociadas');
          this.isLoading = false;
          this.cdr.detectChanges();
          this.createFirstStore();
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

  // filterCustomers(): void {
  //   const term = this.searchTerm.toLowerCase();
  //   this.filteredCustomers = this.customers.filter(c =>
  //     c.id.toString().includes(term) ||
  //     c.storeDTO.name.toLowerCase().includes(term)
  //   );
  // }

  // openPointsModal(card: LoyaltyCard): void {
  //   this.selectedCard = card;
  //   this.pointsToAdd = 0;
  // }

  // confirmAddPoints(): void {
  //   if (!this.selectedCard || this.pointsToAdd <= 0) return;
  //   this.isProcessing = true;
  //   this.loyaltyService.addPoints(this.selectedCard.id, this.pointsToAdd).subscribe({
  //     next: (updatedCard) => {
  //       const index = this.customers.findIndex(c => c.id === updatedCard.id);
  //       if (index !== -1) {
  //         this.customers[index] = updatedCard;
  //         this.filterCustomers();
  //       }
  //       this.selectedCard = null;
  //       this.isProcessing = false;
  //       this.cdr.detectChanges();
  //     },
  //     error: (err) => {
  //       console.error('Error al sumar puntos:', err);
  //       this.isProcessing = false;
  //       this.cdr.detectChanges();
  //     }
  //   });
  // }

  createFirstStore() {
    if (this.company?.planDTO?.maxStores! === this.stores.length) {
      alert('Has alcanzado el límite de tiendas permitido por tu plan. Por favor, actualiza tu plan para crear más tiendas.');
    }
    else {
      console.log('Mostrando modal para crear nueva tienda. Tiendas actuales:', this.stores.length, 'Límite del plan:', this.company?.planDTO?.maxStores);
      this.showCreateStoreModal = true;
    }
  }

  confirmCreateStore() {
    if (this.storeForm.invalid) return;

    this.isCreatingStore = true;

    const newStore: Store = {
      ...this.storeForm.value,
      companyDTO: this.company!,
      imageUrl: 'default-store.jpg', // Imagen por defecto
      rewardsList: [],
    };

    if (newStore) {
      this.storesService.saveStore(newStore).subscribe({
        next: (createdStore) => {
          console.log('Tienda creada con éxito:', createdStore);
          setTimeout(() => {
            this.isCreatingStore = false;
            this.showCreateStoreModal = false;
            this.storeForm.reset({ pointsRatio: 1, isVisible: true });

          }, 500);

        },
        error: (err) => {
          console.error('Error al crear la tienda:', err);
          this.isCreatingStore = false;
          this.cdr.detectChanges();
        }
      });
      window.location.reload();
    }
  }

  goToCustomerDashboard() {
    localStorage.setItem('userRole', 'CLIENTE');
    this.router.navigate(['/customer/dashboard']);
  }


  editarStore(sid: number | undefined) {
    if (!sid) return;

    this.router.navigate(['/business/setup-store'], {
      state: {
        storeId: sid,
        userId: this.company?.ownerDTO.id
      }
    });
  }

  verTarjetasDeFidelidad(sid: number | undefined, storeName: string) {
    if (!sid) return;

    this.selectedStoreName = storeName;
    this.showCardsModal = true;
    this.isLoading = true;
    this.customers = []; // Limpiamos datos anteriores

    this.loadStoreCustomers(sid);
    this.isLoading = false;
  }

  verAnaliticas(sid: number | undefined) {
    if (!sid) return;

    this.router.navigate(['/business/analytics'], {
      state: { storeId: sid }
    });
  }
}

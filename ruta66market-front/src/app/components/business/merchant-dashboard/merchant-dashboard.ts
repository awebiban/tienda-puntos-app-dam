import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
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
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  loadMerchantData(companyId: number): void {
    this.isLoading = true;
    this.storesService.getStoresByCompanyId(companyId).subscribe({
      next: (storeData) => {
        if (storeData && storeData.length > 0) {
          this.stores = storeData;
          this.isLoading = false;
          this.cdr.detectChanges();
        } else {
          this.isLoading = false;
          this.cdr.detectChanges();
          this.createFirstStore();
        }
      },
      error: (err) => {
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
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  updatePointsDirectly(card: LoyaltyCard) {
    if (!card) return
    card.storeDTO.isVisible = true
    this.loyaltyService.updateCard(card.id, card).subscribe({
      next: (data) => {
        alert("Puntos actualizados correctamente")
      },
      error(err) {
        console.error(err)
      },
    })
  }

  createFirstStore() {
    if (this.company?.planDTO?.maxStores! === this.stores.length) {
      this.fireInfoSwalAlert("Limite alcanzado", `Has alcanzado el límite de tiendas permitido por tu plan.
        Por favor, actualiza tu plan para crear más tiendas.`, true, "Ver Planes", "No Gracias");
    }
    else {
      this.showCreateStoreModal = true;
    }
  }

  confirmCreateStore() {
    if (this.storeForm.invalid) return;

    this.isCreatingStore = true;

    const newStore: Store = {
      ...this.storeForm.value,
      companyDTO: this.company!,
      imageUrl: 'default-store.jpg',
      rewardsList: [],
    };

    if (newStore) {
      this.storesService.saveStore(newStore).subscribe({
        next: (createdStore) => {
          setTimeout(() => {
            this.isCreatingStore = false;
            this.showCreateStoreModal = false;
            this.storeForm.reset({ pointsRatio: 1, isVisible: true });
          }, 500);
        },
        error: (err) => {
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
        userId: this.company?.ownerDTO.id,
        companyId: this.company?.id
      }
    });
  }

  verTarjetasDeFidelidad(sid: number | undefined, storeName: string) {
    if (!sid) return;

    this.selectedStoreName = storeName;
    this.showCardsModal = true;
    this.isLoading = true;
    this.customers = [];

    this.loadStoreCustomers(sid);
    this.isLoading = false;
  }

  verAnaliticas(sid: number | undefined) {
    if (!sid) return;

    this.router.navigate(['/business/analytics'], {
      state: { storeId: sid }
    });
  }

  fireInfoSwalAlert(tit: string, txt: string, showCancelButton: boolean, confirmText: string, cancelText: string) {
    Swal.fire({
      title: tit,
      text: txt,
      icon: 'info',
      showCancelButton: showCancelButton,
      confirmButtonColor: '#6366f1',
      cancelButtonColor: '#1e293b',
      confirmButtonText: confirmText,
      cancelButtonText: cancelText,
      background: '#0f172a',
      color: '#ffffff'
    }).then((result) => {
      if (result.isConfirmed) {
        this.router.navigate(["/business/view-plans"], {
          state: {
            ownerId: this.company?.ownerDTO?.id
          }
        })
      }
    });
  }
}

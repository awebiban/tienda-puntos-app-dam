import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core'; // 1. Añade ChangeDetectorRef
import { ActivatedRoute, Router } from '@angular/router';
import { LoyaltyCard } from '../../../models/LoyaltyCard';
import { Reward } from '../../../models/Reward';
import { Store } from '../../../models/Store';
import { AuthService } from '../../../services/auth.service';
import { LoyaltycardsService } from '../../../services/loyaltycards.service';
import { RewardsService } from '../../../services/rewards.service';
import { TransactionsService } from '../../../services/transactions.service';

@Component({
  selector: 'app-store-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './store-detail.html',
  styleUrl: './store-detail.scss',
})
export class StoreDetail implements OnInit {
  currentUserId: number = 0;

  isLoading: boolean = true; // Empieza en true
  cardId: number | null = null;
  loyaltyCard: LoyaltyCard | null = null;
  store: Store | null = null;

  isLoadingRewards: boolean = false;
  rewards: Reward[] = [];

  constructor(
    private route: ActivatedRoute,
    private loyaltyCardService: LoyaltycardsService,
    private rewardService: RewardsService,
    private transactionService: TransactionsService,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    const storedId = localStorage.getItem('userId');

    if (storedId) {
      this.currentUserId = Number(storedId);
    } else {
      this.logout();
    }

    this.route.queryParams.subscribe(params => {
      const cardIdParam = params['cardId'];

      // Si el parámetro no es válido, apagamos y salimos para evitar el bucle
      if (!cardIdParam || isNaN(Number(cardIdParam))) {
        console.warn('%c[StoreDetail - ngOnInit] %cID de tarjeta no válido o ausente', 'color: #f59e0b; font-weight: bold', 'color: gray');
        this.isLoading = false;
        this.cdr.detectChanges();
        return;
      }

      this.cardId = Number(cardIdParam);

      // Lanzamos la carga de la tarjeta (Spinner principal)
      this.loadCardData(this.cardId);
    });
  }

  loadCardData(cardId: number) {
    this.isLoading = true; // Nos aseguramos de que esté activo al empezar

    this.loyaltyCardService.findByCardId(cardId).subscribe({
      next: (data) => {
        // Asignamos los datos recibidos
        this.loyaltyCard = data;
        if (data && data.storeDTO) {
          this.store = data.storeDTO;
        }

        console.log('%c[StoreDetail - loadCardData] %cDatos cargados correctamente', 'color: #10b981; font-weight: bold', 'color: gray');

        // APAGADO DEL LOADER: Lo sacamos del flujo principal por si hay microtareas pendientes
        setTimeout(() => {
          this.isLoading = false;
          this.cdr.detectChanges();
        }, 100);

        // Lanzamos la carga de premios (Spinner independiente)
        if (this.store && this.store.id) {
          this.loadStoreRewards(this.store.id);
        }
      },
      error: (err) => {
        console.error('%c[StoreDetail Error]', 'color: #ef4444; font-weight: bold', err);
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
    this.loyaltyCardService.updateLastAccess(cardId).subscribe({
      next: () => {
        console.log('%c[StoreDetail - updateLastAccess] %cÚltimo acceso actualizado', 'color: #3b82f6; font-weight: bold', 'color: gray');
      },
      error: (err) => {
        console.error('%c[StoreDetail Update Last Access Error]', 'color: #ef4444; font-weight: bold', err);
      }
    });
  }
  loadStoreRewards(storeId: number) {
    console.log(storeId)
    if (!storeId || storeId <= 0) {
      console.warn('%c[StoreDetail - loadStoreRewards] %cID de tienda no válido para cargar premios', 'color: #f59e0b; font-weight: bold', 'color: gray');
      return;
    }
    this.isLoadingRewards = true;

    this.rewardService.getRewardsByStoreId(storeId).subscribe({
      next: (data) => {
        this.rewards = data;
        console.log('%c[StoreDetail - loadStoreRewards] %cPremios cargados:', 'color: #10b981; font-weight: bold', 'color: gray', data);
        this.isLoadingRewards = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('%c[StoreDetail Rewards Error]', 'color: #ef4444; font-weight: bold', err);
        this.isLoadingRewards = false;
        this.cdr.detectChanges();
      }
    });
  }

  reclamarRecompensa(reward: Reward) {
    if (!this.currentUserId || !this.store?.id || !reward.id) {
      console.warn('%c[StoreDetail - reclamarRecompensa] %cDatos insuficientes para reclamar recompensa', 'color: #f59e0b; font-weight: bold', 'color: gray');
      console.log({ currentUserId: this.currentUserId, storeId: this.store?.id, rewardId: reward });
      return;
    }
    // Aquí iría la lógica para reclamar la recompensa, por ejemplo:
    this.rewardService.redeemReward(this.currentUserId, this.store?.id, reward.id).subscribe({
      next: (response) => {
        console.log('%c[StoreDetail - reclamarRecompensa] %cRecompensa reclamada con éxito', 'color: #10b981; font-weight: bold', 'color: gray', response);
        // Después de reclamar, podríamos recargar la tarjeta para actualizar el balance
        this.loadCardData(this.cardId!);
      },
      error: (err) => {
        console.error('%c[StoreDetail Redeem Reward Error]', 'color: #ef4444; font-weight: bold', err);
      }
    });
  }

  returnButton() {
    window.history.back();
  }

  logout(): void {
    localStorage.clear();
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
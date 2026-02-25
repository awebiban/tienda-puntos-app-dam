import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { StoresService } from '../../../services/stores.service';
import { LoyaltycardsService } from '../../../services/loyaltycards.service';
import { RewardsService } from '../../../services/rewards.service';
import { Store } from '../../../models/Store';
import { Reward } from '../../../models/Reward';
import { LoyaltyCard } from '../../../models/LoyaltyCard';

@Component({
  selector: 'app-store-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './store-detail.html',
  styleUrl: './store-detail.scss'
})
export class StoreDetail implements OnInit {

  store: Store | null = null;
  rewards: Reward[] = [];
  loyaltyCard: LoyaltyCard | null = null;
  cardId: number | null = null;
  isLoading: boolean = true;
  currentUserId: number | null = null;

  constructor(
    private route: ActivatedRoute,
    private storesService: StoresService,
    private loyaltyService: LoyaltycardsService,
    private rewardsService: RewardsService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    const storedId = localStorage.getItem('userId');
    this.currentUserId = storedId ? Number(storedId) : null;

    this.route.queryParams.subscribe(params => {
      this.cardId = params['cardId'] ? Number(params['cardId']) : null;
      if (this.cardId) {
        this.loadFullContext(this.cardId);
      }
    });

    this.route.params.subscribe(params => {
      const storeName = params['storeName'];
      if (storeName && !this.cardId) {
        this.loadStoreOnly(storeName);
      }
    });
  }

  loadFullContext(id: number): void {
    this.isLoading = true;
    this.loyaltyService.findByCardId(id).subscribe({
      next: (card) => {
        this.loyaltyCard = card;
        this.store = card.storeDTO;
        this.rewards = this.store?.rewardsList || [];
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al cargar contexto', err);
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  loadStoreOnly(name: string): void {
    this.isLoading = true;
    this.storesService.getAllStores().subscribe({
      next: (stores) => {
        this.store = stores.find(s =>
          s.name.toLowerCase().replace(/[^a-z0-9]+/g, '-') === name
        ) || null;
        if (this.store) {
          this.rewards = this.store.rewardsList || [];
        }
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  redeemReward(reward: Reward): void {
    if (!this.loyaltyCard || !this.currentUserId || !this.store?.id || !reward.id) return;

    if (this.loyaltyCard.currentBalance < reward.pointsCost) {
      alert('Puntos insuficientes');
      return;
    }

    if (confirm(`¿Quieres canjear ${reward.name} por ${reward.pointsCost} puntos?`)) {
      this.rewardsService.redeemReward(this.currentUserId, this.store.id, reward.id).subscribe({
        next: (updatedCard) => {
          this.loyaltyCard = updatedCard; // Actualiza el saldo en la vista automáticamente
          alert('¡Premio canjeado con éxito!');
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Error al canjear', err);
          alert('Hubo un error al procesar el canje');
        }
      });
    }
  }
}

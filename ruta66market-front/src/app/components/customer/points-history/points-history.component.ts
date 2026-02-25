import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { TransactionsService } from '../../../services/transactions.service';
import { LoyaltycardsService } from '../../../services/loyaltycards.service';
import { Transaction } from '../../../models/Transaction';
import { LoyaltyCard } from '../../../models/LoyaltyCard';

@Component({
  selector: 'app-points-history',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './points-history.component.html',
  styleUrl: './points-history.component.scss'
})
export class PointsHistoryComponent implements OnInit {

  transactions: Transaction[] = [];
  loyaltyCard: LoyaltyCard | null = null;

  isLoading: boolean = true;
  totalEarned: number = 0;
  totalRedeemed: number = 0;

  constructor(
    private route: ActivatedRoute,
    private transactionsService: TransactionsService,
    private loyaltyCardService: LoyaltycardsService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const cardId = Number(params['cardId']);
      if (cardId) {
        this.loadHistory(cardId);
      } else {
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  loadHistory(cardId: number): void {
    this.isLoading = true;

    // Carga de info de tarjeta
    this.loyaltyCardService.findByCardId(cardId).subscribe({
      next: (card) => {
        this.loyaltyCard = card;
        this.cdr.detectChanges();
      }
    });

    // Carga de historial
    this.transactionsService.getTransactionsByUserId(cardId).subscribe({
      next: (data) => {
        this.transactions = data;
        this.calculateTotals();
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al cargar historial:', err);
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  calculateTotals(): void {
    this.totalEarned = this.transactions
      .filter(t => t.type === 'EARN')
      .reduce((sum, t) => sum + t.amount, 0);

    this.totalRedeemed = Math.abs(this.transactions
      .filter(t => t.type === 'REDEEM')
      .reduce((sum, t) => sum + t.amount, 0));
  }

  goBack(): void {
    window.history.back();
  }
}

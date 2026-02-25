import { Component, OnInit, ElementRef, ViewChild, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Chart, registerables } from 'chart.js';
import { StoresService } from '../../../services/stores.service';
import { LoyaltycardsService } from '../../../services/loyaltycards.service';
import { LoyaltyCard } from '../../../models/LoyaltyCard';
import { Store } from '../../../models/Store';

Chart.register(...registerables);

@Component({
  selector: 'app-analytics',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './analytics.component.html',
  styleUrl: './analytics.component.scss'
})
export class AnalyticsComponent implements OnInit {
  @ViewChild('mainChart') mainChart!: ElementRef;

  store: Store | null = null;
  isLoading: boolean = true;
  chart: any;

  // Métricas reales
  totalPointsGiven: number = 0;
  totalPointsRedeemed: number = 0;
  activeCustomers: number = 0;

  constructor(
    private storesService: StoresService,
    private loyaltyService: LoyaltycardsService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const ownerId = localStorage.getItem('userId');
    if (ownerId) {
      this.loadAnalyticsData(Number(ownerId));
    }
  }

  loadAnalyticsData(ownerId: number): void {
    this.isLoading = true;
    this.storesService.getStoreByOwnerId(ownerId).subscribe({
      next: (storeData: Store) => {
        this.store = storeData;
        if (this.store?.id) {
          this.fetchStoreMetrics(this.store.id);
        }
      },
      error: (err: Error) => {
        console.error('Error cargando analíticas', err);
        this.isLoading = false;
      }
    });
  }

  fetchStoreMetrics(storeId: number): void {
    this.loyaltyService.getCardsByStoreId(storeId).subscribe({
      next: (cards: LoyaltyCard[]) => {
        this.activeCustomers = cards.length;

        // Calculamos métricas desde los datos reales de las tarjetas
        this.totalPointsGiven = cards.reduce((acc, card) => acc + card.totalAccumulated, 0);
        this.totalPointsRedeemed = cards.reduce((acc, card) => acc + (card.totalAccumulated - card.currentBalance), 0);

        this.isLoading = false;
        this.cdr.detectChanges();
        this.createChart();
      },
      error: (err: Error) => {
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  createChart(): void {
    if (this.chart) this.chart.destroy();

    const ctx = this.mainChart.nativeElement.getContext('2d');
    this.chart = new Chart(ctx, {
      type: 'doughnut',
      data: {
        labels: ['Puntos en Circulación', 'Puntos Canjeados'],
        datasets: [{
          data: [this.totalPointsGiven - this.totalPointsRedeemed, this.totalPointsRedeemed],
          backgroundColor: ['#6366f1', '#10b981'],
          borderWidth: 0,
          hoverOffset: 20
        }]
      },
      options: {
        responsive: true,
        plugins: {
          legend: {
            display: false
          }
        },
        cutout: '80%'
      }
    });
  }
}

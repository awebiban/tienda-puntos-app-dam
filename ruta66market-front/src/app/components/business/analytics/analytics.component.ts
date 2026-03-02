import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { LoyaltyCard } from '../../../models/LoyaltyCard';
import { Store } from '../../../models/Store';
import { LoyaltycardsService } from '../../../services/loyaltycards.service';
import { StoresService } from '../../../services/stores.service';

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

  cards: LoyaltyCard[] = [];

  totalPointsGiven: number = 0;
  totalPointsRedeemed: number = 0;
  activeCustomers: number = 0;

  constructor(
    private storesService: StoresService,
    private loyaltyService: LoyaltycardsService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    const storeId = history.state?.storeId;
    if (storeId) {
      this.loadAnalyticsData(Number(storeId));
    } else {
      this.isLoading = false;
    }
  }

  loadAnalyticsData(storeId: number): void {
    this.isLoading = true;
    this.storesService.getStoreById(storeId).subscribe({
      next: (storeData) => {
        this.store = storeData;
        if (this.store?.id) {
          this.fetchStoreMetrics(this.store.id);
        }
      },
      error: () => this.isLoading = false
    });
  }

  fetchStoreMetrics(storeId: number): void {
    this.loyaltyService.getCardsByStoreId(storeId).subscribe({
      next: (cards: LoyaltyCard[]) => {
        this.cards = cards;
        this.calculateMetrics();
        this.isLoading = false;
        this.cdr.detectChanges();

        setTimeout(() => this.createChart(), 0);
      },
      error: () => this.isLoading = false
    });
  }

  calculateMetrics(): void {
    this.activeCustomers = this.cards.length;

    this.totalPointsGiven = this.cards.reduce((acc, c) => acc + c.totalAccumulated, 0);

    this.totalPointsRedeemed = this.cards.reduce((acc, c) =>
      acc + (c.totalAccumulated - c.currentBalance), 0);

    if (this.chart) {
      this.updateChartAnimation();
    }
  }

  updateChartAnimation(): void {
    const inCirculation = this.totalPointsGiven - this.totalPointsRedeemed;

    this.chart.data.datasets[0].data = [inCirculation, this.totalPointsRedeemed];
    this.chart.update('active');
  }

  createChart(): void {
    if (!this.mainChart) return;
    if (this.chart) this.chart.destroy();

    const ctx = this.mainChart.nativeElement.getContext('2d');
    const inCirculation = this.totalPointsGiven - this.totalPointsRedeemed;

    this.chart = new Chart(ctx, {
      type: 'doughnut',
      data: {
        labels: ['En Monederos', 'Canjeados'],
        datasets: [{
          data: [inCirculation, this.totalPointsRedeemed],
          backgroundColor: ['#6366f1', '#10b981'],
          hoverBackgroundColor: ['#4f46e5', '#059669'],
          borderWidth: 0,
          weight: 0.5,
          borderRadius: 20,
          spacing: 10
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        cutout: '82%',
        plugins: {
          legend: { display: false },
          tooltip: {
            backgroundColor: '#0f172a',
            titleFont: { size: 14, weight: 'bold' },
            bodyFont: { size: 12 },
            padding: 12,
            displayColors: false,
            callbacks: {
              label: (context) => ` ${context.raw} Puntos`
            }
          }
        },
        animation: {
          duration: 1000,
          easing: 'easeInOutQuart'
        }
      }
    });
  }

  onCustomerPointsChanged(updatedCard: LoyaltyCard) {
    const index = this.cards.findIndex(c => c.id === updatedCard.id);
    if (index !== -1) {
      this.cards[index] = updatedCard;
      this.calculateMetrics();
      this.cdr.detectChanges();
    }
  }

  goBack() {
    window.history.back()
  }
}

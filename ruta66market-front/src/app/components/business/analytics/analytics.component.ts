import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

interface AnalyticsSummary {
  totalPointsIssued: number;
  totalPointsRedeemed: number;
  activeCustomers: number;
  conversionRate: number;
}

interface TopCustomer {
  nickname: string;
  totalSpent: number;
  pointsBalance: number;
}

@Component({
  selector: 'app-analytics',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './analytics.component.html',
  styleUrl: './analytics.component.scss'
})
export class AnalyticsComponent implements OnInit {

  summary: AnalyticsSummary = {
    totalPointsIssued: 12540,
    totalPointsRedeemed: 4200,
    activeCustomers: 156,
    conversionRate: 33.5
  };

  topCustomers: TopCustomer[] = [
    { nickname: 'Carlos', totalSpent: 450.20, pointsBalance: 450 },
    { nickname: 'Laura', totalSpent: 320.50, pointsBalance: 120 },
    { nickname: 'Enzo', totalSpent: 280.00, pointsBalance: 280 }
  ];

  constructor() {}

  ngOnInit(): void {}
}

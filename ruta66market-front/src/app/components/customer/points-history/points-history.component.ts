import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

interface Transaction {
  id: number;
  date: string;
  storeName: string;
  amount: number;
  points: number;
  type: 'EARNED' | 'REDEEMED';
  description: string;
}

@Component({
  selector: 'app-points-history',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './points-history.component.html',
  styleUrl: './points-history.component.scss'
})
export class PointsHistoryComponent implements OnInit {

  transactions: Transaction[] = [
    { id: 1, date: '2026-02-20', storeName: 'USA-dos Central', amount: 45.50, points: 45, type: 'EARNED', description: 'Compra de artículos vintage' },
    { id: 2, date: '2026-02-18', storeName: 'USA-dos Central', amount: 0, points: -100, type: 'REDEEMED', description: 'Canje de Cupón Descuento 10€' },
    { id: 3, date: '2026-02-15', storeName: 'Ruta 66 Market', amount: 12.00, points: 12, type: 'EARNED', description: 'Compra de accesorios' },
    { id: 4, date: '2026-02-10', storeName: 'USA-dos Central', amount: 80.00, points: 80, type: 'EARNED', description: 'Compra Chaqueta de Cuero' }
  ];

  constructor() {}

  ngOnInit(): void {}
}

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LoyaltycardsService } from '../../../services/loyaltycards.service';

@Component({
  selector: 'app-merchant-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './merchant-dashboard.html'
})
export class MerchantDashboardComponent implements OnInit {
  clients: any[] = [];
  storeId: number = 1; // En el futuro, esto vendrá del perfil del usuario logueado

  constructor(private loyaltyService: LoyaltycardsService) {}

  ngOnInit(): void {
    this.loadClients();
  }

  loadClients(): void {
    this.loyaltyService.getCardsByStoreId(this.storeId).subscribe({
      next: (data) => this.clients = data,
      error: (err) => console.error('Error cargando clientes', err)
    });
  }

  addPoints(cardId: number, input: HTMLInputElement): void {
    const points = Number(input.value);
    if (points <= 0) return;

    this.loyaltyService.addPoints(cardId, points).subscribe({
      next: () => {
        alert('¡Puntos añadidos correctamente!');
        this.loadClients();
        input.value = '10';
      },
      error: (err) => alert('Error al sumar puntos')
    });
  }
}

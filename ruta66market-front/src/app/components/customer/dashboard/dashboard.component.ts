import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { LoyaltycardsService } from '../../../services/loyaltycards.service';
import { AuthService } from '../../../services/auth.service';
import { LoyaltyCard } from '../../../models/LoyaltyCard';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  loyaltyCards: LoyaltyCard[] = [];
  isLoading: boolean = true;

  userName: string = 'Cliente';
  currentUserId: number = 0;

  constructor(
    private loyaltycardsService: LoyaltycardsService,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef // 👈 La herramienta para forzar el redibujado
  ) {}

  ngOnInit(): void {
    // 1. Recuperamos tus datos reales del inicio de sesión
    const storedId = localStorage.getItem('userId');
    const storedName = localStorage.getItem('userName');

    if (storedId) {
      this.currentUserId = Number(storedId);
      this.userName = storedName || 'Cliente';
      this.loadMyCards();
    } else {
      // Si por lo que sea no hay ID, te mandamos al login por seguridad
      this.logout();
    }
  }

  loadMyCards(): void {
    this.isLoading = true;

    this.loyaltycardsService.getAllLoyaltyCardsByUserId(this.currentUserId).subscribe({
      next: (data: any) => {
        this.loyaltyCards = data;
        this.isLoading = false;
        this.cdr.detectChanges(); // 👈 ¡Obliga a Angular a quitar el spinner ahora mismo!
        console.log('%c[Dashboard] %cTarjetas de ' + this.userName + ' cargadas:', 'color: #10b981; font-weight: bold', 'color: gray', data);
      },
      error: (err: any) => {
        this.isLoading = false;
        this.cdr.detectChanges(); // 👈 Obliga a Angular a actualizar aunque haya error
        console.error('%c[Dashboard Error] %cFallo al cargar:', 'color: #ef4444; font-weight: bold', 'color: gray', err);
      }
    });
  }

  logout(): void {
    localStorage.clear(); // Limpiamos la memoria al salir
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}

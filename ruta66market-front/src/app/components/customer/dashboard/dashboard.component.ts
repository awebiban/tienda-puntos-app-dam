import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { LoyaltyCard } from '../../../models/LoyaltyCard';
import { Store } from '../../../models/Store';
import { AuthService } from '../../../services/auth.service';
import { CompaniesService } from '../../../services/companies.service';
import { LoyaltycardsService } from '../../../services/loyaltycards.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {

  hasCompany: boolean = false;
  checkingCompany: boolean = true;

  loyaltyCards: LoyaltyCard[] = [];
  isLoading: boolean = true;

  userName: string = 'Cliente';
  userRole: string = '';
  currentUserId: number = 0;

  constructor(
    private loyaltycardsService: LoyaltycardsService,
    private companyService: CompaniesService,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    const storedId = localStorage.getItem('userId');
    const storedName = localStorage.getItem('userName');
    const storedRole = localStorage.getItem('userRole');

    if (storedId) {
      this.currentUserId = Number(storedId);
      this.userName = storedName || 'Cliente';
      this.userRole = storedRole || '';
      this.loadMyCards();
      this.checkUserCompany();
    } else {
      this.logout();
    }
  }

  loadMyCards(): void {
    this.isLoading = true;

    this.loyaltycardsService.getAllLoyaltyCardsByUserId(this.currentUserId).subscribe({
      next: (data: any) => {
        this.loyaltyCards = data;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  checkUserCompany(): void {
    if (!this.currentUserId) return;

    this.checkingCompany = true;

    this.companyService.getCompanyByOwnerId(this.currentUserId).subscribe({
      next: (company) => {
        if (company && company.id) {
          this.hasCompany = true;
        } else {
          this.hasCompany = false;
        }

        this.checkingCompany = false;
        this.cdr.detectChanges();
      }
    });
  }

  logout(): void {
    localStorage.clear();
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  goToStore(store: Store | null, card: LoyaltyCard) {
    const trimedName = store?.name?.trim().toLowerCase().replace(/\s+/g, '-');

    this.router.navigate(['/customer/store', trimedName], {
      state: { cardId: card.id }
    });
  }

  goToMerchanPanel() {
    localStorage.setItem('userRole', 'ADMIN_NEGOCIO');
    this.router.navigate(['/business/dashboard']);
  }
}

import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { loadScript } from '@paypal/paypal-js';
import Swal from 'sweetalert2';
import { Company } from '../../../models/Company';
import { Plan } from '../../../models/Plan';
import { CompaniesService } from '../../../services/companies.service';
import { PlansService } from '../../../services/plans.service';

@Component({
  selector: 'app-plans-view',
  imports: [CommonModule, RouterLink],
  templateUrl: './plans-view.html',
  styleUrl: './plans-view.scss',
})
export class PlansView implements OnInit {
  selectedPlan: Plan | null = null;
  plans: Plan[] = [];
  company: Company | null = null;
  isLoading: boolean = true;

  constructor(
    private plansService: PlansService,
    private companyService: CompaniesService,
    private cdr: ChangeDetectorRef,
    private router: Router,
  ) { }

  ngOnInit(): void {
    const ownerId = history.state?.ownerId;
    if (ownerId) {
      this.loadCompanyData(Number(ownerId));
      this.fetchPlans();
    } else {
      this.router.navigate(["/login"])
    }
  }

  loadCompanyData(ownerId: number) {
    this.isLoading = true;
    this.companyService.getCompanyByOwnerId(ownerId).subscribe({
      next: (data) => {
        this.company = data;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.isLoading = false;
        this.cdr.detectChanges();
        console.error(err);
      }
    });
  }

  fetchPlans(): void {
    this.isLoading = true;
    setTimeout(() => {
      this.plansService.getAllActivePlans().subscribe({
        next: (data) => {
          this.plans = data;
          this.isLoading = false;
          this.cdr.detectChanges()
        },
        error: (err) => {
          console.error(err)
          this.isLoading = false;
          this.cdr.detectChanges()
        },
      })
    }, 800);
  }

  isRecommended(name: string): boolean {
    return name.toLowerCase().includes('pro');
  }

  async selectPlan(plan: Plan) {
    this.selectedPlan = plan;
    this.cdr.detectChanges();

    try {
      const paypal = await loadScript({
        "clientId": "ASxpXVzlr6n5JwoUYkoIyfeavD5-c3sK32DAQblf3zpmSGnqefjDp2Ex1B9VafZXMZLXzVmf_LvLDyTe",
        currency: "EUR"
      });

      if (paypal && paypal.Buttons) {
        await paypal.Buttons({
          style: {
            layout: 'vertical',
            color: 'blue',
            shape: 'pill',
            label: 'pay'
          },
          createOrder: (data, actions) => {
            return actions.order.create({
              intent: "CAPTURE",
              purchase_units: [{
                description: `Plan ${plan.planName} - USA-dos`,
                amount: {
                  currency_code: "EUR",
                  value: plan.price.toString()
                }
              }]
            });
          },
          onApprove: async (data, actions) => {
            const order = await actions.order?.capture();
            this.handlePaymentSuccess(order, plan);
          },
          onError: (err) => {
            Swal.fire('Error', 'El proceso de pago ha fallado', 'error');
            console.error(err);
          }
        }).render("#paypal-button-container");
      }
    } catch (error) {
      console.error("Fallo al cargar el SDK de PayPal", error);
    }
  }

  handlePaymentSuccess(order: any, plan: Plan) {
    this.selectedPlan = null;
    Swal.fire({
      title: '¡Pago Completado!',
      text: `Tu cuenta ha sido actualizada al plan ${plan.planName}`,
      icon: 'success',
      background: '#0f172a',
      color: '#fff',
      confirmButtonColor: '#6366f1'
    });
  }
}

import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { Company } from '../../../models/Company';
import { Plan } from '../../../models/Plan';
import { User } from '../../../models/User';
import { CompaniesService } from '../../../services/companies.service';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-register-business',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './register-business.component.html'
})
export class RegisterBusinessComponent implements OnInit {
  registerForm: FormGroup;
  isLoading = false;
  isSuccess = false; // Estado para mostrar la vista de éxito
  errorMessage = '';
  currentUserId: number | null = null;
  currentUserData: User | null = null;

  freePlan: Plan = {
    id: 1,
    planName: 'FREE',
    price: 0,
    maxStores: 1,
    maxUsers: 100,
    active: true
  };

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private userService: UserService,
    private companyService: CompaniesService,
  ) {
    this.registerForm = this.fb.group({
      legalName: ['', [Validators.required, Validators.minLength(3)]],
      // Validación mejorada: 8 números y 1 letra (RegEx estándar CIF/NIF básico)
      cif: ['', [Validators.required, Validators.pattern('^[0-9]{8}[A-Z]$|^[A-Z][0-9]{8}$')]],
    });
  }

  ngOnInit(): void {
    const storedId = localStorage.getItem('userId');
    this.currentUserId = storedId ? Number(storedId) : null;

    if (!this.currentUserId) {
      this.router.navigate(['/login']);
      return;
    }

    this.userService.getUserById(this.currentUserId).subscribe({
      next: (user) => {
        this.currentUserData = user;
        console.log('Usuario actual cargado:', this.currentUserData);
      },
      error: (err) => {
        console.error('Error al obtener el usuario:', err);
        this.router.navigate(['/login']);
      }
    });
  }

  get legalNameControl() { return this.registerForm.get('legalName'); }
  get cifControl() { return this.registerForm.get('cif'); }

  onSubmit(): void {
    if (this.registerForm.invalid || !this.currentUserData) return;

    this.isLoading = true; // Aquí empieza el "Procesando..."
    this.errorMessage = '';

    const newCompany: Company = {
      legalName: this.registerForm.value.legalName,
      cif: this.registerForm.value.cif,
      ownerDTO: this.currentUserData, // Usamos los nombres que espera tu DTO
      planDTO: this.freePlan,
      subscriptionStatus: 'ACTIVE',
      nextBillingDate: this.obtenerFechaEnFormatoISO() as any
    };

    this.companyService.registerNewCompany(newCompany).subscribe({
      next: (data) => {
        console.log('Empresa registrada con éxito:', data);

        this.isLoading = false;
        this.isSuccess = true;

      },
      error: (err) => {
        console.error('Error al registrar la empresa:', err);
        this.isLoading = false;
        this.errorMessage = 'Hubo un error al registrar la empresa.';
      }
    });
  }

  // Limpieza de sesión y redirección al login
  goToLogin(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }

  obtenerFechaEnFormatoISO(): string {
    const date = new Date();
    date.setMonth(date.getMonth() + 1);
    return date.toISOString();
  }
}
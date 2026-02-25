import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  loginForm: FormGroup;
  isLoading = false;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  get emailControl() { return this.loginForm.get('email'); }
  get passwordControl() { return this.loginForm.get('password'); }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    // 💡 LIMPIEZA PREVIA: Borramos rastro de sesiones antiguas
    localStorage.clear();
    this.isLoading = true;
    this.errorMessage = '';

    const { email, password } = this.loginForm.value;

    this.authService.login(email, password).subscribe({
      next: (res: any) => {
        this.isLoading = false;

        // Guardamos los datos nuevos
        localStorage.setItem('token', res.token);
        localStorage.setItem('userId', res.id);
        localStorage.setItem('userName', res.nickname);
        localStorage.setItem('userRole', res.role);

        // Redirección según rol
        if (res.role === 'ADMIN_NEGOCIO') {
          this.router.navigate(['/business/dashboard']);
        } else {
          this.router.navigate(['/customer/dashboard']);
        }
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'Credenciales incorrectas o error en el servidor';
        console.error('Error Login:', err);
      }
    });
  }
}

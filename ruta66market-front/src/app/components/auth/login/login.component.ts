import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../services/auth.service';

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
    private router: Router,
    private cdr: ChangeDetectorRef
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
    this.isLoading = true;
    this.errorMessage = '';

    const { email, password } = this.loginForm.value;

    this.authService.login(email, password).subscribe({
      next: (res: any) => {
        console.log('Login exitoso, procesando redirección...');

        // 1. Guardar datos (asegurándonos de que no sean null)
        localStorage.setItem('token', res.token || '');
        localStorage.setItem('userId', String(res.id || ''));
        localStorage.setItem('userName', res.nickname || '');
        localStorage.setItem('userRole', res.role || '');

        // 2. Apagar loader y forzar UI
        this.isLoading = false;
        this.cdr.detectChanges();

        // 3. Navegación con pequeña pausa para que el Guard vea el LocalStorage actualizado
        setTimeout(() => {
          const targetRoute = res.role === 'ADMIN_NEGOCIO'
            ? '/business/dashboard'
            : '/customer/dashboard';

          this.router.navigate([targetRoute]).then(success => {
            if (!success) {
              console.error('La navegación falló. ¿Está la ruta bien definida?');
              this.errorMessage = 'Error al redirigir al panel de control.';
            }
          });
        }, 150);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'Credenciales incorrectas o error en el servidor';
        this.cdr.detectChanges();
        console.error('Error Login:', err);
      }
    });
  }
}

import { Component, OnInit } from '@angular/core'; // CORREGIDO: Viene de @angular/core
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common'; // CommonModule sí viene de @angular/common
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-store-config',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink],
  templateUrl: './store-config.component.html',
  styleUrl: './store-config.component.scss'
})
export class StoreConfigComponent implements OnInit {
  storeForm: FormGroup;
  isLoading: boolean = false;
  saveSuccess: boolean = false;

  constructor(private fb: FormBuilder) {
    this.storeForm = this.fb.group({
      storeName: ['', [Validators.required, Validators.minLength(3)]],
      category: ['', [Validators.required]],
      pointsRatio: [1, [Validators.required, Validators.min(1)]], // Puntos por cada 1€
      description: ['', [Validators.maxLength(200)]],
      imageUrl: ['']
    });
  }

  ngOnInit(): void {
    // Aquí cargaríamos los datos si la tienda ya existe
  }

  onSubmit(): void {
    if (this.storeForm.valid) {
      this.isLoading = true;
      // Simulamos la llamada al servicio de Stores
      setTimeout(() => {
        console.log('Datos de la tienda guardados:', this.storeForm.value);
        this.isLoading = false;
        this.saveSuccess = true;
        // Ocultar mensaje de éxito tras 3 segundos
        setTimeout(() => this.saveSuccess = false, 3000);
      }, 1500);
    }
  }
}

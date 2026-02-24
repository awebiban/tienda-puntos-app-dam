import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

interface Reward {
  id?: number;
  title: string;
  pointsCost: number;
  description: string;
  category: string;
}

@Component({
  selector: 'app-rewards-editor',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink],
  templateUrl: './rewards-editor.component.html',
  styleUrl: './rewards-editor.component.scss'
})
export class RewardsEditorComponent implements OnInit {
  rewardForm: FormGroup;
  rewards: Reward[] = [
    { id: 1, title: 'Cupón 10€ Descuento', pointsCost: 100, description: 'Válido para compras superiores a 50€', category: 'Descuento' },
    { id: 2, title: 'Camiseta USA-dos', pointsCost: 500, description: 'Edición limitada algodón orgánico', category: 'Producto' }
  ];

  constructor(private fb: FormBuilder) {
    this.rewardForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(5)]],
      pointsCost: [100, [Validators.required, Validators.min(10)]],
      description: ['', [Validators.required]],
      category: ['Descuento', [Validators.required]]
    });
  }

  ngOnInit(): void {}

  addReward() {
    if (this.rewardForm.valid) {
      this.rewards.unshift({...this.rewardForm.value, id: Date.now()});
      this.rewardForm.reset({pointsCost: 100, category: 'Descuento'});
    }
  }

  deleteReward(id: number | undefined) {
    this.rewards = this.rewards.filter(r => r.id !== id);
  }
}

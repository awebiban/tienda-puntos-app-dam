import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Reward } from '../../../models/Reward';
import { Store } from '../../../models/Store';
import { RewardsService } from '../../../services/rewards.service';
import { StoresService } from '../../../services/stores.service';

@Component({
  selector: 'app-rewards-editor',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './rewards-editor.component.html',
  styleUrl: './rewards-editor.component.scss'
})
export class RewardsEditorComponent implements OnInit {

  store: Store | null = null;
  rewards: Reward[] = [];
  isLoading: boolean = true;
  isSaving: boolean = false;

  // Inicializamos con un objeto que cumpla con la interfaz Reward
  newReward: Reward = {
    name: '',
    description: '',
    pointsCost: 0,
    imageUrl: '',
    isVisible: true
  };

  constructor(
    private rewardsService: RewardsService,
    private storesService: StoresService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    const ownerId = localStorage.getItem('userId');
    if (ownerId) {
      this.loadMerchantContext(Number(ownerId));
    }
  }

  loadMerchantContext(ownerId: number): void {
    this.isLoading = true;
    this.storesService.getStoresByCompanyId(ownerId).subscribe({
      next: (storeData) => {
        this.store = storeData[0]; // Asignamos el primer store del array
        if (this.store?.id) {
          this.loadRewards(this.store.id);
          this.newReward.storeId = this.store.id;
        }
      },
      error: (err) => {
        console.error('Error cargando contexto del negocio', err);
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  loadRewards(storeId: number): void {
    this.rewardsService.getRewardsByStoreId(storeId).subscribe({
      next: (data) => {
        this.rewards = data;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error cargando recompensas', err);
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  saveReward(): void {
    if (!this.newReward.name || this.newReward.pointsCost <= 0) {
      alert('Por favor, completa los campos obligatorios.');
      return;
    }

    this.isSaving = true;
    this.rewardsService.createReward(this.newReward).subscribe({
      next: (savedReward) => {
        // Al recibir el objeto del back, ya trae su ID real
        this.rewards.push(savedReward);
        this.resetForm();
        this.isSaving = false;
        alert('¡Recompensa creada con éxito!');
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al guardar recompensa', err);
        this.isSaving = false;
        this.cdr.detectChanges();
      }
    });
  }

  deleteReward(rewardId: number | undefined): void {
    if (!rewardId) return;

    if (confirm('¿Estás seguro de que quieres eliminar esta recompensa?')) {
      this.rewardsService.deleteReward(rewardId).subscribe({
        next: () => {
          this.rewards = this.rewards.filter(r => r.id !== rewardId);
          this.cdr.detectChanges();
        },
        error: (err) => console.error('Error al eliminar', err)
      });
    }
  }

  resetForm(): void {
    this.newReward = {
      storeId: this.store?.id,
      name: '',
      description: '',
      pointsCost: 0,
      imageUrl: '',
      isVisible: true
    };
  }
}

import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Reward } from '../../../models/Reward';
import { Store } from '../../../models/Store';
import { User } from '../../../models/User';
import { RewardsService } from '../../../services/rewards.service';
import { StoresService } from '../../../services/stores.service';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-store-config',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './store-config.component.html',
  styleUrl: './store-config.component.scss'
})
export class StoreConfigComponent implements OnInit {

  store: Store | null = null;
  owner: User = {} as User;
  companyId: number = 0
  isLoading: boolean = true;
  isLoadingRewards: boolean = true;
  isSaving: boolean = false;
  successMessage: boolean = false;
  rewards: Reward[] = [];

  newReward: Reward = {
    name: '',
    description: '',
    pointsCost: 100,
    isVisible: true,
    imageUrl: '',
  };

  constructor(
    private storesService: StoresService,
    private cdr: ChangeDetectorRef,
    private userService: UserService,
    private rewardsService: RewardsService,
  ) { }

  ngOnInit(): void {
    const storeId = history.state?.storeId;
    const userId = history.state?.userId;
    this.companyId = history.state?.companyId;
    if (storeId && userId && this.companyId) {
      console.log("datos para editar:", storeId, userId, this.companyId)
      this.loadStoreData(Number(storeId), Number(userId));
    }
    else {
      console.warn('No se recibió el ID de la tienda para configurar');
      this.isLoading = false;
      this.cdr.detectChanges();
    }

  }

  loadStoreData(storeId: number, userId: number): void {
    this.isLoading = true;
    this.storesService.getStoreById(storeId).subscribe({
      next: (data) => {
        this.store = data;
        this.isLoading = false;
        this.cdr.detectChanges();
        this.loadRewardData(storeId);
        this.loadOwnerData(userId);
      },
      error: (err: Error) => {
        console.error('Error al cargar configuración de la tienda', err);
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  loadOwnerData(userId: number) {
    this.isLoading = true;
    this.userService.getUserById(userId).subscribe({
      next: (data) => {
        this.owner = data;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error(err) {
        console.error("Error en la solicitud de datos del usuario")
      },
    })
    this.isLoading = false;
  }

  updateStore(): void {
    if (!this.store || !this.store.id) return;
    if (this.store && this.store.companyDTO) {
      this.store!.companyDTO!.ownerDTO = this.owner;
      this.store.companyDTO.id = this.companyId;
      console.log("datos asignados")
    }
    console.log("tienda a actualiozar", this.store)
    this.isSaving = true;
    this.storesService.updateStore(this.store!.id, this.store).subscribe({
      next: (updated: Store) => {
        this.store = updated;
        this.isSaving = false;
        this.successMessage = true;
        setTimeout(() => this.successMessage = false, 3000);
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('Error al actualizar la tienda', err);
        this.isSaving = false;
        this.cdr.detectChanges();
      }
    });
  }

  loadRewardData(storeId: number) {
    this.isLoadingRewards = true;
    this.rewardsService.getRewardsByStoreId(storeId).subscribe({
      next: (data) => {
        console.log("Listado de rewards recibido", data);
        this.rewards = data;

        if (this.store) {
          this.store.rewardsList = data;
        }

        this.isLoadingRewards = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error("Error cargando recompensas", err);
        this.isLoadingRewards = false;
      },
    });
  }

  createReward(): void {
    if (!this.store) return;

    // Clonamos el store para enviarlo actualizado con la nueva recompensa
    const rewardToAdd = { ...this.newReward };

    if (!this.store.rewardsList) {
      this.rewards = [];
    }

    // this.rewards.push(rewardToAdd as Reward);


    this.saveReward(rewardToAdd);

    // Limpiamos el formulario
    this.newReward = { name: '', pointsCost: 100, description: '', imageUrl: '', isVisible: true };
  }

  saveReward(reward: Reward) {
    this.rewardsService.createReward(reward).subscribe({
      next: (data) => {
        console.log("Recomensa creada", data)
        this.cdr.detectChanges();
      },
      error(err) {
        console.error(err)
      },
    })
  }

  deleteReward(id: number | undefined): void {
    if (!this.store || !id) return;
    this.store.rewardsList = this.store.rewardsList?.filter(r => r.id !== id);
    this.rewardsService.deleteReward(id).subscribe({
      next: (data) => {
        console.log("Recomensa borrada", data)
        this.cdr.detectChanges();
      },
      error(err) {
        console.error(err)
      },
    })
  }

}

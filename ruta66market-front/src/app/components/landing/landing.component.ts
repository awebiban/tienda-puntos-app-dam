import { Component } from '@angular/core';
import { LoyaltyCard } from '../../models/LoyaltyCard';
import { Store } from '../../models/Store';
import { User } from '../../models/User';
import { LoyaltycardsService } from '../../services/loyaltycards.service';
import { StoresService } from '../../services/stores.service';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-landing',
  imports: [],
  templateUrl: './landing.component.html',
  styleUrl: './landing.component.scss'
})
export class LandingComponent {

  constructor(
    private userService: UserService,
    private loyaltyCardService: LoyaltycardsService,
    private storesService: StoresService,
  ) { }

  user: User | null = null;

  loyaltyCards: LoyaltyCard[] = [];

  stores: Store[] = [];


  scrollCards(arg0: number) {
    throw new Error('Method not implemented.');
  }

}

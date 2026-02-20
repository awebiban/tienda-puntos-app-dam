import { Store } from "./Store";

export interface LoyaltyCard {
    id: number;
    storeDTO: Store;
    currentBalance: 120,
    totalAccumulated: number;
    lastVisited: Date;
}
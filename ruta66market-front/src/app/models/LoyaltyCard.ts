import { Store } from "./Store";

export interface LoyaltyCard {
    id: number;
    storeDTO: Store;
    currentBalance: number;
    totalAccumulated: number;
    lastVisited: Date;
}

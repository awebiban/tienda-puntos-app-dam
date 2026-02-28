import { Store } from "./Store";
import { User } from "./User";

export interface LoyaltyCard {
    id: number;
    storeDTO: Store;
    userDTO?: User
    currentBalance: number;
    totalAccumulated: number;
    lastVisited: Date;
}

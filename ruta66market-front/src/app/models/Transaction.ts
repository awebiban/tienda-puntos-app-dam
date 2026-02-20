import { LoyaltyCard } from "./LoyaltyCard";

export interface Transaction {
    id: number;
    type: string;
    amount: number;
    LoyaltyCard: LoyaltyCard;
    createdAt: string;
}

import { Plan } from "./Plan";
import { User } from "./User";

export interface Company {
    id?: number;
    legalName: string;
    cif: string;
    ownerDTO: User;
    planDTO: Plan;
    subscriptionStatus?: string;
    nextBillingDate?: string;

}
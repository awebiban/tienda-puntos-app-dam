import { Plan } from "./Plan";
import { User } from "./User";

export interface Company {
    id?: number;
    legalName: string;
    cif: string;
    owner: User;
    plan: Plan;
    subscriptionStatus?: string;
    nextBillingDate?: Date;

}
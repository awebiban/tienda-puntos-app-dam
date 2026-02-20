export interface Plan {
    id?: number;
    planName: string;
    price: number;
    maxStores: number;
    maxUsers: number;
    active: boolean;
}
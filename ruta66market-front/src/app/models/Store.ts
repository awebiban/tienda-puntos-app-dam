import { Company } from "./Company";

export interface Store {
    id?: number;
    name: string;
    category?: string;
    address?: string;
    pointsRatio?: number;
    imageUrl?: string;
    company: Company;
    isVisible: boolean;
}
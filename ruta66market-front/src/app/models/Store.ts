import { Company } from "./Company";
import { Reward } from "./Reward";

export interface Store {
    id?: number;
    name: string;
    category?: string;
    address?: string;
    pointsRatio?: number;
    imageUrl?: string;
    companyDTO?: Company; // Alineado con el DTO del backend
    isVisible: boolean;
    rewardsList?: Reward[]; // ✅ Añadido para solucionar el error
}

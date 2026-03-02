import { Company } from "./Company";
import { Reward } from "./Reward";

export interface Store {
    id?: number;
    name: string;
    category?: string;
    address?: string;
    pointsRatio?: number;
    imageUrl?: string;
    companyDTO?: Company;
    isVisible: boolean;
    rewardsList?: Reward[];
}

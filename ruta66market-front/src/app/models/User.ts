export interface User {
    id?: number;
    nickname: string;
    email: string;
    password?: string;
    role?: string;
    createdAt?: Date;
}
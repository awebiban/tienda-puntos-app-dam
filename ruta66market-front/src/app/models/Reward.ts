export interface Reward {
    id?: number;          // Opcional con '?' para que no de error al crear nuevos
    storeId?: number;     // Añadido para que el componente pueda asociarlo a la tienda
    name: string;
    description: string;
    pointsCost: number;
    imageUrl: string;
    isVisible: boolean;
}

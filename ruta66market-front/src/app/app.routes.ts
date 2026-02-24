import { Routes } from '@angular/router';

export const routes: Routes = [
    // 1. Ruta de Bienvenida / Landing / Listado de Tiendas
    {
        path: '',
        loadComponent: () => import('./components/landing/landing.component').then(m => m.LandingComponent),
    },

    // 2. Rutas de Autenticación (Login/Registro separadas)
    {
        path: 'login',
        loadComponent: () => import('./components/auth/login/login.component').then(m => m.LoginComponent),
    },
    {
        path: 'register',
        loadComponent: () => import('./components/auth/register/register.component').then(m => m.RegisterComponent),
    },

    // 3. Área del CLIENTE (Perfil y Puntos)
    {
        path: 'customer',
        children: [
            {
                path: 'dashboard',
                loadComponent: () => import('./components/customer/dashboard/dashboard.component').then(m => m.DashboardComponent),
            },
            {
                path: 'store/:storeName',
                loadComponent: () => import('./components/customer/store-detail/store-detail').then(m => m.StoreDetail),
            },
            {
                path: 'my-points',
                loadComponent: () => import('./components/customer/points-history/points-history.component').then(m => m.PointsHistoryComponent),
            },
        ]
    },

    // 4. Área de la EMPRESA (Gestión de Tienda)
    {
        path: 'business',
        children: [
            {
                // NUEVA RUTA: Panel principal para sumar puntos
                path: 'dashboard',
                loadComponent: () => import('./components/business/merchant-dashboard/merchant-dashboard').then(m => m.MerchantDashboardComponent),
            },
            {
                path: 'setup-store',
                loadComponent: () => import('./components/business/store-config/store-config.component').then(m => m.StoreConfigComponent),
            },
            {
                path: 'manage-rewards',
                loadComponent: () => import('./components/business/rewards-editor/rewards-editor.component').then(m => m.RewardsEditorComponent),
            },
            {
                path: 'analytics',
                loadComponent: () => import('./components/business/analytics/analytics.component').then(m => m.AnalyticsComponent),
            },
        ]
    },

    // 5. Manejo de errores (404)
    {
        path: '**',
        redirectTo: '',
        pathMatch: 'full'
    }
];

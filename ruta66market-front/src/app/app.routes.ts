import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';
import { roleGuard } from './guards/role.guard';

export const routes: Routes = [
    // 1. Ruta de Bienvenida / Landing / Listado de Tiendas (PÚBLICA)
    {
        path: '',
        loadComponent: () => import('./components/landing/landing.component').then(m => m.LandingComponent),
    },

    // 2. Rutas de Autenticación (PÚBLICAS)
    {
        path: 'login',
        loadComponent: () => import('./components/auth/login/login.component').then(m => m.LoginComponent),
    },
    {
        path: 'register',
        loadComponent: () => import('./components/auth/register/register.component').then(m => m.RegisterComponent),
    },

    // 3. Área del CLIENTE (PROTEGIDA)
    {
        path: 'customer',
        canActivate: [authGuard, roleGuard], // <-- Aplicamos los vigilantes
        data: { expectedRole: 'CLIENTE' },   // <-- Exigimos rol de Cliente
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
            {
                path: 'register-business',
                loadComponent: () => import('./components/customer/register-business/register-business.component').then(m => m.RegisterBusinessComponent),
            },
        ]
    },

    // 4. Área de la EMPRESA (PROTEGIDA)
    {
        path: 'business',
        canActivate: [authGuard, roleGuard],    // <-- Aplicamos los vigilantes
        data: { expectedRole: 'ADMIN_NEGOCIO' }, // <-- Exigimos rol de Negocio
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

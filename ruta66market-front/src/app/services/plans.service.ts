import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { development } from '../models/environments/environment';
import { Plan } from '../models/Plan';

@Injectable({
  providedIn: 'root'
})
export class PlansService {

  private dev = development.url;

  constructor(private http: HttpClient) { }

  getAllActivePlans(): Observable<Plan[]> {
    return this.http.get<Plan[]>(`${this.dev}/plans`).pipe(
      tap(data => console.log(`%c[GET] /plans %cLista de planes:`, 'color: #8b5cf6; font-weight: bold', 'color: gray', data))
    );
  }

  // async initPayPal(plan: Plan) {
  //   const paypal = await loadScript({ "client-id": "ASY_ffVdiHCTjU5ZFTydaJvkin5K8CaD8IWiZ6nEW-3Bi5_v5BBZdwjcpgIlHzIZXghGAhEtdldycXUO" });

  //   if (paypal && paypal.Buttons) {
  //     paypal.Buttons({
  //       createOrder: (data, actions) => {
  //         // Aquí llamarías a tu Backend para crear la orden
  //         return this.http.post<any>(`${this.dev}/paypal/create/${plan.id}`, {}).toPromise()
  //           .then(res => res.id);
  //       },
  //       onApprove: (data, actions) => {
  //         // El usuario pagó, ahora confirmamos en el Backend
  //         return this.http.post<any>(`${this.dev}/paypal/capture/${data.orderID}`, {}).toPromise()
  //           .then(res => {
  //             Swal.fire('¡Éxito!', 'Tu plan ha sido actualizado.', 'success');
  //           });
  //       }
  //     }).render("#paypal-button-container");
  //   }
  // }
}

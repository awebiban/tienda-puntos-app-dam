import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  // Canal de comunicación para los puntos
  private pointsSource = new Subject<number>();

  // Observable que los componentes (como el Navbar) pueden "escuchar"
  points$ = this.pointsSource.asObservable();

  // Método para disparar la notificación
  notifyNewPoints(amount: number) {
    this.pointsSource.next(amount);
  }
}

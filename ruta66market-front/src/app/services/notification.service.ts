import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private pointsSource = new Subject<number>();

  points$ = this.pointsSource.asObservable();

  notifyNewPoints(amount: number) {
    this.pointsSource.next(amount);
  }
}

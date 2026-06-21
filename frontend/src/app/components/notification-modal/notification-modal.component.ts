import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationService, Notification } from '../../services/notification.service';

@Component({
  selector: 'app-notification-modal',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="notification-container">
      <div *ngFor="let notification of notifications" 
           [ngClass]="'notification notification-' + notification.type"
           [@slideIn]>
        <div class="notification-content">
          <span class="notification-icon">
            <span *ngIf="notification.type === 'success'">✓</span>
            <span *ngIf="notification.type === 'error'">✕</span>
            <span *ngIf="notification.type === 'info'">ℹ</span>
            <span *ngIf="notification.type === 'warning'">!</span>
          </span>
          <span class="notification-message">{{ notification.message }}</span>
        </div>
        <div class="notification-progress" 
             [style.animation]="'shrink ' + (notification.duration || 3000) + 'ms linear forwards'"></div>
      </div>
    </div>
  `,
  styles: [`
    .notification-container {
      position: fixed;
      top: 20px;
      right: 20px;
      z-index: 10000;
      display: flex;
      flex-direction: column;
      gap: 10px;
      max-width: 400px;
    }

    .notification {
      display: flex;
      flex-direction: column;
      border-radius: 8px;
      padding: 16px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
      animation: slideIn 0.3s ease-out;
      overflow: hidden;
    }

    .notification-content {
      display: flex;
      align-items: center;
      gap: 12px;
      font-size: 14px;
      font-weight: 500;
    }

    .notification-icon {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 24px;
      height: 24px;
      border-radius: 50%;
      font-weight: bold;
      flex-shrink: 0;
    }

    .notification-message {
      flex-grow: 1;
      word-wrap: break-word;
    }

    .notification-progress {
      height: 3px;
      background: rgba(0, 0, 0, 0.1);
      margin-top: 8px;
    }

    /* Success */
    .notification-success {
      background: #d4edda;
      color: #155724;
      border-left: 4px solid #28a745;
    }

    .notification-success .notification-icon {
      background: #28a745;
      color: white;
    }

    .notification-success .notification-progress {
      background: #28a745;
    }

    /* Error */
    .notification-error {
      background: #f8d7da;
      color: #721c24;
      border-left: 4px solid #dc3545;
    }

    .notification-error .notification-icon {
      background: #dc3545;
      color: white;
    }

    .notification-error .notification-progress {
      background: #dc3545;
    }

    /* Info */
    .notification-info {
      background: #d1ecf1;
      color: #0c5460;
      border-left: 4px solid #17a2b8;
    }

    .notification-info .notification-icon {
      background: #17a2b8;
      color: white;
    }

    .notification-info .notification-progress {
      background: #17a2b8;
    }

    /* Warning */
    .notification-warning {
      background: #fff3cd;
      color: #856404;
      border-left: 4px solid #ffc107;
    }

    .notification-warning .notification-icon {
      background: #ffc107;
      color: white;
    }

    .notification-warning .notification-progress {
      background: #ffc107;
    }

    @keyframes slideIn {
      from {
        transform: translateX(400px);
        opacity: 0;
      }
      to {
        transform: translateX(0);
        opacity: 1;
      }
    }

    @keyframes shrink {
      from {
        transform: scaleX(1);
      }
      to {
        transform: scaleX(0);
      }
    }
  `]
})
export class NotificationModalComponent implements OnInit {
  notifications: Notification[] = [];

  constructor(private notificationService: NotificationService) {}

  ngOnInit() {
    this.notificationService.notifications$.subscribe(notifications => {
      this.notifications = notifications;
    });
  }
}

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { NotificationModalComponent } from './components/notification-modal/notification-modal.component';
import { ApiService } from './services/api.service';
import { NotificationService } from './services/notification.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule, DashboardComponent, NotificationModalComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [ApiService, NotificationService]
})
export class AppComponent implements OnInit {
  title = 'Calorie Tracker & Macro Dashboard';
  currentUserId: number = 1; // Default user ID
  isLoggedIn: boolean = false;
  showLoginForm: boolean = true;

  username: string = '';
  email: string = '';
  password: string = '';
  loginUsername: string = '';
  loginPassword: string = '';

  constructor(private apiService: ApiService, private notificationService: NotificationService) {}

  ngOnInit(): void {
    // Check if user is already logged in (from localStorage)
    const storedUserId = localStorage.getItem('userId');
    if (storedUserId) {
      this.currentUserId = parseInt(storedUserId);
      this.isLoggedIn = true;
      this.showLoginForm = false;
    }
  }

  registerUser(): void {
    if (!this.username || !this.email || !this.password) {
      this.notificationService.warning('Please fill all fields');
      return;
    }

    this.apiService.registerUser(this.username, this.email, this.password)
      .subscribe({
        next: (user) => {
          this.notificationService.success('User registered successfully!');
          this.currentUserId = user.userId;
          localStorage.setItem('userId', user.userId.toString());
          this.isLoggedIn = true;
          this.showLoginForm = false;
          this.username = '';
          this.email = '';
          this.password = '';
        },
        error: (error) => {
          this.notificationService.error('Registration failed: ' + error.error.error);
        }
      });
  }

  loginUser(): void {
    if (!this.loginUsername || !this.loginPassword) {
      this.notificationService.warning('Please fill all fields');
      return;
    }

    this.apiService.loginUser(this.loginUsername, this.loginPassword)
      .subscribe({
        next: (user) => {
          this.notificationService.success('Login successful!');
          this.currentUserId = user.userId;
          localStorage.setItem('userId', user.userId.toString());
          this.isLoggedIn = true;
          this.showLoginForm = false;
          this.loginUsername = '';
          this.loginPassword = '';
        },
        error: (error) => {
          this.notificationService.error('Login failed: ' + (error.error?.error || 'Invalid credentials'));
        }
      });
  }

  logout(): void {
    localStorage.removeItem('userId');
    this.isLoggedIn = false;
    this.showLoginForm = true;
    this.username = '';
    this.email = '';
    this.password = '';
    this.loginUsername = '';
    this.loginPassword = '';
  }

  toggleLoginRegister(): void {
    this.showLoginForm = !this.showLoginForm;
  }
}

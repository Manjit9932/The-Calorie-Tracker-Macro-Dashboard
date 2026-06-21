import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  // ==================== USER ENDPOINTS ====================

  registerUser(username: string, email: string, password: string): Observable<any> {
    let params = new HttpParams()
      .set('username', username)
      .set('email', email)
      .set('password', password);
    return this.http.post(`${this.apiUrl}/users/register`, null, { params });
  }

  loginUser(username: string, password: string): Observable<any> {
    let params = new HttpParams()
      .set('username', username)
      .set('password', password);
    return this.http.post(`${this.apiUrl}/users/login`, null, { params });
  }

  getUser(userId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/users/${userId}`);
  }

  updateFitnessGoal(userId: number, goalType: string): Observable<any> {
    let params = new HttpParams().set('goalType', goalType);
    return this.http.put(`${this.apiUrl}/users/${userId}/fitness-goal`, null, { params });
  }

  getUserFitnessGoals(userId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/users/${userId}/fitness-goals`);
  }

  // ==================== MEAL ENDPOINTS ====================

  addMeal(userId: number, foodId: number, portionWeight: number, mealType: string = 'SNACK'): Observable<any> {
    let params = new HttpParams()
      .set('userId', userId.toString())
      .set('foodId', foodId.toString())
      .set('portionWeightGrams', portionWeight.toString())
      .set('mealType', mealType);
    return this.http.post(`${this.apiUrl}/meals/add`, null, { params });
  }

  addMealFromImage(userId: number, mockFoodName: string, mockPortionWeight: number): Observable<any> {
    let params = new HttpParams()
      .set('userId', userId.toString())
      .set('mockFoodName', mockFoodName)
      .set('mockPortionWeight', mockPortionWeight.toString());
    return this.http.post(`${this.apiUrl}/meals/add-from-image`, null, { params });
  }

  deleteMeal(entryId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/meals/${entryId}`);
  }

  getTodayLog(userId: number): Observable<any> {
    let params = new HttpParams().set('userId', userId.toString());
    return this.http.get(`${this.apiUrl}/meals/today`, { params });
  }

  getLogByDate(userId: number, date: string): Observable<any> {
    let params = new HttpParams()
      .set('userId', userId.toString())
      .set('date', date);
    return this.http.get(`${this.apiUrl}/meals/by-date`, { params });
  }

  // ==================== FOOD ENDPOINTS ====================

  getAllFoods(): Observable<any> {
    return this.http.get(`${this.apiUrl}/foods`);
  }

  getFoodById(foodId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/foods/${foodId}`);
  }

  searchFoods(name: string): Observable<any> {
    let params = new HttpParams().set('name', name);
    return this.http.get(`${this.apiUrl}/foods/search`, { params });
  }

  getFoodsByCategory(category: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/foods/category/${category}`);
  }
}

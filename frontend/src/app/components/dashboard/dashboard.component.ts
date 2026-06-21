import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  @Input() userId: number = 1;

  // Data
  dailyLog: any = null;
  foods: any[] = [];
  fitnessGoals: any[] = [];
  selectedFitnessGoal: string = 'MAINTENANCE';

  // Form inputs
  selectedFood: number | null = null;
  portionWeight: number = 100;
  searchFoodName: string = '';
  filteredFoods: any[] = [];

  // UI State
  showBudgetExceededWarning: boolean = false;
  caloriePercentage: number = 0;
  isLoading: boolean = false;

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    this.loadAllFoods();
    this.loadTodayLog();
    this.loadFitnessGoals();
  }

  /**
   * Load all foods from backend
   */
  loadAllFoods(): void {
    this.apiService.getAllFoods().subscribe({
      next: (foods) => {
        this.foods = foods;
        this.filteredFoods = foods;
      },
      error: (error) => {
        console.error('Error loading foods:', error);
        this.showDefaultFoods();
      }
    });
  }

  /**
   * Show default foods if API fails
   */
  showDefaultFoods(): void {
    this.foods = [
      { foodId: 1, foodName: 'Chicken Breast', caloriesPer100g: 165, proteinPer100g: 31, carbsPer100g: 0, fatsPer100g: 3.6 },
      { foodId: 2, foodName: 'Brown Rice', caloriesPer100g: 111, proteinPer100g: 2.6, carbsPer100g: 23, fatsPer100g: 0.9 },
      { foodId: 3, foodName: 'Egg', caloriesPer100g: 155, proteinPer100g: 13, carbsPer100g: 1.1, fatsPer100g: 11 },
      { foodId: 4, foodName: 'Broccoli', caloriesPer100g: 34, proteinPer100g: 2.8, carbsPer100g: 7, fatsPer100g: 0.4 },
      { foodId: 5, foodName: 'Apple', caloriesPer100g: 52, proteinPer100g: 0.3, carbsPer100g: 14, fatsPer100g: 0.2 }
    ];
    this.filteredFoods = this.foods;
  }

  /**
   * Load today's meal log
   */
  loadTodayLog(): void {
    this.isLoading = true;
    this.apiService.getTodayLog(this.userId).subscribe({
      next: (log) => {
        this.dailyLog = log;
        this.updateCaloriePercentage();
        this.showBudgetExceededWarning = log.isBudgetExceeded;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading daily log:', error);
        this.initializeEmptyLog();
        this.isLoading = false;
      }
    });
  }

  /**
   * Initialize empty daily log
   */
  initializeEmptyLog(): void {
    this.dailyLog = {
      logId: 0,
      userId: this.userId,
      totalCalories: 0,
      totalProteinGrams: 0,
      totalCarbsGrams: 0,
      totalFatsGrams: 0,
      isBudgetExceeded: false,
      dailyCalorieTarget: 2000,
      caloriePercentage: 0,
      meals: []
    };
  }

  /**
   * Load fitness goals
   */
  loadFitnessGoals(): void {
    this.apiService.getUserFitnessGoals(this.userId).subscribe({
      next: (goals) => {
        this.fitnessGoals = goals;
      },
      error: (error) => {
        console.error('Error loading fitness goals:', error);
        this.showDefaultFitnessGoals();
      }
    });
  }

  /**
   * Show default fitness goals if API fails
   */
  showDefaultFitnessGoals(): void {
    this.fitnessGoals = [
      { goalType: 'WEIGHT_LOSS', dailyCalorieTarget: 2000, description: 'Weight Loss: 20% deficit' },
      { goalType: 'MAINTENANCE', dailyCalorieTarget: 2000, description: 'Maintenance: Balanced' },
      { goalType: 'MUSCLE_GAIN', dailyCalorieTarget: 3000, description: 'Muscle Gain: 20% surplus' }
    ];
  }

  /**
   * Change fitness goal
   */
  changeFitnessGoal(goalType: string): void {
    this.selectedFitnessGoal = goalType;
    this.apiService.updateFitnessGoal(this.userId, goalType).subscribe({
      next: (user) => {
        this.dailyLog.dailyCalorieTarget = this.fitnessGoals.find(g => g.goalType === goalType).dailyCalorieTarget;
        this.updateCaloriePercentage();
        console.log('Fitness goal updated:', goalType);
      },
      error: (error) => {
        console.error('Error updating fitness goal:', error);
      }
    });
  }

  /**
   * Search foods by name
   */
  searchFoods(): void {
    if (this.searchFoodName.trim() === '') {
      this.filteredFoods = this.foods;
    } else {
      this.filteredFoods = this.foods.filter(food =>
        food.foodName.toLowerCase().includes(this.searchFoodName.toLowerCase())
      );
    }
  }

  /**
   * Add meal to daily log
   */
  addMeal(): void {
    if (!this.selectedFood || !this.portionWeight) {
      alert('Please select a food and enter portion weight');
      return;
    }

    this.isLoading = true;

    this.apiService.addMeal(this.userId, this.selectedFood, this.portionWeight, 'SNACK')
      .subscribe({
        next: (meal) => {
          console.log('Meal added:', meal);
          this.loadTodayLog();
          this.selectedFood = null;
          this.portionWeight = 100;
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error adding meal:', error);
          alert('Error adding meal: ' + error.error.error);
          this.isLoading = false;
        }
      });
  }

  /**
   * Simulate image upload (add mock meal)
   */
  simulateImageUpload(): void {
    const mockFoods = [
      { name: 'Chicken Breast', weight: 150 },
      { name: 'Brown Rice', weight: 150 },
      { name: 'Broccoli', weight: 200 },
      { name: 'Apple', weight: 180 }
    ];

    const randomMock = mockFoods[Math.floor(Math.random() * mockFoods.length)];

    this.isLoading = true;

    this.apiService.addMealFromImage(this.userId, randomMock.name, randomMock.weight)
      .subscribe({
        next: (meal) => {
          console.log('Meal from image added:', meal);
          this.loadTodayLog();
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error adding meal from image:', error);
          alert('Error: ' + error.error.error);
          this.isLoading = false;
        }
      });
  }

  /**
   * Delete meal from log
   */
  deleteMeal(entryId: number): void {
    if (confirm('Are you sure you want to delete this meal?')) {
      this.apiService.deleteMeal(entryId).subscribe({
        next: () => {
          console.log('Meal deleted');
          this.loadTodayLog();
        },
        error: (error) => {
          console.error('Error deleting meal:', error);
          alert('Error deleting meal');
        }
      });
    }
  }

  /**
   * Update calorie percentage
   */
  updateCaloriePercentage(): void {
    if (this.dailyLog) {
      this.caloriePercentage = (this.dailyLog.totalCalories / this.dailyLog.dailyCalorieTarget) * 100;
      if (this.caloriePercentage > 100) {
        this.caloriePercentage = 100;
      }
    }
  }

  /**
   * Get calorie bar color
   */
  getCalorieBarColor(): string {
    if (this.dailyLog && this.dailyLog.isBudgetExceeded) {
      return '#e74c3c'; // Red
    }
    return '#667eea'; // Blue/Purple
  }

  /**
   * Get macro percentage
   */
  getMacroPercentage(value: number, target: number): number {
    if (target === 0) return 0;
    const percentage = (value / target) * 100;
    return percentage > 100 ? 100 : percentage;
  }
}

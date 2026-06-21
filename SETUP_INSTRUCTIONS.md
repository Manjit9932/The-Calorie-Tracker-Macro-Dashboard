# The Calorie Tracker & Macro Dashboard

A comprehensive health-tracking application that serves as a user's daily food journal. The application calculates nutritional intake in real time, manages a strict calorie budget, and visually warns users if they overeat.

## ✨ Features

### Core Features
- **Daily Calorie Tracking**: Real-time calorie budget tracking with visual progress bars
- **Macronutrient Breakdown**: Monitor Protein, Carbs, and Fats intake
- **Fitness Goal Toggle**: Switch between Weight Loss, Maintenance, and Muscle Gain goals
- **Meal Logging**: Log meals by searching for food items and entering portion sizes
- **Image Upload Simulation**: Simulate AI food photo scanning with mock data
- **Budget Warning**: Crimson red alert when budget is exceeded
- **Meal History**: View and delete logged meals with instant recalculation

### Technical Stack
- **Backend**: Spring Boot 3.1.5, Java 17
- **Frontend**: Angular 17
- **Database**: MySQL 8.0
- **ORM**: JPA/Hibernate

## 🗂️ Project Structure

```
Calorie Tracker/
├── backend/                          # Spring Boot Backend
│   ├── src/main/java/com/calorietracker/
│   │   ├── CalorieTrackerApplication.java
│   │   ├── config/                   # Configuration classes
│   │   │   └── CorsConfig.java
│   │   ├── controller/               # REST API Controllers
│   │   │   ├── MealController.java
│   │   │   ├── UserController.java
│   │   │   └── FoodController.java
│   │   ├── model/                    # JPA Entity Models
│   │   │   ├── User.java
│   │   │   ├── FitnessGoal.java
│   │   │   ├── FoodDatabase.java
│   │   │   ├── DailyLog.java
│   │   │   ├── MealEntry.java
│   │   │   ├── FitnessGoalType.java
│   │   │   └── MealType.java
│   │   ├── repository/               # Spring Data JPA Repositories
│   │   │   ├── UserRepository.java
│   │   │   ├── FitnessGoalRepository.java
│   │   │   ├── FoodDatabaseRepository.java
│   │   │   ├── DailyLogRepository.java
│   │   │   └── MealEntryRepository.java
│   │   ├── service/                  # Business Logic Services
│   │   │   ├── UserService.java
│   │   │   └── MealService.java
│   │   └── dto/                      # Data Transfer Objects
│   │       ├── MealEntryDTO.java
│   │       └── DailyLogDTO.java
│   ├── src/main/resources/
│   │   └── application.properties    # Application Configuration
│   └── pom.xml                       # Maven Dependencies
├── frontend/                         # Angular Frontend
│   ├── src/
│   │   ├── app/
│   │   │   ├── app.component.ts      # Root Component
│   │   │   ├── app.component.html
│   │   │   ├── app.component.css
│   │   │   ├── components/
│   │   │   │   └── dashboard/        # Main Dashboard Component
│   │   │   │       ├── dashboard.component.ts
│   │   │   │       ├── dashboard.component.html
│   │   │   │       └── dashboard.component.css
│   │   │   └── services/
│   │   │       └── api.service.ts    # HTTP API Service
│   │   ├── main.ts                   # Angular Entry Point
│   │   ├── index.html                # HTML Template
│   │   └── styles.css                # Global Styles
│   ├── angular.json                  # Angular CLI Configuration
│   ├── tsconfig.json                 # TypeScript Configuration
│   └── package.json                  # NPM Dependencies
├── database-schema.sql               # MySQL Database Schema
└── README.md
```

## 🚀 Quick Start Guide

### Prerequisites
- Java 17 or higher
- Node.js 18+ and npm
- MySQL 8.0+
- Git

### Step 1: Backend Setup

1. **Navigate to backend folder**:
   ```bash
   cd backend
   ```

2. **Install Maven dependencies** (if you haven't already):
   ```bash
   mvn clean install
   ```

3. **Run the Spring Boot application**:
   ```bash
   mvn spring-boot:run
   ```
   
   The backend will start on `http://localhost:8080/api`

### Step 2: Frontend Setup

1. **Open a new terminal and navigate to frontend folder**:
   ```bash
   cd frontend
   ```

2. **Install Node dependencies**:
   ```bash
   npm install
   ```

3. **Start the Angular development server**:
   ```bash
   npm start
   ```
   
   The frontend will automatically open at `http://localhost:4200`

### Step 3: Database Configuration

The database has been pre-configured with the following credentials:
- **Host**: localhost
- **Port**: 3306
- **Username**: root
- **Password**: Manjit@17032003
- **Database**: calorie_tracker_db

Tables are automatically validated by JPA on startup.

## 📱 How to Use the Application

### 1. Registration/Login
- Create a new account or log in
- Default demo user ID is set to 1 if no user is selected

### 2. Select Fitness Goal
- Choose between **Weight Loss**, **Maintenance**, or **Muscle Gain**
- The daily calorie target and macro targets update automatically

### 3. Log Meals
- **Method 1**: Search for food by name and enter portion weight
- **Method 2**: Click "Image Upload" to simulate AI food photo scanning

### 4. View Progress
- **Calorie Budget Bar**: Shows percentage of daily calories consumed
- **Color Indicator**: Green/Blue when under budget, Red when exceeded
- **Macro Breakdown**: Real-time tracking of Protein, Carbs, and Fats

### 5. Manage Meals
- View all logged meals for today
- Delete meals to recalculate totals instantly

## 🔌 API Endpoints

### User Endpoints
- `POST /api/users/register` - Create new user
- `GET /api/users/{userId}` - Get user details
- `PUT /api/users/{userId}/fitness-goal` - Update fitness goal
- `GET /api/users/{userId}/fitness-goals` - Get all user fitness goals

### Meal Endpoints
- `POST /api/meals/add` - Add meal entry
- `POST /api/meals/add-from-image` - Add meal from image (mock)
- `DELETE /api/meals/{entryId}` - Delete meal entry
- `GET /api/meals/today` - Get today's log
- `GET /api/meals/by-date` - Get log for specific date

### Food Endpoints
- `GET /api/foods` - Get all foods
- `GET /api/foods/{foodId}` - Get food details
- `GET /api/foods/search?name=X` - Search foods by name
- `GET /api/foods/category/{category}` - Get foods by category

## 🧮 Nutrient Scaling Algorithm

The application uses a 100g baseline for all foods. When a user logs a meal:

1. Portion weight is divided by 100
2. Each nutritional value is multiplied by this ratio
3. Results are calculated to 2 decimal places

**Example**:
- Chicken Breast: 165 cal per 100g
- User logs: 150g
- Calculated: 165 × (150/100) = 247.5 calories

## 🎨 UI Features

- **Responsive Design**: Works on desktop, tablet, and mobile
- **Real-time Updates**: All values update instantly
- **Color-coded Progress Bars**: Visual indicators for nutritional intake
- **Warning Modal**: Pops up when daily budget is exceeded
- **Smooth Animations**: Transitions and loading states

## 🔐 Security Notes

- Use HTTPS in production
- Hash passwords with bcrypt (currently using plain text for demo)
- Implement proper authentication/JWT tokens
- Validate all inputs on backend

## 📊 Database Schema

### Tables
1. **users** - User accounts and preferences
2. **fitness_goals** - Goal configurations (Weight Loss/Maintenance/Muscle Gain)
3. **food_database** - Nutrition reference data (25+ pre-loaded foods)
4. **daily_logs** - Daily summaries per user
5. **meal_entries** - Individual logged meals

## 🐛 Troubleshooting

### Backend won't start
- Check if port 8080 is available
- Verify MySQL is running and database exists
- Check application.properties credentials

### Frontend won't connect to backend
- Verify backend is running on http://localhost:8080/api
- Check CORS configuration in CorsConfig.java
- Open browser DevTools (F12) to check Network tab for errors

### No foods showing up
- Verify the food_database table has data
- Check the API response in browser DevTools
- Default foods are shown if API fails

### Meal won't delete
- Verify meal entry exists
- Check browser console for errors
- Ensure valid entryId is provided

## 🚀 Production Deployment

### Backend
1. Build with `mvn clean package`
2. Deploy `target/calorie-tracker-backend-1.0.0.jar`
3. Set environment variables for database credentials
4. Use production MySQL database

### Frontend
1. Build with `ng build --configuration production`
2. Deploy `dist/calorie-tracker-frontend/` folder
3. Configure web server (nginx, Apache) to serve Angular files

## 📝 Future Enhancements

- User authentication with JWT
- Food photo recognition with AI
- Weekly/monthly analytics and reports
- Customizable macro targets
- Social sharing features
- Mobile app with offline support
- Recipe database
- Barcode scanning

## 📧 Support

For issues or questions, please refer to the backend logs or browser DevTools console.

## 📄 License

This project is proprietary and confidential.

---

**Happy tracking! 🎯**

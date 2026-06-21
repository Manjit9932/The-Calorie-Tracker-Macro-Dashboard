# The Calorie Tracker & Macro Dashboard

A comprehensive health-tracking application that serves as a user's daily food journal. The application calculates nutritional intake in real time, manages a strict calorie budget, and visually warns the user if they overeat.

## ✨ Quick Features
- 📊 Real-time calorie tracking with visual progress bars
- 🥗 Macronutrient breakdown (Protein, Carbs, Fats)
- 🎯 Fitness goal toggle (Weight Loss, Maintenance, Muscle Gain)
- 🍽️ Meal logging with food search
- 📷 AI food photo scanner simulation
- ⚠️ Budget exceeded alerts
- 🗑️ Instant meal deletion with recalculation

## 🚀 Quick Start

**⚠️ IMPORTANT: Please read [SETUP_INSTRUCTIONS.md](SETUP_INSTRUCTIONS.md) for complete installation and setup guide.**

### Prerequisites
- Java 17+
- Node.js 18+
- MySQL 8.0+

### Quick Commands

**Backend (Terminal 1)**:
```bash
cd backend
mvn spring-boot:run
```

**Frontend (Terminal 2)**:
```bash
cd frontend
npm install
npm start
```

The application will open at `http://localhost:4200`

## 📁 Project Structure

```
├── backend/              # Spring Boot REST API
├── frontend/             # Angular 17 Web App
├── database-schema.sql   # MySQL Schema
└── SETUP_INSTRUCTIONS.md # Full setup guide
```

## 🗄️ Database

Pre-configured database `calorie_tracker_db` with:
- 5 tables (users, fitness_goals, food_database, daily_logs, meal_entries)
- 25+ pre-loaded foods with nutrition data
- User and fitness goal management

## 📊 Tech Stack

| Layer | Technology |
|-------|-----------|
| **Frontend** | Angular 17, TypeScript, RxJS |
| **Backend** | Spring Boot 3.1.5, Java 17 |
| **Database** | MySQL 8.0 |
| **ORM** | JPA/Hibernate |

## 🔧 Configuration

Database credentials (pre-configured):
- **Host**: localhost
- **Port**: 3306
- **Username**: root
- **Password**: Manjit@17032003
- **Database**: calorie_tracker_db

Backend runs on: `http://localhost:8080/api`
Frontend runs on: `http://localhost:4200`

## 📖 Documentation

For detailed setup, troubleshooting, and deployment guide, see **[SETUP_INSTRUCTIONS.md](SETUP_INSTRUCTIONS.md)**

---

**Status**: ✅ Fully Functional | **Version**: 1.0.0 | **Last Updated**: 2026-06-21

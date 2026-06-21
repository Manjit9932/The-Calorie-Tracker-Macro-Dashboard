-- ============================================
-- CALORIE TRACKER DATABASE SCHEMA
-- ============================================

-- Create Users Table
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    daily_calorie_target INT DEFAULT 2000,
    current_fitness_goal ENUM('WEIGHT_LOSS', 'MAINTENANCE', 'MUSCLE_GAIN') DEFAULT 'MAINTENANCE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_username (username)
);

-- Create Fitness Goals Reference Table
-- This stores predefined targets for different fitness goals and body types
CREATE TABLE fitness_goals (
    goal_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    goal_type ENUM('WEIGHT_LOSS', 'MAINTENANCE', 'MUSCLE_GAIN') NOT NULL,
    daily_calorie_target INT NOT NULL,
    daily_protein_target_grams DECIMAL(10, 2) NOT NULL,
    daily_carbs_target_grams DECIMAL(10, 2) NOT NULL,
    daily_fats_target_grams DECIMAL(10, 2) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_goal (user_id, goal_type),
    INDEX idx_user_id (user_id)
);

-- Create Food Database (Reference Data)
-- This stores nutritional information for common foods
CREATE TABLE food_database (
    food_id INT PRIMARY KEY AUTO_INCREMENT,
    food_name VARCHAR(150) NOT NULL UNIQUE,
    calories_per_100g DECIMAL(10, 2) NOT NULL,
    protein_per_100g DECIMAL(10, 2) NOT NULL,
    carbs_per_100g DECIMAL(10, 2) NOT NULL,
    fats_per_100g DECIMAL(10, 2) NOT NULL,
    food_category VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_food_name (food_name),
    INDEX idx_category (food_category),
    INDEX idx_is_active (is_active)
);

-- Create Daily Logs Table
-- Each user has one log entry per day
CREATE TABLE daily_logs (
    log_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    log_date DATE NOT NULL,
    total_calories DECIMAL(10, 2) DEFAULT 0,
    total_protein_grams DECIMAL(10, 2) DEFAULT 0,
    total_carbs_grams DECIMAL(10, 2) DEFAULT 0,
    total_fats_grams DECIMAL(10, 2) DEFAULT 0,
    is_budget_exceeded BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_date (user_id, log_date),
    INDEX idx_user_id (user_id),
    INDEX idx_log_date (log_date)
);

-- Create Meal Entries Table
-- Individual food items logged by user
CREATE TABLE meal_entries (
    entry_id INT PRIMARY KEY AUTO_INCREMENT,
    log_id INT NOT NULL,
    user_id INT NOT NULL,
    food_id INT NOT NULL,
    portion_weight_grams DECIMAL(10, 2) NOT NULL,
    calories DECIMAL(10, 2) NOT NULL,
    protein_grams DECIMAL(10, 2) NOT NULL,
    carbs_grams DECIMAL(10, 2) NOT NULL,
    fats_grams DECIMAL(10, 2) NOT NULL,
    meal_time TIME DEFAULT NULL,
    meal_type ENUM('BREAKFAST', 'LUNCH', 'DINNER', 'SNACK') DEFAULT 'SNACK',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (log_id) REFERENCES daily_logs(log_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (food_id) REFERENCES food_database(food_id),
    INDEX idx_log_id (log_id),
    INDEX idx_user_id (user_id),
    INDEX idx_food_id (food_id)
);

-- ============================================
-- INSERT SAMPLE FOOD DATA
-- ============================================

INSERT INTO food_database (food_name, calories_per_100g, protein_per_100g, carbs_per_100g, fats_per_100g, food_category) VALUES
-- Proteins
('Chicken Breast', 165, 31, 0, 3.6, 'PROTEIN'),
('Salmon', 208, 20, 0, 13, 'PROTEIN'),
('Egg', 155, 13, 1.1, 11, 'PROTEIN'),
('Beef (Lean)', 250, 26, 0, 15, 'PROTEIN'),
('Tofu', 76, 8, 1.9, 4.8, 'PROTEIN'),

-- Carbohydrates
('Brown Rice', 111, 2.6, 23, 0.9, 'CARBS'),
('Oats', 389, 17, 66, 7, 'CARBS'),
('Sweet Potato', 86, 1.6, 20, 0.1, 'CARBS'),
('Whole Wheat Bread', 247, 13, 41, 3.3, 'CARBS'),
('Banana', 89, 1.1, 23, 0.3, 'CARBS'),

-- Vegetables
('Broccoli', 34, 2.8, 7, 0.4, 'VEGETABLES'),
('Spinach', 23, 2.7, 3.6, 0.4, 'VEGETABLES'),
('Carrot', 41, 0.9, 10, 0.2, 'VEGETABLES'),
('Tomato', 18, 0.9, 3.9, 0.2, 'VEGETABLES'),
('Bell Pepper', 31, 1, 7.3, 0.3, 'VEGETABLES'),

-- Fruits
('Apple', 52, 0.3, 14, 0.2, 'FRUITS'),
('Orange', 47, 0.9, 12, 0.1, 'FRUITS'),
('Blueberry', 57, 0.7, 14, 0.3, 'FRUITS'),
('Strawberry', 32, 0.7, 8, 0.3, 'FRUITS'),
('Watermelon', 30, 0.6, 8, 0.2, 'FRUITS'),

-- Dairy
('Greek Yogurt', 59, 10, 3.3, 0.4, 'DAIRY'),
('Milk (Whole)', 61, 3.2, 4.8, 3.3, 'DAIRY'),
('Cheese (Cheddar)', 403, 23, 1.3, 33, 'DAIRY'),

-- Fats & Oils
('Olive Oil', 884, 0, 0, 100, 'FATS'),
('Almonds', 579, 21, 22, 50, 'FATS'),
('Peanut Butter', 588, 25, 20, 50, 'FATS');

-- ============================================
-- INSERT SAMPLE FITNESS GOALS
-- ============================================
-- These will be inserted dynamically when user registers
-- But here's the template for different goals:

-- Weight Loss: 300-500 calorie deficit
-- Maintenance: 1.0x TDEE
-- Muscle Gain: 300-500 calorie surplus

-- Example for a user with 2500 TDEE:
-- Weight Loss: 2000 calories, 150g protein, 150g carbs, 50g fats
-- Maintenance: 2500 calories, 140g protein, 280g carbs, 80g fats
-- Muscle Gain: 3000 calories, 180g protein, 320g carbs, 100g fats

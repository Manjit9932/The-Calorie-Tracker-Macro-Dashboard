@echo off
REM Calorie Tracker - Startup Script for Windows

cls
echo.
echo ========================================
echo Calorie Tracker ^& Macro Dashboard
echo ========================================
echo.

if not exist "backend" (
    echo ERROR: backend folder not found!
    exit /b 1
)

if not exist "frontend" (
    echo ERROR: frontend folder not found!
    exit /b 1
)

REM Install frontend dependencies if needed
if not exist "frontend\node_modules" (
    echo Installing frontend dependencies...
    cd frontend
    call npm install
    cd ..
)

REM Start Backend
echo Starting Backend (Spring Boot)...
where mvn >nul 2>&1
if %ERRORLEVEL%==0 (
    start cmd /k "cd /d %~dp0backend && mvn spring-boot:run"
) else if exist "%~dp0backend\target\calorie-tracker-backend-1.0.0.jar" (
    start cmd /k "cd /d %~dp0backend && java -jar target\calorie-tracker-backend-1.0.0.jar"
) else (
    echo ERROR: Maven is not installed and backend JAR is not built.
    echo Build the backend first, then run this script again.
    echo.
    echo Option 1: Install Maven and run: cd backend ^&^& mvn clean install
    echo Option 2: Use the pre-built JAR after running mvn clean install once.
    pause
    exit /b 1
)

REM Wait for backend to initialize
echo Waiting for backend to start...
timeout /t 15 /nobreak

REM Start Frontend
echo Starting Frontend (Angular)...
start cmd /k "cd /d %~dp0frontend && npm start"

echo.
echo ========================================
echo Startup Complete!
echo ========================================
echo.
echo Backend:  http://localhost:8080/api
echo Frontend: http://localhost:4200
echo.
echo Wait for both terminal windows to finish starting, then open the frontend URL.
echo Press any key to close this window...
pause

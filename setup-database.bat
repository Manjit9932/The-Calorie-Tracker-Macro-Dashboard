@echo off
echo Setting up Calorie Tracker database...
echo You will be prompted for your MySQL root password.
echo.
mysql -u root -p < "%~dp0setup-database.sql"
if %ERRORLEVEL%==0 (
    echo Database setup completed successfully.
) else (
    echo Database setup failed. Check your MySQL password and that MySQL is running.
)
pause

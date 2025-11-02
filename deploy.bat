@echo off
REM Krishi Bandhu Deployment Script for Windows
REM This script builds and deploys the application

echo 🚀 Krishi Bandhu Deployment Script
echo ==================================

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java is not installed. Please install Java 17 or higher.
    exit /b 1
)

REM Check if Maven is installed
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Maven is not installed. Please install Maven.
    exit /b 1
)

echo ✅ Java and Maven are installed

REM Clean and build
echo.
echo 📦 Building application...
call mvn clean package -DskipTests

if %errorlevel% neq 0 (
    echo ❌ Build failed!
    exit /b 1
)

echo ✅ Build successful!

REM Check if JAR file exists
if not exist "target\krishibandhu-0.0.1-SNAPSHOT.jar" (
    echo ❌ JAR file not found
    exit /b 1
)

echo.
echo 📦 JAR file created: target\krishibandhu-0.0.1-SNAPSHOT.jar

REM Ask for deployment method
echo.
echo Select deployment method:
echo 1) Run locally with H2 database (development)
echo 2) Run with PostgreSQL (production)
echo 3) Docker deployment
set /p choice="Enter choice [1-3]: "

if "%choice%"=="1" (
    echo 🚀 Starting application with H2 database...
    java -jar target\krishibandhu-0.0.1-SNAPSHOT.jar
) else if "%choice%"=="2" (
    echo 🚀 Starting application with PostgreSQL...
    echo Make sure PostgreSQL is running and configured in application.properties
    java -jar target\krishibandhu-0.0.1-SNAPSHOT.jar --spring.profiles.active=production
) else if "%choice%"=="3" (
    echo 🐳 Building Docker image...
    docker build -t krishibandhu:latest .
    
    echo 🚀 Starting with Docker Compose...
    docker-compose up -d
    
    echo ✅ Application deployed!
    echo 📍 Access at: http://localhost:8080
    echo.
    echo To view logs: docker-compose logs -f
    echo To stop: docker-compose down
) else (
    echo ❌ Invalid choice
    exit /b 1
)


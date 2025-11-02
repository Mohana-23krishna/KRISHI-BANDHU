#!/bin/bash

# Krishi Bandhu Deployment Script
# This script builds and deploys the application

set -e

echo "🚀 Krishi Bandhu Deployment Script"
echo "=================================="

# Check if Java 17+ is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 17 or higher."
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | awk -F '.' '{print $1}')
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ Java 17 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed. Please install Maven."
    exit 1
fi

echo "✅ Java $(java -version 2>&1 | head -n 1)"
echo "✅ Maven $(mvn -version | head -n 1)"

# Clean and build
echo ""
echo "📦 Building application..."
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ Build failed!"
    exit 1
fi

echo "✅ Build successful!"

# Check if JAR file exists
JAR_FILE="target/krishibandhu-0.0.1-SNAPSHOT.jar"
if [ ! -f "$JAR_FILE" ]; then
    echo "❌ JAR file not found: $JAR_FILE"
    exit 1
fi

echo ""
echo "📦 JAR file created: $JAR_FILE"

# Ask for deployment method
echo ""
echo "Select deployment method:"
echo "1) Run locally with H2 database (development)"
echo "2) Run with PostgreSQL (production)"
echo "3) Docker deployment"
read -p "Enter choice [1-3]: " choice

case $choice in
    1)
        echo "🚀 Starting application with H2 database..."
        java -jar "$JAR_FILE"
        ;;
    2)
        echo "🚀 Starting application with PostgreSQL..."
        echo "Make sure PostgreSQL is running and configured in application.properties"
        java -jar "$JAR_FILE" --spring.profiles.active=production
        ;;
    3)
        echo "🐳 Building Docker image..."
        docker build -t krishibandhu:latest .
        
        echo "🚀 Starting with Docker Compose..."
        docker-compose up -d
        
        echo "✅ Application deployed!"
        echo "📍 Access at: http://localhost:8080"
        echo ""
        echo "To view logs: docker-compose logs -f"
        echo "To stop: docker-compose down"
        ;;
    *)
        echo "❌ Invalid choice"
        exit 1
        ;;
esac


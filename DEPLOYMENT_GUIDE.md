# 🚀 Krishi Bandhu - Complete Deployment Guide

This guide covers all deployment methods for the Krishi Bandhu application.

## 📋 Table of Contents

1. [Prerequisites](#prerequisites)
2. [Quick Start (H2 Database)](#quick-start-h2-database)
3. [Production Deployment with PostgreSQL](#production-deployment-with-postgresql)
4. [Docker Deployment](#docker-deployment)
5. [Cloud Platform Deployment](#cloud-platform-deployment)
6. [Environment Variables](#environment-variables)
7. [Troubleshooting](#troubleshooting)

---

## Prerequisites

### Required
- **Java 17 or higher** - [Download](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.6+** - [Download](https://maven.apache.org/download.cgi)

### Optional (for production)
- **PostgreSQL 14+** - [Download](https://www.postgresql.org/download/)
- **Docker & Docker Compose** - [Download](https://www.docker.com/products/docker-desktop/)

---

## Quick Start (H2 Database)

Perfect for testing and development. Uses in-memory database (data is lost on restart).

### Option 1: Using Deployment Script

**Windows:**
```bash
deploy.bat
# Select option 1
```

**Linux/Mac:**
```bash
chmod +x deploy.sh
./deploy.sh
# Select option 1
```

### Option 2: Manual Deployment

```bash
# 1. Build the application
mvn clean package -DskipTests

# 2. Run the JAR
java -jar target/krishibandhu-0.0.1-SNAPSHOT.jar
```

**Access:**
- Homepage: http://localhost:8080
- Buyer Dashboard: http://localhost:8080/buyer
- Voice Test: http://localhost:8080/voice-test
- API: http://localhost:8080/api

---

## Production Deployment with PostgreSQL

### Step 1: Install and Setup PostgreSQL

```bash
# Create database
createdb krishibandhu

# Or using SQL
psql -U postgres
CREATE DATABASE krishibandhu;
```

### Step 2: Update Configuration

Edit `src/main/resources/application.properties`:

```properties
# Uncomment PostgreSQL configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/krishibandhu
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Comment out H2 configuration
# spring.datasource.url=jdbc:h2:mem:krishibandhu
```

### Step 3: Build and Deploy

```bash
# Build
mvn clean package -DskipTests

# Run with production profile
java -jar target/krishibandhu-0.0.1-SNAPSHOT.jar --spring.profiles.active=production
```

Or use the deployment script and select option 2.

---

## Docker Deployment

The easiest way to deploy with all dependencies.

### Step 1: Build Docker Image

```bash
docker build -t krishibandhu:latest .
```

### Step 2: Deploy with Docker Compose

```bash
# Create .env file (optional)
cat > .env << EOF
OPENAI_API_KEY=your_openai_api_key_here
TWILIO_ACCOUNT_SID=your_twilio_sid
TWILIO_AUTH_TOKEN=your_twilio_token
TWILIO_PHONE_NUMBER=+1234567890
EOF

# Start all services
docker-compose up -d
```

### Step 3: Verify Deployment

```bash
# Check logs
docker-compose logs -f app

# Check status
docker-compose ps

# Test API
curl http://localhost:8080/api/crops
```

### Useful Docker Commands

```bash
# Stop services
docker-compose down

# Stop and remove volumes (clean data)
docker-compose down -v

# Restart services
docker-compose restart

# View logs
docker-compose logs -f app
```

---

## Cloud Platform Deployment

### Railway.app

1. **Install Railway CLI:**
   ```bash
   npm i -g @railway/cli
   railway login
   ```

2. **Deploy:**
   ```bash
   railway init
   railway up
   ```

3. **Set Environment Variables:**
   - `OPENAI_API_KEY`
   - `DATABASE_URL` (auto-provisioned)
   - `TWILIO_ACCOUNT_SID` (optional)
   - `TWILIO_AUTH_TOKEN` (optional)

### Render.com

1. **Create New Web Service:**
   - Connect your repository
   - Build Command: `mvn clean package -DskipTests`
   - Start Command: `java -jar target/krishibandhu-0.0.1-SNAPSHOT.jar`

2. **Add PostgreSQL Database:**
   - Create new PostgreSQL service
   - Copy connection string to environment variables

3. **Set Environment Variables:**
   - `SPRING_DATASOURCE_URL` (from PostgreSQL service)
   - `SPRING_DATASOURCE_USERNAME`
   - `SPRING_DATASOURCE_PASSWORD`
   - `OPENAI_API_KEY`
   - `TWILIO_ACCOUNT_SID` (optional)

### Heroku

1. **Install Heroku CLI:**
   ```bash
   heroku login
   ```

2. **Create App:**
   ```bash
   heroku create krishibandhu-app
   ```

3. **Add PostgreSQL:**
   ```bash
   heroku addons:create heroku-postgresql:hobby-dev
   ```

4. **Set Config Vars:**
   ```bash
   heroku config:set OPENAI_API_KEY=your_key
   heroku config:set TWILIO_ACCOUNT_SID=your_sid
   heroku config:set TWILIO_AUTH_TOKEN=your_token
   ```

5. **Deploy:**
   ```bash
   git push heroku main
   ```

### AWS EC2

1. **Launch EC2 Instance:**
   - Ubuntu 22.04 LTS
   - t2.micro or larger
   - Open port 8080 in security group

2. **SSH into Instance:**
   ```bash
   ssh -i your-key.pem ubuntu@your-ec2-ip
   ```

3. **Install Dependencies:**
   ```bash
   sudo apt update
   sudo apt install openjdk-17-jdk maven postgresql -y
   ```

4. **Setup PostgreSQL:**
   ```bash
   sudo -u postgres psql
   CREATE DATABASE krishibandhu;
   CREATE USER app_user WITH PASSWORD 'secure_password';
   GRANT ALL PRIVILEGES ON DATABASE krishibandhu TO app_user;
   ```

5. **Clone and Deploy:**
   ```bash
   git clone your-repo-url
   cd krishibandhu
   mvn clean package -DskipTests
   ```

6. **Create Systemd Service:**
   ```bash
   sudo nano /etc/systemd/system/krishibandhu.service
   ```

   Add:
   ```ini
   [Unit]
   Description=Krishi Bandhu Application
   After=network.target

   [Service]
   User=ubuntu
   WorkingDirectory=/home/ubuntu/krishibandhu
   ExecStart=/usr/bin/java -jar target/krishibandhu-0.0.1-SNAPSHOT.jar
   Restart=always
   RestartSec=10

   [Install]
   WantedBy=multi-user.target
   ```

7. **Start Service:**
   ```bash
   sudo systemctl daemon-reload
   sudo systemctl enable krishibandhu
   sudo systemctl start krishibandhu
   sudo systemctl status krishibandhu
   ```

---

## Environment Variables

### Required

| Variable | Description | Example |
|----------|-------------|---------|
| `OPENAI_API_KEY` | OpenAI API key for voice assistant | `sk-...` |

### Optional

| Variable | Description | Default |
|----------|-------------|---------|
| `PORT` | Server port | `8080` |
| `DATABASE_URL` | PostgreSQL connection string | H2 (in-memory) |
| `DATABASE_USERNAME` | Database username | `postgres` |
| `DATABASE_PASSWORD` | Database password | - |
| `TWILIO_ACCOUNT_SID` | Twilio account SID | - |
| `TWILIO_AUTH_TOKEN` | Twilio auth token | - |
| `TWILIO_PHONE_NUMBER` | Twilio phone number | - |
| `CORS_ALLOWED_ORIGINS` | Allowed CORS origins | `*` |

---

## Troubleshooting

### Build Errors

**Problem:** `java.lang.UnsupportedClassVersionError`
- **Solution:** Use Java 17 or higher

**Problem:** `Cannot find symbol` during build
- **Solution:** Run `mvn clean install` to download all dependencies

### Runtime Errors

**Problem:** `Port 8080 already in use`
- **Solution:** Change port in `application.properties` or stop the process using port 8080
  ```bash
  # Linux/Mac
  lsof -i :8080
  kill -9 <PID>
  
  # Windows
  netstat -ano | findstr :8080
  taskkill /PID <PID> /F
  ```

**Problem:** Database connection failed
- **Solution:** 
  - Verify PostgreSQL is running
  - Check connection credentials
  - Ensure database exists

**Problem:** OpenAI API errors
- **Solution:**
  - Verify API key is set correctly
  - Check API key validity
  - Ensure sufficient API credits

### Docker Issues

**Problem:** `Cannot connect to Docker daemon`
- **Solution:** Start Docker Desktop or Docker service

**Problem:** `Port already allocated`
- **Solution:** Change port in `docker-compose.yml`

---

## Health Check

After deployment, verify the application is running:

```bash
# Check API health
curl http://localhost:8080/api/crops

# Check homepage
curl http://localhost:8080

# View application logs
tail -f logs/krishibandhu.log
```

---

## Next Steps

1. **Configure OpenAI API Key** for voice assistant features
2. **Setup Twilio** (optional) for SMS notifications
3. **Configure Production Database** for data persistence
4. **Setup SSL/HTTPS** for production deployment
5. **Configure Monitoring** and logging

---

## Support

For issues or questions:
- Check the [README.md](README.md)
- Review [API Documentation](API_DOCUMENTATION.md)
- Check application logs in `logs/krishibandhu.log`

---

**Happy Deploying! 🚀**


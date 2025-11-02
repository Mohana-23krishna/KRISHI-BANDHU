# 🚀 Quick Deployment Guide

## Prerequisites

- ✅ **Java 17+** installed
- ✅ **Maven 3.6+** installed (optional, JAR already built)
- ✅ Project is built (`target/krishibandhu-0.0.1-SNAPSHOT.jar` exists)

---

## Option 1: Quick Start (H2 Database) ⚡

**Fastest way to run the application:**

```bash
java -jar target/krishibandhu-0.0.1-SNAPSHOT.jar
```

**Access:**
- 🌐 Homepage: http://localhost:8080
- 👥 Buyer Dashboard: http://localhost:8080/buyer
- 🎤 Voice Test: http://localhost:8080/voice-test

---

## Option 2: Docker Deployment 🐳

**Best for production with PostgreSQL:**

```bash
# 1. Build and start all services
docker-compose up -d

# 2. View logs
docker-compose logs -f

# 3. Check status
docker-compose ps
```

**Access:** http://localhost:8080

**Stop services:**
```bash
docker-compose down
```

---

## Option 3: Production with PostgreSQL 🗄️

**1. Install PostgreSQL:**
- Download: https://www.postgresql.org/download/

**2. Create Database:**
```sql
CREATE DATABASE krishibandhu;
```

**3. Update `application.properties`:**
```properties
# Uncomment PostgreSQL configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/krishibandhu
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Comment out H2
# spring.datasource.url=jdbc:h2:mem:krishibandhu
```

**4. Rebuild and Run:**
```bash
mvn clean package -DskipTests
java -jar target/krishibandhu-0.0.1-SNAPSHOT.jar --spring.profiles.active=production
```

---

## Environment Variables (Optional)

Set these before running for production:

```bash
# Windows
set OPENAI_API_KEY=your_api_key_here
set DATABASE_URL=jdbc:postgresql://localhost:5432/krishibandhu

# Linux/Mac
export OPENAI_API_KEY=your_api_key_here
export DATABASE_URL=jdbc:postgresql://localhost:5432/krishibandhu
```

---

## Deployment Scripts

### Windows:
```bash
deploy.bat
```

### Linux/Mac:
```bash
chmod +x deploy.sh
./deploy.sh
```

---

## Verify Deployment ✅

```bash
# Test API
curl http://localhost:8080/api/crops

# Check homepage
curl http://localhost:8080
```

---

## Troubleshooting

**Port 8080 already in use?**
- Change port in `application.properties`: `server.port=8081`

**Java not found?**
- Install Java 17+: https://www.oracle.com/java/technologies/downloads/

**Docker issues?**
- Start Docker Desktop
- Check: `docker --version`

---

## 📚 Full Documentation

For detailed deployment instructions, see:
- [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md) - Complete deployment guide
- [README.md](README.md) - Project overview

---

**🎉 Ready to Deploy!**


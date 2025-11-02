# Krishi Bandhu - Deployment Guide

## Quick Start

### 1. Database Setup

Ensure PostgreSQL is running and create a database:

```sql
CREATE DATABASE krishibandhu;
```

Update `application.properties` with your credentials.

### 2. Run the Application

```bash
mvn spring-boot:run
```

The application will start on http://localhost:8080

### 3. Access the Application

- **Homepage**: http://localhost:8080
- **Buyer Dashboard**: http://localhost:8080/buyer
- **API Base URL**: http://localhost:8080/api

## Sample Data

The application automatically creates sample data on first run:
- 2 Sample farmers
- 3 Sample crops
- 1 Sample buyer
- 1 Sample transporter

## Testing the API

### 1. View all crops
```bash
curl http://localhost:8080/api/crops
```

### 2. Test voice AI
```bash
curl "http://localhost:8080/api/voice/test?speechText=I%20want%20to%20sell%2050%20kg%20of%20wheat&phone=%2B919876543210&language=en"
```

### 3. Register a buyer
```bash
curl -X POST "http://localhost:8080/api/buyers/register?name=Test%20Buyer&email=test@example.com&password=test123"
```

### 4. Search crops
```bash
curl "http://localhost:8080/api/crops/search?cropName=wheat"
```

## Configuration

### Database Connection

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Twilio Setup (Optional)

For voice integration:

1. Sign up at https://www.twilio.com
2. Get your Account SID and Auth Token
3. Update `application.properties`:

```properties
twilio.account.sid=your_sid_here
twilio.auth.token=your_token_here
twilio.phone.number=+1234567890
```

## Production Deployment

### Building for Production

```bash
mvn clean package
```

The JAR file will be at: `target/krishibandhu-0.0.1-SNAPSHOT.jar`

### Running the JAR

```bash
java -jar target/krishibandhu-0.0.1-SNAPSHOT.jar
```

### Environment Variables

Set these for production:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://your-db-host:5432/krishibandhu
export SPRING_DATASOURCE_USERNAME=your_username
export SPRING_DATASOURCE_PASSWORD=your_password
export SERVER_PORT=8080
```

## API Testing with Postman

Import these endpoints for testing:

### Collection

```
POST http://localhost:8080/api/farmers
GET http://localhost:8080/api/farmers
GET http://localhost:8080/api/farmers/phone/{phone}
POST http://localhost:8080/api/crops
GET http://localhost:8080/api/crops
GET http://localhost:8080/api/crops/available
GET http://localhost:8080/api/crops/search?cropName=wheat
POST http://localhost:8080/api/transactions
GET http://localhost:8080/api/voice/test
```

## Troubleshooting

### Database Connection Issues

1. Ensure PostgreSQL is running
2. Check credentials in application.properties
3. Verify database exists

### Port Already in Use

Change the port in `application.properties`:
```properties
server.port=8081
```

### Build Errors

1. Clean and rebuild:
```bash
mvn clean install
```

2. Check Java version:
```bash
java -version
```
Should be Java 17+

## Features

✅ Farmer Management
✅ Crop Listings
✅ Buyer Platform
✅ Transaction Processing
✅ AI Voice Integration
✅ Credit Scoring
✅ Transport Network
✅ Rating System

## Support

For issues or questions, refer to the main README.md file.



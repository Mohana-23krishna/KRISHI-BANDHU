# 🌾 Krishi Bandhu - Farmer-Buyer Connection Platform

A comprehensive Spring Boot-based backend and web application that connects farmers (voice users) with buyers, transporters, and banks through AI voice assistance.

## 🎯 Features

- **Voice-First Interface**: Farmers can call the system to sell crops, get loan advice, and receive farming tips
- **Direct Marketplace**: Buyers can browse and purchase crops with transparent pricing
- **Credit Scoring**: Automatically tracks farmer credit scores based on transaction history
- **Transport Network**: Connect with transporters for efficient delivery
- **AI Integration**: Natural language processing for voice commands and farming advice

## 🏗️ Architecture

```
[Farmer Phone Call]
        |
   [Twilio/Exotel]
        |
   [Spring Boot Voice API]
        |
   [AI Intent Detection]
        |
   [Business Logic]
        |
   [MySQL/PostgreSQL Database]
```

## 📁 Project Structure

```
krishibandhu/
├── src/main/java/com/buildathon/krishibandhu/
│   ├── controller/          # REST Controllers
│   │   ├── FarmerController.java
│   │   ├── CropController.java
│   │   ├── BuyerController.java
│   │   ├── TransactionController.java
│   │   ├── VoiceController.java
│   │   └── RatingController.java
│   ├── model/               # Entity Models
│   │   ├── Farmer.java
│   │   ├── Crop.java
│   │   ├── Buyer.java
│   │   ├── Transaction.java
│   │   ├── Rating.java
│   │   └── Transporter.java
│   ├── repository/           # JPA Repositories
│   ├── service/             # Business Logic
│   │   ├── FarmerService.java
│   │   ├── CropService.java
│   │   ├── TransactionService.java
│   │   ├── BuyerService.java
│   │   └── AIVoiceService.java
│   ├── dto/                  # Data Transfer Objects
│   ├── config/               # Configuration
│   └── KrishibandhuApplication.java
└── src/main/resources/
    ├── application.properties
    └── templates/            # Thymeleaf Templates
```

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven 3.6+
- PostgreSQL database
- (Optional) Twilio account for voice features

### Installation

1. **Clone the repository**
```bash
git clone <repository-url>
cd krishibandhu
```

2. **Configure Database**
Update `src/main/resources/application.properties` with your PostgreSQL credentials:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. **Build the project**
```bash
mvn clean install
```

4. **Run the application**
```bash
mvn spring-boot:run
```

5. **Access the application**
- Web Interface: http://localhost:8080
- Buyer Dashboard: http://localhost:8080/buyer
- API Documentation: All endpoints under `/api/*`

## 📡 API Endpoints

### Farmers
- `POST /api/farmers` - Register a farmer
- `GET /api/farmers` - Get all farmers
- `GET /api/farmers/{id}` - Get farmer by ID
- `GET /api/farmers/phone/{phone}` - Get farmer by phone

### Crops
- `POST /api/crops` - Add a crop listing
- `GET /api/crops` - Get all crops
- `GET /api/crops/available` - Get available crops
- `GET /api/crops/search?cropName={name}` - Search crops
- `GET /api/crops/farmer/{farmerId}` - Get crops by farmer

### Buyers
- `POST /api/buyers/register` - Register a buyer
- `POST /api/buyers/login` - Login buyer
- `GET /api/buyers/{id}` - Get buyer by ID

### Transactions
- `POST /api/transactions?cropId={id}&buyerId={id}&quantity={qty}` - Create transaction
- `GET /api/transactions/buyer/{buyerId}` - Get buyer's transactions
- `GET /api/transactions/farmer/{farmerId}` - Get farmer's transactions

### Voice API
- `POST /api/voice/process` - Process voice request
- `GET /api/voice/test` - Test voice functionality

### Ratings
- `POST /api/ratings` - Add a rating
- `GET /api/ratings/farmer/{farmerId}` - Get farmer ratings

### Transport
- `POST /api/transport` - Add transporter
- `GET /api/transport` - Get all transporters
- `GET /api/transport/available` - Get available transporters

## 🧪 Testing Voice Integration

Test the voice AI without making actual calls:

```bash
curl -X GET "http://localhost:8080/api/voice/test?speechText=I%20want%20to%20sell%2050%20kg%20of%20wheat&phone=%2B919876543210&language=en"
```

The API will return a response with the detected intent and actions taken.

## 💡 Usage Examples

### 1. Register a Farmer
```bash
curl -X POST http://localhost:8080/api/farmers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Rajesh Kumar",
    "phone": "+919876543210",
    "language": "hi",
    "address": "Village Shyam Nagar",
    "state": "Andhra Pradesh"
  }'
```

### 2. Add a Crop
```bash
curl -X POST http://localhost:8080/api/crops \
  -H "Content-Type: application/json" \
  -d '{
    "farmerId": 1,
    "cropName": "Wheat",
    "quantity": 500,
    "price": 25,
    "availableQty": 300,
    "location": "Shyam Nagar",
    "qualityGrade": "A"
  }'
```

### 3. Purchase a Crop
```bash
curl -X POST "http://localhost:8080/api/transactions?cropId=1&buyerId=1&quantity=100"
```

## 🎙️ Voice Commands

Farmers can use voice commands like:

- "I want to sell 50 kg of wheat at 2000 rupees" - List a crop for sale
- "What are the current wheat prices?" - Check market prices
- "I need loan advice" - Get loan information
- "How to use fertilizer?" - Get farming advice
- "Register me" - Register as a new farmer

## 🔧 Configuration

### Twilio Setup (Optional)
For voice integration, add to `application.properties`:
```properties
twilio.account.sid=your_sid
twilio.auth.token=your_token
twilio.phone.number=+1234567890
```

### OpenAI Setup (Future Enhancement)
```properties
openai.api.key=your_api_key
```

## 🗄️ Database Schema

### Farmers Table
- id, name, phone, language, credit_score, address, state

### Crops Table
- id, farmer_id, crop_name, quantity, price, available_qty, harvest_date, location, quality_grade, is_organic

### Buyers Table
- id, name, email, password, phone

### Transactions Table
- id, crop_id, buyer_id, quantity, total_amount, transaction_date, status

### Ratings Table
- id, buyer_id, farmer_id, stars, comment, rated_date

### Transporters Table
- id, name, phone, vehicle_type, capacity, available, location

## 🚀 Future Enhancements

- Mobile app for buyers
- Payment gateway integration (UPI)
- Government scheme notifications via SMS
- Multilingual AI chatbot on WhatsApp
- Advanced ML-based price prediction
- Weather-based crop recommendations

## 📝 License

This project is created for the Buildathon competition.

## 👥 Contributors

Built with ❤️ for connecting farmers with modern technology.

---

**Note**: This is a working prototype. For production deployment, add proper security, authentication, payment gateways, and cloud infrastructure setup.



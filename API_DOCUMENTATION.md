# Krishi Bandhu API Documentation

## Base URL
```
http://localhost:8080/api
```

## Endpoints

### Farmer Management

#### Register Farmer
```
POST /api/farmers
Content-Type: application/json

{
  "name": "Rajesh Kumar",
  "phone": "+919876543210",
  "language": "hi",
  "address": "Village Shyam Nagar",
  "state": "Andhra Pradesh"
}
```

#### Get All Farmers
```
GET /api/farmers
```

#### Get Farmer by ID
```
GET /api/farmers/{id}
```

#### Get Farmer by Phone
```
GET /api/farmers/phone/{phone}
```

### Crop Management

#### Add Crop
```
POST /api/crops
Content-Type: application/json

{
  "farmerId": 1,
  "cropName": "Wheat",
  "quantity": 500,
  "price": 25,
  "availableQty": 300,
  "location": "Shyam Nagar",
  "qualityGrade": "A",
  "isOrganic": false
}
```

#### Get All Crops
```
GET /api/crops
```

#### Get Available Crops
```
GET /api/crops/available
```

#### Search Crops
```
GET /api/crops/search?cropName=wheat
```

#### Get Crops by Farmer
```
GET /api/crops/farmer/{farmerId}
```

### Buyer Management

#### Register Buyer
```
POST /api/buyers/register
Parameters:
- name: string
- email: string
- password: string
- phone: string (optional)

Example:
POST /api/buyers/register?name=John%20Doe&email=john@example.com&password=pass123
```

#### Login Buyer
```
POST /api/buyers/login
Parameters:
- email: string
- password: string

Example:
POST /api/buyers/login?email=john@example.com&password=pass123
```

#### Get Buyer by ID
```
GET /api/buyers/{id}
```

### Transaction Management

#### Create Transaction
```
POST /api/transactions?cropId={id}&buyerId={id}&quantity={qty}

Example:
POST /api/transactions?cropId=1&buyerId=1&quantity=100
```

#### Get Transactions by Buyer
```
GET /api/transactions/buyer/{buyerId}
```

#### Get Transactions by Farmer
```
GET /api/transactions/farmer/{farmerId}
```

### Voice AI

#### Process Voice Request
```
POST /api/voice/process
Content-Type: application/json

{
  "speechText": "I want to sell 50 kg of wheat at 2000 rupees",
  "phone": "+919876543210",
  "language": "en"
}
```

#### Test Voice
```
GET /api/voice/test
Parameters:
- speechText: string
- phone: string
- language: string (default: en)

Example:
GET /api/voice/test?speechText=I%20want%20to%20sell%2050%20kg%20of%20wheat&phone=%2B919876543210&language=en
```

#### Voice Welcome Message
```
GET /api/voice/welcome
```

### Rating Management

#### Add Rating
```
POST /api/ratings
Content-Type: application/json

{
  "buyerId": 1,
  "farmerId": 1,
  "stars": 5,
  "comment": "Excellent quality produce"
}
```

#### Get Ratings by Farmer
```
GET /api/ratings/farmer/{farmerId}
```

### Transport Management

#### Add Transporter
```
POST /api/transport
Content-Type: application/json

{
  "name": "Shiva Transport",
  "phone": "+919876543230",
  "vehicleType": "Truck",
  "capacity": 5000,
  "available": true,
  "location": "Hyderabad"
}
```

#### Get All Transporters
```
GET /api/transport
```

#### Get Available Transporters
```
GET /api/transport/available
```

## Response Format

### Success Response
```json
{
  "id": 1,
  "name": "Rajesh Kumar",
  ...
}
```

### Error Response
```json
{
  "error": "Error message"
}
```

## Voice AI Commands

The AI can process the following intents:

### Sell Crop
```
"I want to sell 50 kg of wheat at 2000 rupees"
"I want to sell 100 kg of rice at 30 per kg"
```

### Check Prices
```
"What are the current wheat prices?"
"Price of tomato"
```

### Loan Advice
```
"I need loan advice"
"Where to get agricultural loan?"
```

### Farming Tips
```
"How to use fertilizer?"
"Advice on pesticides"
"Irrigation techniques"
```

### Registration
```
"Register me"
"New farmer registration"
```

## Status Codes

- 200: Success
- 201: Created
- 400: Bad Request
- 404: Not Found
- 500: Internal Server Error

## CORS

All endpoints support CORS from any origin.



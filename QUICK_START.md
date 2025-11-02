# 🚀 Krishi Bandhu - Quick Start Guide

## ✅ Application is Running!

The application is now running on **http://localhost:8080**

---

## 📱 How to Test Everything

### 1. Homepage
**Open in browser:** http://localhost:8080

Features:
- Modern UI with features overview
- Multi-language support buttons
- Links to all modules

### 2. Buyer Dashboard (Purchase Crops)
**Open in browser:** http://localhost:8080/buyer

**To Register:**
1. Fill in your name, email, password
2. Click "Login / Register"
3. You'll see the dashboard with crops
4. Search, browse, and purchase crops!

**Default buyer credentials:**
- Email: `contact@greenmarket.com`
- Password: `password123`

### 3. Farmer Registration
**Open in browser:** http://localhost:8080/farmer-register

**Features:**
- Manual farmer registration form
- Multi-language support (English, Hindi, Telugu)
- Also supports voice registration (see below)

**How to register:**
1. Enter name
2. Enter phone number
3. Select preferred language
4. Enter address and state (optional)
5. Click "Register as Farmer"

### 4. Voice API Testing (AI Farmer Assistant)
**Open in browser:** http://localhost:8080/voice-test

**Test Commands:**

#### Sell Crops via Voice:
```
http://localhost:8080/api/voice/test?speechText=I%20want%20to%20sell%2050%20kg%20of%20wheat%20at%2020%20rupees%20per%20kg&phone=%2B919876543210&language=en
```

#### Different Languages:

**English:**
```
http://localhost:8080/api/voice/test?speechText=I%20want%20to%20sell%20100%20kg%20of%20rice&phone=%2B919876543211&language=en
```

**Hindi:**
```
http://localhost:8080/api/voice/test?speechText=Main%20gehu%20bechna%20chahta%20hun&phone=%2B919876543210&language=hi
```

**Telugu:**
```
http://localhost:8080/api/voice/test?speechText=Nenu%20godhuma%20amchagalanu&phone=%2B919876543210&language=te
```

#### Check Prices:
```
http://localhost:8080/api/voice/test?speechText=What%20are%20the%20current%20wheat%20prices?&phone=%2B919876543210&language=en
```

#### Loan Advice:
```
http://localhost:8080/api/voice/test?speechText=I%20need%20loan%20advice&phone=%2B919876543210&language=en
```

#### Farming Tips:
```
http://localhost:8080/api/voice/test?speechText=How%20to%20use%20fertilizer?&phone=%2B919876543210&language=en
```

---

## 🌾 Complete Farmer Journey

### Option 1: Voice Registration (Automatic)
1. Farmer calls the system
2. Says: "I want to sell 50 kg of wheat at 20 rupees per kg"
3. System automatically:
   - Creates farmer profile
   - Lists the crop
   - Updates digital crop card
   - Credits the farmer

### Option 2: Manual Registration
1. Go to http://localhost:8080/farmer-register
2. Fill in the form
3. Submit
4. Then use voice API to sell crops

### Viewing Digital Crop Card:
```
http://localhost:8080/api/crops/farmer/1
```

---

## 🔍 API Endpoints for Testing

### View All Available Crops
```
http://localhost:8080/api/crops
```

### Search Crops
```
http://localhost:8080/api/crops/search?cropName=wheat
```

### Get Farmer Details
```
http://localhost:8080/api/farmers/phone/+919876543210
```

### View Transporters
```
http://localhost:8080/api/transport/available
```

### Purchase a Crop
```
POST http://localhost:8080/api/transactions?cropId=1&buyerId=1&quantity=100
```

---

## 💡 Key Features Tested

✅ Voice-first interface for farmers
✅ Automatic farmer registration via voice
✅ Digital crop card for farmers
✅ Multi-language support (English, Hindi, Telugu)
✅ Buyer registration and dashboard
✅ Crop browsing and purchasing
✅ Credit scoring system
✅ Transport network
✅ Real-time inventory updates

---

## 🌐 Multilingual Support

The application supports three languages:
- **English** (en)
- **हिंदी** - Hindi (hi)
- **తెలుగు** - Telugu (te)

All interfaces can be accessed in multiple languages.

---

## 🎯 Testing Scenarios

### Scenario 1: Farmer Sells via Voice
1. Open voice test page: http://localhost:8080/voice-test
2. Try command: "I want to sell 50 kg of wheat"
3. Check crops: http://localhost:8080/api/crops
4. You'll see the new crop listed!

### Scenario 2: Buyer Purchases
1. Register as buyer on http://localhost:8080/buyer
2. Browse crops
3. Enter quantity
4. Click "Buy Now"
5. Check your orders in "My Orders" tab

### Scenario 3: Check Credits & Transport
1. View farmers: http://localhost:8080/api/farmers
2. View credit scores
3. Check transporters: http://localhost:8080/api/transport

---

## 📊 Sample Data Included

The application comes with:
- 4 Farmers (with different languages)
- 5 Crops (Wheat, Rice, Tomato, Cotton, Sugarcane)
- 3 Buyers
- 4 Transporters

All ready for testing!

---

## 🎉 Quick Access Links

- **Homepage:** http://localhost:8080
- **Buyer Dashboard:** http://localhost:8080/buyer
- **Farmer Registration:** http://localhost:8080/farmer-register
- **Voice Test:** http://localhost:8080/voice-test
- **H2 Database Console:** http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:krishibandhu`
  - Username: `sa`
  - Password: (leave empty)

---

## 🚀 Your Application is READY!

All features are working:
- ✅ Voice API
- ✅ Auto-registration
- ✅ Digital crop cards
- ✅ Buyer dashboard
- ✅ Multi-language support
- ✅ Credit scoring
- ✅ Transport network

**Start testing now!** 🎉



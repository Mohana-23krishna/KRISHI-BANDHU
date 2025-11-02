# 🧪 Krishi Bandhu - Testing Guide

## Application is Starting...

The application is now running on **http://localhost:8080** (give it 30 seconds to fully start)

---

## 📞 Testing Voice API (Farmer Side)

### Test 1: Selling Crops via Voice

**Example 1: Sell Wheat**
```
http://localhost:8080/api/voice/test?speechText=I%20want%20to%20sell%2050%20kg%20of%20wheat%20at%2020%20rupees%20per%20kg&phone=%2B919876543210&language=en
```

**Response:**
```json
{
  "response": "Great! I've listed your WHEAT crop - 50 kg at 0.20 per kg. Buyers can now see your crop.",
  "action": "SELL_CROP_SUCCESS",
  "data": {
    "cropId": 1,
    "cropName": "WHEAT",
    "quantity": 50
  }
}
```

### Test 2: Sell Different Crops

**Rice:**
```
http://localhost:8080/api/voice/test?speechText=I%20want%20to%20sell%20100%20kg%20of%20rice&phone=%2B919876543210&language=en
```

**Tomato:**
```
http://localhost:8080/api/voice/test?speechText=I%20want%20to%20sell%20200%20kg%20of%20tomato%20at%2030%20rupees&phone=%2B919876543211&language=en
```

### Test 3: Multi-Language Support

**English:**
```
http://localhost:8080/api/voice/test?speechText=I%20want%20to%20sell%2050%20kg%20of%20wheat&phone=%2B919876543210&language=en
```

**Hindi:**
```
http://localhost:8080/api/voice/test?speechText=Main%2050%20kg%20gehu%20bechna%20chahta%20hun&phone=%2B919876543210&language=hi
```

**Telugu:**
```
http://localhost:8080/api/voice/test?speechText=Nenu%2050%20kg%20godhuma%20amchagalanu&phone=%2B919876543210&language=te
```

### Test 4: Check Prices

```
http://localhost:8080/api/voice/test?speechText=What%20are%20the%20current%20wheat%20prices?&phone=%2B919876543210&language=en
```

### Test 5: Loan Advice

```
http://localhost:8080/api/voice/test?speechText=I%20need%20loan%20advice&phone=%2B919876543210&language=en
```

### Test 6: Farming Tips

```
http://localhost:8080/api/voice/test?speechText=How%20to%20use%20fertilizer?&phone=%2B919876543210&language=en
```

```
http://localhost:8080/api/voice/test?speechText=I%20need%20advice%20on%20pesticides&phone=%2B919876543210&language=en
```

### Test 7: Registration via Voice

```
http://localhost:8080/api/voice/test?speechText=Register%20me&phone=%2B919999999999&language=en
```

---

## 🌾 Testing Digital Crop Card

### View All Available Crops

```
http://localhost:8080/api/crops
```

### View Only Available Crops

```
http://localhost:8080/api/crops/available
```

### Search Crops

```
http://localhost:8080/api/crops/search?cropName=wheat
```

### Get Farmer's Crop List (Digital Crop Card)

```
http://localhost:8080/api/crops/farmer/1
```

---

## 🎯 Complete Testing Workflow

### Scenario 1: Farmer Sells Crop via Voice

1. **Farmer calls the system**
```
http://localhost:8080/api/voice/test?speechText=I%20want%20to%20sell%2050%20kg%20of%20wheat&phone=%2B919876543210&language=en
```

2. **Check if crop was listed**
```
http://localhost:8080/api/crops
```

3. **View farmer's digital crop card**
```
http://localhost:8080/api/crops/farmer/1
```

4. **Buyer purchases the crop**
```
http://localhost:8080/api/transactions?cropId=1&buyerId=1&quantity=25
```

5. **Check updated availability**
```
http://localhost:8080/api/crops/1
```

6. **View farmer's credit score**
```
http://localhost:8080/api/farmers/1
```

### Scenario 2: Language Testing

Test different languages:

**Hindi:**
```
http://localhost:8080/api/voice/test?speechText=Main%20gehu%20bechna%20chahta%20hun&phone=%2B919876543210&language=hi
```

**Telugu:**
```
http://localhost:8080/api/voice/test?speechText=Nenu%20godhuma%20amchagalanu&phone=%2B919876543210&language=te
```

**English:**
```
http://localhost:8080/api/voice/test?speechText=I%20want%20to%20sell%20wheat&phone=%2B919876543210&language=en
```

### Scenario 3: Full Farmer Journey

1. **Register Farmer** (via API)
```
POST http://localhost:8080/api/farmers

{
  "name": "Rajesh Kumar",
  "phone": "+919876543210",
  "language": "hi",
  "address": "Village Test",
  "state": "Andhra Pradesh"
}
```

2. **Farmer sells crop via voice**
```
http://localhost:8080/api/voice/test?speechText=I%20want%20to%20sell%20100%20kg%20of%20rice&phone=%2B919876543210&language=hi
```

3. **View farmer's listing**
```
http://localhost:8080/api/farmers/phone/+919876543210
```

4. **Get farmer's crops (Digital Crop Card)**
```
http://localhost:8080/api/crops/farmer/1
```

5. **Buyer purchases**
```
http://localhost:8080/api/transactions?cropId=1&buyerId=1&quantity=50
```

6. **Check credit score update**
```
http://localhost:8080/api/farmers/1
```

---

## 🌐 Web Interface Testing

### 1. Open in Browser

- **Homepage**: http://localhost:8080
- **Buyer Dashboard**: http://localhost:8080/buyer

### 2. Test Buyer Dashboard

1. Go to http://localhost:8080/buyer
2. Register as buyer
3. Search for crops
4. Purchase crops
5. See real-time updates

---

## 🗄️ Database Console

Access H2 database console:
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:krishibandhu`
- Username: `sa`
- Password: (leave empty)

---

## 📱 Phone Call Simulation

### Simulated Voice Flow

```
Farmer: "I want to sell 50 kg of wheat at 20 rupees per kg"
System: "Great! I've listed your wheat. Buyers can see it now."
[System creates crop entry automatically]
```

### Test Different Phrase Variations

**Variation 1:**
```
I want to sell 50 kg of wheat
```

**Variation 2:**
```
Sell my 50 kg wheat at 20 per kg
```

**Variation 3:**
```
I have 50 kg wheat to sell
```

All these should work!

---

## 🧪 API Testing Tools

### Using Browser
Just open the URLs in your browser

### Using PowerShell
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/crops"
```

### Using curl (if available)
```bash
curl http://localhost:8080/api/crops
```

### Using Postman
1. Create a collection
2. Import the endpoints from API_DOCUMENTATION.md
3. Test each endpoint

---

## ✅ Expected Results

### Voice API Should:
✅ Detect intent to sell crops
✅ Extract crop name, quantity, and price
✅ Register farmer automatically
✅ Create crop entry in database
✅ Return confirmation message

### Digital Crop Card Should:
✅ Show all crops by farmer
✅ Display available quantity
✅ Show quality grade
✅ Show location
✅ Update when sold

### Multi-Language Should:
✅ Work in English
✅ Work in Hindi  
✅ Work in Telugu
✅ Work in any language with English keywords

---

## 🔍 Troubleshooting

### Application Not Starting?
1. Check if port 8080 is in use
2. Ensure Java 17+ is installed
3. Check database connection

### Voice API Not Working?
1. Test with simple phrases
2. Check browser console for errors
3. Verify endpoint URL

### No Crops Showing?
1. First create crops via voice API
2. Then check /api/crops endpoint

---

## 📊 Test Checklist

- [ ] Voice API responds to crop selling
- [ ] Different crops can be sold
- [ ] Multi-language support works
- [ ] Digital crop card shows farmer's crops
- [ ] Buyer can purchase crops
- [ ] Credit score updates
- [ ] Availability updates correctly
- [ ] Web interface works
- [ ] Search functionality works

---

## 🎉 Ready to Test!

The application is running. Start testing now:

1. Open http://localhost:8080 in browser
2. Try the voice API endpoints
3. Test buyer dashboard
4. Simulate a complete transaction

Happy Testing! 🚀



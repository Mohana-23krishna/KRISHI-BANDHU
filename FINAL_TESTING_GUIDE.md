# 🧪 Final Testing Guide - Krishi Bandhu

## Complete Testing Checklist for Submission

This guide will help you test all features manually, including voice testing, before final submission.

---

## 🚀 Step 1: Start the Application

```bash
cd krishibandhu
mvn clean install
mvn spring-boot:run
```

**Wait 30-60 seconds** for the application to fully start.

**Verify**: Open http://localhost:8080 - you should see the landing page.

---

## ✅ Step 2: Feature Testing Checklist

### 1. Homepage ✅
- [ ] Navigate to http://localhost:8080
- [ ] Verify landing page loads correctly
- [ ] Check all navigation links work
- [ ] Test responsive design (resize browser)

**Expected**: Beautiful landing page with feature showcase

---

### 2. Voice Bot Testing (ChatGPT-like Assistant) 🎤

#### 2.1. Test Voice Test Page
- [ ] Go to: http://localhost:8080/voice-test
- [ ] Verify page loads correctly

#### 2.2. Test Voice API with Text Input

**English Test:**
```
Speech Text: "I have 20 kgs of tomatoes from Hyderabad"
Phone: +919876543210
Language: English
```

**Expected Response:**
```json
{
  "response": "I understood! You have 20 kg of tomato. I'm creating a post for you.",
  "action": "SELL_CROP_SUCCESS",
  "cropId": 1,
  "postCreated": true,
  "data": {
    "cropName": "tomato",
    "quantity": 20.0,
    "location": "Hyderabad"
  }
}
```

**Hindi Test:**
```
Speech Text: "मेरे पास 50 किलो गेहूं है मैं हैदराबाद से हूं"
Phone: +919876543211
Language: Hindi
```

**Telugu Test:**
```
Speech Text: "నాకు 30 కిలోల బియ్యం ఉంది నేను హైదరాబాద్ నుండి వచ్చాను"
Phone: +919876543212
Language: Telugu
```

**Check:**
- [ ] Voice API returns proper response
- [ ] Crop post is created automatically
- [ ] Response is in correct language
- [ ] SMS confirmation is logged (check console)

#### 2.3. Test with Different Scenarios

**Scenario A: With Price**
```
"I want to sell 100 kg of rice at 25 rupees per kg"
```

**Scenario B: Without Price (should use default)**
```
"I have 30 kgs of wheat"
```

**Scenario C: General Question (should not create post)**
```
"How can I get a loan?"
```

---

### 3. Farmer Registration ✅

#### 3.1. Web Registration
- [ ] Go to: http://localhost:8080/farmer-register
- [ ] Fill in:
  - Name: Rajesh Kumar
  - Phone: +919876543210
  - Language: Hindi
  - Address: Village Shyam Nagar
  - State: Andhra Pradesh
- [ ] Click "Register as Farmer"
- [ ] Verify success message

#### 3.2. API Registration
```bash
POST http://localhost:8080/api/farmers
Content-Type: application/json

{
  "name": "Raju",
  "phone": "+919876543213",
  "language": "hi",
  "address": "Hyderabad",
  "state": "Telangana"
}
```

**Check:**
- [ ] Farmer is registered
- [ ] Can retrieve by phone: GET /api/farmers/phone/+919876543213

---

### 4. Crop Management ✅

#### 4.1. View All Crops
- [ ] GET http://localhost:8080/api/crops
- [ ] Should show crops created via voice bot

#### 4.2. View Available Crops
- [ ] GET http://localhost:8080/api/crops/available
- [ ] Should only show crops with availableQty > 0

#### 4.3. Search Crops
- [ ] GET http://localhost:8080/api/crops/search?cropName=tomato
- [ ] Should filter crops by name

#### 4.4. Get Farmer's Crops (Digital Crop Card)
- [ ] GET http://localhost:8080/api/crops/farmer/1
- [ ] Should show all crops for farmer ID 1

---

### 5. Buyer Dashboard ✅

#### 5.1. Access Dashboard
- [ ] Go to: http://localhost:8080/buyer
- [ ] Verify page loads

#### 5.2. Register Buyer
- [ ] Fill in:
  - Name: John Doe
  - Email: john@example.com
  - Password: password123
- [ ] Click "Login / Register"
- [ ] Verify login success

#### 5.3. Browse Crops
- [ ] After login, verify crops are displayed
- [ ] Test search functionality
- [ ] Verify crop cards show all details

#### 5.4. Purchase Crop
- [ ] Click on a crop
- [ ] Enter quantity (e.g., 10 kg)
- [ ] Click "Buy Now"
- [ ] Verify transaction success
- [ ] Check updated availability

**Check:**
- [ ] Transaction is created
- [ ] Crop availability decreases
- [ ] Farmer credit score increases (check farmer API)

---

### 6. Transaction Management ✅

#### 6.1. Create Transaction via API
```bash
POST http://localhost:8080/api/transactions?cropId=1&buyerId=1&quantity=25
```

**Check:**
- [ ] Transaction created successfully
- [ ] Crop availableQty updated
- [ ] Farmer credit score increased

#### 6.2. View Buyer Transactions
```bash
GET http://localhost:8080/api/transactions/buyer/1
```

#### 6.3. View Farmer Transactions
```bash
GET http://localhost:8080/api/transactions/farmer/1
```

---

### 7. Rating System ✅

#### 7.1. Add Rating
```bash
POST http://localhost:8080/api/ratings
Content-Type: application/json

{
  "buyerId": 1,
  "farmerId": 1,
  "stars": 5,
  "comment": "Excellent quality produce"
}
```

#### 7.2. Get Farmer Ratings
```bash
GET http://localhost:8080/api/ratings/farmer/1
```

**Check:**
- [ ] Rating is saved
- [ ] Can retrieve ratings by farmer

---

### 8. Transport Network ✅

#### 8.1. Add Transporter
```bash
POST http://localhost:8080/api/transport
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

#### 8.2. Get All Transporters
```bash
GET http://localhost:8080/api/transport
```

#### 8.3. Get Available Transporters
```bash
GET http://localhost:8080/api/transport/available
```

---

### 9. Complete Workflow Test 🔄

Test the **complete farmer journey**:

1. **Farmer calls via voice**:
   ```
   Voice Input: "I have 50 kgs of wheat, I am from Hyderabad, I want 25 rupees per kg"
   ```

2. **Check Post Created**:
   ```bash
   GET http://localhost:8080/api/crops
   ```
   - [ ] Crop appears in list
   - [ ] Details are correct

3. **Buyer purchases**:
   - [ ] Login as buyer
   - [ ] Browse crops
   - [ ] Purchase 20 kg
   - [ ] Verify transaction

4. **Check Updates**:
   - [ ] Crop availability = 30 kg (50 - 20)
   - [ ] Transaction recorded
   - [ ] Farmer credit score increased

5. **Rate Farmer**:
   - [ ] Buyer rates farmer
   - [ ] Check rating appears

---

## 🎙️ Voice Testing Options

### Option 1: Browser Speech Recognition (Recommended)

I'll create an enhanced voice test page that uses browser's speech recognition API so you can speak directly!

### Option 2: Manual Text Input
Use the voice test page and type what farmers would say.

### Option 3: API Testing
Use curl or Postman to test the voice API directly.

---

## 📊 Final Verification Checklist

Before submission, verify:

- [ ] ✅ Application starts without errors
- [ ] ✅ All web pages load correctly
- [ ] ✅ Voice bot understands and creates posts
- [ ] ✅ Farmer registration works (web + API)
- [ ] ✅ Crop listing works
- [ ] ✅ Buyer dashboard works
- [ ] ✅ Purchase flow works end-to-end
- [ ] ✅ Credit scoring updates
- [ ] ✅ Ratings system works
- [ ] ✅ Transport network works
- [ ] ✅ Multi-language support works
- [ ] ✅ SMS notifications are logged (even if Twilio not configured)
- [ ] ✅ Database persists data
- [ ] ✅ No console errors

---

## 🐛 Common Issues & Solutions

### Issue: Voice API returns error
**Solution**: Check if OpenAI API key is configured. If not, it will use fallback parsing.

### Issue: No crops showing
**Solution**: Create crops via voice API or direct API call first.

### Issue: Transaction fails
**Solution**: Ensure crop has enough available quantity.

### Issue: Buyer login fails
**Solution**: Register buyer first via API or web interface.

---

## 📝 Submission Checklist

- [ ] All features tested
- [ ] No critical errors
- [ ] Documentation complete
- [ ] README updated
- [ ] Application.properties configured (at least with OpenAI key placeholder)
- [ ] All endpoints respond correctly
- [ ] Voice bot working (with or without OpenAI)
- [ ] Demo video/screenshots ready (if required)

---

## 🎯 Quick Test Commands

**Test Voice (English):**
```
http://localhost:8080/api/voice/test?speechText=I%20have%2020%20kgs%20of%20tomatoes&phone=%2B919876543210&language=en
```

**Test Voice (Hindi):**
```
http://localhost:8080/api/voice/test?speechText=मेरे%20पास%2050%20किलो%20गेहूं%20है&phone=%2B919876543211&language=hi
```

**View All Crops:**
```
http://localhost:8080/api/crops
```

**View All Farmers:**
```
http://localhost:8080/api/farmers
```

---

## 🎉 You're Ready!

If all tests pass, your application is ready for submission!

**Good Luck! 🌾**





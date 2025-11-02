# 🤖 Voice Bot Integration Guide - ChatGPT-like Assistant

## Overview

The Krishi Bandhu voice assistant has been integrated with OpenAI GPT to provide a ChatGPT-like conversational experience for farmers. The bot understands natural language in multiple Indian languages and automatically creates crop posts based on what farmers say.

## Features

✅ **GPT-Powered Understanding**: Uses OpenAI GPT-4o-mini to understand farmer requests in natural language  
✅ **Multi-Language Support**: Works with Hindi, Telugu, Tamil, Kannada, Malayalam, Marathi, Gujarati, Bengali, Punjabi, and English  
✅ **Automatic Post Creation**: Extracts crop details (name, quantity, price, location) and creates posts automatically  
✅ **SMS Confirmation**: Sends confirmation message to farmer when post is created  
✅ **Fallback Parsing**: If OpenAI is not configured, uses keyword-based parsing  

## Setup Instructions

### 1. Get OpenAI API Key

1. Go to https://platform.openai.com/api-keys
2. Sign up or log in
3. Create a new API key
4. Copy the key (it starts with `sk-...`)

### 2. Configure API Key

Edit `src/main/resources/application.properties`:

```properties
openai.api.key=sk-your-actual-api-key-here
```

**Note**: If you don't configure the API key, the system will use fallback keyword-based parsing which works but is less accurate.

### 3. Optional: Configure Twilio for SMS

To send SMS confirmations to farmers, update Twilio settings in `application.properties`:

```properties
twilio.account.sid=your_account_sid
twilio.auth.token=your_auth_token
twilio.phone.number=+1234567890
```

**Note**: SMS is optional. Even without Twilio, the system will log the messages that would be sent.

## How It Works

### Step 1: Farmer Calls and Speaks
Farmer says something like:
- English: "I have 20 kgs of tomatoes, I am from Hyderabad"
- Hindi: "मेरे पास 50 किलो गेहूं है, मैं इसे 25 रुपये प्रति किलो में बेचना चाहता हूं"
- Telugu: "నాకు 30 కిలోల బియ్యం ఉంది, నేను దానిని 20 రూపాయలకు విక్రయించాలనుకుంటున్నాను"

### Step 2: GPT Processes the Request
The OpenAI service:
1. Sends the farmer's speech to GPT-4o-mini
2. GPT extracts structured information:
   - Intent (SELL_CROP, ASK_QUESTION, etc.)
   - Crop name
   - Quantity (in kg)
   - Price (per kg in rupees)
   - Location
3. Returns a JSON response with all extracted data

### Step 3: Create Post and Notify
1. System creates a crop post with extracted details
2. Sends SMS confirmation to farmer (if Twilio configured)
3. Returns friendly response in farmer's language

## Testing the Voice Bot

### Using the Test Endpoint

```bash
# English
GET http://localhost:8080/api/voice/test?speechText=I%20have%2020%20kgs%20of%20tomatoes%20from%20Hyderabad&phone=%2B919876543210&language=en

# Hindi
GET http://localhost:8080/api/voice/test?speechText=मेरे%20पास%2050%20किलो%20गेहूं%20है%20मैं%20हैदराबाद%20से%20हूं&phone=%2B919876543210&language=hi

# Telugu
GET http://localhost:8080/api/voice/test?speechText=నాకు%2030%20కిలోల%20బియ్యం%20ఉంది%20నేను%20హైదరాబాద్%20నుండి%20వచ్చాను&phone=%2B919876543211&language=te
```

### Using the Voice Test Page

1. Start the application: `mvn spring-boot:run`
2. Go to: http://localhost:8080/voice-test
3. Enter:
   - Speech Text: "I have 20 kgs of tomatoes"
   - Phone: +919876543210
   - Language: English (or Hindi/Telugu)
4. Click "🚀 Test Voice API"

### Expected Response

```json
{
  "response": "Great! I've created your tomato post - 20.0 kg at ₹10.00 per kg. Buyers can now see it.",
  "action": "SELL_CROP_SUCCESS",
  "cropId": 1,
  "postCreated": true,
  "data": {
    "cropId": 1,
    "cropName": "tomato",
    "quantity": 20.0,
    "price": 10.0,
    "location": "Hyderabad"
  }
}
```

## Supported Languages

The bot supports these Indian languages:

- **English** (en) - Default
- **Hindi** (hi) - हिंदी
- **Telugu** (te) - తెలుగు
- **Tamil** (ta) - தமிழ்
- **Kannada** (kn) - ಕನ್ನಡ
- **Malayalam** (ml) - മലയാളം
- **Marathi** (mr) - मराठी
- **Gujarati** (gu) - ગુજરાતી
- **Bengali** (bn) - বাংলা
- **Punjabi** (pa) - ਪੰਜਾਬੀ

## Example Conversations

### Example 1: Selling Tomatoes
**Farmer**: "I have 20 kgs of tomatoes, I am from Hyderabad"

**Bot**: "I understood! You have 20 kg of tomato. I'm creating a post for you."

**Result**: 
- Post created with crop: tomato, quantity: 20 kg, location: Hyderabad
- SMS sent to farmer (if configured)

### Example 2: Selling Wheat with Price
**Farmer**: "मेरे पास 50 किलो गेहूं है, मैं इसे 25 रुपये प्रति किलो में बेचना चाहता हूं"

**Bot**: "बहुत बढ़िया! मैंने आपका गेहूं का पोस्ट बना दिया है - 50.0 kg, ₹25.00 प्रति किलो। खरीदार अब देख सकते हैं।"

**Result**:
- Post created with crop: wheat, quantity: 50 kg, price: ₹25/kg
- SMS sent to farmer in Hindi

### Example 3: General Question
**Farmer**: "How can I get a loan?"

**Bot**: "How can I help you? Do you want to sell crops, get loan information, or farming advice?"

**Result**: No post created, just helpful response

## Technical Details

### GPT Model Used
- Model: `gpt-4o-mini` (cost-effective and fast)
- Temperature: 0.3 (for consistent responses)
- Max Tokens: 500

### Fallback Mode

If OpenAI API key is not configured, the system uses keyword-based parsing:
- Detects keywords like "sell", "kg", crop names
- Extracts numbers for quantity and price
- Works but less accurate than GPT

### Response Format

The GPT service returns structured JSON:
```json
{
  "intent": "SELL_CROP",
  "cropName": "tomato",
  "quantity": 20.0,
  "price": 25.0,
  "location": "Hyderabad",
  "shouldCreatePost": true,
  "responseText": "Friendly response in farmer's language"
}
```

## Troubleshooting

### Issue: "Sorry, I had trouble understanding"
- **Cause**: GPT couldn't parse the input or API call failed
- **Solution**: Check OpenAI API key is correct, check internet connection

### Issue: SMS not sending
- **Cause**: Twilio not configured
- **Solution**: Configure Twilio credentials or check logs for SMS messages (they're logged even if not sent)

### Issue: Wrong crop name extracted
- **Cause**: Ambiguous input or fallback parsing limitations
- **Solution**: Be more specific in input, or configure OpenAI API key for better understanding

## Cost Considerations

OpenAI GPT-4o-mini is very cost-effective:
- ~$0.15 per 1M input tokens
- ~$0.60 per 1M output tokens
- Typical farmer request: ~200 tokens
- Cost per request: ~$0.0002 (very cheap!)

## Next Steps

1. Configure your OpenAI API key
2. Test with the voice test page
3. Try different languages
4. Integrate with actual phone call system (Twilio/Exotel)
5. Customize the system prompts for better understanding

## Support

For issues or questions:
1. Check logs in console output
2. Test with `/api/voice/test` endpoint
3. Verify API key is correct
4. Check network connectivity

---

**Happy Farming! 🌾**


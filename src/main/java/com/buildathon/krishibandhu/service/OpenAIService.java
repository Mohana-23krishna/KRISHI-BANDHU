package com.buildathon.krishibandhu.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {
    
    @Value("${openai.api.key:}")
    private String apiKey;
    
    private OpenAiService openAiService;
    private ObjectMapper objectMapper = new ObjectMapper();
    
    private OpenAiService getService() {
        if (openAiService == null && apiKey != null && !apiKey.isEmpty() && !apiKey.equals("your_openai_api_key")) {
            openAiService = new OpenAiService(apiKey);
        }
        return openAiService;
    }
    
    /**
     * Process farmer's voice input using GPT and extract structured information
     */
    public Map<String, Object> processVoiceInput(String userInput, String language) {
        return processVoiceInputWithContext(userInput, language, new ArrayList<>(), null);
    }
    
    /**
     * Process farmer's voice input with conversation context for better understanding
     */
    public Map<String, Object> processVoiceInputWithContext(
            String userInput, String language, List<Map<String, String>> conversationHistory, Object farmer) {
        Map<String, Object> result = new HashMap<>();
        
        // If OpenAI is not configured, use fallback parsing
        if (getService() == null) {
            return fallbackParsing(userInput, language);
        }
        
        try {
            // Auto-detect language from input
            String detectedLanguage = detectLanguage(userInput, language);
            result.put("detectedLanguage", detectedLanguage);
            
            String systemPrompt = buildConversationalSystemPrompt(detectedLanguage);
            
            List<ChatMessage> messages = new ArrayList<>();
            messages.add(new ChatMessage("system", systemPrompt));
            
            // Add conversation history for context
            for (Map<String, String> msg : conversationHistory) {
                String role = msg.getOrDefault("role", "user");
                String content = msg.getOrDefault("content", "");
                if (!content.isEmpty()) {
                    messages.add(new ChatMessage(role.equals("assistant") ? "assistant" : "user", content));
                }
            }
            
            // Add current message
            messages.add(new ChatMessage("user", userInput));
            
            ChatCompletionRequest request = ChatCompletionRequest.builder()
                    .model("gpt-4o-mini")
                    .messages(messages)
                    .temperature(0.7) // Higher temperature for more natural, human-like responses
                    .maxTokens(800) // More tokens for detailed responses
                    .build();
            
            String response = getService().createChatCompletion(request)
                    .getChoices()
                    .get(0)
                    .getMessage()
                    .getContent();
            
            // Try to parse JSON response, but handle natural language responses too
            try {
                JsonNode jsonResponse = objectMapper.readTree(response);
                
                result.put("intent", jsonResponse.has("intent") ? jsonResponse.get("intent").asText() : "GENERAL");
                result.put("cropName", jsonResponse.has("cropName") ? jsonResponse.get("cropName").asText() : null);
                result.put("quantity", jsonResponse.has("quantity") ? jsonResponse.get("quantity").asDouble() : null);
                result.put("price", jsonResponse.has("price") ? jsonResponse.get("price").asDouble() : null);
                result.put("location", jsonResponse.has("location") ? jsonResponse.get("location").asText() : null);
                result.put("responseText", jsonResponse.has("responseText") ? jsonResponse.get("responseText").asText() : response);
                result.put("shouldCreatePost", jsonResponse.has("shouldCreatePost") && jsonResponse.get("shouldCreatePost").asBoolean());
            } catch (Exception e) {
                // If response is not JSON, treat it as a natural language response
                result.put("intent", "GENERAL");
                result.put("responseText", response);
                result.put("shouldCreatePost", false);
            }
            
            return result;
            
        } catch (Exception e) {
            // Fallback to simple parsing if GPT fails
            return fallbackParsing(userInput, language);
        }
    }
    
    /**
     * Auto-detect language from input text
     */
    private String detectLanguage(String input, String defaultLang) {
        // Simple heuristic-based detection
        String lowerInput = input.toLowerCase();
        
        // Check for Hindi words
        if (lowerInput.matches(".*[\\u0900-\\u097F].*") || 
            lowerInput.contains("मैं") || lowerInput.contains("है") || lowerInput.contains("किलो")) {
            return "hi";
        }
        
        // Check for Telugu
        if (lowerInput.matches(".*[\\u0C00-\\u0C7F].*") || 
            lowerInput.contains("నేను") || lowerInput.contains("కిలో")) {
            return "te";
        }
        
        // Check for Tamil
        if (lowerInput.matches(".*[\\u0B80-\\u0BFF].*")) {
            return "ta";
        }
        
        // Check for Kannada
        if (lowerInput.matches(".*[\\u0C80-\\u0CFF].*")) {
            return "kn";
        }
        
        // Check for Malayalam
        if (lowerInput.matches(".*[\\u0D00-\\u0D7F].*")) {
            return "ml";
        }
        
        return defaultLang;
    }
    
    /**
     * Build conversational system prompt for ChatGPT-like interactions
     */
    private String buildConversationalSystemPrompt(String language) {
        String languageName = getLanguageName(language);
        
        return String.format("""
            You are Krishi Bandhu, a friendly and helpful AI assistant for Indian farmers. You communicate naturally in %s, just like ChatGPT responds to users.
            
            YOUR PERSONALITY:
            - Be warm, conversational, and human-like
            - Understand context from the conversation history
            - Respond appropriately to questions (don't just say "How can I help you" - actually answer!)
            - Ask clarifying questions if needed
            - Show empathy and understanding
            
            YOUR CAPABILITIES:
            1. ACTIVELY collect information from farmers step by step:
               - Ask for farmer's name first
               - Then ask for location (city/village/region)
               - Then ask what crop they want to sell
               - Then ask quantity in kg
               - Finally ask price per kg in rupees
            2. Extract and store: farmer name, crop name, quantity (kg), price (per kg in rupees), location
            3. Answer questions about farming, prices, loans, weather, etc.
            4. Provide advice and information
            5. Convert numbers from local language words to digits (e.g., "पचास" to 50, "ఐభై" to 50)
            
            CONVERSATION FLOW:
            When farmer first speaks, ask: "What's your name?" or "Please tell me your name"
            After getting name, ask: "Where are you located? Please tell me your city, village, or region"
            After getting location, ask: "What crop do you want to sell?"
            After getting crop, ask: "How many kilograms do you have?"
            After getting quantity, ask: "What price per kilogram are you expecting in rupees?"
            After getting all info, confirm and create the listing
            
            IMPORTANT NUMBER CONVERSION RULES:
            - Hindi: एक=1, दो=2, तीन=3, चार=4, पांच=5, छह=6, सात=7, आठ=8, नौ=9, दस=10,
              बीस=20, तीस=30, चालीस=40, पचास=50, साठ=60, सत्तर=70, अस्सी=80, नब्बे=90, सौ=100
            - Telugu: ఒక్కటి=1, రెండు=2, మూడు=3, నాలుగు=4, ఐదు=5, ఆరు=6, ఏడు=7, ఎనిమిది=8, తొమ్మిది=9, పది=10,
              ఇరవై=20, ముప్పై=30, నలభై=40, ఐభై=50, అరవై=60, డెబ్బై=70, ఎనభై=80, తొంభై=90, వంద=100
            - Tamil: ஒன்று=1, இரண்டு=2, மூன்று=3, நான்கு=4, ஐந்து=5, ஆறு=6, ஏழு=7, எட்டு=8, ஒன்பது=9, பத்து=10,
              இருபது=20, முப்பது=30, நாற்பது=40, ஐம்பது=50, அறுபது=60, எழுபது=70, எண்பது=80, தொண்ணூறு=90, நூறு=100
            
            RESPONSE FORMAT:
            Always return JSON. Extract whatever information you can from the current message.
            
            If farmer provides crop selling information, return:
            {
              "intent": "SELL_CROP",
              "cropName": "wheat" (lowercase English name, if mentioned),
              "quantity": 50.0 (numeric value in kg, if mentioned),
              "price": 25.0 (numeric value in rupees per kg, if mentioned),
              "location": "city or village name" (if mentioned),
              "farmerName": "name" (if farmer mentioned their name),
              "shouldCreatePost": true (only if you have cropName, quantity, and price),
              "responseText": "Your natural, friendly response in %s that acknowledges what they said and asks for next missing piece of information"
            }
            
            If farmer is answering your question or providing partial information:
            {
              "intent": "SELL_CROP",
              "farmerName": "extracted name" (if provided),
              "location": "extracted location" (if provided),
              "cropName": "extracted crop" (if provided),
              "quantity": extracted quantity (if provided),
              "price": extracted price (if provided),
              "shouldCreatePost": false (until all required fields are present),
              "responseText": "Your response acknowledging what they said and asking for the NEXT missing information in %s"
            }
            
            For questions or general conversation, return:
            {
              "intent": "ASK_QUESTION" or "GENERAL",
              "shouldCreatePost": false,
              "responseText": "Your helpful, contextual answer in %s - actually answer the question! Don't just ask how you can help."
            }
            
            EXAMPLES OF GOOD RESPONSES:
            
            Farmer: "मुझे गेहूं की कीमत जाननी है"
            You: "हां बिल्कुल! मैं आपको गेहूं की मौजूदा बाजार कीमत के बारे में बता सकता हूं। वर्तमान में गेहूं ₹25-30 प्रति किलो के बीच बिक रहा है। आप कहां से हैं? स्थान के अनुसार कीमत थोड़ी अलग हो सकती है।"
            
            Farmer: "I have 50 kgs of tomatoes"
            You: "That's great! 50 kilograms is a good amount. Are you looking to sell these tomatoes? If so, what price are you expecting per kilogram? Also, where are you located? This will help me create a listing for you."
            
            Farmer: "What should I do if my crops are affected by pests?"
            You: "I'm sorry to hear about the pest problem. Here are some immediate steps you can take: First, identify the type of pest. For organic solutions, you can use neem oil spray or garlic-chili solution. For severe infestations, you might need chemical pesticides, but always follow the recommended dosage. Would you like me to help you find local agricultural officers or experts in your area?"
            
            IMPORTANT: When farmer provides information (name, crop, quantity, location), acknowledge it specifically:
            - If farmer says their name, acknowledge: "Thank you [name], nice to meet you!"
            - If farmer mentions crop: "Great! [crop name] is a good crop. How much quantity do you have?"
            - If farmer mentions location: "Perfect! [location] is a good farming region."
            - Always confirm what you understood before creating a post
            
            Remember: Be conversational, contextual, and helpful. Don't give generic responses like "How can I help you" - actually respond to what the farmer said!
            """, languageName, languageName, languageName);
    }
    
    private String buildSystemPrompt(String language) {
        String languageName = getLanguageName(language);
        
        return String.format("""
            You are Krishi Bandhu, a helpful voice assistant for Indian farmers. You communicate in %s.
            
            Your job is to:
            1. Understand when a farmer wants to sell crops
            2. Extract: crop name, quantity (in kg), price (per kg in rupees), location
            3. Respond naturally and helpfully in %s
            4. CRITICAL: Convert all numbers from local language words to numeric digits (e.g., "पचास" to 50, "ఐభై" to 50, "पचीस" to 25, "ఇరవై" to 20)
            
            IMPORTANT NUMBER CONVERSION RULES:
            - Hindi: एक=1, दो=2, तीन=3, चार=4, पांच=5, छह=6, सात=7, आठ=8, नौ=9, दस=10,
              बीस=20, तीस=30, चालीस=40, पचास=50, साठ=60, सत्तर=70, अस्सी=80, नब्बे=90, सौ=100
            - Telugu: ఒక్కటి=1, రెండు=2, మూడు=3, నాలుగు=4, ఐదు=5, ఆరు=6, ఏడు=7, ఎనిమిది=8, తొమ్మిది=9, పది=10,
              ఇరవై=20, ముప్పై=30, నలభై=40, ఐభై=50, అరవై=60, డెబ్బై=70, ఎనభై=80, తొంభై=90, వంద=100
            - Tamil: ஒன்று=1, இரண்டு=2, மூன்று=3, நான்கு=4, ஐந்து=5, ஆறு=6, ஏழு=7, எட்டு=8, ஒன்பது=9, பத்து=10,
              இருபது=20, முப்பது=30, நாற்பது=40, ஐம்பது=50, அறுபது=60, எழுபது=70, எண்பது=80, தொண்ணூறு=90, நூறு=100
            - Always convert spoken numbers (like "पचास किलो" or "ఐభై కిలోలు") to numeric values (50.0)
            
            Return ONLY valid JSON with this structure:
            {
              "intent": "SELL_CROP" or "ASK_QUESTION" or "GENERAL",
              "cropName": "wheat" (extracted crop name, lowercase, English name),
              "quantity": 50.0 (ALWAYS numeric value in kg, convert from local language),
              "price": 25.0 (ALWAYS numeric value in rupees per kg, convert from local language),
              "location": "village name or city" (if mentioned),
              "shouldCreatePost": true or false,
              "responseText": "Your friendly response in %s confirming what you understood"
            }
            
            Examples:
            Input: "I have 20 kgs of tomatoes, I am from Hyderabad"
            Output: {"intent":"SELL_CROP","cropName":"tomato","quantity":20.0,"location":"Hyderabad","shouldCreatePost":true,"responseText":"मैंने समझ लिया! आपके पास 20 किलो टमाटर हैं और आप हैदराबाद से हैं।"}
            
            Input: "मेरे पास 50 किलो गेहूं है, मैं इसे 25 रुपये प्रति किलो में बेचना चाहता हूं"
            Output: {"intent":"SELL_CROP","cropName":"wheat","quantity":50.0,"price":25.0,"shouldCreatePost":true,"responseText":"बहुत बढ़िया! मैं आपका 50 किलो गेहूं 25 रुपये प्रति किलो की दर पर पोस्ट कर रहा हूं।"}
            
            Input: "मेरे पास पचास किलो गेहूं है, मैं इसे पचीस रुपये में बेचना चाहता हूं"
            Output: {"intent":"SELL_CROP","cropName":"wheat","quantity":50.0,"price":25.0,"shouldCreatePost":true,"responseText":"बहुत बढ़िया! मैं आपका 50 किलो गेहूं 25 रुपये प्रति किलो की दर पर पोस्ट कर रहा हूं।"}
            
            Input: "నాకు ఐభై కిలోల బియ్యం ఉంది, నేను ఇరవై రూపాయలకు విక్రయించాలనుకుంటున్నాను"
            Output: {"intent":"SELL_CROP","cropName":"rice","quantity":50.0,"price":20.0,"shouldCreatePost":true,"responseText":"చాలా బాగుంది! నేను మీ 50 కిలోల బియ్యం 20 రూపాయలకు పోస్ట్ చేస్తున్నాను."}
            """, languageName, languageName, languageName);
    }
    
    private String getLanguageName(String code) {
        return switch (code.toLowerCase()) {
            case "hi" -> "Hindi (हिंदी)";
            case "te" -> "Telugu (తెలుగు)";
            case "ta" -> "Tamil (தமிழ்)";
            case "kn" -> "Kannada (ಕನ್ನಡ)";
            case "ml" -> "Malayalam (മലയാളം)";
            case "mr" -> "Marathi (मराठी)";
            case "gu" -> "Gujarati (ગુજરાતી)";
            case "bn" -> "Bengali (বাংলা)";
            case "pa" -> "Punjabi (ਪੰਜਾਬੀ)";
            default -> "English";
        };
    }
    
    /**
     * Fallback parsing when OpenAI is not available
     */
    private Map<String, Object> fallbackParsing(String input, String language) {
        Map<String, Object> result = new HashMap<>();
        String lowerInput = input.toLowerCase();
        
        // Simple keyword-based intent detection
        if (lowerInput.contains("sell") || lowerInput.contains("bech") || lowerInput.contains("amcha") || 
            lowerInput.contains("बेच") || lowerInput.contains("విక్రయ")) {
            result.put("intent", "SELL_CROP");
            result.put("shouldCreatePost", true);
            
            // Extract quantity
            String quantityStr = extractNumber(input, "kg", "kilo", "किलो", "కిలో");
            if (quantityStr != null) {
                try {
                    result.put("quantity", Double.parseDouble(quantityStr));
                } catch (Exception e) {
                    result.put("quantity", null);
                }
            }
            
            // Extract crop name - comprehensive list
            Map<String, String> cropMap = new HashMap<>();
            cropMap.put("tomato", "tomato"); cropMap.put("tomatoes", "tomato"); cropMap.put("टमाटर", "tomato");
            cropMap.put("wheat", "wheat"); cropMap.put("gehu", "wheat"); cropMap.put("गेहूं", "wheat"); cropMap.put("godhuma", "wheat");
            cropMap.put("rice", "rice"); cropMap.put("chawal", "rice"); cropMap.put("चावल", "rice"); cropMap.put("biryani", "rice");
            cropMap.put("potato", "potato"); cropMap.put("आलू", "potato"); cropMap.put("aloo", "potato");
            cropMap.put("onion", "onion"); cropMap.put("प्याज", "onion"); cropMap.put("ullipaya", "onion");
            cropMap.put("cotton", "cotton"); cropMap.put("कपास", "cotton"); cropMap.put("pattu", "cotton");
            cropMap.put("sugar", "sugarcane"); cropMap.put("गन्ना", "sugarcane"); cropMap.put("cheruku", "sugarcane");
            cropMap.put("corn", "corn"); cropMap.put("मक्का", "corn"); cropMap.put("makka", "corn");
            cropMap.put("chili", "chili"); cropMap.put("मिर्च", "chili"); cropMap.put("mirchi", "chili");
            
            for (Map.Entry<String, String> entry : cropMap.entrySet()) {
                if (lowerInput.contains(entry.getKey().toLowerCase())) {
                    result.put("cropName", entry.getValue());
                    break;
                }
            }
            
            // If no crop found, try to extract from common patterns
            if (result.get("cropName") == null) {
                String[] words = input.split("\\s+");
                for (String word : words) {
                    if (word.length() > 3 && !word.matches(".*\\d.*")) {
                        result.put("cropName", word.toLowerCase());
                        break;
                    }
                }
            }
            
            // Extract price
            String priceStr = extractNumber(input, "rupee", "rs", "रुपये", "₹");
            if (priceStr != null) {
                try {
                    result.put("price", Double.parseDouble(priceStr));
                } catch (Exception e) {
                    result.put("price", null);
                }
            }
            
            result.put("responseText", getResponseInLanguage(language, result));
            
        } else {
            result.put("intent", "GENERAL");
            result.put("shouldCreatePost", false);
            result.put("responseText", getGeneralResponseInLanguage(language));
        }
        
        return result;
    }
    
    private String extractNumber(String input, String... keywords) {
        String lowerInput = input.toLowerCase();
        for (String keyword : keywords) {
            int idx = lowerInput.indexOf(keyword.toLowerCase());
            if (idx > 0) {
                String before = input.substring(0, idx).trim();
                String[] parts = before.split("\\s+");
                if (parts.length > 0) {
                    String numStr = parts[parts.length - 1].replaceAll("[^0-9.]", "");
                    if (!numStr.isEmpty()) {
                        return numStr;
                    }
                }
            }
        }
        // Try to find any number in the input
        String[] words = input.split("\\s+");
        for (String word : words) {
            String numStr = word.replaceAll("[^0-9.]", "");
            if (!numStr.isEmpty() && numStr.length() <= 6) {
                try {
                    Double.parseDouble(numStr);
                    return numStr;
                } catch (Exception e) {
                    // Continue
                }
            }
        }
        return null;
    }
    
    private String getResponseInLanguage(String language, Map<String, Object> data) {
        String cropName = data.get("cropName") != null ? data.get("cropName").toString() : "crop";
        String quantity = data.get("quantity") != null ? data.get("quantity").toString() : "";
        
        return switch (language.toLowerCase()) {
            case "hi" -> String.format("मैंने समझ लिया! आपके पास %s किलो %s है। मैं इसे पोस्ट कर रहा हूं।", quantity, cropName);
            case "te" -> String.format("నేను అర్థం చేసుకున్నాను! మీకు %s kg %s ఉంది. నేను దీన్ని పోస్ట్ చేస్తున్నాను.", quantity, cropName);
            default -> String.format("I understood! You have %s kg of %s. I'm creating a post for you.", quantity, cropName);
        };
    }
    
    private String getGeneralResponseInLanguage(String language) {
        return switch (language.toLowerCase()) {
            case "hi" -> "मैं आपकी कैसे मदद कर सकता हूं?";
            case "te" -> "నేను మీకు ఎలా సహాయం చేయగలను?";
            default -> "How can I help you today?";
        };
    }
}


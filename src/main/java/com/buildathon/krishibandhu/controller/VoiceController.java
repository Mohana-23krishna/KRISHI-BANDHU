package com.buildathon.krishibandhu.controller;

import com.buildathon.krishibandhu.dto.VoiceRequest;
import com.buildathon.krishibandhu.dto.VoiceResponse;
import com.buildathon.krishibandhu.model.Crop;
import com.buildathon.krishibandhu.model.Farmer;
import com.buildathon.krishibandhu.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/voice")
@CrossOrigin(origins = "*")
public class VoiceController {
    
    @Autowired
    private OpenAIService openAIService;
    
    @Autowired
    private FarmerService farmerService;
    
    @Autowired
    private CropService cropService;
    
    @Autowired
    private NotificationService notificationService;
    
    /**
     * Test endpoint - replaces the old voice test API with GPT-powered bot
     */
    @GetMapping("/test")
    public ResponseEntity<VoiceResponse> testVoice(
            @RequestParam String speechText,
            @RequestParam(required = false, defaultValue = "+919876543210") String phone,
            @RequestParam(required = false, defaultValue = "en") String language) {
        
        return processVoiceInput(speechText, phone, language);
    }
    
    /**
     * Process voice request - Main endpoint for voice processing
     */
    @PostMapping("/process")
    public ResponseEntity<VoiceResponse> processVoice(@RequestBody VoiceRequest request) {
        return processVoiceInput(
            request.getSpeechText(),
            request.getPhone(),
            request.getLanguage()
        );
    }
    
    /**
     * Core logic to process voice input using GPT
     */
    private ResponseEntity<VoiceResponse> processVoiceInput(String speechText, String phone, String language) {
        try {
            // Step 1: Get or create farmer
            Farmer farmer = farmerService.registerOrGetFarmer(phone, language);
            
            // Step 2: Use OpenAI GPT to understand the farmer's request
            Map<String, Object> aiResult = openAIService.processVoiceInput(speechText, language);
            
            String intent = (String) aiResult.getOrDefault("intent", "GENERAL");
            Boolean shouldCreatePost = (Boolean) aiResult.getOrDefault("shouldCreatePost", false);
            
            VoiceResponse response = new VoiceResponse();
            Map<String, Object> responseData = new HashMap<>();
            
            // Step 3: If intent is to sell crop, create the post
            if ("SELL_CROP".equals(intent) && shouldCreatePost) {
                String cropName = (String) aiResult.get("cropName");
                Double quantity = aiResult.get("quantity") != null ? 
                    Double.parseDouble(aiResult.get("quantity").toString()) : null;
                Double price = aiResult.get("price") != null ? 
                    Double.parseDouble(aiResult.get("price").toString()) : null;
                String location = (String) aiResult.get("location");
                
                // Use default values if not provided
                if (quantity == null || quantity <= 0) {
                    quantity = 1.0; // Default to 1 kg
                }
                if (price == null || price <= 0) {
                    price = 10.0; // Default to ₹10/kg
                }
                if (cropName == null || cropName.isEmpty()) {
                    cropName = "Crop";
                }
                if (location == null || location.isEmpty()) {
                    location = farmer.getAddress() != null ? farmer.getAddress() : "Not specified";
                }
                
                // Create the crop post
                Crop crop = cropService.createCrop(farmer, cropName, quantity, price, location);
                
                // Step 4: Send confirmation SMS to farmer
                notificationService.sendCropPostConfirmation(
                    phone, 
                    language, 
                    cropName, 
                    quantity, 
                    price, 
                    crop.getId()
                );
                
                // Build success response
                response.setResponse((String) aiResult.getOrDefault("responseText", 
                    getSuccessMessage(language, cropName, quantity, price)));
                response.setAction("SELL_CROP_SUCCESS");
                response.setCropId(crop.getId());
                response.setPostCreated(true);
                
                responseData.put("cropId", crop.getId());
                responseData.put("cropName", cropName);
                responseData.put("quantity", quantity);
                responseData.put("price", price);
                responseData.put("location", location);
                response.setData(responseData);
                
                return ResponseEntity.ok(response);
            } else {
                // General query or other intent
                String responseText = (String) aiResult.getOrDefault("responseText", 
                    getGeneralMessage(language));
                response.setResponse(responseText);
                response.setAction(intent);
                response.setPostCreated(false);
                response.setData(responseData);
                
                return ResponseEntity.ok(response);
            }
            
        } catch (Exception e) {
            // Error handling
            VoiceResponse errorResponse = new VoiceResponse();
            errorResponse.setResponse(getErrorMessage(language));
            errorResponse.setAction("ERROR");
            errorResponse.setPostCreated(false);
            return ResponseEntity.ok(errorResponse);
        }
    }
    
    private String getSuccessMessage(String language, String cropName, Double quantity, Double price) {
        return switch (language.toLowerCase()) {
            case "hi" -> String.format("बहुत बढ़िया! मैंने आपका %s का पोस्ट बना दिया है - %.1f kg, ₹%.2f प्रति किलो। खरीदार अब देख सकते हैं।", 
                cropName, quantity, price);
            case "te" -> String.format("చాలా బాగుంది! నేను మీ %s పోస్ట్ సృష్టించాను - %.1f kg, ₹%.2f per kg. కొనుగోలుదారులు ఇప్పుడు చూడగలరు.", 
                cropName, quantity, price);
            default -> String.format("Great! I've created your %s post - %.1f kg at ₹%.2f per kg. Buyers can now see it.", 
                cropName, quantity, price);
        };
    }
    
    private String getGeneralMessage(String language) {
        return switch (language.toLowerCase()) {
            case "hi" -> "मैं आपकी कैसे मदद कर सकता हूं? आप फसल बेचना चाहते हैं, लोन की जानकारी चाहते हैं, या खेती के सुझाव?";
            case "te" -> "నేను మీకు ఎలా సహాయం చేయగలను? మీరు పంట విక్రయించాలనుకుంటున్నారా, రుణ సమాచారం కావాలా, లేదా వ్యవసాయ సలహాలు కావాలా?";
            default -> "How can I help you? Do you want to sell crops, get loan information, or farming advice?";
        };
    }
    
    private String getErrorMessage(String language) {
        return switch (language.toLowerCase()) {
            case "hi" -> "क्षमा करें, मुझे समझने में कुछ समस्या हुई। कृपया फिर से बताएं।";
            case "te" -> "క్షమించండి, నాకు అర్థం చేసుకోవడంలో సమస్య ఉంది. దయచేసి మళ్లీ చెప్పండి.";
            default -> "Sorry, I had trouble understanding. Please try again.";
        };
    }
    
    /**
     * Welcome message endpoint
     */
    @GetMapping("/welcome")
    public ResponseEntity<Map<String, String>> welcome(
            @RequestParam(required = false, defaultValue = "en") String language) {
        Map<String, String> response = new HashMap<>();
        response.put("message", getWelcomeMessage(language));
        return ResponseEntity.ok(response);
    }
    
    private String getWelcomeMessage(String language) {
        return switch (language.toLowerCase()) {
            case "hi" -> "नमस्ते! मैं Krishi Bandhu हूं, किसानों का सच्चा दोस्त। मैं आपकी कैसे मदद कर सकता हूं?";
            case "te" -> "నమస్కారం! నేను Krishi Bandhu, రైతుల యొక్క నిజమైన స్నేహితుడు. నేను మీకు ఎలా సహాయం చేయగలను?";
            default -> "Hello! I'm Krishi Bandhu, a true friend of farmers. How can I help you today?";
        };
    }
}


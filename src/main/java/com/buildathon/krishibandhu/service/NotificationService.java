package com.buildathon.krishibandhu.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    
    @Value("${twilio.account.sid:}")
    private String accountSid;
    
    @Value("${twilio.auth.token:}")
    private String authToken;
    
    @Value("${twilio.phone.number:}")
    private String twilioPhoneNumber;
    
    /**
     * Send SMS confirmation to farmer
     */
    public void sendSMS(String phoneNumber, String message) {
        if (accountSid == null || accountSid.isEmpty() || accountSid.equals("your_account_sid")) {
            // SMS not configured, just log
            System.out.println("[SMS] Would send to " + phoneNumber + ": " + message);
            return;
        }
        
        try {
            Twilio.init(accountSid, authToken);
            Message.creator(
                    new com.twilio.type.PhoneNumber(phoneNumber),
                    new com.twilio.type.PhoneNumber(twilioPhoneNumber),
                    message
            ).create();
        } catch (Exception e) {
            System.err.println("Failed to send SMS: " + e.getMessage());
            // Don't fail the entire request if SMS fails
        }
    }
    
    /**
     * Send confirmation message when crop post is created
     */
    public void sendCropPostConfirmation(String phoneNumber, String language, String cropName, Double quantity, Double price, Long cropId) {
        String message = buildConfirmationMessage(language, cropName, quantity, price, cropId);
        sendSMS(phoneNumber, message);
    }
    
    private String buildConfirmationMessage(String language, String cropName, Double quantity, Double price, Long cropId) {
        return switch (language.toLowerCase()) {
            case "hi" -> String.format(
                "✅ Krishi Bandhu: आपका %s का पोस्ट बन गया है!\n" +
                "📦 फसल: %s\n" +
                "⚖️ मात्रा: %.1f kg\n" +
                "💰 मूल्य: ₹%.2f/kg\n" +
                "🆔 पोस्ट ID: %d\n\n" +
                "खरीदार अब आपकी फसल देख सकते हैं।",
                cropName, cropName, quantity, price, cropId
            );
            case "te" -> String.format(
                "✅ Krishi Bandhu: మీ %s పోస్ట్ సృష్టించబడింది!\n" +
                "📦 పంట: %s\n" +
                "⚖️ పరిమాణం: %.1f kg\n" +
                "💰 ధర: ₹%.2f/kg\n" +
                "🆔 పోస్ట్ ID: %d\n\n" +
                "కొనుగోలుదారులు ఇప్పుడు మీ పంటను చూడగలరు.",
                cropName, cropName, quantity, price, cropId
            );
            default -> String.format(
                "✅ Krishi Bandhu: Your %s post has been created!\n" +
                "📦 Crop: %s\n" +
                "⚖️ Quantity: %.1f kg\n" +
                "💰 Price: ₹%.2f/kg\n" +
                "🆔 Post ID: %d\n\n" +
                "Buyers can now see your crop.",
                cropName, cropName, quantity, price, cropId
            );
        };
    }
}


package com.buildathon.krishibandhu.dto;

import lombok.Data;

@Data
public class VoiceRequest {
    private String speechText;
    private String phone;
    private String language = "en";
}


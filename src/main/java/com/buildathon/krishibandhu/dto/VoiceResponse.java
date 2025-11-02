package com.buildathon.krishibandhu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoiceResponse {
    private String response;
    private String action;
    private Map<String, Object> data;
    private Long cropId;
    private Boolean postCreated;
}


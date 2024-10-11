package com.shopingcart.shopingcart.responses;

import lombok.Data;

@Data
public class SuccessResponse {
    private String message;
    private int statusCode;

    public SuccessResponse(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    // Getters and Setters
}

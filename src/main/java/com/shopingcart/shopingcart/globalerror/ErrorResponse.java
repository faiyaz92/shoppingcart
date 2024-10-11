package com.shopingcart.shopingcart.globalerror;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorResponse {
    private String message;
    private int status;
    private String error;

    // Optionally, you can add a constructor to initialize fields
    public ErrorResponse(String message, int status, String error) {
        this.message = message;
        this.status = status;
        this.error = error;
    }
}

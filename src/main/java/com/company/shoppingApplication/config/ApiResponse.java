package com.company.shoppingApplication.config;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ApiResponse {

    private String message;
    private Object data;

    public ApiResponse(String message, Object o) {
    }
}

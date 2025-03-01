package com.company.shoppingApplication.services;


import lombok.Data;

@Data
public class UserUpdateRequest extends com.company.shoppingApplication.request.UserUpdateRequest {

    private String firstName;
    private String lastName;
}

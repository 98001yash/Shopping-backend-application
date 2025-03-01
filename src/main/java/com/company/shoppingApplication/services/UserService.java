package com.company.shoppingApplication.services;

import com.company.shoppingApplication.entities.User;
import com.company.shoppingApplication.request.CreateUserRequest;

public interface UserService {

    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);

}

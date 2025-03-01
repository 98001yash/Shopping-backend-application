package com.company.shoppingApplication.services.Impl;

import com.company.shoppingApplication.Exceptions.AlreadyExistsException;
import com.company.shoppingApplication.Exceptions.ResourceNotFoundException;
import com.company.shoppingApplication.entities.User;
import com.company.shoppingApplication.repository.UserRepository;
import com.company.shoppingApplication.request.CreateUserRequest;
import com.company.shoppingApplication.services.UserService;
import com.company.shoppingApplication.services.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(()->
                new ResourceNotFoundException("User not found"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(request.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setEmail(req.getEmail());
                    user.setPassword(req.getPassword());
                    user.setFirstName(req.getFirstName());
                    user.setLastName(req.getLastName());
                    return userRepository.save(user);
                }).orElseThrow(() -> new AlreadyExistsException(request.getEmail() + " User Already exists!!"));
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
       return userRepository.findById(userId).map(exisitingUser -> {
           exisitingUser.setFirstName(request.getFirstName());
           exisitingUser.setLastName(request.getLastName());
           return userRepository.save(exisitingUser);

       }).orElseThrow(()-> new ResourceNotFoundException("user not found"));
    }

    @Override
    public void deleteUser(Long userId) {
         userRepository.findById(userId).ifPresentOrElse(userRepository:: delete, ()->{
             throw new ResourceNotFoundException("User not found");
         });
    }
}

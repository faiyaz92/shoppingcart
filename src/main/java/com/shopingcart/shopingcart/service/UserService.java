package com.shopingcart.shopingcart.service;

import com.shopingcart.shopingcart.data.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    UserDto createUser(UserDto user);
    UserDto updateUser(UserDto user);
    void deleteUser(Long id);
    boolean userExists(Long id);
    boolean userExistsByEmail(String email);
}

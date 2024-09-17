package com.shopingcart.shopingcart.service;

import com.shopingcart.shopingcart.data.UserDto;
import com.shopingcart.shopingcart.entity.UserEntity;
import com.shopingcart.shopingcart.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();
        List<UserDto> users = new ArrayList<>();
        for (UserEntity userEntity : userEntities) {
            UserDto user = new UserDto();
            BeanUtils.copyProperties(userEntity, user);
            users.add(user);
        }
        return users;
    }
    
    @Override
    public UserDto getUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElse(null);
        if (userEntity != null) {
            UserDto user = new UserDto();
            BeanUtils.copyProperties(userEntity, user);
            return user;
        }
        return null;
    }
    
    @Override
    public UserDto createUser(UserDto user) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);
        userEntity = userRepository.save(userEntity);
        BeanUtils.copyProperties(userEntity, user);
        return user;
    }
    
    @Override
    public UserDto updateUser(UserDto user) {
        UserEntity userEntity = userRepository.findById(user.getId()).orElse(null);
        if (userEntity != null) {
            BeanUtils.copyProperties(user, userEntity);
            userEntity = userRepository.save(userEntity);
            BeanUtils.copyProperties(userEntity, user);
            return user;
        }
        return null;
    }
    
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    @Override
    public boolean userExists(Long id) {
        return userRepository.existsById(id);
    }
    
    @Override
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}

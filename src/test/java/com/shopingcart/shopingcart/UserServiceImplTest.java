package com.shopingcart.shopingcart;

import com.shopingcart.shopingcart.data.UserDto;
import com.shopingcart.shopingcart.entity.UserEntity;
import com.shopingcart.shopingcart.repository.UserRepository;
import com.shopingcart.shopingcart.service.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    public void testGetAllUsers() {
        // Given
        List<UserEntity> userEntities = new ArrayList<>();
        UserEntity userEntity1 = new UserEntity();
        userEntity1.setId(1L);
        userEntity1.setName("Faiyaz");
        userEntity1.setEmail("Faiyaz@gmail.com");
        userEntities.add(userEntity1);

        UserEntity userEntity2 = new UserEntity();
        userEntity2.setId(2L);
        userEntity2.setName("Jane");
        userEntity2.setEmail("jane@gmail.com");
        userEntities.add(userEntity2);

        when(userRepository.findAll()).thenReturn(userEntities);

        // When
        List<UserDto> users = userServiceImpl.getAllUsers();

        // Then
        assertNotNull(users);
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testGetUserByIdExisting() {
        // Given
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("Faiyaz");
        userEntity.setEmail("Faiyaz@gmail.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        // When
        UserDto user = userServiceImpl.getUserById(1L);

        // Then
        assertNotNull(user);
        assertEquals(1L, user.getId());
        verify(userRepository, times(1)).findById(1L);
    }
    @Test
    public void testGetUserByIdNonExisting() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When and Then
        assertNull(userServiceImpl.getUserById(1L));
    }

    @Test
    public void testCreateUser() {
        // Given
        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("Faiyaz");
        user.setEmail("Faiyaz@gmail.com");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("Faiyaz");
        userEntity.setEmail("Faiyaz@gmail.com");
        when(userRepository.save(org.mockito.Mockito.any(UserEntity.class))).thenReturn(userEntity);

        // When
        UserDto createdUser = userServiceImpl.createUser(user);

        // Then
        assertNotNull(createdUser);
        assertEquals(1L, createdUser.getId());
        verify(userRepository, times(1)).save(org.mockito.Mockito.any(UserEntity.class));
    }

    @Test(expected = RuntimeException.class)
    public void testCreateUserInvalidInput() {
        // Given
        UserDto user = null;

        // When
        userServiceImpl.createUser(user);
    }

    @Test
    public void testUpdateUserExisting() {
        // Given
        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("Faiyaz");
        user.setEmail("Faiyaz@gmail.com");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("Faiyaz");
        userEntity.setEmail("Faiyaz@gmail.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(org.mockito.Mockito.any(UserEntity.class))).thenReturn(userEntity);

        // When
        UserDto updatedUser = userServiceImpl.updateUser(user);

        // Then
        assertNotNull(updatedUser);
        assertEquals(1L, updatedUser.getId());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(org.mockito.Mockito.any(UserEntity.class));
    }

    @Test
    public void testDeleteUser() {
        // Given
        doNothing().when(userRepository).deleteById(1L);

        // When
        userServiceImpl.deleteUser(1L);

        // Then
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testUserExists() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);

        // When
        boolean exists = userServiceImpl.userExists(1L);

        // Then
        assertTrue(exists);
        verify(userRepository, times(1)).existsById(1L);
    }

    @Test
    public void testUserExistsByEmail() {
        // Given
        when(userRepository.existsByEmail("Faiyaz@gmail.com")).thenReturn(true);

        // When
        boolean exists = userServiceImpl.userExistsByEmail("Faiyaz@gmail.com");

        // Then
        assertTrue(exists);
        verify(userRepository, times(1)).existsByEmail("Faiyaz@gmail.com");
    }
}
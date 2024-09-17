package com.shopingcart.shopingcart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopingcart.shopingcart.controller.UserController;
import com.shopingcart.shopingcart.data.UserDto;
import com.shopingcart.shopingcart.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    private static final String API_URL = "/api/users";
    private static final Long USER_ID = 1L;
    private static final String USER_NAME = "Faiyaz";
    private static final String USER_EMAIL = "Faiyaz@email.com";
    private static final String USER_CONTACT_NUMBER = "1234567890";
    private static final MediaType APPLICATION_JSON = MediaType.APPLICATION_JSON;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    private UserDto getUser() {
        return new UserDto(USER_ID, USER_NAME, USER_CONTACT_NUMBER, USER_EMAIL);
    }

    private String asJsonString(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetAllUsers() throws Exception {
        List<UserDto> users = new ArrayList<>();
        users.add(getUser());
        users.add(new UserDto(2L, "Jane", "1111111111", "jane@email.com"));
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get(API_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(is(2)));
    }

    @Test
    public void testGetUserByIdExisting() throws Exception {
        UserDto user = getUser();
        when(userService.getUserById(USER_ID)).thenReturn(user);

        mockMvc.perform(get(API_URL + "/" + USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(is(USER_ID.intValue())))
                .andExpect(jsonPath("$.name").value(is(USER_NAME)))
                .andExpect(jsonPath("$.email").value(is(USER_EMAIL)))
                .andExpect(jsonPath("$.contactNumber").value(is(USER_CONTACT_NUMBER)));
    }

    @Test
    public void testGetUserByIdNonExisting() throws Exception {
        when(userService.getUserById(USER_ID)).thenReturn(null);

        mockMvc.perform(get(API_URL + "/" + USER_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateUser() throws Exception {
        UserDto user = getUser();
        when(userService.createUser(any(UserDto.class))).thenReturn(user);

        String jsonRequest = asJsonString(user);
        System.out.println("JSON Request: " + jsonRequest);

        mockMvc.perform(post(API_URL)
                        .contentType(APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print()) // Print JSON response
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(is(USER_ID.intValue())))
                .andExpect(jsonPath("$.name").value(is(USER_NAME)))
                .andExpect(jsonPath("$.email").value(is(USER_EMAIL)))
                .andExpect(jsonPath("$.contactNumber").value(is(USER_CONTACT_NUMBER)));

    }

    @Test
    public void testUpdateUser() throws Exception {
        UserDto user = getUser();
        when(userService.updateUser(any(UserDto.class))).thenReturn(user);

        mockMvc.perform(put(API_URL)
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(is(USER_ID.intValue())))
                .andExpect(jsonPath("$.name").value(is(USER_NAME)))
                .andExpect(jsonPath("$.email").value(is(USER_EMAIL)))
                .andExpect(jsonPath("$.contactNumber").value(is(USER_CONTACT_NUMBER)));
    }

    @Test
    public void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(USER_ID);

        mockMvc.perform(delete(API_URL + "/" + USER_ID))
                .andExpect(status().isNoContent());
    }
}
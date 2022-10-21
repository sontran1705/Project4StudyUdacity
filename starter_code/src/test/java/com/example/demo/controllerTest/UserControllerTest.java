package com.example.demo.controllerTest;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.Assert.*;

public class UserControllerTest {
    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        UtilsTest.injectObjects(userController, "userRepository", userRepository);
        UtilsTest.injectObjects(userController, "cartRepository", cartRepository);
        UtilsTest.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void testCreateUserSuccessfully() {
        when(bCryptPasswordEncoder.encode("1234567")).thenReturn("1234567");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("sontv15");
        request.setPassword("1234567");
        request.setCfmPassword("1234567");
        final ResponseEntity<User> response = userController.createUser(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("sontv15", user.getUsername());
        assertEquals("1234567", user.getPassword());
    }

    @Test
    public void testCreateUserConfirmPasswordNotMatch() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("sontv15");
        request.setPassword("1234567");
        request.setCfmPassword("1234567");
        ResponseEntity<?> response = userController.createUser(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testFindByUserId() {
        User userMock = new User();
        userMock.setId(1L);
        when(userRepository.findById(1l)).thenReturn(Optional.of(userMock));
        ResponseEntity<User> user = userController.findById(1l);
        assertEquals(200, user.getStatusCodeValue());
    }

    @Test
    public void testFindByUsername() {
        User userMock = new User();
        userMock.setId(1L);
        userMock.setUsername("sontv15");
        when(userRepository.findByUsername("sontv15")).thenReturn(userMock);
        ResponseEntity<User> user = userController.findByUserName("sontv15");
        assertEquals(200, user.getStatusCodeValue());
    }
}

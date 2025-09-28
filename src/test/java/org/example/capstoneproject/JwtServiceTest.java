package org.example.capstoneproject;


import org.example.capstoneproject.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        userDetails = new User("testuser", "password", Collections.emptyList());

        // set default secret and expiration manually (because @Value doesn't work in plain unit test)
        setField(jwtService, "secretKey", "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        setField(jwtService, "jwtExpiration", 1000L * 60 * 60); // 1 hour
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGenerateAndValidateToken() {
        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token, userDetails));
        assertEquals("testuser", jwtService.extractUsername(token));
    }

    @Test
    void testInvalidToken() {
        // corrupt token by appending garbage
        String token = jwtService.generateToken(userDetails) + "123";

        assertThrows(Exception.class, () -> jwtService.extractUsername(token));
    }

    @Test
    void testExpiredTokenThrows() throws Exception {
        setField(jwtService, "jwtExpiration", 1L);

        String token = jwtService.generateToken(userDetails);

        Thread.sleep(5);

        assertThrows(io.jsonwebtoken.ExpiredJwtException.class,
                () -> jwtService.extractUsername(token));
    }
}

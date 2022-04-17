package com.example.taskmanager.security.JWTSecurity;

import com.example.taskmanager.entity.User;
import com.example.taskmanager.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtUtilTest {
    @Mock
    TokenRepository tokenRepository;
    @Mock
    Claims claims;
    @InjectMocks
    JwtUtil jwtUtil;

    private final User user = new User(1L, "Sarah", "Sarahajam@gmail.com", "sarahY", "sarah123", 21);

    @Test
    void isTokenInDBPass(){
        when(tokenRepository.existsById(anyString())).thenReturn(true);
        assertEquals(true, jwtUtil.isTokenInDB(anyString(),this.user));
    }
    @Test
    void isTokenInDBFail(){
        when(tokenRepository.existsById(anyString())).thenReturn(false);
        assertEquals(false, jwtUtil.isTokenInDB(anyString(),this.user));
    }

    @Test
    void generateToken(){
        String Token = jwtUtil.generateToken(this.user);
        assertEquals(Token,jwtUtil.generateToken(this.user));
    }

    @Test
    void validateTokenPass(){
        String Token = jwtUtil.generateToken(this.user);
        when(tokenRepository.existsById(Token)).thenReturn(true);
        assertEquals(true,jwtUtil.validateToken(Token,this.user));
    }

    @Test
    void validateTokenFail(){
        String Token = jwtUtil.generateToken(this.user);
        when(tokenRepository.existsById(Token)).thenReturn(false);
        assertEquals(false,jwtUtil.validateToken(Token,this.user));



    }




}

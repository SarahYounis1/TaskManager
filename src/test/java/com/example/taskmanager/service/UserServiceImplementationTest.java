package com.example.taskmanager.service;
import com.example.taskmanager.entity.Tokens;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.exception.UserAlreadyExistException;
import com.example.taskmanager.models.AuthenticationRequest;
import com.example.taskmanager.models.AuthenticationResponse;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.TokenRepository;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.security.JWTSecurity.JwtUtil;
import com.example.taskmanager.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class UserServiceImplementationTest {

    @Mock
    private UserDetailsServiceImpl userDetailsService;
    @Mock
    private JwtUtil jwtTokenUtil;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
     private UserServiceImplementation userServiceImplementation;


    User user = new User(1L,"Sarah","Sarahajam@gmail.com","sarahY","sarah123",21);

    @Test
    void createNewUserPass() throws Exception{
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.user.setPassword("{bcrypt}" + encoder.encode(this.user.getPassword()));
        when(userRepository.save(this.user)).thenReturn(this.user);
        assertEquals(this.user,userServiceImplementation.createNewUser(this.user));

    }
    @Test
    void createNewUserFail(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.user.setPassword("{bcrypt}" + encoder.encode(this.user.getPassword()));
        when(userRepository.findByUsername(this.user.getUsername())).
                thenReturn(Optional.of(this.user));
        assertThrows(UserAlreadyExistException.class,
                ()-> userServiceImplementation.createNewUser(this.user));
    }


    @Test
    void getUserInfo() throws Exception{
        //we have to create security context holder
        SecurityContext context = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);
        when(SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).thenReturn(this.user);
        assertEquals(this.user,userServiceImplementation.getUserInfo());
    }

    @Test
    void createAuthenticationTokenPass() throws Exception{
        AuthenticationRequest authenticationRequest=
                new AuthenticationRequest("sarahY","sarah123");
        when(userDetailsService.loadUserByUsername
                (authenticationRequest.getUsername())).thenReturn(this.user);
        String token= jwtTokenUtil.generateToken(this.user);
        when(jwtTokenUtil.generateToken(any())).thenReturn(token);
        Tokens tokens = new Tokens();
        tokens.setUser(this.user);
        tokens.setJwtToken(token);
        when(tokenRepository.save(tokens)).thenReturn(tokens);
        this.user.addToken(tokens);
        when(userRepository.save((this.user))).thenReturn(this.user);
        AuthenticationResponse authenticationResponse =new AuthenticationResponse(token);
        assertEquals(authenticationResponse.getJwt(),
                userServiceImplementation.createAuthenticationToken(authenticationRequest).getJwt());

    }
    @Test
    void createAuthenticationTokenFail()throws Exception {
        AuthenticationRequest authenticationRequest=
                new AuthenticationRequest("sarahY","sarah123");
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                (authenticationRequest.getUsername(), authenticationRequest.getPassword())))
                .thenThrow(BadCredentialsException.class);
        assertThrows(BadCredentialsException.class,()->userServiceImplementation.createAuthenticationToken(authenticationRequest));

    }
    @Test
    void editUser() throws Exception{
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.user.setPassword( "{bcrypt}" + encoder.encode(this.user.getPassword()));
        SecurityContext context = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);
        when(SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).thenReturn(this.user);
        when(userRepository.save(this.user)).thenReturn(this.user);
        assertEquals(this.user,userServiceImplementation.editUser(this.user));

    }


    @Test
    void deleteUser() throws IOException {
        SecurityContext context = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);
        when(SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).thenReturn(this.user);
        userServiceImplementation.deleteUser();
        verify(taskRepository,times(1)).deleteAllByUser_Id(this.user.getId());
        verify(tokenRepository,times(1)).deleteAllByUserId(this.user.getId());
        verify(userRepository,times(1)).deleteById(this.user.getId());
    }


    @Test
    void logOut() throws Exception{
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class); //parameter in original
        when(request.getHeader("Authorization"))
                .thenReturn("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYWFkMTIiLCJleHAiOjE2NDk3OTAxMTYsImlhdCI6MTY0OTc1NDExNn0.qfob_KPRXFRUQMSZdKesqVjqC8o-am__EGeapCc8TV8");
        final String authorizationHeader = request.getHeader("Authorization");
        String jwt = authorizationHeader.substring(7);
       userServiceImplementation.logOut(request);
        verify(tokenRepository,times(1)).deleteById(jwt);
    }

    @Test
    void logOutAll(){
        SecurityContext context = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);
        when(SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).thenReturn(this.user);
        userServiceImplementation.logOutAll();
        verify(tokenRepository,times(1)).deleteAllByUserId(this.user.getId());

    }


}

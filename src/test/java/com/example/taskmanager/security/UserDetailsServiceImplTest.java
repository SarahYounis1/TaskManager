package com.example.taskmanager.security;

import com.example.taskmanager.entity.User;
import com.example.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserDetailsServiceImpl userDetailsService;

    private final User user = new User(1L, "Sarah", "Sarahajam@gmail.com", "sarahY", "sarah123", 21);

    @Test
    void loadUserByUsernameSuccess() {
        when(userRepository.findByUsername(this.user.getUsername())).thenReturn(Optional.of(this.user));
        when(userRepository.findByUsername(this.user.getUsername())).thenReturn(Optional.of(this.user));
        assertEquals(userDetailsService.loadUserByUsername(user.getUsername()),this.user);
    }

    @Test
    void loadUserByUsernameFail() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,()->userDetailsService.loadUserByUsername(anyString()));
    }
}

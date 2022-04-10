package com.example.taskmanager.security;

import com.example.taskmanager.daoRepository.UserRepository;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@ComponentScan
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(!userRepository.findByUsername(username).isPresent()){

            throw new UsernameNotFoundException(username);
        }
        return  userRepository.findByUsername(username).get();
    }
}

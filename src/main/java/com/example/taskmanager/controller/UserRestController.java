package com.example.taskmanager.controller;

import com.example.taskmanager.entity.User;
import com.example.taskmanager.models.AuthenticationRequest;
import com.example.taskmanager.models.AuthenticationResponse;
import com.example.taskmanager.service.UserServiceImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
//@RequestMapping("/api")
public class UserRestController {

    private UserServiceImplementation userServiceImplementation;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);

    @Autowired
    public UserRestController(UserServiceImplementation userServiceImplementation) {
        this.userServiceImplementation = userServiceImplementation;
    }


    @GetMapping("/user")
    public  User returnUser()  {
        LOGGER.info("A get user request initialized ");
        return userServiceImplementation.getUserInfo();
    }

    //Adding Post Mapping to add new user
    @PostMapping("/register")
    public User createNewUser(@RequestBody User newUser)  {
        LOGGER.info("A create user request initialized ");
        LOGGER.trace("Creating new user ");
        newUser.setId(0L);
       return userServiceImplementation.createNewUser(newUser);
        //return newUser;
    }

    //add mapping for login authentication
    @PostMapping("/login")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
            throws BadCredentialsException {
        return  userServiceImplementation.createAuthenticationToken(authenticationRequest);
    }

    @PutMapping("/user")
    public User editUser(@RequestBody User editUser )  {
        LOGGER.info("A update user request initialized ");
        LOGGER.trace("updating user information " );
        return userServiceImplementation.editUser(editUser);
    }
    @DeleteMapping("/user")
    public void deleteUser() throws IOException {
        userServiceImplementation.deleteUser( );
    }

    @PostMapping("/user/logout")
    public String logOut(HttpServletRequest request){
        userServiceImplementation.logOut(request);
        return "You're logged out";
    }

    @PostMapping("/user/logoutAll")
    public String logOutAll(){
        userServiceImplementation.logOutAll();
        return "You're logged out from all devices";
    }
}

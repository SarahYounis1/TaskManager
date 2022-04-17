package com.example.taskmanager.controller;

import com.example.taskmanager.controller.UserRestController;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.models.AuthenticationResponse;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.security.JWTSecurity.JwtUtil;
import com.example.taskmanager.security.UserDetailsServiceImpl;
import com.example.taskmanager.service.UserServiceImplementation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.security.test.context.support.WithMockUser;

@WebMvcTest(UserRestController.class)
public class UserRestControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserRepository userRepository;

    @MockBean
    UserServiceImplementation userServiceImplementation;

    @MockBean
    UserDetailsServiceImpl userDetailsService;

    @MockBean
    AuthenticationManager authenticationManager;
    @MockBean
    JwtUtil jwtUtil;

    private User user = new User(1L,"Sarah","Sarahajam@gmail.com","sarahY","sarah123",21);

    @Test
   void createAuthenticationToken() throws Exception {
        AuthenticationResponse resp= new AuthenticationResponse();
        resp.setJwt(jwtUtil.generateToken(this.user));
        String JsonToken= objectMapper.writeValueAsString(resp);
         mockMvc.perform(MockMvcRequestBuilders
                .post("/login")
                .accept(MediaType.APPLICATION_JSON).content(JsonToken)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }

    @Test
    void createNewUser()throws Exception{
       String userJson = objectMapper.writeValueAsString(this.user);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/register")
                .accept(MediaType.APPLICATION_JSON).content(userJson)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }
    @WithMockUser
    @Test
    void returnUser() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                .get("/user")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void editUser() throws Exception {
        String userJson = objectMapper.writeValueAsString(this.user);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/user")
                .accept(MediaType.APPLICATION_JSON).content(userJson)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void deleteUser() throws Exception{
        mockMvc.perform( MockMvcRequestBuilders
                .delete("/user")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }

    @WithMockUser
    @Test
    void logOut()throws Exception{
        mockMvc.perform( MockMvcRequestBuilders
                .post("/user/logout")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void logOutAll()throws Exception{
        mockMvc.perform( MockMvcRequestBuilders
                .post("/user/logoutAll")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }


}

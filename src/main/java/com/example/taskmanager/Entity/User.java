package com.example.taskmanager.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name="user") //matches the table
public class User implements UserDetails {

    //define & Annotate  the fields

    @Id //Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // For Auto Increment
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;


    @Column(name = "password")
    private String password;

    @Column(name = "age")
    private int age;

    @Column(name ="username")
    private String username;

         //Generate The relationships
    @OneToMany(mappedBy="user", cascade= CascadeType.REMOVE ,fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Task> tasks;

    @OneToMany(mappedBy = "user", cascade= CascadeType.REMOVE)
    private List<Tokens> tokens = new ArrayList<>();

    //Generate Constructors

      //No-Argument Constructor

    public User() {
    }

    public User(String name, String email, String password, int age) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
    }


    //Generate Getter and setter for each attribute
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;//We removed the role authorities
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {

        this.tasks.add(task);
        System.out.println("added");
    }

//To string method



    @Override
    public String toString(){
        return "User [id =" + id +" ,Name =" +name +" ,age =" +
                age + " ,Email =" + email + " ,Password" + password +" ]";
    }

    public void addToken(Tokens tokens) {

        this.tokens.add(tokens);
        System.out.println("added");
    }
}

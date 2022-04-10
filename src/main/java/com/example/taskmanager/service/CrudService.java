package com.example.taskmanager.service;

import com.example.taskmanager.models.AuthenticationRequest;
import com.example.taskmanager.models.AuthenticationResponse;

import java.util.List;
import java.util.Optional;

public interface CrudService <T>{ //Service Layer
    public List<T> findAll();

    public T findById(Long theId);

    public void save(T theEmployee);

    public void deleteById(Long theId);

}

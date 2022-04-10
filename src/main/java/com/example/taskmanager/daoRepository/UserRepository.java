package com.example.taskmanager.daoRepository;

import com.example.taskmanager.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long> {

  Optional<User> findByUsername(String username);
}

package com.example.taskmanager.repository;

import com.example.taskmanager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TaskRepository extends JpaRepository<Task,Long> {
    void deleteAllByUser_Id(Long id);
    List<Task> findAllByUser_Id(Long id);
    //No code is needed CRUD functions already exist within JPARepository
}

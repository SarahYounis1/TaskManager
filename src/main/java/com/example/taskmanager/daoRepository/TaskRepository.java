package com.example.taskmanager.daoRepository;

import com.example.taskmanager.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface TaskRepository extends JpaRepository<Task,Long> {
    void deleteAllByUser_Id(Long id);
    List<Task> findAllByUser_Id(Long id);
    //No code is needed CRUD functions already exist within JPARepository
}

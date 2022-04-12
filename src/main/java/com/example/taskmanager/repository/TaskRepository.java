package com.example.taskmanager.repository;

import com.example.taskmanager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;


public interface TaskRepository extends JpaRepository<Task,Long> {
    void deleteAllByUser_Id(Long id);
    List<Task> findAllByUser_Id(Long id);
    //function to find tasks for specific user but not the completed ones
    @Query("select a from Task a where a. <= :creationDateTime")
    List<Task> findAllWithCreationDateTimeBefore(@Param("creationDateTime") Date creationDateTime);

}
    //No code is needed CRUD functions already exist within JPARepository


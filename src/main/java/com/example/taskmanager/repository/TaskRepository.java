package com.example.taskmanager.repository;

import com.example.taskmanager.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;


public interface TaskRepository extends JpaRepository<Task,Long> {
    void deleteAllByUser_Id(Long id);

    List<Task> findAllByUser_Id(Long id);

    Page<Task>findAllByUser_Id(Long id,Pageable pageable);

    List<Task> findAllByUser_IdAndEndDateIsAfterAndStartDateBefore
            (Long id,@Param("start") Date start,@Param("end") Date end);


    //No code is needed CRUD functions already exist within JPARepository

}
package com.example.taskmanager.service;

import com.example.taskmanager.Entity.Task;
import com.example.taskmanager.Entity.User;
import com.example.taskmanager.daoRepository.TaskRepository;
import com.example.taskmanager.daoRepository.UserRepository;
import com.example.taskmanager.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImplementation {

    private TaskRepository taskRepository;
    private UserRepository userRepository;

    @Autowired
    public TaskServiceImplementation(TaskRepository theTaskRepository ,UserRepository userRepository) {
        this.taskRepository = theTaskRepository;
        this.userRepository=userRepository;
    }


    public List<Task> getAllTasks() {
        User requestingUser= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return taskRepository.findAllByUser_Id(requestingUser.getId());
    }

    public Task getTask(Long id) throws AccessDeniedException {
        Task task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task Not Found" +id));
        User requestedUser = userRepository.findById(task.getUserId()).orElseThrow(()
                -> new NotFoundException("User not found"+task.getUserId()));
        User requestingUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (requestedUser.getId().longValue()==requestingUser.getId().longValue()
                && requestedUser.getPassword().equals(requestingUser.getPassword())) {
            return  task;}
        else {
            throw new AccessDeniedException("You are not allowed to access this page!");
        }

    }

    public Task createTask(Task task) {
        User requestingUser= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Task newTask = taskRepository.save(task);
        task.setUser(requestingUser);
        //notice that we have to add the task to the user object and add the user object to the task so that jpa can create load the table correctly for us
        requestingUser.addTask(task);
        userRepository.save(requestingUser);
        return newTask;
    }

    public Task editTask(Task editTask, Long id)  throws AccessDeniedException {
        Task task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("task not found"+id));
        User requestedUser=userRepository.findById(task.getUserId()).orElseThrow(()
                -> new NotFoundException("User not found" + task.getUserId()));
        User requestingUser= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (requestedUser.getId().longValue()==requestingUser.getId().longValue() && requestedUser.getPassword().equals(requestingUser.getPassword())) {
            task.setDescription(editTask.getDescription());
            task.setCompleted(editTask.isCompleted());
            taskRepository.save(task);
            return task;
        }
        else {
            throw new AccessDeniedException("You are not allowed to access this page!");
        }
    }

    public void deleteTask(Long id)  throws AccessDeniedException{
        Task task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not Found" +id));
        User requestedUser=userRepository.findById(task.getUserId()).orElseThrow(()
                -> new NotFoundException("User not found "+task.getUserId()));
        User requestingUser= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (requestedUser.getId().longValue()==requestingUser.getId().longValue() && requestedUser.getPassword().equals(requestingUser.getPassword())) {
            if (taskRepository.existsById(id)) {
                taskRepository.deleteById(id);
            }
        }else {
            throw new AccessDeniedException("You are not allowed to access this page!");
        }

    }
}

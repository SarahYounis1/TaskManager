package com.example.taskmanager.service;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.exception.NotAllowedDateException;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Date;
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

    public Page<Task> getAllTasks(Optional<Integer> page, Optional <String> sortDirection , Optional<String> sortBy ){
        User requestingUser= (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return taskRepository.findAllByUser_Id(requestingUser.getId(), PageRequest.of(page.orElse(0), 5,
                Sort.Direction.fromString(sortDirection.orElse("asc")),sortBy.orElse("id")));
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
        checkTimeValidation(task,false);
        task.setUser(requestingUser);
        taskRepository.save(task);
        //notice that we have to add the task to the user object and add the user object to the task so that jpa can create load the table correctly for us
        requestingUser.addTask(task);
        userRepository.save(requestingUser);

        return task;
    }

    public Task editTask(Task editTask, Long id)  throws AccessDeniedException {
        Task task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("task not found"+id));
        User requestedUser=userRepository.findById(task.getUserId()).orElseThrow(()
                -> new NotFoundException("User not found" + task.getUserId()));
        User requestingUser= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (requestedUser.getId().longValue()==requestingUser.getId().longValue()
                && requestedUser.getPassword().equals(requestingUser.getPassword())) {
            checkTimeValidation(editTask,true);
            task.setDescription(editTask.getDescription());
            task.setCompleted(editTask.isCompleted());
            task.setUser(requestingUser);
            task.setStartDate(editTask.getStartDate());
            task.setEndDate(editTask.getEndDate());
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

    public void checkTimeValidation(Task task , boolean edit){
        User requestingUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //get user from token
//        List<Task> tasks = taskRepository.findAllByUser_Id(requestingUser.getId());//get all user Tasks
        Date startDate = task.getStartDate();
        Date endDate = task.getEndDate();
        List<Task> tasks = taskRepository.findAllByUser_IdAndEndDateIsAfterAndStartDateBefore(
                requestingUser.getId(),startDate,endDate);
        int count= tasks.size();

        if(count>1)throw new NotAllowedDateException("invalid Date");
        else if(count==1)
            if(edit)
            if(tasks.get(0).getId() != task.getId())throw new NotAllowedDateException("invalid time");
            else throw new NotAllowedDateException("invalid Date");

    }
}

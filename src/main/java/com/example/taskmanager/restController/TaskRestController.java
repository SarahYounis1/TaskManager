package com.example.taskmanager.restController;

import com.example.taskmanager.Entity.Task;
import com.example.taskmanager.Entity.User;
import com.example.taskmanager.exception.NotFoundException;
import com.example.taskmanager.service.TaskServiceImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api")

public class TaskRestController {

    private TaskServiceImplementation taskServiceImplementation;
   // private UserServiceImplementation userServiceImplementation;
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskRestController.class);

    @Autowired
    public TaskRestController(TaskServiceImplementation taskServiceImplementation) {
        this.taskServiceImplementation =taskServiceImplementation;
    }

    @GetMapping("/tasks")
    public List<Task> returnAllTasks() {
        LOGGER.info("A get all tasks  request initialized ");
        LOGGER.trace("retrieve all tasks ");
        return taskServiceImplementation.getAllTasks();
    }
    @GetMapping("/tasks/{id}")
    public Task returnTask(@PathVariable Long id) throws  AccessDeniedException {
        LOGGER.info("A get task request initialized ");
        LOGGER.trace("retrieve task with id "+ id );
        return  taskServiceImplementation.getTask(id);
    }
    @PostMapping("/tasks")
    public Task createTask(@RequestBody Task task) {
        LOGGER.info("A create task request initialized ");
        LOGGER.trace("Creating new  task");
        return taskServiceImplementation.createTask(task);
    }
    @PutMapping("/tasks/{id}")
    public Task edTask(@RequestBody Task editTask, @PathVariable Long id) throws AccessDeniedException {
        LOGGER.info("A Update task request initialized ");
        LOGGER.trace("Updating a task to a user with id : " + id );
        return taskServiceImplementation.editTask(editTask,id);


    }
    @DeleteMapping("/tasks/{id}")
    public String deleteTask(@PathVariable Long id) throws IOException {
        LOGGER.info("A delete task  request initialized ");
        LOGGER.trace("Redirecting to the Tasks page after deleting task with id : " + id);

        taskServiceImplementation.deleteTask(id);


      return "Deleted task no." +id;

    }





}

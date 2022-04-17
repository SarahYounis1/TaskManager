package com.example.taskmanager.service;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.exception.NotAllowedDateException;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplementationTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private TaskServiceImplementation taskServiceImplementation;

    private final User user = new User(1L, "Sarah", "Sarahajam@gmail.com", "sarahY", "sarah123", 21);
    private final User userF = new User(2L, "Sarah2", "Sarahajam2@gmail.com", "sarahY2", "sarah1232", 23);
    private final Task task = new Task("Testing the application", false, new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 10), new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2));
    private final Task task1 = new Task("description", false, new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 10), new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2));

    @Test
    void getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Task task = new Task("description", false, null, null);
            tasks.add(task); }
        Page<Task> tasksPage = new PageImpl<>(tasks, PageRequest.of(0, 3), tasks.size());
        SecurityContext context = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);
        when(SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).thenReturn(this.user);
        Optional<Integer> page = Optional.of(1);
        Optional<String> sortBy = Optional.of("id");
        Optional<String> sortDirection = Optional.of("asc");
        when(taskRepository.findAllByUser_Id(this.user.getId(),
                PageRequest.of(page.orElse(0), 5,
                        Sort.Direction.fromString(sortDirection.orElse("asc")),
                        sortBy.orElse("id")))).thenReturn(tasksPage);
        assertEquals(tasksPage, taskServiceImplementation.getAllTasks(page, sortDirection, sortBy));
    }

    @Test
    void getTaskPass() throws AccessDeniedException {
        this.task.setUser(this.user);
        this.user.addTask(this.task);
        when(taskRepository.findById(this.task.getId())).thenReturn(Optional.of(this.task));
        when(userRepository.findById(this.task.getUserId())).thenReturn(Optional.of(this.user));
        SecurityContext context = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);
        when(SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).thenReturn(this.user);
        assertEquals(this.task, taskServiceImplementation.getTask(this.task.getId()));
    }

    @Test
    void getTaskFailAccess() {
        this.task.setUser(this.user);
        this.user.addTask(this.task);
        when(taskRepository.findById(this.task.getId())).thenReturn(Optional.of(this.task));
        when(userRepository.findById(this.task.getUserId())).thenReturn(Optional.of(this.userF));//return another user logged in
        SecurityContext context = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);
        when(SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).thenReturn(this.user);
        assertThrows(AccessDeniedException.class, () -> taskServiceImplementation.getTask(this.task.getId()));
    }


    @Test
    void createTaskPass(){
        this.task.setUser(this.user);
        this.user.addTask(this.task);
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);
        task.setUser(user);
        when(taskRepository.save(task)).thenReturn(task);
        assertEquals(task, taskServiceImplementation.createTask(task));

    }

    @Test
    void createTaskFail() throws NotAllowedDateException{
        this.task.setUser(this.user);
        this.user.addTask(this.task);
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);
        List<Task> tasks = new ArrayList<>();
       tasks.add(this.task1);
      when(taskRepository.findAllByUser_IdAndEndDateIsAfterAndStartDateBefore
              (this.user.getId(),this.task.getStartDate(),this.task.getEndDate())).thenReturn(tasks);
      assertThrows(NotAllowedDateException.class, ()-> taskServiceImplementation.createTask(this.task));
    }

    @Test
    void editTaskPass() throws AccessDeniedException{
        this.task.setUser(this.user);
        this.user.addTask(task);
        when(taskRepository.findById(this.task.getId())).thenReturn(Optional.of(this.task));
        when(userRepository.findById(this.task.getUserId())).thenReturn(Optional.of(this.user));
        SecurityContext context = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);
        when(SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).thenReturn(this.user);
        when(taskRepository.save(this.task)).thenReturn(this.task);
        assertEquals(this.task,taskServiceImplementation.editTask(task,this.task.getId()));
    }
    @Test
    void editTaskFail() throws AccessDeniedException{
        this.task.setUser(this.user);
        user.addTask(task);
        when(taskRepository.findById(this.task.getId())).thenReturn(Optional.of(this.task));
        when(userRepository.findById(this.task.getUserId())).thenReturn(Optional.of(this.user));
        SecurityContext context = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);
        when(SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).thenReturn(this.userF);
        assertThrows(AccessDeniedException.class,()->taskServiceImplementation.editTask(task,this.task.getId()));
    }
    @Test
    void editTaskFailDate() throws NotAllowedDateException{
        this.task.setUser(this.user);
        this.user.addTask(this.task);
        this.task.setId(1L);
        when(taskRepository.findById(this.task.getId())).thenReturn(Optional.of(this.task));
        when(userRepository.findById(this.task.getUserId())).thenReturn(Optional.of(this.user));
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(this.user);
        List<Task> tasks = new ArrayList<>();
        tasks.add(this.task1);
        when(taskRepository.findAllByUser_IdAndEndDateIsAfterAndStartDateBefore
                (this.user.getId(),this.task.getStartDate(),this.task.getEndDate())).thenReturn(tasks);
        assertThrows(NotAllowedDateException.class, ()-> taskServiceImplementation.editTask(this.task,this.task.getId()));
    }

    @Test
    void  deleteTaskPass()throws AccessDeniedException{
        this.task.setUser(this.user);
        user.addTask(task);
        when(taskRepository.findById(this.task.getId())).thenReturn(Optional.of(this.task));
        when(userRepository.findById(this.task.getUserId())).thenReturn(Optional.of(this.user));
        SecurityContext context = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);
        when(SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).thenReturn(this.user);
        when(taskRepository.existsById(this.task.getId())).thenReturn(true);
        taskServiceImplementation.deleteTask(this.task.getId());
        verify(taskRepository,times(1)).deleteById(task.getId());
    }
    @Test
    void  deleteTaskFail()throws AccessDeniedException{
        this.task.setUser(this.user);
        this.user.addTask(task);
        this.task.setId(1L);
        when(taskRepository.findById(this.task.getId())).thenReturn(Optional.of(this.task));
        when(userRepository.findById(this.task.getUserId())).thenReturn(Optional.of(this.user));
        SecurityContext context = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);
        when(SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).thenReturn(this.userF);
        assertThrows(AccessDeniedException.class,()->taskServiceImplementation.deleteTask(this.task.getId()));
    }
}
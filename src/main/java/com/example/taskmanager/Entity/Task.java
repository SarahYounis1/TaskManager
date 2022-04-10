package com.example.taskmanager.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name ="task")
public class Task {

    //define the fields and annotations
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="description")
    private String description;

    @Column(name="completed")
    private boolean completed;

    //Generate Relationships

    @ManyToOne() // Not Cascade.all because we don't want to remove the user when deleting the task
    @JoinColumn(name="user_id")  //Foreign Key
    @JsonBackReference
    private User user;


    //Generate Constructors

    public Task() {

    }

    public Task(String description , boolean completed ) {

        this.description = description;
        this.completed =completed;

    }

    //Generate Setter and Getter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //ToString
    @Override
    public String toString() {
        return "Course [id=" + id +
                " ,title=" + description + " ,Completed"+ completed +"]";
    }


    public Long getUserId() {
        return user.getId();
    }
}

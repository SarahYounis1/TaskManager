package com.example.taskmanager.exception;

public class NotAllowedDateException extends  RuntimeException{

    public NotAllowedDateException(String message){
        super(message);
    }
}

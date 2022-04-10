package com.example.taskmanager.exception;

public class UserAlreadyExistException extends RuntimeException  {

      public UserAlreadyExistException(){
      super("This username is already registered please try with a different username!");
}

}

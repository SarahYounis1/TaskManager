package com.example.taskmanager.exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class NotFoundRestExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotFoundRestExceptionHandler.class);
    private ErrorResponse errorResponse = new ErrorResponse();
    //For Not Found Objects in DataBase
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(NotFoundException exc){

        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage(exc.getMessage());
        errorResponse.setTimeStamp(System.currentTimeMillis());

        LOGGER.error("An error occurred while looking for the object", exc);
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }


    //add another generic exception handler for other exceptions that may happen (Unspecified ones) error code = 500
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception exc){

        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setMessage(exc.getMessage());
        errorResponse.setTimeStamp(System.currentTimeMillis());

        LOGGER.error("An error occurred ", exc);
        return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(UserAlreadyExistException exc){
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage(exc.getMessage());
        errorResponse.setTimeStamp(System.currentTimeMillis());
        LOGGER.error("An error occurred while looking for the object", exc);
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

}

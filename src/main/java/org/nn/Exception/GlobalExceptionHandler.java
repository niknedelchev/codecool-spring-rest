package org.nn.Exception;

import org.nn.response.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Message> handleException(Exception ex) {
        return new ResponseEntity<Message>(new Message(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

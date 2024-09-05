package ar.edu.utn.frbb.tup.presentation.controller.handler;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.BadRequestException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class APIExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { BadRequestException.class })
    protected ResponseEntity<Object> handleBadRequest(Exception ex) {
        return handleExceptionInternal(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<Object> handleMateriaNotFound(Exception ex) {
        return handleExceptionInternal(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = { ConflictException.class})
    protected ResponseEntity<Object> handleConflict(Exception ex) {
        return handleExceptionInternal(ex, HttpStatus.CONFLICT);
    }

    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, HttpStatus status) {
        CustomApiError error = new CustomApiError();
        error.setErrorMessage(ex.getMessage());
        error.setErrorCode(status.value());
        return new ResponseEntity<>(error, new HttpHeaders(), status);
    }
}
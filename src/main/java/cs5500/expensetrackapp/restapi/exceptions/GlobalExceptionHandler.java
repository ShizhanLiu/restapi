package cs5500.expensetrackapp.restapi.exceptions;

import cs5500.expensetrackapp.restapi.io.ErrorObject;
import cs5500.expensetrackapp.restapi.exceptions.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exceptional handler for all the exceptions
 * @author Bushan SC
 *
 * */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(ResourceNotFoundException.class)
  public ErrorObject handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
    log.error("Throwing the ResourceNotFoundException from GlobalExceptionHandler {}", ex.getMessage());
    return ErrorObject.builder()
        .errorCode("DATA_NOT_FOUND")
        .statusCode(HttpStatus.NOT_FOUND.value())
        .message(ex.getMessage())
        .timestamp(new Date())
        .build();
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    Map<String, Object> errorResponse = new HashMap<>();
    List<String> errors = ex.getBindingResult().getFieldErrors()
        .stream().map(field -> field.getDefaultMessage())
        .collect(Collectors.toList());
    errorResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
    errorResponse.put("message", errors);
    errorResponse.put("timestamp", new Date());
    errorResponse.put("errorCode", "VALIDATION_FAILED");
    return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
  }
}
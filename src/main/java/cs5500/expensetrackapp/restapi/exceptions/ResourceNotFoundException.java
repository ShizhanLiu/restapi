package cs5500.expensetrackapp.restapi.exceptions;


public class ResourceNotFoundException extends RuntimeException{

  public ResourceNotFoundException(String message) {
    super(message);
  }
}
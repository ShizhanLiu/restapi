package cs5500.expensetrackapp.restapi.exceptions;

public class ItemExistsException extends RuntimeException{
    public ItemExistsException(String message) {
        super(message);
    }
}

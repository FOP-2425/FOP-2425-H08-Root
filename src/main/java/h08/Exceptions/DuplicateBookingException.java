package h08.Exceptions;

public class DuplicateBookingException extends InvalidBookingException{
    public DuplicateBookingException(String message) {
        super(message);
    }
}

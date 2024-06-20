package h08;

public class DuplicateBookingException extends InvalidBookingException{
    public DuplicateBookingException(String message) {
        super(message);
    }
}

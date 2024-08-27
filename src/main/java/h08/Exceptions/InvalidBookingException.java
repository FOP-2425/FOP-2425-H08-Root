package h08.Exceptions;

public class InvalidBookingException extends BookingManagementException{
    public InvalidBookingException(String message) {
        super(message);
    }
}

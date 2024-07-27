package h08.Exceptions;

public class BookingNotFoundException extends BookingManagementException {
    public BookingNotFoundException(String message) {
        super(message);
    }
}

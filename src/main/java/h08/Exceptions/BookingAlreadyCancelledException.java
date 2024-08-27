package h08.Exceptions;

public class BookingAlreadyCancelledException extends BookingNotFoundException {
    public BookingAlreadyCancelledException(String message) {
        super(message);
    }
}

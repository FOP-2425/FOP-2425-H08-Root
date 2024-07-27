package h08;

public class BookingAlreadyCancelledException extends BookingNotFoundException {
    public BookingAlreadyCancelledException(String message) {
        super(message);
    }
}

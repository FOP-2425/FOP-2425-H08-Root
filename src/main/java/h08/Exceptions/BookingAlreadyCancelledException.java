package h08.Exceptions;

public class BookingAlreadyCancelledException extends FlightNotFoundException {
    public BookingAlreadyCancelledException(String message) {
        super(message);
    }
}

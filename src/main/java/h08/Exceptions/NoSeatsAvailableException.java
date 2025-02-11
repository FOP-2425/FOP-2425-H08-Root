package h08.Exceptions;

public class NoSeatsAvailableException extends Exception {
    public NoSeatsAvailableException(String flightNumber) {
        super("No seats available for flight: " + flightNumber);
    }
}

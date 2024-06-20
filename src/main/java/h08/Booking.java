package h08;
public class Booking {
    private String bookingId;
    private String flightNumber;
    private String passengerId;
    private boolean isCancelled;

    // Constructor to initialize the booking attributes
    public Booking(String bookingId, String flightNumber, String passengerId) {
        this.bookingId = bookingId;
        this.flightNumber = flightNumber;
        this.passengerId = passengerId;
        this.isCancelled = false;
    }

    // Getter for booking ID
    public String getBookingId() {
        return bookingId;
    }

    // Getter for flight number
    public String getFlightNumber() {
        return flightNumber;
    }

    // Getter for passenger ID
    public String getPassengerId() {
        return passengerId;
    }

    // Getter for cancellation status
    public boolean isCancelled() {
        return isCancelled;
    }

    // Method to cancel the booking
    public void cancelBooking() throws BookingAlreadyCancelledException {
        if (isCancelled) {
            throw new BookingAlreadyCancelledException("Booking is already cancelled: " + bookingId);
        }
        this.isCancelled = true;
    }

    // Method to view booking details
    public String viewBooking() {
        return String.format("Booking ID: %s, Flight Number: %s, Passenger ID: %s, Is Cancelled: %b",
            bookingId, flightNumber, passengerId, isCancelled);
    }
}

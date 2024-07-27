package h08;

import h08.Exceptions.BookingAlreadyCancelledException;

/**
 * Represents a flight booking. A booking allows the reservation of a flight as long as managing its identification and its relevant information.
 */
public class Booking {

    /**
     * The booking ID of a booking.
     */
    private String bookingId;

    /**
     * The flight number of a booking.
     */
    private String flightNumber;

    /**
     * The passenger ID of a booking.
     */
    private String passengerId;

    /**
     * The cancellations status of a booking.
     */
    private boolean isCancelled;

    /**
     * Constructs a new booking with the specified booking ID, flight number and passenger ID.
     *
     * @param bookingId    the booking ID of the booking
     * @param flightNumber the flight number of the booking
     * @param passengerId  the passenger ID of the booking
     */
    public Booking(String bookingId, String flightNumber, String passengerId) {
        this.bookingId = bookingId;
        this.flightNumber = flightNumber;
        this.passengerId = passengerId;
        this.isCancelled = false;
    }

    /**
     * Returns the booking ID of the booking.
     *
     * @return the booking ID of the booking
     */
    public String getBookingId() {
        return bookingId;
    }

    /**
     * Returns the flight number of the booking.
     *
     * @return the flight number of the booking
     */
    public String getFlightNumber() {
        return flightNumber;
    }

    /**
     * Returns the passenger ID of the booking.
     *
     * @return the passenger ID of the booking
     */
    public String getPassengerId() {
        return passengerId;
    }

    /**
     * Returns the cancellation status of the booking.
     *
     * @return the cancellation status of the booking
     */
    public boolean isCancelled() {
        return isCancelled;
    }

    /**
     * Cancels the booking.
     *
     * @throws BookingAlreadyCancelledException if the booking is already cancelled
     */
    public void cancelBooking() throws BookingAlreadyCancelledException {
        if (isCancelled) {
            throw new BookingAlreadyCancelledException("Booking is already cancelled: " + bookingId);
        }
        this.isCancelled = true;
    }

    /**
     * Returns the booking details.
     *
     * @return the booking details
     */
    public String viewBooking() {
        return String.format("Booking ID: %s, Flight Number: %s, Passenger ID: %s, Is Cancelled: %b",
            bookingId, flightNumber, passengerId, isCancelled);
    }
}

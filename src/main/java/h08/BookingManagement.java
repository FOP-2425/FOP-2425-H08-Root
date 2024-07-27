package h08;

import java.util.Arrays;

/**
 * Represents a booking management. A booking management oversees booking operations, ensuring validity and handling duplicates.
 */
public class BookingManagement {

    /**
     * The bookings to be managed.
     */
    private Booking[] bookings;

    /**
     * The current number of bookings.
     */
    private int size = 100;

    /**
     * The flight management for the bookings.
     */
    private FlightManagement flightManagement;

    // Constructor to initialize the booking management system with a capacity
    /**
     * Constructs a new booking management with the specified initial capacity and flight management.
     *
     * @param initialCapacity  the initial number of bookings that can be managed
     * @param flightManagement the flight management for the bookings
     */
    public BookingManagement(int initialCapacity, FlightManagement flightManagement) {
        this.bookings = new Booking[initialCapacity];
        this.size = 0;
        this.flightManagement = flightManagement;
    }

    /**
     * Creates a booking.
     *
     * @param bookingId    the booking ID of the booking
     * @param flightNumber the flight number of the booking
     * @param passengerId  the passenger ID of the booking
     */
    public void createBooking(String bookingId, String flightNumber, String passengerId) {
        try {
            // Validate booking details
            validateBookingDetails(bookingId, flightNumber, passengerId);
            // Check for duplicate booking
            checkForDuplicateBooking(bookingId);
            // Resize array if necessary
            if (size >= bookings.length) {
                bookings = Arrays.copyOf(bookings, bookings.length * 2);
            }
            // Add the new booking
            bookings[size++] = new Booking(bookingId, flightNumber, passengerId);
            // use the method of the flight to book a seat
            flightManagement.getFlight(flightNumber).bookSeat();
        } catch (DuplicateBookingException e) {
            System.out.println("Booking already exists: " + e.getMessage());
        } catch (InvalidBookingException e) {
            System.out.println("Invalid booking details: " + e.getMessage());
        } catch (NoSeatsAvailableException e) {
            System.out.println("No seats available for flight: " + e.getMessage());
        }
    }

    /**
     * Validates the booking details.
     *
     * @param bookingId    the booking ID of the booking
     * @param flightNumber the flight number of the booking
     * @param passengerId  the passenger ID of the booking
     * @throws InvalidBookingException if the booking details are invalid
     */
    private void validateBookingDetails(String bookingId, String flightNumber, String passengerId) throws InvalidBookingException {
        if (bookingId == null || bookingId.isEmpty() || flightNumber == null || flightNumber.isEmpty() || passengerId == null || passengerId.isEmpty()) {
            throw new InvalidBookingException("Invalid booking details");
        }
    }

    /**
     * Checks for duplicate booking.
     *
     * @param bookingId the booking ID of the booking
     * @throws DuplicateBookingException if the booking ID is duplicate
     */
    private void checkForDuplicateBooking(String bookingId) throws DuplicateBookingException {
        for (Booking booking : bookings) {
            if (booking!=null && booking.getBookingId().equals(bookingId)) {
                throw new DuplicateBookingException("Duplicate booking ID: " + bookingId);
            }
        }
    }

    /**
     * Searches for a booking by booking ID.
     *
     * @param bookingId the booking ID of the booking
     * @return the booking with the specified booking ID
     * @throws BookingNotFoundException if the booking ist not found
     */
    private Booking searchBooking(String bookingId) throws BookingNotFoundException {
        for (Booking booking : bookings) {
            // Check if booking exists and return it
            if (booking!=null && booking.getBookingId().equals(bookingId)) {
                return booking;
            }
        }
        throw new BookingNotFoundException("Booking not found: " + bookingId);
    }

    /**
     * Returns a booking by booking ID.
     *
     * @param bookingId the booking ID of the booking
     * @return the booking with the specified booking ID
     */
    public Booking getBooking(String bookingId) {
        try {
            return searchBooking(bookingId);
        } catch (BookingNotFoundException e) {
            System.out.println("Error retrieving booking: " + e.getMessage());
            return null;
        }
    }

    /**
     * Cancels a booking by booking ID.
     *
     * @param bookingId the booking ID of the booking
     */
    public void cancelBooking(String bookingId) {
        try {
            Booking booking = searchBooking(bookingId);
            booking.cancelBooking();
            System.out.println("Booking cancelled successfully: " + bookingId);
        } catch (BookingAlreadyCancelledException e) {
            System.out.println("Already cancelled booking: " + e.getMessage());
        } catch (BookingNotFoundException e) {
            System.out.println("Error cancelling booking: " + e.getMessage());
        }
    }
}

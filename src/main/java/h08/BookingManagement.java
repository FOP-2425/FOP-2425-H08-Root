package h08;

import java.util.Arrays;

public class BookingManagement {
    private Booking[] bookings;
    private int size = 100;

    // Constructor to initialize the booking management system with a capacity
    public BookingManagement(int initialCapacity) {
        this.bookings = new Booking[initialCapacity];
    }

    // Method to create a booking
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
        } catch (DuplicateBookingException e) {
            System.out.println("Booking already exists: " + e.getMessage());
        } catch (InvalidBookingException e) {
            System.out.println("Invalid booking details: " + e.getMessage());
        }
    }

    // Method to validate booking details
    private void validateBookingDetails(String bookingId, String flightNumber, String passengerId) throws InvalidBookingException {
        if (bookingId == null || bookingId.isEmpty() || flightNumber == null || flightNumber.isEmpty() || passengerId == null || passengerId.isEmpty()) {
            throw new InvalidBookingException("Invalid booking details");
        }
    }

    //Method to check for duplicate booking
    private void checkForDuplicateBooking(String bookingId) throws DuplicateBookingException {
        for (int i = 0; i < size; i++) {
            if (bookings[i].getBookingId().equals(bookingId)) {
                throw new DuplicateBookingException("Duplicate booking ID: " + bookingId);
            }
        }
    }

    // Method to search for a booking by booking ID
    private Booking searchBooking(String bookingId) throws BookingNotFoundException {
        for (int i = 0; i < size; i++) {
            if (bookings[i].getBookingId().equals(bookingId)) {
                return bookings[i];
            }
        }
        throw new BookingNotFoundException("Booking not found: " + bookingId);
    }

    // Method to get a booking by booking ID
    public Booking getBooking(String bookingId) {
        try {
            return searchBooking(bookingId);
        } catch (BookingNotFoundException e) {
            System.out.println("Error retrieving booking: " + e.getMessage());
            return null;
        }
    }

    // Method to cancel a booking by booking ID
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


package h08;

import h08.Exceptions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * Main entry point in executing the program.
 */
public class Main {
    /**
     * Main entry point in executing the program.
     *
     * @param args program arguments, currently ignored
     */
    public static void main(String[] args) {
        // Create a new Airport
        Airport airport1 = new Airport("JFK", 2);
        Airport airport2 = new Airport("LAX", 2);

        // Create two flights with limited seats
        Flight flight1 = new Flight("F1001", "JFK", "LAX", LocalDateTime.of(2024, 7, 20, 10, 0), 2);
        Flight flight2 = new Flight("F1002", "JFK", "ORD", LocalDateTime.of(2024, 7, 20, 14, 0), 2);
        Flight flight3 = new Flight("F1003", "LAX", "ORD", LocalDateTime.of(2024, 7, 20, 14, 0), 2);
        Flight flight4 = new Flight("F1004", "LAX", "JFK", LocalDateTime.of(2024, 7, 20, 14, 0), 2);
        // Create a FlightManagement instance
        FlightManagement flightManagement = new FlightManagement(2);
        // Add airports to the flight management system
        flightManagement.addAirport(airport1);
        flightManagement.addAirport(airport2);

        // Add flights to the airport
        airport1.addFlight(flight1,true);
        airport1.addFlight(flight2,true);
        airport2.addFlight(flight3,true);
        airport2.addFlight(flight4,true);

        // Create a BookingManagement instance
        BookingManagement bookingManagement = new BookingManagement(2, flightManagement);

        // Create passengers
        Passenger passenger1 = new Passenger("Max", "Mustermann", LocalDate.of(2002,6,6));
        System.out.println("Passenger1 ID: "+passenger1.getPassengerID());
        Passenger passenger2 = new Passenger("Erika", "Mustermann ", LocalDate.of(2003,7,9));
        System.out.println("Passenger2 ID: "+passenger2.getPassengerID());
        // Create bookings
        bookingManagement.createBooking("B001", "F1001", passenger1.getPassengerID());
        bookingManagement.createBooking("B002", "F1001", passenger2.getPassengerID());

        // Display bookings
        System.out.println(bookingManagement.getBooking("B001").viewBooking());
        System.out.println(bookingManagement.getBooking("B002").viewBooking());

        // Book seats on the flight
        try {
            flight1.bookSeat();
            flight1.bookSeat();
            flight1.bookSeat();  // This should throw NoSeatsAvailableException
        } catch (NoSeatsAvailableException e) {
            System.out.println(e.getMessage());
        }

        // Cancel a booking
            bookingManagement.cancelBooking("B001");
            bookingManagement.cancelBooking("B001");  // This should throw BookingAlreadyCancelledException
            bookingManagement.cancelBooking("B005");  // This should throw BookingNotFoundException

        // Try to get a non-existing booking
        try {
            Booking nonExistentBooking = bookingManagement.getBooking("B999");  // This should throw BookingNotFoundException
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Remove departing flight
        try {
            airport1.removeFlight("F1001",true);
            airport1.removeFlight("F1001",true);  // This should throw FlightNotFoundException
        } catch (FlightNotFoundException e) {
            System.out.println(e.getMessage());
        }

        // Display remaining departing flights
        Flight[] remainingDepartingFlights = airport1.getAllDepartingFlights();
        for (Flight flight : remainingDepartingFlights) {
            System.out.println(flight);
        }
        System.out.println(flightManagement.getFlight("F9875"));

    }
}

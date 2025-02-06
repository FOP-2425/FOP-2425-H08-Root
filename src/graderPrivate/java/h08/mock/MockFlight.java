package h08.mock;

import h08.Flight;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A mock class for testing the Flight class.
 */
public class MockFlight extends Flight {

    /**
     * Constructs a new flight with the specified flight number, departure airport, destination airport, departure time
     * and initial number of seats.
     *
     * @param flightNumber  the flight number of the flight
     * @param departure     the departure airport of the flight
     * @param destination   the destination airport of the flight
     * @param departureTime the departure time of the flight
     * @param initialSeats  the initial number of seats of the flight
     */
    public MockFlight(String flightNumber, String departure, String destination, LocalDateTime departureTime, int initialSeats) {
        super(flightNumber, departure, destination, departureTime, initialSeats);
    }

    /**
     * Returns {@code true} if the specified flight is equal to this flight, {@code false} otherwise.
     *
     * @param flight the flight to compare
     * @param other  the mock flight to compare
     *
     * @return {@code true} if the specified flight is equal to this flight, {@code false} otherwise
     */
    public static boolean equals(Flight flight, Object other) {
        if (flight == other) {
            return true;
        }
        if (!(other instanceof MockFlight that)) {
            return false;
        }
        return Objects.equals(flight.getFlightNumber(), that.getFlightNumber())
            && Objects.equals(flight.getDeparture(), that.getDeparture())
            && Objects.equals(flight.getDestination(), that.getDestination())
            && Objects.equals(flight.getDepartureTime(), that.getDepartureTime())
            && flight.getAvailableSeats() == that.getAvailableSeats();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Flight other)) {
            return false;
        }
        return Objects.equals(getFlightNumber(), other.getFlightNumber())
            && Objects.equals(getDeparture(), other.getDeparture())
            && Objects.equals(getDestination(), other.getDestination())
            && Objects.equals(getDepartureTime(), other.getDepartureTime())
            && getAvailableSeats() == other.getAvailableSeats();
    }
}

package h08.assertions;

import h08.Flight;
import org.mockito.Mockito;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.time.LocalDateTime;

/**
 * Provides utility methods for creating mock objects.
 *
 * @author Nhan Huynh
 */
public final class Mocks {

    /**
     * Prevents instantiation of this utility class.
     */
    private Mocks() {
    }

    /**
     * Creates a mock flight object with the given parameters.
     *
     * @param flightNumber   the flight number of the flight
     * @param departure      the departure location of the flight
     * @param destination    the destination location of the flight
     * @param departureTime  the departure time of the flight
     * @param initialSeats   the initial number of seats of the flight
     * @param availableSeats the available number of seats of the flight
     *
     * @return a mock flight object
     */
    public static Flight createFlight(
        String flightNumber,
        String departure,
        String destination,
        LocalDateTime departureTime,
        int initialSeats,
        int availableSeats
    ) {
        Flight flight = Mockito.mock(Flight.class, Mockito.CALLS_REAL_METHODS);
        TypeLink typeLink = Links.getType(Flight.class);
        FieldLink flightNumberLink = typeLink.getField(Matcher.of(field -> field.name().equals("flightNumber")));
        flightNumberLink.set(flight, flightNumber);
        FieldLink departureLink = typeLink.getField(Matcher.of(field -> field.name().equals("departure")));
        departureLink.set(flight, departure);
        FieldLink destinationLink = typeLink.getField(Matcher.of(field -> field.name().equals("destination")));
        destinationLink.set(flight, destination);
        FieldLink departureTimeLink = typeLink.getField(Matcher.of(field -> field.name().equals("departureTime")));
        departureTimeLink.set(flight, departureTime);
        FieldLink initialSeatsLink = typeLink.getField(Matcher.of(field -> field.name().equals("initialSeats")));
        initialSeatsLink.set(flight, initialSeats);
        FieldLink availableSeatsLink = typeLink.getField(Matcher.of(field -> field.name().equals("availableSeats")));
        availableSeatsLink.set(flight, availableSeats);
        Mockito.when(flight.toString()).thenReturn(
            "Flight{"
                + "flightNumber='" + flightNumber + '\''
                + ", departure='" + departure + '\''
                + ", destination='" + destination + '\''
                + ", departureTime=" + departureTime
                + ", initialSeats=" + initialSeats
                + ", availableSeats=" + availableSeats
                + '}'
        );
        return flight;
    }
}

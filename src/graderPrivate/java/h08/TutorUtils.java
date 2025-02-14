package h08;

import h08.assertions.Links;
import org.jetbrains.annotations.Nullable;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.Objects;

/**
 * Tutor specific utility related to this assignment.
 */
public final class TutorUtils {

    /**
     * Prevent instantiation of this utility class.
     */
    private TutorUtils() {
    }

    /**
     * Returns {@code true} if the two flights are equal, {@code false} otherwise. This method compares the flight
     * since the {@link Flight#equals(Object)} method is not implemented. Use this method to compare two flights.
     *
     * @param flight1 the first flight to compare
     * @param flight2 the second flight to compare
     *
     * @return {@code true} if the two flights are equal, {@code false} otherwise
     */
    public static boolean equalFlight(Flight flight1, Flight flight2) {
        if (flight1 == null || flight2 == null) {
            return flight1 == flight2;
        }
        TypeLink typeLink = Links.getType(Flight.class);
        FieldLink initialSeatsLink = typeLink.getField(Matcher.of(field -> field.name().equals("initialSeats")));
        return Objects.equals(flight1.getFlightNumber(), flight2.getFlightNumber())
            && Objects.equals(flight1.getDepartureTime(), flight2.getDepartureTime())
            && Objects.equals(flight1.getDestination(), flight2.getDestination())
            && Objects.equals(initialSeatsLink.get(flight1), initialSeatsLink.get(flight2))
            && Objects.equals(flight1.getAvailableSeats(), flight2.getAvailableSeats());
    }

    /**
     * Returns {@code true} if the two arrays of flights are equal, {@code false} otherwise. This method compares the
     *
     * @param flights1 the first array of flights
     * @param flights2 the second array of flights
     *
     * @return {@code true} if the two arrays of flights are equal, {@code false} otherwise
     * @see #equalFlight(Flight, Flight)
     */
    public static boolean equalFlights(Flight[] flights1, Flight[] flights2) {
        if (flights1.length != flights2.length) {
            return false;
        }
        for (int i = 0; i < flights1.length; i++) {
            @Nullable Flight flight1 = flights1[i];
            @Nullable Flight flight2 = flights2[i];
            if (flight1 == null && flight2 == null) {
                continue;
            }
            if (flight1 == null || flight2 == null) {
                return false;
            }
            if (!equalFlight(flight1, flight2)) {
                return false;
            }
        }
        return true;
    }

    public static boolean equalBookings(Booking booking1, Booking booking2) {
        return Objects.equals(booking1.getBookingId(), booking2.getBookingId())
            && Objects.equals(booking1.getFlightNumber(), booking2.getFlightNumber())
            && Objects.equals(booking1.getPassengerId(), booking2.getPassengerId())
            && booking1.isCancelled() == booking2.isCancelled();
    }

    public static String cleanOutput(String output) {
        String[] lines = output.split("\n");
        StringBuilder actual = new StringBuilder();

        for (String line : lines) {
            if (!line.contains("Mockito")) {
                actual.append(line).append("\n");
            }
        }
        return actual.toString();
    }
}

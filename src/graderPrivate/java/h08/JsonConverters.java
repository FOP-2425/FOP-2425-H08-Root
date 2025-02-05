package h08;

import com.fasterxml.jackson.databind.JsonNode;
import h08.mock.MockAirport;
import h08.mock.MockFlight;
import org.mockito.Mockito;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Defines JSON converters for the H08 assignment.
 *
 * @author Nhan Huynh
 */
public final class JsonConverters extends org.tudalgo.algoutils.tutor.general.json.JsonConverters {

    /**
     * The date formatter for the date of birth.
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * The date time formatter for the departure time.
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Prevent instantiation of this utility class.
     */
    private JsonConverters() {
    }

    /**
     * Converts a JSON node to a local date.
     *
     * @param node the JSON node to convert
     *
     * @return the local date represented by the JSON node
     */
    public static LocalDate toLocalDate(JsonNode node) {
        if (!node.isTextual()) {
            throw new IllegalArgumentException("Expected a textual value");
        }
        return LocalDate.parse(node.asText(), DATE_FORMATTER);
    }

    /**
     * Converts a JSON node to a local date time.
     *
     * @param node the JSON node to convert
     *
     * @return the local date time represented by the JSON node
     */
    public static LocalDateTime toLocalDateTime(JsonNode node) {
        if (!node.isTextual()) {
            throw new IllegalArgumentException("Expected a textual value");
        }
        return LocalDateTime.parse(node.asText(), DATE_TIME_FORMATTER);
    }

    /**
     * Converts a JSON node to a flight.
     *
     * @param node the JSON node to convert
     *
     * @return the flight represented by the JSON node
     */
    public static MockFlight toFlight(JsonNode node) {
        if (!node.isObject()) {
            throw new IllegalArgumentException("Expected an object");
        }
        MockFlight instance = Mockito.mock(MockFlight.class, Mockito.CALLS_REAL_METHODS);
        TypeLink type = BasicTypeLink.of(Flight.class);
        FieldLink initialSeatsLink = type.getField(Matcher.of(field -> field.name().equals("initialSeats")));
        initialSeatsLink.set(instance, node.get("initialSeats").asInt());
        Mockito.when(instance.getFlightNumber()).thenReturn(node.get("flightNumber").asText());
        Mockito.when(instance.getDeparture()).thenReturn(node.get("departure").asText());
        Mockito.when(instance.getDestination()).thenReturn(node.get("destination").asText());
        Mockito.when(instance.getDepartureTime()).thenReturn(toLocalDateTime(node.get("departureTime")).toString());
        Mockito.when(instance.getAvailableSeats()).thenReturn(node.get("availableSeats").asInt());
        return instance;
    }

    /**
     * Converts a JSON node to an airport.
     *
     * @param node the JSON node to convert
     *
     * @return the airport represented by the JSON node
     */
    public static MockAirport toAirport(JsonNode node) {
        if (!node.isObject()) {
            throw new IllegalArgumentException("Expected an object");
        }
        return new MockAirport(
            node.get("airportCode").asText(),
            node.get("initialCapacity").asInt(),
            toList(node.get("departingFlights"), JsonConverters::toFlight),
            toList(node.get("arrivingFlights"), JsonConverters::toFlight)
        );
    }
}

package h08;

import com.fasterxml.jackson.databind.JsonNode;
import h08.assertions.Mocks;
import h08.mocks.FakeAirport;
import org.jetbrains.annotations.Nullable;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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
    public static @Nullable Flight toFlight(JsonNode node) {
        if (node.isNull()) {
            return null;
        }
        if (!node.isObject()) {
            throw new IllegalArgumentException("Node is not an object");
        }
        return Mocks.createFlight(
            node.get("flightNumber").asText(),
            node.get("departure").asText(),
            node.get("destination").asText(),
            toLocalDateTime(node.get("departureTime")),
            node.get("initialSeats").asInt(),
            node.get("availableSeats").asInt()
        );
    }

    /**
     * Converts a JSON node to an airport.
     * @param node the JSON node to convert
     * @return the airport represented by the JSON node
     */
    public static Airport toAirport(JsonNode node) {
        if (!node.isObject()) {
            throw new IllegalArgumentException("Node is not an object");
        }
        return new FakeAirport(
            node.get("airportCode").asText(),
            node.get("initialCapacity").asInt(),
            toList(node.get("departingFlights"), JsonConverters::toFlight).toArray(Flight[]::new),
            toList(node.get("arrivingFlights"), JsonConverters::toFlight).toArray(Flight[]::new),
            node.get("departingSize").asInt(),
            node.get("arrivingSize").asInt()
        );
    }

    /**
     * Converts a JSON node to a booking.
     *
     * @param node the JSON node to convert
     *
     * @return the booking represented by the JSON node
     */
    public static Booking toBooking(JsonNode node) {
        if (!node.isObject()) {
            throw new IllegalArgumentException("Expected an object");
        }
        String bookingId = node.get("bookingId").asText();
        String flightNumber = node.get("flightNumber").asText();
        String passengerId = node.get("passengerId").asText();
        Booking booking = new Booking(bookingId, flightNumber, passengerId);
        if (node.has("isCancelled")) {
            boolean isCancelled = node.get("isCancelled").asBoolean();
            FieldLink isCancelledLink = BasicTypeLink.of(Booking.class)
                .getField(Matcher.of(field -> field.name().equals("isCancelled")));
            isCancelledLink.set(booking, isCancelled);
        }
        return booking;
    }

    /**
     * Converts a JSON node to a flight management.
     *
     * @param node the JSON node to convert
     *
     * @return the flight management represented by the JSON node
     */
    public static FlightManagement toFlightManagement(JsonNode node) {
        if (!node.isObject()) {
            throw new IllegalArgumentException("Expected an object");
        }
        List<Airport> airports = toList(node.get("airports"), JsonConverters::toAirport);
        FlightManagement instance = new FlightManagement(airports.size());
        for (Airport airport : airports) {
            instance.addAirport(airport);
        }
        return instance;
    }
}

package h08;

import com.fasterxml.jackson.databind.JsonNode;
import h08.assertions.ClassReference;
import h08.assertions.Links;
import h08.mocks.FakeBookingManagement;
import h08.rubric.context.TestInformation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mockito;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

/**
 * Defines the private tests for the subtask H08.5.2.
 *
 * @author Nhan Huynh
 */
@TestForSubmission
@DisplayName("H08.5.2 | Flight and Booking Management")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class H08_5_2_TestsPrivate extends H08_Tests {


    /**
     * The converters used for the test cases using JSON files to read the test data.
     */
    public static final Map<String, Function<JsonNode, ?>> CONVERTERS = Map.of(
        "flightManagement", JsonConverters::toFlightManagement,
        "bookingManagement", JsonConverters::toBookingManagement,
        "bookingId", node -> node.isNull() ? null : node.asText(),
        "flightNumber", node -> node.isNull() ? null : node.asText(),
        "passengerId", node -> node.isNull() ? null : node.asText(),
        "airportCode", JsonNode::asText,
        "flight", JsonConverters::toFlight,
        "isAddOperation", JsonNode::asBoolean,
        "expected", JsonConverters::toAirport
    );

    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_5_2_validateAndCheckBookingDuplicate.json", customConverters = CUSTOM_CONVERTERS)
    @SuppressWarnings("unchecked")
    void validateAndCheckBookingDuplicate(JsonParameterSet parameters) {
        TypeLink typeLink = Links.getType(BookingManagement.class);
        MethodLink methodLink = Links.getMethod(typeLink, "validateAndCheckBooking",
            String.class, String.class, String.class);

        BookingManagement management = parameters.<FakeBookingManagement>get("bookingManagement");
        String bookingId = parameters.getString("bookingId");
        String flightNumber = parameters.getString("flightNumber");
        String passengerId = parameters.getString("passengerId");

        ClassReference reference = ClassReference.DUPLICATE_BOOKING_EXCEPTION;
        Class<? extends Exception> exceptionClass = (Class<? extends Exception>) reference.getLink().reflection();
        TestInformation info = TestInformation.builder()
            .input(builder -> builder
                .add("bookings", management)
                .add("bookingId", bookingId)
                .add("flightNumber", flightNumber)
                .add("passengerId", passengerId)
            )
            .expect(builder -> builder.cause(exceptionClass))
            .build();
        Exception exception = Assertions2.assertThrows(
            exceptionClass,
            () -> methodLink.invoke(management, bookingId, flightNumber, passengerId),
            info,
            comment -> "No exception thrown for invalid input."
        );
        Assertions2.assertEquals(
            "A booking with this ID already exists.",
            exception.getMessage(),
            info,
            comment -> "Message does not match expected message."
        );
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_5_2_validateAndCheckBookingInvalid.json", customConverters = CUSTOM_CONVERTERS)
    @SuppressWarnings("unchecked")
    void validateAndCheckBookingInvalid(JsonParameterSet parameters) {
        TypeLink typeLink = Links.getType(BookingManagement.class);
        MethodLink methodLink = Links.getMethod(typeLink, "validateAndCheckBooking",
            String.class, String.class, String.class);

        BookingManagement management = parameters.<FakeBookingManagement>get("bookingManagement");
        String bookingId = parameters.get("bookingId");
        String flightNumber = parameters.get("flightNumber");
        String passengerId = parameters.get("passengerId");

        ClassReference reference = ClassReference.INVALID_BOOKING_EXCEPTION;
        Class<? extends Exception> exceptionClass = (Class<? extends Exception>) reference.getLink().reflection();
        TestInformation info = TestInformation.builder()
            .input(builder -> builder
                .add("bookings", management)
                .add("bookingId", bookingId)
                .add("flightNumber", flightNumber)
                .add("passengerId", passengerId)
            )
            .expect(builder -> builder.cause(exceptionClass))
            .build();
        Exception exception = Assertions2.assertThrows(
            exceptionClass,
            () -> methodLink.invoke(management, bookingId, flightNumber, passengerId),
            info,
            comment -> "No exception thrown for invalid input."
        );
        Assertions2.assertEquals(
            "Invalid booking details provided.",
            exception.getMessage(),
            info,
            comment -> "Message does not match expected message."
        );
    }

    @Test
    void testManageFlightCode() {
        FlightManagement management = Mockito.mock(FlightManagement.class, Mockito.CALLS_REAL_METHODS);
        TypeLink airportLink = Links.getType(FlightManagement.class);
        MethodLink manageFlightLink = Links.getMethod(airportLink, "manageFlight", String.class, Flight.class, boolean.class);
        FieldLink airportsLink = Links.getField(airportLink, "airports");
        FieldLink sizeLink = Links.getField(airportLink, "size");
        Airport[] airports = new Airport[5];
        for (int i = 0; i < airports.length; i++) {
            airports[i] = Mockito.mock(Airport.class);
            Mockito.doReturn("Airport " + i).when(airports[i]).getAirportCode();
        }
        airportsLink.set(management, airports);
        sizeLink.set(management, airports.length);

        Flight flight = Mockito.mock(Flight.class);
        Airport airport = airports[0];
        TestInformation info = TestInformation.builder()
            .subject(manageFlightLink)
            .input(builder -> builder
                .add("airportCode", airport.getAirportCode())
                .add("flight", flight)
                .add("isAddOperation", true))
            .build();
        Assertions2.call(
            () -> management.manageFlight(airport.getAirportCode(), flight, true),
            info,
            comment -> "Unknown exception occurred while managing flight."
        );
        Mockito.verify(airport, Mockito.times(1)).addFlight(Mockito.eq(flight), Mockito.anyBoolean());
    }

        @ParameterizedTest
    @JsonParameterSetTest(value = "H08_5_2_testManageFlight.json", customConverters = CUSTOM_CONVERTERS)
    void testManageFlight(JsonParameterSet parameters) {
        FlightManagement management = parameters.get("flightManagement");
        String airportCode = parameters.get("airportCode");
        Flight flight = parameters.get("flight");
        boolean isAddOperation = parameters.get("isAddOperation");
        TestInformation info = TestInformation.builder()
            .input(builder -> builder
                .add("airportCode", airportCode)
                .add("flight", flight)
                .add("isAddOperation", isAddOperation))
            .build();
        management.manageFlight(airportCode, flight, isAddOperation);
        FieldLink airportsLink = Links.getField(Links.getType(FlightManagement.class), "airports");
        Airport[] airports = airportsLink.get(management);
        Airport actual = Arrays.stream(airports).
            filter(it -> it.getAirportCode().equals(airportCode))
            .findFirst()
            .orElseThrow();
        Airport expected = parameters.get("expected");
        Assertions2.assertEquals(
            expected,
            actual,
            info,
            comment -> "Airport does not match expected airport."
        );
    }
}

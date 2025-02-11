package h08;

import com.fasterxml.jackson.databind.JsonNode;
import h08.assertions.ClassReference;
import h08.assertions.Links;
import h08.mocks.FakeBookingManagement;
import h08.rubric.context.TestInformation;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mockito;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

/**
 * Defines the private tests for the subtask H08.5.4.
 *
 * @author Nhan Huynh
 */
@TestForSubmission
@DisplayName("H08.5.5 | Creating a Booking")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class H08_5_5_TestsPrivate extends H08_Tests {

    public static final Map<String, Function<JsonNode, ?>> CONVERTERS = Map.of(
        "bookingManagement", JsonConverters::toBookingManagement,
        "booking", JsonConverters::toBooking
    );

    private MethodLink method;

    private PrintStream out;

    private PrintStream testOut;

    private ByteArrayOutputStream baos;

    @BeforeAll
    protected void globalSetup() {
        TypeLink type = Links.getType(BookingManagement.class);
        method = Links.getMethod(type, "createBooking", String.class, String.class, String.class);
        out = System.out;
    }

    @BeforeEach
    void setup() {
        baos = new ByteArrayOutputStream();
        testOut = new PrintStream(baos);
        System.setOut(testOut);
    }

    @AfterEach
    void tearDown() {
        testOut.close();
    }

    /**
     * Tears down the global test environment.
     */
    @AfterAll
    void globalTearDown() {
        System.setOut(out);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_5_5_testCreateBooking.json", customConverters = CUSTOM_CONVERTERS)
    void testCreateBooking(JsonParameterSet parameters) throws Throwable {
        FakeBookingManagement management = parameters.get("bookingManagement");
        String bookingId = parameters.getString("bookingId");
        String flightNumber = parameters.getString("flightNumber");
        String passengerId = parameters.getString("passengerId");
        Booking booking = parameters.get("booking");

        TestInformation info = TestInformation.builder()
            .subject(method)
            .input(builder -> builder
                .add("management", management)
                .add("bookingId", bookingId)
                .add("flightNumber", flightNumber)
                .add("passengerId", passengerId))
            .build();
        management.createBooking(bookingId, flightNumber, passengerId);
        Assertions2.assertTrue(
            Arrays.stream(management.getBookings()).anyMatch(b -> TutorUtils.equalBookings(b, booking)),
            info,
            comment -> "Booking should be created."
        );
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_5_5_testCreateBooking.json", customConverters = CUSTOM_CONVERTERS)
    @SuppressWarnings("unchecked")
    void testCreateBookingNoFlight(JsonParameterSet parameters) throws Throwable {
        FakeBookingManagement management = parameters.get("bookingManagement");
        FlightManagement flightManagement = management.getFlightManagement();
        String bookingId = parameters.getString("bookingId");
        String flightNumber = parameters.getString("flightNumber");
        String passengerId = parameters.getString("passengerId");
        String message = parameters.getString("message");
        Mockito.doReturn(null).when(flightManagement).getFlight(flightNumber);
        ClassReference reference = ClassReference.FLIGHT_NOT_FOUND_EXCEPTION;
        Class<? extends Exception> exceptionClass = (Class<? extends Exception>) reference.getLink().reflection();
        TestInformation info = TestInformation.builder()
            .subject(method)
            .input(builder -> builder
                .add("management", management)
                .add("bookingId", bookingId)
                .add("flightNumber", flightNumber)
                .add("passengerId", passengerId))
            .expect(builder -> builder
                .cause(exceptionClass)
                .add("Message", message))
            .build();
        Exception exception = Assertions2.assertThrows(
            exceptionClass,
            () -> management.createBooking(bookingId, flightNumber, passengerId),
            info,
            comment -> "Should throw a FlightNotFoundException."
        );
        Assertions2.assertEquals(
            message,
            exception.getMessage(),
            info,
            comment -> "The message should be correct."
        );
    }

    private void assertCreateBookingMessage(JsonParameterSet parameters) throws Throwable {
        BookingManagement management = parameters.<FakeBookingManagement>get("bookingManagement");
        String bookingId = parameters.getString("bookingId");
        String flightNumber = parameters.getString("flightNumber");
        String passengerId = parameters.getString("passengerId");
        String message = parameters.getString("message");

        TestInformation info = TestInformation.builder()
            .subject(method)
            .input(builder -> builder
                .add("management", management)
                .add("bookingId", bookingId)
                .add("flightNumber", flightNumber)
                .add("passengerId", passengerId))
            .expect(builder -> builder.add("Message", message))
            .build();
        management.createBooking(bookingId, flightNumber, passengerId);
        Assertions2.assertEquals(
            message,
            TutorUtils.cleanOutput(baos.toString()).trim(),
            info,
            comment -> "Printed message does not match expected message."
        );
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_5_5_testCreateBookingDuplicate.json", customConverters = CUSTOM_CONVERTERS)
    void testCreateBookingDuplicate(JsonParameterSet parameters) throws Throwable {
        assertCreateBookingMessage(parameters);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_5_5_testCreateBookingInvalid.json", customConverters = CUSTOM_CONVERTERS)
    void testCreateBookingInvalid(JsonParameterSet parameters) throws Throwable {
        assertCreateBookingMessage(parameters);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_5_5_testCreateBookingNoSeats.json", customConverters = CUSTOM_CONVERTERS)
    void testCreateBookingNoSeats(JsonParameterSet parameters) throws Throwable {
        assertCreateBookingMessage(parameters);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_5_5_testCreateBookingValidation.json", customConverters = CUSTOM_CONVERTERS)
    void testCreateBookingValidation(JsonParameterSet parameters) throws Throwable {
        FakeBookingManagement management = parameters.get("bookingManagement");
        FlightManagement flightManagement = management.getFlightManagement();
        String bookingId = parameters.getString("bookingId");
        String flightNumber = parameters.getString("flightNumber");
        String passengerId = parameters.getString("passengerId");
        Flight flight = Mockito.mock(Flight.class);
        Mockito.doReturn(flightNumber).when(flight).getFlightNumber();
        Mockito.doReturn(flight).when(flightManagement).getFlight(flightNumber);
        management.createBooking(bookingId, flightNumber, passengerId);
        Mockito.verify(flight, Mockito.times(1)).bookSeat();
    }
}

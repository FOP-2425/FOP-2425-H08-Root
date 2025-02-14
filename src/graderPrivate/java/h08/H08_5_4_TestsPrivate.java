package h08;

import com.fasterxml.jackson.databind.JsonNode;
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
@DisplayName("H08.5.4 | Cancelling a Booking")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class H08_5_4_TestsPrivate extends H08_Tests {

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
        method = Links.getMethod(type, "cancelBooking", String.class);
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

    private void assertCancelBookingMessage(JsonParameterSet parameters) throws Throwable {
        BookingManagement management = parameters.<FakeBookingManagement>get("bookingManagement");
        Booking booking = parameters.get("booking");
        String message = parameters.getString("message");

        TestInformation info = TestInformation.builder()
            .subject(method)
            .input(builder -> builder
                .add("management", management)
                .add("bookingId", booking.getBookingId())
            ).expect(builder -> builder.add("message", message))
            .build();
        management.cancelBooking(booking.getBookingId());
        Assertions2.assertEquals(
            message,
            TutorUtils.cleanOutput(baos.toString()).trim(),
            info,
            comment -> "Printed message does not match expected message."
        );
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_5_4_testCancelBooking.json", customConverters = CUSTOM_CONVERTERS)
    void testCancelBooking(JsonParameterSet parameters) throws Throwable {
        FakeBookingManagement management = parameters.<FakeBookingManagement>get("bookingManagement");
        Booking booking = Arrays.stream(management.getBookings())
            .filter(b -> TutorUtils.equalBookings(b, parameters.get("booking")))
            .findFirst()
            .orElseThrow();
        String message = parameters.getString("message");
        TestInformation info = TestInformation.builder()
            .subject(method)
            .input(builder -> builder
                .add("management", management)
                .add("bookingId", booking.getBookingId())
            ).expect(builder -> builder.add("message", message))
            .build();
        management.cancelBooking(booking.getBookingId());
        Assertions2.assertTrue(
            booking.isCancelled(),
            info,
            comment -> "Booking should be cancelled."
        );
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_5_4_testCancelBooking.json", customConverters = CUSTOM_CONVERTERS)
    void testCancelBookingMessage(JsonParameterSet parameters) throws Throwable {
        assertCancelBookingMessage(parameters);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_5_4_testCancelBookingAlreadyCancelled.json", customConverters = CUSTOM_CONVERTERS)
    void testCancelBookingAlreadyCancelled(JsonParameterSet parameters) throws Throwable {
        assertCancelBookingMessage(parameters);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_5_4_testCancelBookingNotFound.json", customConverters = CUSTOM_CONVERTERS)
    void testCancelBookingNotFound(JsonParameterSet parameters) throws Throwable {
        assertCancelBookingMessage(parameters);
    }
}

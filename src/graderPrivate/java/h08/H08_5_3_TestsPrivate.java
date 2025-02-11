package h08;

import com.fasterxml.jackson.databind.JsonNode;
import h08.assertions.ClassReference;
import h08.assertions.Links;
import h08.mocks.FakeBookingManagement;
import h08.rubric.context.TestInformation;
import org.junit.jupiter.api.AfterAll;
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
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * Defines the private tests for the subtask H08.5.3.
 *
 * @author Nhan Huynh
 */
@TestForSubmission
@DisplayName("H8.5.3 | Searching a Booking")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class H08_5_3_TestsPrivate extends H08_Tests {

    public static final Map<String, Function<JsonNode, ?>> CONVERTERS = Map.of(
        "bookingManagement", JsonConverters::toBookingManagement,
        "booking", JsonConverters::toBooking
    );

    private TypeLink type;

    private PrintStream out;

    private PrintStream testOut;

    private ByteArrayOutputStream baos;

    @BeforeAll
    protected void globalSetup() {
        type = Links.getType(BookingManagement.class);
        out = System.out;
    }

    @BeforeEach
    void setup() {
        baos = new ByteArrayOutputStream();
        testOut = new PrintStream(baos);
        System.setOut(testOut);
    }

    @AfterAll
    void globalTearDown() {
        System.setOut(out);
    }

    private void assertBooking(JsonParameterSet parameters, MethodLink method) throws Throwable {
        BookingManagement management = parameters.<FakeBookingManagement>get("bookingManagement");
        Booking booking = parameters.get("booking");
        TestInformation info = TestInformation.builder()
            .subject(method)
            .input(builder -> builder
                .add("bookingManagement", management)
                .add("bookingId", booking.getBookingId()))
            .expect(builder -> builder.add("booking", booking))
            .build();
        AtomicReference<Booking> result = new AtomicReference<>();
        Assertions2.call(
            () -> result.set(method.invoke(management, booking.getBookingId())),
            info,
            comment -> "Unexpected exception occured while searching for booking."
        );
        Assertions2.assertTrue(
            TutorUtils.equalBookings(booking, result.get()),
            info,
            comment -> "The booking found is not the same as the expected booking."
        );
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_5_3_testSearchBooking.json", customConverters = CUSTOM_CONVERTERS)
    void testSearchBooking(JsonParameterSet parameters) throws Throwable {
        assertBooking(parameters, Links.getMethod(type, "searchBooking", String.class));
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_5_3_testSearchBookingException.json", customConverters = CUSTOM_CONVERTERS)
    @SuppressWarnings("unchecked")
    void testSearchBookingException(JsonParameterSet parameters) throws Throwable {
        BookingManagement management = parameters.<FakeBookingManagement>get("bookingManagement");
        Booking booking = parameters.get("booking");
        MethodLink method = Links.getMethod(type, "searchBooking", String.class);
        ClassReference reference = ClassReference.BOOKING_NOT_FOUND_EXCEPTION;
        Class<? extends Exception> exceptionClass = (Class<? extends Exception>) reference.getLink().reflection();
        TestInformation info = TestInformation.builder()
            .subject(method)
            .input(builder -> builder
                .add("bookingManagement", management)
                .add("bookingId", booking.getBookingId()))
            .expect(builder -> builder.cause(exceptionClass))
            .build();
        Exception exception = Assertions2.assertThrows(
            exceptionClass,
            () -> method.invoke(management, booking.getBookingId()),
            info,
            comment -> "Expected exception was not thrown."
        );
        String message = parameters.getString("message");
        Assertions2.assertEquals(
            message,
            exception.getMessage(),
            info,
            comment -> "Exception message does not match the expected message."
        );
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_5_3_testGetBooking.json", customConverters = CUSTOM_CONVERTERS)
    void testGetBooking(JsonParameterSet parameters) throws Throwable {
        assertBooking(parameters, Links.getMethod(type, "getBooking", String.class));
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_5_3_testGetBookingException.json", customConverters = CUSTOM_CONVERTERS)
    void testGetBookingException(JsonParameterSet parameters) throws Throwable {
        BookingManagement management = parameters.get("bookingManagement");
        Booking booking = parameters.get("booking");
        String message = parameters.getString("message");
        MethodLink method = Links.getMethod(type, "getBooking", String.class);
        TestInformation info = TestInformation.builder()
            .subject(method)
            .input(builder -> builder
                .add("bookingManagement", management)
                .add("bookingId", booking.getBookingId())
            )
            .expect(builder -> builder.add("message", message))
            .build();
        Booking result = method.invoke(management, booking.getBookingId());
        Assertions2.assertNull(
            result,
            info,
            comment -> "Expected null to be returned."
        );

        Assertions2.assertEquals(
            message,
            TutorUtils.cleanOutput(baos.toString()).trim(),
            info,
            comment -> "Printed message does not match the expected message."
        );
    }
}

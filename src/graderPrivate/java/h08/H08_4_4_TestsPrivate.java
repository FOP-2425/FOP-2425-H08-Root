package h08;

import com.fasterxml.jackson.databind.JsonNode;
import h08.assertions.Links;
import h08.rubric.context.TestInformation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.Map;
import java.util.function.Function;

/**
 * Defines the private tests for the subtask H08.4.3.
 *
 * @author Nhan Huynh
 */
@TestForSubmission
@DisplayName("H08.4.4 | Removing a booking")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class H08_4_4_TestsPrivate extends H08_Tests {

    /**
     * The converters used for the test cases using JSON files to read the test data.
     */
    public static final Map<String, Function<JsonNode, ?>> CONVERTERS = Map.of(
        "booking", JsonConverters::toBooking,
        "isCancelled", JsonNode::asInt,
        "message", JsonNode::asText
    );

    /**
     * The link to the cancelBooking method of the Booking class.
     */
    private MethodLink methodLink;

    /**
     * Sets up the global test environment.
     */
    @BeforeAll
    protected void globalSetup() {
        methodLink = Links.getMethod(BasicTypeLink.of(Booking.class), "cancelBooking");
    }

    /**
     * Initializes the test with the given parameters.
     *
     * @param parameters the parameters to initialize the test with
     */
    private TestInformation.TestInformationBuilder info(JsonParameterSet parameters) {
        Booking booking = parameters.get("booking");
        boolean isCancelled = parameters.getBoolean("isCancelled");
        return TestInformation.builder()
            .subject(methodLink)
            .input(builder -> builder
                .add("booking", booking)
                .add("isCancelled", isCancelled)
            );
    }

    @DisplayName("Die Methode getFlight gibt Flüge korrekt zurück.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_4_4_testCancelBooking.json", customConverters = CUSTOM_CONVERTERS)
    void testCancelBooking(JsonParameterSet parameters) throws Throwable {
        Booking booking = parameters.get("booking");
        TestInformation info = info(parameters)
            .expect(builder -> builder
                .cause(null).
                add("isCancelled", true)
            ).build();
        Assertions2.call(booking::cancelBooking, info,
            comment -> "Something went wrong while cancelling the booking.");
        Assertions2.assertTrue(booking.isCancelled(), info, comment -> "The status of the cancellation is not correct.");
    }

    @DisplayName("Die Methode wirft korrekt eine BookingAlreadyCancelledException, wenn die Buchung bereits storniert wurde.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_4_4_testCancelBookingException.json", customConverters = CUSTOM_CONVERTERS)
    @SuppressWarnings("unchecked")
    void testCancelBookingException(JsonParameterSet parameters) throws Throwable {
        TypeLink type = Links.getType("h08.Exceptions", "BookingAlreadyCancelledException");
        Class<? extends Exception> exceptionType = (Class<? extends Exception>) type.reflection();
        TestInformation info = info(parameters)
            .expect(builder -> builder
                .cause(exceptionType)
                .add("isCancelled", parameters.getBoolean("isCancelled")))
            .build();
        Booking booking = parameters.get("booking");
        Throwable exception = Assertions2.assertThrows(
            exceptionType,
            booking::cancelBooking,
            info,
            comment -> "Cannot cancel a booking that has already been cancelled."
        );
        String message = parameters.get("message");
        Assertions2.assertEquals(
            message,
            exception.getMessage(),
            info,
            comment -> "Exception message does not match expected message."
        );
    }
}

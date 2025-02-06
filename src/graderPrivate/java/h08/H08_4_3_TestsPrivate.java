package h08;

import com.fasterxml.jackson.databind.JsonNode;
import h08.mock.MockAirport;
import h08.mock.MockFlight;
import h08.rubric.context.TestInformation;
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
@DisplayName("H08.4.3 | Getting a Flight\"")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class H08_4_3_TestsPrivate extends H08_Tests {

    /**
     * The converters used for the test cases using JSON files to read the test data.
     */
    public static final Map<String, Function<JsonNode, ?>> CONVERTERS = Map.of(
        "flight", JsonConverters::toFlight,
        "airport", JsonConverters::toAirport,
        "isDeparting", JsonNode::asBoolean,
        "message", JsonNode::asText
    );

    /**
     * The link to the getFlight method of the Airport class.
     */
    private MethodLink getFlightLink;

    /**
     * The builder for the test information.
     */
    private TestInformation.TestInformationBuilder builder;

    /**
     * Sets up the global test environment.
     */
    @BeforeAll
    protected void globalSetup() {
        getFlightLink = Links.getMethod(BasicTypeLink.of(Airport.class), "getFlight", String.class, boolean.class);
    }

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setup() {
        builder = null;
    }

    /**
     * Initializes the test with the given parameters.
     *
     * @param parameters the parameters to initialize the test with
     */
    private void initTest(JsonParameterSet parameters) {
        MockAirport airport = parameters.get("airport");
        Flight flight = parameters.get("flight");
        boolean isDeparting = parameters.get("isDeparting");
        builder = TestInformation.builder()
            .subject(getFlightLink)
            .input(builder -> builder
                .add("airport", airport)
                .add("flight", flight)
                .add("isDeparting", isDeparting)
            );
    }

    @DisplayName("Die Methode cancelBooking() storniert eine Buchung korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_4_3_testGetFlight.json", customConverters = CUSTOM_CONVERTERS)
    void testGetFlight(JsonParameterSet parameters) {
        initTest(parameters);

        MockAirport airport = parameters.get("airport");
        Flight flight = parameters.get("flight");
        boolean isDeparting = parameters.get("isDeparting");

        TestInformation info = builder.expect(builder->builder.cause(null)).build();
        Assertions2.call(() -> Assertions2.assertTrue(
                MockFlight.equals(flight, airport.getFlight(flight.getFlightNumber(), isDeparting)),
                info, comment -> "Unexpected exception occurred while searching for the flight!"
            ),
            info, comment -> "Flight should be found!");
    }

    @DisplayName("Die Methode wirft korrekt eine FlightNotFoundException, wenn der Flug nicht existiert.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_4_3_testGetFlightFlightNotFoundException.json", customConverters = CUSTOM_CONVERTERS)
    @SuppressWarnings("unchecked")
    void testGetFlightFlightNotFoundException(JsonParameterSet parameters) {
        initTest(parameters);

        MockAirport airport = parameters.get("airport");
        Flight flight = parameters.get("flight");
        boolean isDeparting = parameters.get("isDeparting");

        TypeLink type = Links.getType("h08.Exceptions", "FlightNotFoundException");
        Class<? extends Exception> exceptionType = (Class<? extends Exception>) type.reflection();
        TestInformation info = builder.expect(builder -> builder.cause(exceptionType)).build();

        Exception exception = Assertions2.assertThrows(
            exceptionType,
            () -> airport.removeFlight(flight.getFlightNumber(), isDeparting),
            info,
            comment -> "The exception should be thrown!"
        );
        String message = parameters.get("message");
        Assertions2.assertEquals(message, exception.getMessage(), info, comment -> "The exception message is incorrect!");
    }
}

package h08;

import com.fasterxml.jackson.databind.JsonNode;
import h08.mock.MockAirport;
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

import java.util.Map;
import java.util.function.Function;

/**
 * Defines the private tests for the subtask H08.4.1.
 *
 * @author Nhan Huynh
 */
@TestForSubmission
@DisplayName("H08.4.1 | Adding a Flight")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class H08_4_1_TestsPrivate extends H08_Tests {

    /**
     * The converters used for the test cases using JSON files to read the test data.
     */
    public static final Map<String, Function<JsonNode, ?>> CONVERTERS = Map.of(
        "flight", JsonConverters::toFlight,
        "airport", JsonConverters::toAirport,
        "isDeparting", JsonNode::asBoolean,
        "airportPost", JsonConverters::toAirport,
        "message", JsonNode::asText
    );

    /**
     * The link to the addFlight method of the Airport class.
     */
    private MethodLink addFlightLink;

    /**
     * The builder for the test information.
     */
    private TestInformation.TestInformationBuilder builder;

    /**
     * Sets up the global test environment.
     */
    @BeforeAll
    protected void globalSetup() {
        addFlightLink = Links.getMethod(BasicTypeLink.of(Airport.class), "addFlight", Flight.class, boolean.class);
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
            .subject(addFlightLink)
            .input(builder -> builder
                .add("airport", airport)
                .add("flight", flight)
                .add("isDeparting", isDeparting)
            );
    }

    @DisplayName("Die Methode addFlight fügt Flüge korrekt zu abgehenden oder ankommenden Flügen hinzu.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_4_1_testAddFlight.json", customConverters = CUSTOM_CONVERTERS)
    void testAddFlight(JsonParameterSet parameters) {
        initTest(parameters);

        MockAirport airport = parameters.get("airport");
        Flight flight = parameters.get("flight");
        boolean isDeparting = parameters.get("isDeparting");

        TestInformation info = builder.expect(builder->builder.cause(null)).build();
        Assertions2.call(() -> airport.addFlight(flight, isDeparting), info,
            comment -> "Unexpected exception occurred while adding the flight!");
        MockAirport postAirport = parameters.get("airportPost");
        Assertions2.assertEquals(postAirport, airport, info, comment -> "The airport should be modified correctly!");
    }

    @DisplayName("Die Methode prüft und behandelt korrekt falsche Flughafencodes.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_4_1_testAddFlightInvalidAirportCode.json", customConverters = CUSTOM_CONVERTERS)
    void testAddFlightInvalidAirportCode(JsonParameterSet parameters) {
        initTest(parameters);

        MockAirport airport = parameters.get("airport");
        Flight flight = parameters.get("flight");
        boolean isDeparting = parameters.get("isDeparting");

        TestInformation info = builder.expect(builder -> builder.cause(IllegalArgumentException.class)).build();
        Exception exception = Assertions2.assertThrows(
            IllegalArgumentException.class,
            () -> airport.addFlight(flight, isDeparting),
            info,
            comment -> "The exception should be thrown!"
        );
        String message = parameters.get("message");
        Assertions2.assertEquals(message, exception.getMessage(), info, comment -> "The exception message is incorrect!");
        MockAirport preAirport = parameters.get("airport");
        Assertions2.assertEquals(preAirport, airport, info, comment -> "The airport should not be modified!");
    }
}

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
        "preAirport", JsonConverters::toAirport,
        "postAirport", JsonConverters::toAirport,
        "flight", JsonConverters::toFlight,
        "isDeparting", JsonNode::asBoolean
    );

    /**
     * The link to the addFlight method of the Airport class.
     */
    private MethodLink methodLink;

    /**
     * The link to the addFlight method of the Airport class.
     */
    @BeforeAll
    protected void globalSetup() {
        methodLink = Links.getMethod(Links.getType(Airport.class), "addFlight", Flight.class, boolean.class);
    }

    private TestInformation.TestInformationBuilder infoBuilder(JsonParameterSet parameters) {
        Airport preAirport = parameters.get("preAirport");
        Flight flight = parameters.get("flight");
        boolean isDeparting = parameters.get("isDeparting");
        return TestInformation.builder()
            .subject(methodLink)
            .input(builder -> builder
                .add("airport", preAirport)
                .add("flight", flight)
                .add("isDeparting", isDeparting));
    }

    @DisplayName("Die Methode addFlight fügt Flüge korrekt zu abgehenden oder ankommenden Flügen hinzu.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_4_1_testAddFlight.json", customConverters = CUSTOM_CONVERTERS)
    void testAddFlight(JsonParameterSet parameters) throws Throwable {
        Airport preAirport = parameters.get("preAirport");
        Airport postAirport = parameters.get("postAirport");
        Flight flight = parameters.get("flight");
        boolean isDeparting = parameters.get("isDeparting");

        TestInformation info = infoBuilder(parameters).expect(builder -> builder
                .cause(null)
                .add("airport", postAirport))
            .build();
        Assertions2.call(
            () -> preAirport.addFlight(flight, isDeparting),
            info,
            comment -> "Valid depature/destination should not cause an exception."
        );
        Assertions2.assertEquals(
            postAirport,
            preAirport,
            info,
            comment -> "The airport should be updated correctly after adding the flight."
        );
    }

    @DisplayName("Die Methode prüft und behandelt korrekt falsche Flughafencodes.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_4_1_testAddFlightException.json", customConverters = CUSTOM_CONVERTERS)
    void testAddFlightException(JsonParameterSet parameters) throws Throwable {
        Airport preAirport = parameters.get("preAirport");
        Airport postAirport = parameters.get("postAirport");
        Flight flight = parameters.get("flight");
        boolean isDeparting = parameters.get("isDeparting");

        TestInformation info = infoBuilder(parameters)
            .expect(builder -> builder.cause(IllegalAccessError.class))
            .build();
        Assertions2.assertThrows(
            IllegalArgumentException.class,
            () -> preAirport.addFlight(flight, isDeparting),
            info,
            comment -> "Invalid depature/destination should cause an exception."
        );
        Assertions2.assertEquals(
            postAirport,
            preAirport,
            info,
            comment -> "The airport should not be updated when an exception occurs."
        );
    }
}

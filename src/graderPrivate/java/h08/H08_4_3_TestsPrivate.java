package h08;

import com.fasterxml.jackson.databind.JsonNode;
import h08.assertions.ClassReference;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * Defines the private tests for the subtask H08.4.3.
 *
 * @author Nhan Huynh
 */
@TestForSubmission
@DisplayName("H08.4.3 | Getting a Flight")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class H08_4_3_TestsPrivate extends H08_Tests {

    /**
     * The converters used for the test cases using JSON files to read the test data.
     */
    public static final Map<String, Function<JsonNode, ?>> CONVERTERS = Map.of(
        "airport", JsonConverters::toAirport,
        "flight", JsonConverters::toFlight,
        "isDeparting", JsonNode::asBoolean,
        "message", JsonNode::asText
    );

    /**
     * The link to the getFlight method of the Airport class.
     */
    private MethodLink methodLink;

    /**
     * The link to the addFlight method of the Airport class.
     */
    @BeforeAll
    protected void globalSetup() {
        methodLink = Links.getMethod(Links.getType(Airport.class), "getFlight", String.class, boolean.class);
    }

    private TestInformation.TestInformationBuilder infoBuilder(JsonParameterSet parameters) {
        Airport preAirport = parameters.get("airport");
        Flight flight = parameters.get("flight");
        boolean isDeparting = parameters.get("isDeparting");
        return TestInformation.builder()
            .subject(methodLink)
            .input(builder -> builder
                .add("airport", preAirport)
                .add("flightNumber", flight.getFlightNumber())
                .add("isDeparting", isDeparting));
    }

    @DisplayName("Die Methode getFlight gibt Flüge korrekt zurück.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_4_3_testGetFlight.json", customConverters = CUSTOM_CONVERTERS)
    void testGetFlight(JsonParameterSet parameters) throws Throwable {
        Airport airport = parameters.get("airport");
        Flight flight = parameters.get("flight");
        boolean isDeparting = parameters.get("isDeparting");

        TestInformation.TestInformationBuilder infoBuilder = infoBuilder(parameters)
            .expect(builder -> builder.add("flight", flight));
        AtomicReference<Flight> searchedFlight = new AtomicReference<>();
        Assertions2.call(
            () -> searchedFlight.set(airport.getFlight(flight.getFlightNumber(), isDeparting)),
            infoBuilder.build(),
            comment -> "Searching for an existing flight should not cause an exception."
        );
        Assertions2.assertTrue(
            TutorUtils.equalFlight(flight, searchedFlight.get()),
            infoBuilder.actual(builder -> builder.add("flight", searchedFlight.get())).build(),
            comment -> "Searched flight does not match expected search result."
        );
    }

    @DisplayName("Die Methode wirft korrekt eine FlightNotFoundException, wenn der Flug nicht existiert.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_4_3_testGetFlightException.json", customConverters = CUSTOM_CONVERTERS)
    @SuppressWarnings("unchecked")
    void testGetFlightException(JsonParameterSet parameters) throws Throwable {
        Airport airport = parameters.get("airport");
        Flight flight = parameters.get("flight");
        boolean isDeparting = parameters.get("isDeparting");

        ClassReference reference = ClassReference.FLIGHT_NOT_FOUND_EXCEPTION;
        Class<? extends Exception> exceptionClass = (Class<? extends Exception>) reference.getLink().reflection();
        TestInformation info = infoBuilder(parameters)
            .expect(builder -> builder.cause(exceptionClass))
            .build();

        Throwable throwable = Assertions2.assertThrows(
            exceptionClass,
            () -> airport.getFlight(flight.getFlightNumber(), isDeparting),
            info,
            comment -> "Invalid depature/destination should cause an exception."
        );
        String message = parameters.get("message");
        Assertions2.assertEquals(
            message,
            throwable.getMessage(),
            info,
            comment -> "The message does not match the expected message."
        );
    }
}

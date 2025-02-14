package h08;

import com.fasterxml.jackson.databind.JsonNode;
import h08.assertions.ClassReference;
import h08.assertions.Links;
import h08.mocks.FakeAirport;
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

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;

/**
 * Defines the private tests for the subtask H08.4.2.
 *
 * @author Nhan Huynh
 */
@TestForSubmission
@DisplayName("H08.4.2 | Removing a Flight")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class H08_4_2_TestsPrivate extends H08_Tests {

    /**
     * The converters used for the test cases using JSON files to read the test data.
     */
    public static final Map<String, Function<JsonNode, ?>> CONVERTERS = Map.of(
        "preAirport", JsonConverters::toAirport,
        "postAirport", JsonConverters::toAirport,
        "flight", JsonConverters::toFlight,
        "isDeparting", JsonNode::asBoolean,
        "message", JsonNode::asText
    );

    /**
     * The link to the removeFlight method of the Airport class.
     */
    private MethodLink methodLink;

    /**
     * The link to the addFlight method of the Airport class.
     */
    @BeforeAll
    protected void globalSetup() {
        methodLink = Links.getMethod(Links.getType(Airport.class), "removeFlight", String.class, boolean.class);
    }

    private TestInformation.TestInformationBuilder infoBuilder(JsonParameterSet parameters) {
        Airport preAirport = parameters.get("preAirport");
        String flightNumber = parameters.get("flightNumber");
        boolean isDeparting = parameters.get("isDeparting");
        return TestInformation.builder()
            .subject(methodLink)
            .input(builder -> builder
                .add("airport", preAirport)
                .add("flightNumber", flightNumber)
                .add("isDeparting", isDeparting));
    }

    @DisplayName("Die Methode removeFlight entfernt Flüge korrekt aus den Listen.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_4_2_testRemoveFlight.json", customConverters = CUSTOM_CONVERTERS)
    void testRemoveFlight(JsonParameterSet parameters) throws Throwable {
        FakeAirport preAirport = (FakeAirport) parameters.<Airport>get("preAirport");
        FakeAirport postAirport = (FakeAirport) parameters.<Airport>get("postAirport");
        String flightNumber = parameters.get("flightNumber");
        boolean isDeparting = parameters.get("isDeparting");

        TestInformation info = infoBuilder(parameters).expect(builder -> builder
                .cause(null)
                .add("airport", postAirport))
            .build();
        Assertions2.call(
            () -> preAirport.removeFlight(flightNumber, isDeparting),
            info,
            comment -> "Valid depature/destination should not cause an exception."
        );
        Comparator<Flight> cmp = Comparator.nullsLast(Comparator.comparing(Flight::getFlightNumber));
        Arrays.sort(preAirport.getDepartingFlights(), cmp);
        Arrays.sort(preAirport.getArrivingFlights(), cmp);
        Arrays.sort(postAirport.getDepartingFlights(), cmp);
        Arrays.sort(postAirport.getArrivingFlights(), cmp);
        Assertions2.assertEquals(
            postAirport,
            preAirport,
            info,
            comment -> "The airport should be updated correctly after adding the flight."
        );
    }

    @DisplayName("Die Methode wirft korrekt eine FlightNotFoundException, wenn der Flug nicht gefunden wird.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_4_2_testRemoveFlightException.json", customConverters = CUSTOM_CONVERTERS)
    @SuppressWarnings("unchecked")
    void testRemoveFlightException(JsonParameterSet parameters) throws Throwable {
        Airport preAirport = parameters.get("preAirport");
        Airport postAirport = parameters.get("postAirport");
        String flightNumber = parameters.get("flightNumber");
        boolean isDeparting = parameters.get("isDeparting");

        TestInformation info = infoBuilder(parameters)
            .expect(builder -> builder.cause(IllegalAccessError.class))
            .build();
        ClassReference reference = ClassReference.FLIGHT_NOT_FOUND_EXCEPTION;
        Class<? extends Exception> exceptionClass = (Class<? extends Exception>) reference.getLink().reflection();
        Throwable throwable = Assertions2.assertThrows(
            exceptionClass,
            () -> preAirport.removeFlight(flightNumber, isDeparting),
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
        Assertions2.assertEquals(
            postAirport,
            preAirport,
            info,
            comment -> "The airport should not be updated when an exception occurs."
        );
    }
}

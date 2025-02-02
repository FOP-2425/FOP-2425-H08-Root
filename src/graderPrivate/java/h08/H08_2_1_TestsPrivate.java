package h08;

import com.fasterxml.jackson.databind.JsonNode;
import h08.rubric.TestInformation;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
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

import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Function;

/**
 * Defines the private tests for the subtask H08.2.1.
 *
 * @author Nhan Huynh
 */
@TestForSubmission
@DisplayName("H8.2.1 | Let’s get in shape.")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class H08_2_1_TestsPrivate extends H08_Tests {

    /**
     * The converters used for the test cases using JSON files to read the test data.
     */
    public static final Map<String, Function<JsonNode, ?>> CONVERTERS = Map.of(
        "flightNumber", node -> node.asText(),
        "departure", node -> node.isNull() ? null : node.asText(),
        "destination", node -> node.isNull() ? null : node.asText(),
        "departureTime", node -> node.isNull() ? null : JsonConverters.toLocalDateTime(node),
        "initialSeats", JsonNode::asInt,
        "availableSeats", JsonNode::asInt,
        "expectedException", JsonNode::asBoolean,
        "exceptionCause", node -> node.isNull() ? "No cause" : node.asText()
    );

    /**
     * The flight class.
     */
    private TypeLink flight;

    /**
     * The flight number field of the Flight class.
     */
    private FieldLink flightNumber;

    /**
     * The departure field of the Flight class.
     */
    private FieldLink departure;

    /**
     * The destination field of the Flight class.
     */
    private FieldLink destination;

    /**
     * The departure time field of the Flight class.
     */
    private FieldLink departureTime;

    /**
     * The initial seats field of the Flight class.
     */
    private FieldLink initialSeats;

    /**
     * The available seats field of the Flight class.
     */
    private FieldLink availableSeats;

    /**
     * Sets up the global test environment.
     */
    @BeforeAll
    protected void globalSetup() {
        super.globalSetup();
        flight = Links.getType(Flight.class);
        flightNumber = Links.getField(flight, "flightNumber");
        departure = Links.getField(flight, "departure");
        destination = Links.getField(flight, "destination");
        departureTime = Links.getField(flight, "departureTime");
        initialSeats = Links.getField(flight, "initialSeats");
        availableSeats = Links.getField(flight, "availableSeats");
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_2_1_testValidateFlightNumber.json", customConverters = CUSTOM_CONVERTERS)
    void testValidateFlightNumber(JsonParameterSet parameters) throws Throwable {
        // Test setup
        String flightNumber = parameters.get("flightNumber");
        boolean expectedException = parameters.getBoolean("expectedException");
        String exceptionCause = parameters.get("exceptionCause");
        Flight flight = Mockito.mock(Flight.class);
        MethodLink validateFlightNumber = Links.getMethod(this.flight, "validateFlightNumber", String.class);

        // Test verification
        TestInformation info = TestInformation.builder()
            .subject(validateFlightNumber)
            .preState(TestInformation.builder().add("flight", flight).build())
            .postState(TestInformation.builder().add("Exception cause", exceptionCause).build())
            .build();

        if (expectedException) {
            @Nullable Throwable throwable = null;
            try {
                validateFlightNumber.invoke(flight, flightNumber);
            } catch (AssertionError e) {
                throwable = e;
            }
            Assertions2.assertNotNull(throwable, info, comment -> "Invalid flight number");
        } else {
            Assertions2.call(() -> validateFlightNumber.invoke(flight, flightNumber), info,
                comment -> "Valid flight number");
        }
    }

    @DisplayName("Der Konstruktor der Klasse Flight enthält assert-Anweisungen, die die Eingaben überprüfen.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_2_1_testFlightConstructor.json", customConverters = CUSTOM_CONVERTERS)
    void testFlightConstructor(JsonParameterSet parameters) {
        // Test setup
        String flightNumber = parameters.get("flightNumber");
        String departure = parameters.get("departure");
        String destination = parameters.get("destination");
        LocalDateTime departureTime = parameters.get("departureTime");
        int initialSeats = parameters.getInt("initialSeats");
        boolean expectedException = parameters.getBoolean("expectedException");
        String exceptionCause = parameters.get("exceptionCause");

        // Test verification
        TestInformation info = TestInformation.builder()
            .subject(flight)
            .preState(
                TestInformation.builder()
                    .add("flightNumber", flightNumber)
                    .add("departure", departure)
                    .add("destination", destination)
                    .add("departureTime", departureTime)
                    .add("initialSeats", initialSeats)
                    .build()
            ).postState(TestInformation.builder().add("Exception cause", exceptionCause).build())
            .build();

        if (expectedException) {
            @Nullable Throwable throwable = null;
            try {
                Flight instance = new Flight(flightNumber, departure, destination, departureTime, initialSeats);
            } catch (AssertionError e) {
                throwable = e;
            }
            Assertions2.assertNotNull(throwable, info, comment -> "The constructor should throw an error for "
                + exceptionCause + ".");
        } else {
            Assertions2.call(() -> new Flight(flightNumber, departure, destination, departureTime, initialSeats), info,
                comment -> "The constructor should not throw an exception if the inputs are correct.");
        }
    }
}

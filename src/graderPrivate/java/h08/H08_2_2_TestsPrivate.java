package h08;

import com.fasterxml.jackson.databind.JsonNode;
import h08.rubric.context.TestInformation;
import org.jetbrains.annotations.Nullable;
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
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.Map;
import java.util.function.Function;

/**
 * Defines the private tests for the subtask H08.2.2.
 *
 * @author Nhan Huynh
 */
@TestForSubmission
@DisplayName("H08.2.2 | Fasten your seatbelts")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class H08_2_2_TestsPrivate extends H08_Tests {

    /**
     * The converters used for the test cases using JSON files to read the test data.
     */
    public static final Map<String, Function<JsonNode, ?>> CONVERTERS = Map.of(
        "flightNumber", JsonNode::asText,
        "initialSeats", JsonNode::asInt,
        "availableSeats", JsonNode::asInt,
        "availableSeatsAfterBooking", JsonNode::asInt,
        "message", JsonNode::asText
    );

    /**
     * The link to the flight class.
     */
    private TypeLink flightLink;

    /**
     * The link to the flight number field of the Flight class.
     */
    private FieldLink flightNumberLink;

    /**
     * The link to the initial seats field of the Flight class.
     */
    private FieldLink initialSeatsLink;

    /**
     * The link to the available seats field of the Flight class.
     */
    private FieldLink availableSeatsLink;

    /**
     * The flight instance used for testing.
     */
    private @Nullable Flight instance;

    /**
     * The builder for the test information.
     */
    private @Nullable TestInformation.TestInformationBuilder builder;

    /**
     * Sets up the global test environment.
     */
    @BeforeAll
    protected void globalSetup() {
        super.globalSetup();
        flightLink = Links.getType(Flight.class);
        flightNumberLink = Links.getField(flightLink, "flightNumber");
        initialSeatsLink = Links.getField(flightLink, "initialSeats");
        availableSeatsLink = Links.getField(flightLink, "availableSeats");
    }

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setup() {
        instance = null;
        builder = null;
    }

    /**
     * Initializes the test with the given parameters.
     *
     * @param parameters the parameters to initialize the test with
     */
    private void initTest(JsonParameterSet parameters) {
        instance = Mockito.mock(Flight.class, Mockito.CALLS_REAL_METHODS);
        String flightNumber = parameters.get("flightNumber");
        int initialSeats = parameters.getInt("initialSeats");
        int availableSeats = parameters.getInt("availableSeats");

        flightNumberLink.set(instance, flightNumber);
        initialSeatsLink.set(instance, initialSeats);
        availableSeatsLink.set(instance, availableSeats);

        MethodLink methodLink = Links.getMethod(flightLink, "bookSeat", new Class[0]);
        builder = TestInformation.builder()
            .subject(methodLink)
            .input(builder -> builder
                .add("initialSeats", initialSeats)
                .add("availableSeats", availableSeats)
            );
    }

    @DisplayName("Die Methode bookSeat() reserviert korrekt Sitzplätze.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_2_2_testBookSeat.json", customConverters = CUSTOM_CONVERTERS)
    void testBookSeat(JsonParameterSet parameters) {
        initTest(parameters);
        assert builder != null;
        int availableSeatsAfterBooking = parameters.getInt("availableSeatsAfterBooking");
        builder.expect(builder -> builder
            .add("availableSeats", availableSeatsAfterBooking)
            .cause(null)
        );
        assert instance != null;
        Assertions2.call(() -> instance.bookSeat(), builder.build(), comment -> "Seats are available!");
    }

    @DisplayName("Die Methode wirft korrekt eine NoSeatsAvailableException, wenn keine Plätze mehr verfügbar sind.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_2_2_testBookSeatNoSeatsAvailableException.json", customConverters = CUSTOM_CONVERTERS)
    @SuppressWarnings("unchecked")
    void testBookSeatNoSeatsAvailableException(JsonParameterSet parameters) {
        initTest(parameters);
        assert builder != null;
        TypeLink exception = Links.getType("h08.Exceptions", "NoSeatsAvailableException");
        Class<? extends Exception> exceptionClass = (Class<? extends Exception>) exception.reflection();
        builder.expect(builder -> builder.cause(exceptionClass));
        TestInformation info = builder.build();
        assert instance != null;
        Exception e = Assertions2.assertThrows(
            exceptionClass,
            () -> instance.bookSeat(),
            info,
            comment -> "No seats available"
        );
        String message = parameters.get("message");
        Assertions2.assertEquals(message, e.getMessage(), info, comment -> "Invalid exception message!");
    }
}

package h08;

import com.fasterxml.jackson.databind.JsonNode;
import h08.assertions.Links;
import h08.rubric.context.TestInformation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mockito;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.ConstructorLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

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
        "flightNumber", node -> node.isNull() ? null : node.asText(),
        "departure", node -> node.isNull() ? null : node.asText(),
        "destination", node -> node.isNull() ? null : node.asText(),
        "departureTime", node -> node.isNull() ? null : JsonConverters.toLocalDateTime(node),
        "initialSeats", JsonNode::asInt,
        "availableSeats", JsonNode::asInt,
        "message", JsonNode::asText
    );

    /**
     * The link to the flight class.
     */
    private TypeLink typeLink;

    /**
     * Sets up the global test environment.
     */
    @BeforeAll
    protected void globalSetup() {
        super.globalSetup();
        typeLink = Links.getType(Flight.class);
    }

    @DisplayName("Die Methode validateFlightNumber wirft keinen Fehler für valide Eingaben.")
    @Test
    void testValidateFlightNumber() throws Throwable {
        String[] flightNumbers = {"LH123", "LH1234"};
        Flight flight = Mockito.mock(Flight.class, Mockito.CALLS_REAL_METHODS);
        MethodLink methodLink = Links.getMethod(this.typeLink, "validateFlightNumber", String.class);

        for (String flightNumber : flightNumbers) {
            TestInformation info = TestInformation.builder()
                .subject(methodLink)
                .input(builder -> builder.add("flightNumber", flightNumber))
                .expect(builder -> builder.cause(null))
                .build();

            Assertions2.call(
                () -> methodLink.invoke(flight, flightNumber),
                info,
                comment -> "Valid flight number should not throw an exception");
        }
    }

    @DisplayName("Die Methode validateFlightNumber wirft einen Fehler für invalide Eingaben.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_2_1_testValidateFlightNumberException.json", customConverters = CUSTOM_CONVERTERS)
    void testValidateFlightNumberException(JsonParameterSet parameters) throws Throwable {
        String flightNumber = parameters.get("flightNumber");
        Flight flight = Mockito.mock(Flight.class, Mockito.CALLS_REAL_METHODS);
        MethodLink methodLink = Links.getMethod(this.typeLink, "validateFlightNumber", String.class);

        TestInformation info = TestInformation.builder()
            .subject(methodLink)
            .input(builder -> builder.add("flightNumber", flightNumber))
            .expect(builder -> builder.cause(AssertionError.class))
            .build();

        assertAssertions(() -> methodLink.invoke(flight, flightNumber), info);
    }

    @DisplayName("Der Konstruktor der Klasse Flight enthält führt keine assert-Anweisungen aus.")
    @Test
    void testFlightConstructor() throws Throwable {
        String flightNumber = "LH1234";
        String departure = "FRA";
        String destination = "ICN";
        LocalDateTime departureTime = LocalDateTime.of(2025, 2, 7, 13, 0);
        int initialSeats = 0;

        List<TypeLink> parameterTypes = Stream.of(String.class, String.class, String.class, LocalDateTime.class, int.class)
            .<TypeLink>map(BasicTypeLink::of)
            .toList();
        ConstructorLink constructor = typeLink.getConstructor(Matcher.of(m -> m.typeList().equals(parameterTypes)));

        TestInformation info = TestInformation.builder()
            .subject(constructor)
            .input(builder -> builder
                .add("flightNumber", flightNumber)
                .add("departure", departure)
                .add("destination", destination)
                .add("departureTime", departureTime)
                .add("initialSeats", initialSeats)
            ).expect(builder -> builder.cause(null))
            .build();
        Assertions2.call(
            () -> new Flight(flightNumber, departure, destination, departureTime, initialSeats),
            info,
            comment -> "Constructor should not throw an exception for valid input."
        );
    }

    @DisplayName("Der Konstruktor der Klasse Flight enthält assert-Anweisungen, die die Eingaben überprüfen.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_2_1_testFlightConstructor.json", customConverters = CUSTOM_CONVERTERS)
    void testFlightConstructorException(JsonParameterSet parameters) throws Throwable {
        String flightNumber = parameters.get("flightNumber");
        String departure = parameters.get("departure");
        String destination = parameters.get("destination");
        LocalDateTime departureTime = parameters.get("departureTime");
        int initialSeats = parameters.getInt("initialSeats");

        List<TypeLink> parameterTypes = Stream.of(String.class, String.class, String.class, LocalDateTime.class, int.class)
            .<TypeLink>map(BasicTypeLink::of)
            .toList();
        ConstructorLink constructor = typeLink.getConstructor(Matcher.of(m -> m.typeList().equals(parameterTypes)));

        TestInformation info = TestInformation.builder()
            .subject(constructor)
            .input(builder -> builder
                .add("flightNumber", flightNumber)
                .add("departure", departure)
                .add("destination", destination)
                .add("departureTime", departureTime)
                .add("initialSeats", initialSeats)
            ).expect(builder -> builder.cause(AssertionError.class))
            .build();

        assertAssertions(() -> new Flight(flightNumber, departure, destination, departureTime, initialSeats), info);
    }
}

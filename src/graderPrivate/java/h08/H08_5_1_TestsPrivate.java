package h08;

import com.fasterxml.jackson.databind.JsonNode;
import h08.mock.MockFlight;
import h08.rubric.context.TestInformation;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterAll;
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
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * Defines the private tests for the subtask H08.5.1.
 *
 * @author Nhan Huynh
 */
@TestForSubmission
@DisplayName("H8.5.1 | Airport and Flight Lookup")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class H08_5_1_TestsPrivate extends H08_Tests {

    /**
     * The converters used for the test cases using JSON files to read the test data.
     */
    public static final Map<String, Function<JsonNode, ?>> CONVERTERS = Map.of(
        "flightManagement", JsonConverters::toFlightManagement,
        "airportCode", JsonNode::asText,
        "airport", node -> node.isNull() ? null : JsonConverters.toAirport(node),
        "flightNumber", JsonNode::asText,
        "flight", node -> node.isNull() ? null : JsonConverters.toFlight(node),
        "message", node -> node.isNull() ? null : node.asText()
    );

    /**
     * Store the old standard output stream to restore it after the tests.
     */
    private PrintStream out;

    /**
     * The test class to test the methods.
     */
    private TypeLink type;

    /**
     * Sets up the global test environment.
     */
    @BeforeAll
    protected void globalSetup() {
        type = Links.getType(FlightManagement.class);
        out = System.out;
    }

    /**
     * Tears down the global test environment.
     */
    @AfterAll
    void globalTearDown() {
        System.setOut(out);
    }


    @DisplayName("Die Methode searchAirport findet Flughäfen korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_5_1_testSearchAirport.json", customConverters = CUSTOM_CONVERTERS)
    void testSearchAirport(JsonParameterSet parameters) {
        MethodLink method = Links.getMethod(type, "searchAirport", String.class);
        FlightManagement management = parameters.get("flightManagement");
        String airportCode = parameters.get("airportCode");
        Airport airport = parameters.get("airport");
        @Nullable String message = parameters.get("message");
        TestInformation info = TestInformation.builder()
            .subject(method)
            .input(builder -> builder
                .add("Flight Management", management)
                .add("airportCode", airportCode)
            )
            .expect(builder -> {
                    builder.cause(message == null ? null : Exception.class);
                    if (message != null) {
                        builder.add("Result", airport);
                    }
                    return builder;
                }
            ).build();

        if (message == null) {
            AtomicReference<Airport> result = new AtomicReference<>();
            Assertions2.call(() -> result.set(method.invoke(management, airportCode)),
                info,
                comment -> "Unexpected exception occurred while searching for the airport!"
            );
            Assertions2.assertEquals(
                airport,
                result.get(),
                info,
                comment -> "Wrong search result!"
            );
        } else {
            Exception exception = Assertions2.assertThrows(
                Exception.class,
                () -> method.invoke(management, airportCode),
                info,
                comment -> "Expected exception to be thrown!"
            );
            Assertions2.assertEquals(message, exception.getMessage(), info, comment -> "Wrong exception message!");
        }
    }

    @DisplayName("Die Methode searchFlight durchsucht Flüge korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_5_1_testSearchFlight.json", customConverters = CUSTOM_CONVERTERS)
    void testSearchFlight(JsonParameterSet parameters) {
        MethodLink method = Links.getMethod(type, "searchFlight", Airport.class, String.class);
        FlightManagement management = parameters.get("flightManagement");
        Airport airport = parameters.get("airport");
        String flightNumber = parameters.get("flightNumber");
        Flight flight = parameters.get("flight");
        TestInformation info = TestInformation.builder()
            .subject(method)
            .input(builder -> builder
                .add("Flight Management", management)
                .add("airport", airport)
                .add("flightNumber", flightNumber)
            )
            .expect(builder -> builder
                .cause(null)
                .add("Result", String.valueOf(flight))
            ).build();
        AtomicReference<Flight> result = new AtomicReference<>();
        Assertions2.call(() -> result.set(method.invoke(management, airport, flightNumber)),
            info,
            comment -> "Unexpected exception occurred while searching for the airport!"
        );
        Assertions2.assertTrue(MockFlight.equals(flight, result.get()), info, comment -> "Wrong search result!");
    }

    @DisplayName("Die Methode getFlight gibt Flüge korrekt zurück.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_5_1_testGetFlight.json", customConverters = CUSTOM_CONVERTERS)
    void testGetFlight(JsonParameterSet parameters) {
        MethodLink method = Links.getMethod(type, "getFlight", String.class);
        FlightManagement management = parameters.get("flightManagement");
        String flightNumber = parameters.get("flightNumber");
        Flight flight = parameters.get("flight");
        @Nullable String message = parameters.get("message");
        TestInformation info = TestInformation.builder()
            .subject(method)
            .input(builder -> builder
                .add("Flight Management", management)
                .add("flightNumber", flightNumber)
            )
            .expect(builder -> builder
                .cause(null)
                .add("Result", flight)
            ).build();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        AtomicReference<Flight> result = new AtomicReference<>();
        Assertions2.call(() -> result.set(method.invoke(management, flightNumber)),
            info,
            comment -> "Unexpected exception occurred while searching for the airport!"
        );
        Assertions2.assertTrue(MockFlight.equals(flight, result.get()), info, comment -> "Wrong search result!");

        if (message != null) {
            Assertions2.assertEquals(message, baos.toString().trim(), info, comment -> "Wrong exception message!");
        }
    }
}

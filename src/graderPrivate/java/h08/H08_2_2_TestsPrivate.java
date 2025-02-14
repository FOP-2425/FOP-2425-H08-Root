package h08;

import com.fasterxml.jackson.databind.JsonNode;
import h08.assertions.ClassReference;
import h08.assertions.Mocks;
import h08.rubric.context.TestInformation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.ConstructorLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.time.LocalDateTime;
import java.util.List;
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
        "preFlight", JsonConverters::toFlight,
        "postFlight", JsonConverters::toFlight
    );

    @DisplayName("Die NoSeatsAvailableException ist korrekt implementiert.")
    @Test
    void testNoSeatsAvailableException() throws Throwable {
        ClassReference reference = ClassReference.NO_SEATS_AVAILABLE_EXCEPTION;
        assertExceptionDefinedCorrectly(reference);
        TypeLink typeLink = reference.getLink();
        List<TypeLink> constructorParameterTypes = List.of(BasicTypeLink.of(String.class));
        ConstructorLink constructorLink = typeLink.getConstructor(
            Matcher.of(c -> c.typeList().equals(constructorParameterTypes))
        );
        String flightNumber = "YF0802";
        Throwable instance = constructorLink.invoke(flightNumber);
        String actualMessage = instance.getMessage();
        String expectedMessage = "No seats available for flight: YF0802";
        TestInformation info = TestInformation.builder()
            .subject(typeLink)
            .input(builder -> builder.add("flightNumber", flightNumber))
            .expect(builder -> builder.add("getMessage()", expectedMessage))
            .actual(builder -> builder.add("getMessage()", actualMessage))
            .build();
        Assertions2.assertEquals(
            expectedMessage,
            actualMessage,
            info,
            comment -> "Message does not match expected message."
        );
    }

    @DisplayName("Die Methode bookSeat() reserviert korrekt Sitzplätze.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_2_2_testBookSeat.json", customConverters = CUSTOM_CONVERTERS)
    void testBookSeat(JsonParameterSet parameters) throws Throwable {
        Flight preFlight = parameters.get("preFlight");
        Flight postFlight = parameters.get("postFlight");
        TestInformation.TestInformationBuilder infoBuilder = TestInformation.builder()
            .input(builder -> builder.add("flight", preFlight))
            .expect(builder -> builder.add("flight", postFlight));
        preFlight.bookSeat();
        TestInformation info = infoBuilder
            .actual(builder -> builder.add("flight", postFlight))
            .build();
        Assertions2.assertTrue(
            TutorUtils.equalFlight(postFlight, preFlight),
            info,
            comment -> "Flight does not match expected flight."
        );
    }

    @DisplayName("Die Methode bookSeat() reserviert korrekt Sitzplätze.")
    @Test
    @SuppressWarnings("unchecked")
    void testBookSeatException() throws Throwable {
        Flight flight = Mocks.createFlight(
            "LH712",
            "FRA",
            "ICN",
            LocalDateTime.of(2025, 2, 7, 13, 0),
            100,
            0
        );
        ClassReference reference = ClassReference.NO_SEATS_AVAILABLE_EXCEPTION;
        reference.assertDefined();
        Class<? extends Exception> exceptionClass = (Class<? extends Exception>) reference.getLink().reflection();
        TestInformation info = TestInformation.builder()
            .input(builder -> builder.add("flight", flight))
            .expect(builder -> builder.cause(exceptionClass))
            .build();
        Assertions2.assertThrows(
            exceptionClass,
            flight::bookSeat,
            info,
            comment -> "%s should be thrown.".formatted(exceptionClass.getSimpleName())
        );
    }
}

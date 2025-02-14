package h08;

import com.fasterxml.jackson.databind.JsonNode;
import h08.assertions.Links;
import h08.rubric.context.TestInformation;
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

import java.time.LocalDate;
import java.util.Map;
import java.util.function.Function;

/**
 * Defines the private tests for the task H08.1.
 *
 * @author Nhan Huynh
 */
@TestForSubmission
@DisplayName("H08.1 | Have your ID ready")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class H08_1_TestsPrivate extends H08_Tests {

    /**
     * The converters used for the test cases using JSON files to read the test data.
     */
    public static final Map<String, Function<JsonNode, ?>> CONVERTERS = Map.of(
        "firstName", JsonNode::asText,
        "lastName", JsonNode::asText,
        "dateOfBirth", JsonConverters::toLocalDate,
        "expectedInitials", JsonNode::asText,
        "expectedHash", JsonNode::asText
    );

    /**
     * The link to the passenger ID field of the Passenger class.
     */
    private FieldLink passengerIDLink;

    /**
     * The link to the first name field of the Passenger class.
     */
    private FieldLink firstNameLink;

    /**
     * The link to the last name field of the Passenger class.
     */
    private FieldLink lastNameLink;

    /**
     * The link to the date of birth field of the Passenger class.
     */
    private FieldLink dateOfBirthLink;

    /**
     * The link to the generatePassengerID method of the Passenger class.
     */
    private MethodLink generatePassengerIDLink;

    /**
     * The instance of the Passenger class to test.
     */
    private Passenger instance;

    /**
     * Sets up the global test environment.
     */
    @BeforeAll
    protected void globalSetup() {
        super.globalSetup();
        TypeLink passenger = Links.getType(Passenger.class);
        passengerIDLink = Links.getField(passenger, "passengerID");
        firstNameLink = Links.getField(passenger, "firstName");
        lastNameLink = Links.getField(passenger, "lastName");
        dateOfBirthLink = Links.getField(passenger, "dateOfBirth");
        generatePassengerIDLink = Links.getMethod(passenger, "generatePassengerID", String.class, String.class, LocalDate.class);
    }

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setup() {
        instance = null;
    }

    /**
     * Returns pre-setup test information for the given parameters and instance to test the generatePassengerID method.
     *
     * @param parameters the test input and expected output parameters
     *
     * @return pre-setup test information for the given parameters and instance
     */
    private TestInformation testInformation(JsonParameterSet parameters) {
        String firstName = parameters.getString("firstName");
        String lastName = parameters.getString("lastName");
        LocalDate dateOfBirth = parameters.get("dateOfBirth");
        String passengerID = parameters.getString("passengerID");
        return TestInformation.builder()
            .subject(passengerID)
            .input(builder -> builder.add("firstName", firstName)
                .add("lastName", lastName)
                .add("dateOfBirth", dateOfBirth)
            ).expect(builder -> builder.add("passengerID", passengerID))
            .actual(builder -> builder.add("passengerID", this.passengerIDLink.get(instance)))
            .build();
    }

    /**
     * Invokes the generatePassengerID method of the Passenger class with the given parameters.
     *
     * @param parameters the test input and expected output parameters
     *
     * @return the generated passenger ID
     * @throws Throwable if an error occurs during the test execution
     */
    private String invokeMethod(JsonParameterSet parameters) throws Throwable {
        // Test setup
        String firstName = parameters.getString("firstName");
        String lastName = parameters.getString("lastName");
        LocalDate dateOfBirth = parameters.get("dateOfBirth");

        // Test execution
        instance = Mockito.mock(Passenger.class);
        this.firstNameLink.set(instance, firstName);
        this.lastNameLink.set(instance, lastName);
        this.dateOfBirthLink.set(instance, dateOfBirth);
        String result = generatePassengerIDLink.invoke(instance, firstName, lastName, dateOfBirth);
        this.passengerIDLink.set(instance, result);
        return result;
    }

    @DisplayName("Die Methode generatePassengerID stellt sicher, dass die ersten zwei Zeichen der ID die Initialen des Vornamens und Nachnamens sind.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_1_generatePassengerID.json", customConverters = CUSTOM_CONVERTERS)
    void testGeneratePassengerIDNameInitials(JsonParameterSet parameters) throws Throwable {
        // Test execution
        String actual = invokeMethod(parameters);

        // Test verification
        String expected = parameters.getString("expectedInitials");
        Assertions2.assertEquals(expected, actual.substring(0, 2), testInformation(parameters),
            comment -> "The two initials does not match the initials from the first and last name.");
    }

    @DisplayName("Die Methode generatePassengerID stellt sicher, dass der Hash-Code des Datums korrekt im zweiten Teil der ID enthalten ist.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_1_generatePassengerID.json", customConverters = CUSTOM_CONVERTERS)
    void testGeneratePassengerIDDateHash(JsonParameterSet parameters) throws Throwable {
        // Test execution
        String actual = invokeMethod(parameters);

        // Test verification
        String expected = parameters.getString("expectedHash");
        Assertions2.assertEquals(expected, actual.substring(2), testInformation(parameters),
            comment -> "Hash does not match expected hash.");
    }
}

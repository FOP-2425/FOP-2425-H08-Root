package h08;

import com.fasterxml.jackson.databind.JsonNode;
import h08.rubric.TestInformation;
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
     * The passenger ID field of the Passenger class.
     */
    private FieldLink passengerID;

    /**
     * The first name field of the Passenger class.
     */
    private FieldLink firstName;

    /**
     * The last name field of the Passenger class.
     */
    private FieldLink lastName;

    /**
     * The date of birth field of the Passenger class.
     */
    private FieldLink dateOfBirth;

    /**
     * The generatePassengerID method of the Passenger class.
     */
    private MethodLink generatePassengerID;

    /**
     * Sets up the global test environment.
     */
    @BeforeAll
    protected void globalSetup() {
        super.globalSetup();
        TypeLink passenger = Links.getType(Passenger.class);
        passengerID = Links.getField(passenger, "passengerID");
        firstName = Links.getField(passenger, "firstName");
        lastName = Links.getField(passenger, "lastName");
        dateOfBirth = Links.getField(passenger, "dateOfBirth");
        generatePassengerID = Links.getMethod(passenger, "generatePassengerID", String.class, String.class, LocalDate.class);
    }

    /**
     * Returns the test information for better test reporting containing the pre-state, post-state, and actual state
     * for the method generatePassengerID.
     *
     * @param parameters the test input and expected output parameters
     * @param instance   the instance of the Passenger class
     *
     * @return the test information for better test reporting
     */
    private TestInformation.TestInformationBuilder testInformation(JsonParameterSet parameters, Passenger instance) {
        String firstName = parameters.getString("firstName");
        String lastName = parameters.getString("lastName");
        LocalDate dateOfBirth = parameters.get("dateOfBirth");
        String passengerID = parameters.getString("passengerID");
        return TestInformation.builder()
            .subject(passengerID)
            .preState(
                TestInformation.builder()
                    .add("firstName", "Not set yet")
                    .add("lastName", "Not set yet")
                    .add("dateOfBirth", "Not set yet")
                    .add("passengerID", "Not set yet")
                    .build()
            ).postState(
                TestInformation.builder()
                    .add("firstName", firstName)
                    .add("lastName", lastName)
                    .add("dateOfBirth", dateOfBirth)
                    .add("passengerID", passengerID)
                    .build()
            ).actualState(
                TestInformation.builder()
                    .add("firstName", this.firstName.get(instance))
                    .add("lastName", this.lastName.get(instance))
                    .add("dateOfBirth", this.dateOfBirth.get(instance))
                    .add("passengerID", this.passengerID.get(instance))
                    .build()
            );
    }

    /**
     * Invokes the generatePassengerID method of the Passenger class with the given parameters.
     *
     * @param parameters the test input and expected output parameters
     *
     * @return the passenger instance and the generated passenger ID
     * @throws Throwable if an error occurs during the test execution
     */
    private Map.Entry<Passenger, String> invokeGeneratePassengerID(JsonParameterSet parameters) throws Throwable {
        // Test setup
        String firstName = parameters.getString("firstName");
        String lastName = parameters.getString("lastName");
        LocalDate dateOfBirth = parameters.get("dateOfBirth");

        // Test execution
        Passenger passenger = Mockito.mock(Passenger.class);
        this.firstName.set(passenger, firstName);
        this.lastName.set(passenger, lastName);
        this.dateOfBirth.set(passenger, dateOfBirth);
        String actual = generatePassengerID.invoke(passenger, firstName, lastName, dateOfBirth);
        this.passengerID.set(passenger, actual);
        return Map.entry(passenger, actual);
    }

    @DisplayName("Die Methode generatePassengerID stellt sicher, dass die ersten zwei Zeichen der ID die Initialen des Vornamens und Nachnamens sind.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_1_generatePassengerID.json", customConverters = CUSTOM_CONVERTERS)
    void testGeneratePassengerIDNameInitials(JsonParameterSet parameters) throws Throwable {
        // Test execution
        Map.Entry<Passenger, String> result = invokeGeneratePassengerID(parameters);
        Passenger passenger = result.getKey();
        String actual = result.getValue();

        // Test verification
        TestInformation info = testInformation(parameters, passenger).build();
        String expected = parameters.getString("expectedInitials");
        Assertions2.assertEquals(expected, actual.substring(0, 2), info,
            comment -> "The two first characters of the passenger ID should be the initials of the first and last name.");
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "H08_1_generatePassengerID.json", customConverters = CUSTOM_CONVERTERS)
    void testGeneratePassengerIDDateHash(JsonParameterSet parameters) throws Throwable {
        // Test execution
        Map.Entry<Passenger, String> result = invokeGeneratePassengerID(parameters);
        Passenger passenger = result.getKey();
        String actual = result.getValue();

        // Test verification
        TestInformation info = testInformation(parameters, passenger).build();
        String expected = parameters.getString("expectedHash");
        Assertions2.assertEquals(expected, actual.substring(2), info,
            comment -> "The rest of the passenger ID should be the hash code of the date of birth.");
    }
}

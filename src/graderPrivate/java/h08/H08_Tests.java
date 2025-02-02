package h08;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;

/**
 * Defines a test skeleton for the H08 assignment.
 *
 * <p>Use the following schema:
 * <pre>{@code
 *     public class TestClass extends H08_Test {
 *
 *          public static final Map<String, Function<JsonNode, ?>> CUSTOM_CONVERTERS = Map.of(
 *              ...
 *          );
 *
 *          @ParameterizedTest
 *          @JsonParameterSetTest(value = "path-to-json-data.json", customConverters = CUSTOM_CONVERTERS)
 *          void testXYZ(JsonParameterSet parameters) {
 *              ...
 *          }
 *   }
 * }</pre>
 *
 * @author Nhan Huynh
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestForSubmission
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public abstract class H08_Tests {

    /**
     * The attribute name for custom converters in the JSON parameter set test annotation.
     */
    public static final String CUSTOM_CONVERTERS = "CONVERTERS";

    @BeforeAll
    protected void globalSetup() {
        Assertions.assertNotNull(
            getClass().getAnnotation(TestForSubmission.class),
            "The test class is not annotated with @TestForSubmission which is needed for Jagr to work"
        );
    }
}

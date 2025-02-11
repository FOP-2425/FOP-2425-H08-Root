package h08;

import h08.assertions.ClassReference;
import h08.rubric.context.TestInformation;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.opentest4j.AssertionFailedError;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.callable.Callable;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.ConstructorLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.List;

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

    /**
     * Asserts that the given exception class reference is defined correctly. This method checks if the class reference
     * is correctly defined and if a constructor with a single {@link String} parameter is defined.
     *
     * @param reference the exception class reference to check
     */
    public void assertExceptionDefinedCorrectly(ClassReference reference) {
        reference.assertCorrectlyDefined();
        List<TypeLink> constructorParameterTypes = List.of(BasicTypeLink.of(String.class));
        TypeLink typeLink = reference.getLink();
        ConstructorLink constructorLink = typeLink.getConstructor(
            Matcher.of(c -> c.typeList().equals(constructorParameterTypes))
        );
        TestInformation info = TestInformation.builder()
            .subject(typeLink)
            .expect(builder -> builder
                .add("Constructor", "Constructor with parameters %s".formatted(constructorParameterTypes)))
            .actual(builder -> builder.add("Constructor", constructorLink))
            .build();
        Assertions2.assertNotNull(
            constructorLink,
            info,
            comment -> "Could not find constructor with parameters %s".formatted(constructorParameterTypes)
        );
    }

    /**
     * Asserts that the given callable throws an assertion error.
     *
     * @param callable the callable to call and check
     * @param context  the context information about the test case
     *
     * @throws AssertionFailedError if the callable does not throw an assertion error
     */
    @SuppressWarnings("ConstantConditions")
    public void assertAssertions(Callable callable, Context context) {
        @Nullable Throwable throwable = null;
        try {
            callable.call();
        } catch (AssertionError e) {
            throwable = e;
        } catch (Throwable e) {
            Assertions2.fail(context, comment -> "Unexpected exception thrown: %s".formatted(e));
        }

        Assertions2.assertNotNull(throwable, context, comment -> "Expected an assertion to be thrown, but none was thrown.");
        Assertions2.assertEquals(
            AssertionError.class,
            throwable.getClass(),
            context,
            comment -> "Exception type does not match AssertionError.");
    }
}

package h08.rubric;

import org.jetbrains.annotations.Nullable;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.assertions.Property;

import java.util.Collection;
import java.util.function.Function;

/**
 * Test information provides more context to the test results by adding information about the input, expected output
 * and actual output of the test.
 *
 * @author Nhan Huynh
 */
public class TestInformation implements Context {

    /**
     * The subject of the test.
     */
    private final Object subject;

    /**
     * The properties of the test.
     */
    private final Collection<Property> properties;

    /**
     * Constructs new test information with the given subject and properties.
     *
     * @param subject the subject of the test
     * @param require the input of the test
     * @param ensure  the expected output of the test
     * @param actual  the actual output of the test
     */
    public TestInformation(
            Object subject,
            @Nullable SubTestInformation require,
            @Nullable SubTestInformation ensure,
            @Nullable SubTestInformation actual) {
        Context.Builder<?> builder = Assertions2.contextBuilder()
                .subject(subject);

        if (require != null) {
            addState(builder, "Input", require);
        }
        if (ensure != null) {
            addState(builder, "Expected output", ensure);
        }
        if (actual != null) {
            addState(builder, "Actual output", actual);
        }
        Context context = builder.build();
        this.subject = context.subject();
        this.properties = context.properties();
    }

    /**
     * Creates a new test information builder.
     *
     * @return a new test information builder
     */
    public static TestInformationBuilder builder() {
        return new TestInformationBuilderImpl();
    }

    /**
     * Adds the given state to the builder with the given name.
     *
     * @param builder the builder to add the state to
     * @param name    the name of the state
     * @param state   the state to add
     */
    private void addState(Builder<?> builder, String name, SubTestInformation state) {
        if (state != null) {
            builder.add(
                    name,
                    Assertions2.contextBuilder()
                            .add(state.properties().toArray(Property[]::new))
                            .build()
            );
        }
    }

    @Override
    public Object subject() {
        return subject;
    }

    @Override
    public Collection<Property> properties() {
        return properties;
    }

    /**
     * A builder for {@link TestInformation}.
     */
    public interface TestInformationBuilder extends h08.rubric.Builder<TestInformation> {

        /**
         * Sets the subject of the test.
         *
         * @param subject the subject of the test
         *
         * @return this builder instance after setting the subject
         */
        TestInformationBuilder subject(Object subject);

        /**
         * Sets the input of the test.
         *
         * @param input the input of the test
         *
         * @return this builder instance after setting the input
         */
        TestInformationBuilder input(SubTestInformation input);

        /**
         * Sets the input of the test.
         *
         * @param input the input of the test
         *
         * @return this builder instance after setting the input
         */
        TestInformationBuilder input(
                Function<SubTestInformation.InputTestInformationBuilder, SubTestInformation.InputTestInformationBuilder> input
        );

        /**
         * Sets the expected output of the test.
         *
         * @param expect the expected output of the test
         *
         * @return this builder instance after setting the expected output
         */
        TestInformationBuilder expect(SubTestInformation expect);

        /**
         * Sets the expected output of the test.
         *
         * @param expect the expected output of the test
         *
         * @return this builder instance after setting the expected output
         */
        TestInformationBuilder expect(
                Function<SubTestInformation.OutputTestInformationBuilder, SubTestInformation.OutputTestInformationBuilder> expect
        );

        /**
         * Sets the actual output of the test.
         *
         * @param actual the actual output of the test
         *
         * @return this builder instance after setting the actual output
         */
        TestInformationBuilder actual(SubTestInformation actual);

        /**
         * Sets the actual output of the test.
         *
         * @param actual the actual output of the test
         *
         * @return this builder instance after setting the actual output
         */
        TestInformationBuilder actual(
                Function<SubTestInformation.OutputTestInformationBuilder, SubTestInformation.OutputTestInformationBuilder> actual
        );
    }

    /**
     * An implementation of {@link TestInformationBuilder}.
     */
    private static class TestInformationBuilderImpl implements TestInformationBuilder {

        /**
         * The subject of the test.
         */
        private @Nullable Object subject;

        /**
         * The input of the test.
         */
        private @Nullable SubTestInformation input;

        /**
         * The expected output of the test.
         */
        private @Nullable SubTestInformation expect;

        /**
         * The actual output of the test.
         */
        private @Nullable SubTestInformation actual;

        @Override
        public TestInformationBuilder subject(Object subject) {
            this.subject = subject;
            return this;
        }

        @Override
        public TestInformationBuilder input(SubTestInformation input) {
            this.input = input;
            return this;
        }

        @Override
        public TestInformationBuilder input(
                Function<SubTestInformation.InputTestInformationBuilder, SubTestInformation.InputTestInformationBuilder> input
        ) {
            return input(input.apply(new SubTestInformation.InputTestInformationBuilderImpl()).build());
        }

        @Override
        public TestInformationBuilder expect(SubTestInformation expect) {
            this.expect = expect;
            return this;
        }

        @Override
        public TestInformationBuilder expect(
                Function<SubTestInformation.OutputTestInformationBuilder, SubTestInformation.OutputTestInformationBuilder> expect
        ) {
            return expect(expect.apply(new SubTestInformation.OutputTestInformationBuilderImpl()).build());
        }

        @Override
        public TestInformationBuilder actual(SubTestInformation actual) {
            this.actual = actual;
            return this;
        }

        @Override
        public TestInformationBuilder actual(
                Function<SubTestInformation.OutputTestInformationBuilder, SubTestInformation.OutputTestInformationBuilder> actual
        ) {
            return actual(actual.apply(new SubTestInformation.OutputTestInformationBuilderImpl()).build());
        }

        @Override
        public TestInformation build() {
            return new TestInformation(subject, input, expect, actual);
        }
    }
}

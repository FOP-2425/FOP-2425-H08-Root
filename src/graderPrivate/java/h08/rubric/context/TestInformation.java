package h08.rubric.context;

import org.jetbrains.annotations.Nullable;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.assertions.Property;

import java.util.Collection;
import java.util.function.Function;

/**
 * Provides more context information to the test results by adding information about the input, expected output, and
 * actual output of the test.
 *
 * @author Nhan Huynh
 */
public class TestInformation implements Context {

    /**
     * The subject of the test case.
     */
    private final Object subject;

    /**
     * The information about the test case.
     */
    private final Collection<Property> properties;

    /**
     * Constructs new test information with the given test case and information.
     *
     * @param subject the test case of the test
     * @param require the input test case information of the test
     * @param ensure  the expected output test case information of the test
     * @param actual  the actual output test case information of the test
     */
    public TestInformation(
        Object subject,
        @Nullable SubtestInformation require,
        @Nullable SubtestInformation ensure,
        @Nullable SubtestInformation actual) {
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
     * Creates a builder for {@link TestInformation}.
     *
     * @return a builder for {@link TestInformation}
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
    private void addState(Builder<?> builder, String name, SubtestInformation state) {
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
         * Sets the test case of the test.
         *
         * @param subject the test case of the test
         *
         * @return this builder instance after setting the test case
         */
        TestInformationBuilder subject(Object subject);

        /**
         * Sets the input information of the test.
         *
         * @param input the input information of the test
         *
         * @return this builder instance after setting the input information of the test
         */
        TestInformationBuilder input(SubtestInformation input);

        /**
         * Sets the input information of the test.
         *
         * @param builder the builder for creating the input information of the test
         *
         * @return this builder instance after setting the input information of the test
         */
        TestInformationBuilder input(Function<InputTestInformationBuilder, InputTestInformationBuilder> builder);

        /**
         * Sets the expected output information of the test.
         *
         * @param expect the expected output information of the test
         *
         * @return this builder instance after setting the expected output information of the test
         */
        TestInformationBuilder expect(SubtestInformation expect);

        /**
         * Sets the expected output information of the test.
         *
         * @param builder the builder for creating the expected output information of the test
         *
         * @return this builder instance after setting the expected output information of the test
         */
        TestInformationBuilder expect(Function<OutputTestInformationBuilder, OutputTestInformationBuilder> builder);

        /**
         * Sets the actual output information of the test.
         *
         * @param actual the actual output information of the test
         *
         * @return this builder instance after setting the actual output information of the test
         */
        TestInformationBuilder actual(SubtestInformation actual);

        /**
         * Sets the actual output information of the test.
         *
         * @param builder the builder for creating the actual output information of the test
         *
         * @return this builder instance after setting the actual output information of the test
         */
        TestInformationBuilder actual(Function<OutputTestInformationBuilder, OutputTestInformationBuilder> builder);
    }

    /**
     * An implementation of {@link TestInformationBuilder}.
     */
    private static class TestInformationBuilderImpl implements TestInformationBuilder {

        /**
         * The test case of the test.
         */
        private @Nullable Object subject;

        /**
         * The input information of the test.
         */
        private @Nullable SubtestInformation input;

        /**
         * The expected output information of the test.
         */
        private @Nullable SubtestInformation expect;

        /**
         * The actual output information of the test.
         */
        private @Nullable SubtestInformation actual;

        @Override
        public TestInformationBuilder subject(Object subject) {
            this.subject = subject;
            return this;
        }

        @Override
        public TestInformationBuilder input(SubtestInformation input) {
            this.input = input;
            return this;
        }

        @Override
        public TestInformationBuilder input(Function<InputTestInformationBuilder, InputTestInformationBuilder> input) {
            return input(input.apply(new InputTestInformationBuilderImpl()).build());
        }

        @Override
        public TestInformationBuilder expect(SubtestInformation expect) {
            this.expect = expect;
            return this;
        }

        @Override
        public TestInformationBuilder expect(Function<OutputTestInformationBuilder, OutputTestInformationBuilder> expect) {
            return expect(expect.apply(new OutputTestInformationBuilderImpl()).build());
        }

        @Override
        public TestInformationBuilder actual(SubtestInformation actual) {
            this.actual = actual;
            return this;
        }

        @Override
        public TestInformationBuilder actual(Function<OutputTestInformationBuilder, OutputTestInformationBuilder> actual) {
            return actual(actual.apply(new OutputTestInformationBuilderImpl()).build());
        }

        @Override
        public TestInformation build() {
            return new TestInformation(subject, input, expect, actual);
        }
    }
}

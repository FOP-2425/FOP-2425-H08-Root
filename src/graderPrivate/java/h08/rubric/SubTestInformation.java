package h08.rubric;

import org.jetbrains.annotations.Nullable;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.assertions.Property;
import org.tudalgo.algoutils.tutor.general.assertions.basic.BasicProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * Subtest information for an assignment which contains no subject.
 *
 * @param properties the properties of the subtest information
 */
public record SubTestInformation(Collection<Property> properties) implements Context {

    @Override
    public Object subject() {
        return null;
    }

    /**
     * Creates a builder for creating test input information.
     */
    public interface InputTestInformationBuilder extends h08.rubric.Builder<SubTestInformation> {

        /**
         * Adds a property to the test input information.
         *
         * @param key   the key of the property
         * @param value the value of the property
         *
         * @return this builder instance after adding the property
         */
        InputTestInformationBuilder add(String key, Object value);
    }

    /**
     * Creates a builder for creating test output information.
     */
    public interface OutputTestInformationBuilder extends h08.rubric.Builder<SubTestInformation> {

        /**
         * Adds a property to the test output information.
         *
         * @param key   the key of the property
         * @param value the value of the property
         *
         * @return this builder instance after adding the property
         */
        OutputTestInformationBuilder add(String key, Object value);

        /**
         * Adds a property to the test output information that indicates an exception was thrown. If the exception is
         * null, it indicates that no exception was thrown.
         *
         * @param exception the exception that was thrown
         *
         * @return this builder instance after adding the property
         */
        OutputTestInformationBuilder cause(@Nullable Class<? extends Throwable> exception);
    }

    /**
     * An implementation of {@link  InputTestInformationBuilder}.
     */
    static class InputTestInformationBuilderImpl implements InputTestInformationBuilder {

        /**
         * The properties of the test information.
         */
        private final List<Property> properties = new ArrayList<>();

        @Override
        public InputTestInformationBuilder add(String key, Object value) {
            ListIterator<Property> iterator = properties.listIterator();
            String valueString = String.valueOf(value);
            while (iterator.hasNext()) {
                Property property = iterator.next();
                if (property.key().equals(key)) {
                    iterator.set(new BasicProperty(key, valueString));
                    return this;
                }
            }
            properties.add(new BasicProperty(key, valueString));
            return this;
        }

        @Override
        public SubTestInformation build() {
            return new SubTestInformation(properties);
        }
    }

    /**
     * An implementation of {@link OutputTestInformationBuilder}.
     */
    static class OutputTestInformationBuilderImpl implements OutputTestInformationBuilder {

        /**
         * The properties of the test information.
         */
        private final List<Property> properties = new ArrayList<>();

        @Override
        public OutputTestInformationBuilder add(String key, Object value) {
            ListIterator<Property> iterator = properties.listIterator();
            while (iterator.hasNext()) {
                Property property = iterator.next();
                if (property.key().equals(key)) {
                    iterator.set(new BasicProperty(key, value.toString()));
                    return this;
                }
            }
            properties.add(new BasicProperty(key, value.toString()));
            return this;
        }

        @Override
        public OutputTestInformationBuilder cause(@Nullable Class<? extends Throwable> exception) {
            properties.add(new BasicProperty("Throws", exception == null ? "Nothing" : exception.getName()));
            return this;
        }

        @Override
        public SubTestInformation build() {
            return new SubTestInformation(properties);
        }
    }
}

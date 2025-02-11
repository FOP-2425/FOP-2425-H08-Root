package h08.rubric.context;

import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.assertions.Property;

import java.util.Collection;

/**
 * Contains sub information about a test case which divides the main information into structured parts.
 *
 * @param properties the properties of the subtest information
 */
public record SubtestInformation(Collection<Property> properties) implements Context {

    @Override
    public Object subject() {
        return null;
    }

    /**
     * A builder for {@link SubtestInformation}.
     *
     * @param <B> the type of the builder
     */
    public interface SubtestInformationBuilder<B extends SubtestInformationBuilder<B>>
        extends h08.rubric.Builder<SubtestInformation> {

        /**
         * Adds information to the test case.
         *
         * @param information the information to add
         * @param description the description of the information
         *
         * @return this builder instance with the information added
         */
        B add(String information, Object description);
    }
}

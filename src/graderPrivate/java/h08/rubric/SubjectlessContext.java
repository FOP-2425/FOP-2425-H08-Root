package h08.rubric;

import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.assertions.Property;
import org.tudalgo.algoutils.tutor.general.assertions.basic.BasicProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

public record SubjectlessContext(Collection<Property> properties) implements Context {

    @Override
    public Object subject() {
        return null;
    }

    public interface SubjectlessContextBuilder extends h08.rubric.Builder<SubjectlessContext> {

        SubjectlessContextBuilder add(String key, Object value);

        SubjectlessContextBuilder exception(Class<? extends Throwable> exception);
    }

    private static class SubjectlessContextBuilderImpl implements SubjectlessContextBuilder {

        private final List<Property> properties = new ArrayList<>();

        @Override
        public SubjectlessContextBuilder add(String key, Object value) {
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
        public SubjectlessContextBuilder exception(Class<? extends Throwable> exception) {
            properties.add(new BasicProperty("Throws", exception));
            return this;
        }

        @Override
        public SubjectlessContext build() {
            return new SubjectlessContext(properties);
        }
    }
}

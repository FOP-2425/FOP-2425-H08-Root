package h08.rubric.context;

import org.tudalgo.algoutils.tutor.general.assertions.Property;
import org.tudalgo.algoutils.tutor.general.assertions.basic.BasicProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;

public abstract class BasicSubtestInformationBuilder<B extends SubtestInformation.SubtestInformationBuilder<B>>
    implements SubtestInformation.SubtestInformationBuilder<B> {

    protected final List<Property> properties = new ArrayList<>();

    protected abstract B self();

    @Override
    public B add(String information, Object description) {
        ListIterator<Property> iterator = properties.listIterator();
        String valueString = String.valueOf(description);
        while (iterator.hasNext()) {
            Property property = iterator.next();
            if (property.key().equals(information)) {
                iterator.set(new BasicProperty(information, valueString));
                return self();
            }
        }
        properties.add(new BasicProperty(information, valueString));
        return self();
    }

    @Override
    public SubtestInformation build() {
        return new SubtestInformation(properties);
    }
}

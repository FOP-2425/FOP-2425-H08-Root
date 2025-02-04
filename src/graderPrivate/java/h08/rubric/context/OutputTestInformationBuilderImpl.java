package h08.rubric.context;

import org.jetbrains.annotations.Nullable;
import org.tudalgo.algoutils.tutor.general.assertions.basic.BasicProperty;

/**
 * An implementation of {@link OutputTestInformationBuilder}.
 *
 * @author Nhan Huynh
 */
class OutputTestInformationBuilderImpl extends BasicSubtestInformationBuilder<OutputTestInformationBuilder>
    implements OutputTestInformationBuilder {

    @Override
    protected OutputTestInformationBuilder self() {
        return this;
    }

    @Override
    public OutputTestInformationBuilder cause(@Nullable Class<? extends Throwable> exception) {
        properties.add(new BasicProperty("Throws", exception == null ? "Nothing" : exception.getName()));
        return this;
    }
}

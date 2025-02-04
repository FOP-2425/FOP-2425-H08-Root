package h08.rubric.context;

/**
 * An implementation of {@link InputTestInformationBuilder}.
 *
 * @author Nhan Huynh
 */
class InputTestInformationBuilderImpl extends BasicSubtestInformationBuilder<InputTestInformationBuilder>
    implements InputTestInformationBuilder {

    @Override
    protected InputTestInformationBuilder self() {
        return this;
    }
}

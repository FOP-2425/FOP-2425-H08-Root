package h08.rubric.context;

import org.jetbrains.annotations.Nullable;

/**
 * A builder for creating output test case related information.
 */
public interface OutputTestInformationBuilder
    extends SubtestInformation.SubtestInformationBuilder<OutputTestInformationBuilder> {

    OutputTestInformationBuilder cause(@Nullable Class<? extends Throwable> exception);
}

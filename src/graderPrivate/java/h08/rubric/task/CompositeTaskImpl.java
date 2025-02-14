package h08.rubric.task;

import org.jetbrains.annotations.NotNull;
import org.sourcegrade.jagr.api.rubric.Criterion;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An implementation of a {@link CompositeTask}.
 *
 * @param description the description of the composite task
 * @param subtasks    the subtasks of the composite task that must be completed to complete the task
 *
 * @author Nhan Huynh
 */
record CompositeTaskImpl(String description, List<Subtask> subtasks) implements CompositeTask {

    @Override
    public Criterion getCriterion() {
        return Criterion.builder()
            .shortDescription(description)
            .addChildCriteria(
                subtasks.stream()
                    .map(Subtask::getCriterion)
                    .toArray(Criterion[]::new)
            )
            .build();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj
            || obj instanceof CompositeTaskImpl that
            && Objects.equals(description, that.description)
            && Objects.equals(subtasks, that.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, subtasks);
    }

    /**
     * A builder for {@link CompositeTask}.
     */
    static class CompositeTaskBuilderImpl implements CompositeTaskBuilder {

        /**
         * The description of the composite task.
         */
        private @NotNull String description = "";

        /**
         * The subtasks of the composite task that must be completed to complete the task.
         */
        private final List<Subtask> criteria = new ArrayList<>();

        @Override
        public CompositeTaskBuilder description(@NotNull String description) {
            this.description = description;
            return this;
        }

        @Override
        public CompositeTaskBuilder subtasks(Subtask... subtasks) {
            this.criteria.addAll(List.of(subtasks));
            return this;
        }

        @Override
        public CompositeTask build() {
            if (description.isBlank()) {
                throw new IllegalArgumentException("Description must be set!");
            }
            return new CompositeTaskImpl(description, criteria);
        }
    }
}

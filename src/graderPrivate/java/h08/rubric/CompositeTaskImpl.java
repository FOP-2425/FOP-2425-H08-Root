package h08.rubric;

import org.jetbrains.annotations.NotNull;
import org.sourcegrade.jagr.api.rubric.Criterion;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

record CompositeTaskImpl(String description, List<Subtask> subtasks) implements CompositeTask {

    @Override
    public Criterion getCriterion() {
        return Criterion.builder().shortDescription(description).addChildCriteria(subtasks.stream().map(Subtask::getCriterion).toArray(Criterion[]::new)).build();
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

    static class CompositeTaskBuilderImpl implements CompositeTaskBuilder {

        private @NotNull String description = "";

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

package h08.rubric;

import h08.util.TriFunction;
import org.jetbrains.annotations.NotNull;
import org.sourcegrade.jagr.api.rubric.Criterion;
import org.sourcegrade.jagr.api.rubric.Gradable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * An implementation of {@link AtomicTask}.
 *
 * @author Nhan Huynh
 */
class AtomicTaskImpl implements AtomicTask {

    /**
     * The description of the atomic task.
     */
    private final String description;

    /**
     * The criteria of the atomic task that must be met to complete the subtask.
     */
    private final List<Criterion> criteria;

    /**
     * The requirements that must be met to complete the subtask.
     */
    private final List<Criterion> requirements;

    /**
     * Creates a new {@link AtomicTaskImpl} with the given description, criteria, and requirements.
     *
     * @param description  the description of the atomic task
     * @param criteria     the criteria of the atomic task that must be met to complete the subtask
     * @param requirements the requirements that must be met to complete the subtask
     */
    AtomicTaskImpl(String description, List<Criterion> criteria, List<Criterion> requirements) {
        this.description = description;
        this.criteria = criteria;
        this.requirements = requirements;
    }

    @Override
    public Criterion getCriterion() {
        return Criterion.builder()
            .shortDescription(description)
            .minPoints(0)
            .addChildCriteria(Stream.concat(criteria.stream(), requirements.stream()).toArray(Criterion[]::new))
            .build();
    }

    @Override
    public List<Criterion> criteria() {
        return criteria;
    }

    @Override
    public List<Criterion> requirements() {
        return requirements;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj
            || obj instanceof AtomicTaskImpl that
            && Objects.equals(description, that.description)
            && Objects.equals(criteria, that.criteria)
            && Objects.equals(requirements, that.requirements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, criteria, requirements);
    }

    static class AtomicTaskBuilderImpl<A extends AtomicTask> implements AtomicTaskBuilder<A> {

        /**
         * The constructor to invoke to create a new {@link AtomicTask}.
         */
        private final TriFunction<String, List<Criterion>, List<Criterion>, A> constructor;

        /**
         * The description of the atomic task.
         */
        private @NotNull String description = "";

        /**
         * The name of the test class that tests this atomic task.
         */
        private @NotNull String testClassName = "";

        /**
         * The criteria of the atomic task that must be met to complete the subtask.
         */
        private final List<Supplier<Criterion>> criteria = new ArrayList<>();

        /**
         * The requirements that must be met to complete the subtask.
         */
        private final List<Supplier<Criterion>> requirements = new ArrayList<>();

        /**
         * Creates a new {@link AtomicTaskBuilderImpl} with the given constructor to create a new {@link AtomicTask}.
         *
         * @param constructor the constructor to invoke to create a new {@link AtomicTask}
         */
        AtomicTaskBuilderImpl(TriFunction<String, List<Criterion>, List<Criterion>, A> constructor) {
            this.constructor = Objects.requireNonNull(constructor);
        }

        @Override
        public AtomicTaskBuilder<A> description(String description) {
            this.description = description;
            return this;
        }

        @Override
        public AtomicTaskBuilder<A> testClassName(String testClassName) {
            this.testClassName = testClassName;
            return this;
        }

        @Override
        public AtomicTaskBuilder<A> criterion(
            String description,
            boolean publicTest,
            Map<String, List<Class<?>>> testMethodsSignature
        ) {
            criteria.add(
                Rubrics.criterion(
                    description,
                    testClassName + (publicTest ? "Public" : "Private"),
                    testMethodsSignature,
                    () -> 1
                )
            );
            return this;
        }

        @Override
        public AtomicTaskBuilder<A> requirement(String description, Map<String, List<Class<?>>> testMethodsSignature) {
            requirements.add(
                Rubrics.criterion(
                    description,
                    testClassName + "Private",
                    testMethodsSignature,
                    () -> -criteria.stream()
                        .map(Supplier::get)
                        .mapToInt(Gradable::getMaxPoints)
                        .sum()
                )
            );
            return this;
        }

        @Override
        public A build() {
            if (testClassName.isBlank()) {
                throw new IllegalStateException("Test class name must be set!");
            }
            return constructor.apply(
                description,
                criteria.stream().map(Supplier::get).toList(),
                requirements.stream().map(Supplier::get).toList()
            );
        }
    }
}

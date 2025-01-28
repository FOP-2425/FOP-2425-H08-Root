package h08.rubric;

import org.sourcegrade.jagr.api.rubric.Criterion;

import java.util.List;
import java.util.Map;

/**
 * Represents an atomic task in a rubric.
 * An atomic task is a task that does not contain child tasks, and the criteria
 * are defined at the same level as the task itself.
 *
 * @author Nhan Huynh
 */
public interface AtomicTask extends Task {

    /**
     * Creates a new builder for {@link AtomicTask}.
     *
     * @return a new builder for {@link AtomicTask}
     */
    static AtomicTaskBuilder<AtomicTask> builder() {
        return new AtomicTaskImpl.AtomicTaskBuilderImpl<>(AtomicTaskImpl::new);
    }

    /**
     * Returns the criteria of this atomic task that must be met to complete the subtask.
     *
     * @return the criteria of this atomic task that must be met to complete the subtask
     */
    List<Criterion> criteria();

    /**
     * Returns the requirements that must be met to complete the subtask.
     *
     * @return the requirements that must be met to complete the subtask
     */
    List<Criterion> requirements();

    /**
     * A builder for {@link AtomicTask}.
     *
     * @param <T> the type of the {@link AtomicTask} to build
     */
    interface AtomicTaskBuilder<T extends AtomicTask> extends Builder<T> {

        /**
         * Sets the description of the atomic task.
         *
         * @param description the description of the subtask
         *
         * @return this builder instance with the description set
         */
        AtomicTaskBuilder<T> description(String description);

        /**
         * Sets the name of the test class that tests this atomic task.
         *
         * @param testClassName the name of the test class that tests this subtask
         *
         * @return this builder instance with the test class name set
         */
        AtomicTaskBuilder<T> testClassName(String testClassName);

        /**
         * Adds a criterion to the atomic task.
         *
         * @param description          the description of the criterion
         * @param publicTest           whether the test is public
         * @param testMethodsSignature the signature of the test methods
         *
         * @return this builder instance with the criterion added
         */
        AtomicTaskBuilder<T> criterion(
            String description,
            boolean publicTest,
            Map<String, List<Class<?>>> testMethodsSignature
        );

        default AtomicTaskBuilder<T> criterion(String description, Map<String, List<Class<?>>> testMethodsSignature) {
            return criterion(description, true, testMethodsSignature);
        }

        default AtomicTaskBuilder<T> criterion(
            String description,
            boolean publicTest,
            String testMethodName,
            Class<?>... testMethodParameters
        ) {
            return criterion(description, publicTest, Map.of(testMethodName, List.of(testMethodParameters)));
        }

        default AtomicTaskBuilder<T> criterion(String description, String testMethodName, Class<?>... testMethodSignature) {
            return criterion(description, true, testMethodName, testMethodSignature);
        }

        AtomicTaskBuilder<T> requirement(String description, Map<String, List<Class<?>>> testMethodsSignature);

        default AtomicTaskBuilder<T> requirement(String description, String testMethodName, Class<?>... testMethodParameters) {
            return requirement(description, Map.of(testMethodName, List.of(testMethodParameters)));
        }
    }
}

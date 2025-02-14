package h08.rubric.task;

import h08.rubric.Builder;

import java.util.List;

/**
 * Represents a composite task in a rubric. A composite task is a task that contains child tasks.
 *
 * @author Nhan Huynh
 */
public interface CompositeTask extends Task {

    /**
     * Creates a new builder for {@link CompositeTask}.
     *
     * @return a new builder for {@link CompositeTask}
     */
    static CompositeTaskBuilder builder() {
        return new CompositeTaskImpl.CompositeTaskBuilderImpl();
    }

    /**
     * Returns the subtasks of this composite task.
     *
     * @return the subtasks of this composite task
     */
    List<Subtask> subtasks();

    /**
     * A builder for {@link CompositeTask}.
     */
    interface CompositeTaskBuilder extends Builder<CompositeTask> {

        /**
         * Sets the description of the composite task.
         *
         * @param description the description of the composite task
         *
         * @return this builder instance with the description set
         */
        CompositeTaskBuilder description(String description);

        /**
         * Adds subtasks to the composite task.
         *
         * @param subtasks the subtasks to add
         *
         * @return this builder instance with the subtasks added
         */
        CompositeTaskBuilder subtasks(Subtask... subtasks);

        /**
         * Adds a criterion to the composite task.
         *
         * @param criterion the criterion to add
         *
         * @return this builder instance with the criterion added
         */
        default CompositeTaskBuilder subtask(Subtask criterion) {
            return subtasks(criterion);
        }
    }
}

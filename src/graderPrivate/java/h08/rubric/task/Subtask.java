package h08.rubric.task;

/**
 * Represents a subtask in a rubric. A subtask is an atomic task that is part of a composite task.
 *
 * @author Nhan Huynh
 */
public interface Subtask extends AtomicTask {

    /**
     * Creates a new builder for {@link Subtask}.
     *
     * @return a new builder for {@link Subtask}
     */
    static AtomicTaskBuilder<Subtask> builder() {
        return new AtomicTaskImpl.AtomicTaskBuilderImpl<>(SubtaskImpl::new);
    }
}

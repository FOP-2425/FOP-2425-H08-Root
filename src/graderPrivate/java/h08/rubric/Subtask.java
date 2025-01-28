package h08.rubric;

public interface Subtask extends AtomicTask {

    static AtomicTaskBuilder<Subtask> builder() {
        return new AtomicTaskImpl.AtomicTaskBuilderImpl<>(SubtaskImpl::new);
    }
}

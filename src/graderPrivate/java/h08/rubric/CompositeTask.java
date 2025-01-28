package h08.rubric;

import java.util.List;

public interface CompositeTask extends Task {

    List<Subtask> subtasks();

    static CompositeTaskBuilder builder() {
        return new CompositeTaskImpl.CompositeTaskBuilderImpl();
    }

    interface CompositeTaskBuilder extends Builder<CompositeTask> {

        CompositeTaskBuilder description(String description);

        CompositeTaskBuilder subtasks(Subtask... subtasks);

        default CompositeTaskBuilder subtask(Subtask criterion) {
            return subtasks(criterion);
        }
    }
}

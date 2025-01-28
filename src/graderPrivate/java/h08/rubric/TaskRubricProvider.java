package h08.rubric;

import org.sourcegrade.jagr.api.rubric.Criterion;
import org.sourcegrade.jagr.api.rubric.Rubric;
import org.sourcegrade.jagr.api.rubric.RubricProvider;

import java.util.List;

public abstract class TaskRubricProvider implements RubricProvider {

    private final int taskNumber;
    private final String title;

    private final boolean publicTests;

    public TaskRubricProvider(int taskNumber, String title, boolean publicTests) {
        this.taskNumber = taskNumber;
        this.title = title;
        this.publicTests = publicTests;
    }

    public abstract List<Task> getTasks();

    @Override
    public Rubric getRubric() {
        return Rubric.builder()
            .title("H%02d | %s - %s Tests".formatted(taskNumber,title, publicTests ? "Public" : "Private"))
            .addChildCriteria(
                getTasks().stream()
                    .map(Task::getCriterion)
                    .toArray(Criterion[]::new)
            ).build();
    }
}

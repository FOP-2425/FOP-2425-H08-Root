package h08.rubric;

import org.sourcegrade.jagr.api.rubric.Criterion;

import java.util.List;

class SubtaskImpl extends AtomicTaskImpl implements Subtask {

    SubtaskImpl(String description, List<Criterion> criteria, List<Criterion> requirements) {
        super(description, criteria, requirements);
    }
}

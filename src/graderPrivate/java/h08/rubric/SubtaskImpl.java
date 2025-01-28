package h08.rubric;

import org.sourcegrade.jagr.api.rubric.Criterion;

import java.util.List;

/**
 * An implementation of {@link Subtask}.
 *
 * @author Nhan Huynh
 */
class SubtaskImpl extends AtomicTaskImpl implements Subtask {

    /**
     * Creates a new instance of {@link SubtaskImpl}.
     *
     * @param description  the description of the subtask
     * @param criteria     the criteria of the subtask that must be met to complete the subtask
     * @param requirements the requirements that must be met to complete the subtask
     */
    SubtaskImpl(String description, List<Criterion> criteria, List<Criterion> requirements) {
        super(description, criteria, requirements);
    }
}

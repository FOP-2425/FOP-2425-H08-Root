package h08.rubric;

import org.sourcegrade.jagr.api.rubric.Criterion;

import java.util.List;
import java.util.Map;

public interface AtomicTask extends Task {

    static AtomicTaskBuilder<AtomicTask> builder() {
        return new AtomicTaskImpl.AtomicTaskBuilderImpl<>(AtomicTaskImpl::new);
    }

    List<Criterion> criteria();

    List<Criterion> requirements();

    interface AtomicTaskBuilder<T extends AtomicTask> extends Builder<T> {

        AtomicTaskBuilder<T> description(String description);

        AtomicTaskBuilder<T> testClassName(String testClassName);

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

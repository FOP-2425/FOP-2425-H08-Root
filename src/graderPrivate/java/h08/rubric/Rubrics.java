package h08.rubric;

import org.sourcegrade.jagr.api.rubric.Criterion;
import org.sourcegrade.jagr.api.rubric.Grader;
import org.sourcegrade.jagr.api.rubric.JUnitTestRef;
import org.tudalgo.algoutils.tutor.general.jagr.RubricUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class Rubrics {

    private Rubrics() {
    }

    public static Criterion.Builder criterionBuilder(String description, Grader grader) {
        return Criterion.builder()
            .shortDescription(description)
            .grader(grader);
    }

    public static Criterion.Builder criterionBuilder(String description, JUnitTestRef testRef) {
        return criterionBuilder(
            description,
            Grader.testAwareBuilder()
                .requirePass(testRef)
                .pointsFailedMin()
                .pointsPassedMax()
                .build()
        );
    }

    public static Supplier<Criterion> criterion(
        String description,
        String className,
        Map<String, List<Class<?>>> testMethodsSignature,
        Supplier<Integer> points
    ) {
        return () -> {
            Criterion.Builder builder;
            try {
                List<JUnitTestRef> testRefs = new ArrayList<>(testMethodsSignature.size());
                for (Map.Entry<String, List<Class<?>>> entry : testMethodsSignature.entrySet()) {
                    Method method = Class.forName(className).getDeclaredMethod(
                        entry.getKey(),
                        entry.getValue().toArray(Class[]::new)
                    );
                    testRefs.add(JUnitTestRef.ofMethod(method));
                }
                builder = criterionBuilder(description, JUnitTestRef.and(testRefs.toArray(JUnitTestRef[]::new)));
            } catch (Exception e) {
                builder = criterionBuilder(description, RubricUtils.graderPrivateOnly());
            }
            int pointsValue = points.get();
            if (pointsValue >= 0) {
                builder.minPoints(0);
                builder.maxPoints(pointsValue);
            } else {
                builder.maxPoints(0);
                builder.minPoints(pointsValue);
            }
            return builder.build();
        };
    }
}

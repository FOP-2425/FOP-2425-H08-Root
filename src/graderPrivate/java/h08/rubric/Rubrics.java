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

/**
 * Utility class for creating {@link Criterion} instances.
 *
 * @author Nhan Huynh
 */
public final class Rubrics {

    /**
     * Prevent instantiation of this utility class.
     */
    private Rubrics() {
    }

    /**
     * Returns a pre-configured criterion builder where the description and grader are set.
     *
     * @param description the description of the criterion
     * @param grader      the grader of the criterion
     *
     * @return a pre-configured criterion builder with the description and grader set
     */
    public static Criterion.Builder criterionBuilder(String description, Grader grader) {
        return Criterion.builder()
            .shortDescription(description)
            .grader(grader);
    }

    /**
     * Returns a pre-configured criterion builder where the description and test reference are set.
     *
     * @param description the description of the criterion
     * @param testRef     the test reference of the criterion
     *
     * @return a pre-configured criterion builder with the description and test reference set
     */
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

    /**
     * Creates a criterion supplier with the given description, class name, test methods signature, and points.
     * Since the points can only be determined after all subtasks are added, the points are calculated lazily.
     *
     * @param description          the description of the criterion
     * @param className            the name of the test class that tests this subtask
     * @param testMethodsSignature the signature of the test methods
     * @param points               the points of the criterion
     *
     * @return a criterion supplier with the given description, class name, test methods signature, and points
     */
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

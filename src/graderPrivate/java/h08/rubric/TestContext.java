package h08.rubric;

import org.jetbrains.annotations.Nullable;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.assertions.Property;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TestContext implements Context {

    private final Object subject;
    private final Collection<Property> properties;

    public TestContext(
        Object subject,
        @Nullable SubjectlessContext require,
        @Nullable SubjectlessContext ensure,
        @Nullable SubjectlessContext actual) {
        Context.Builder<?> builder = Assertions2.contextBuilder()
            .subject(subject);

        if (require != null) {
            addState(builder, "Require", require);
        }
        if (ensure != null) {
            addState(builder, "Ensure", ensure);
        }
        if (actual != null) {
            addState(builder, "Actual", actual);
        }
        Context context = builder.build();
        this.subject = context.subject();
        this.properties = context.properties();
    }

    public static TestContextBuilder builder() {
        return new TestInformationTestContextBuilder();
    }

    private void addState(Builder<?> builder, String name, SubjectlessContext state) {
        if (state != null) {
            builder.add(
                name,
                Assertions2.contextBuilder()
                    .add(state.properties().toArray(Property[]::new))
                    .build()
            );
        }
    }

    @Override
    public Collection<Property> properties() {
        return List.of();
    }

    @Override
    public Object subject() {
        return null;
    }

    public interface TestContextBuilder extends h08.rubric.Builder<TestContext> {

        TestContextBuilder subject(Object subject);

        TestContextBuilder require(SubjectlessContext context);

        TestContextBuilder ensure(SubjectlessContext context);

        TestContextBuilder actual(SubjectlessContext context);
    }

    private static class TestInformationTestContextBuilder implements TestContextBuilder {

        private @Nullable Object subject;
        private @Nullable SubjectlessContext require;
        private @Nullable SubjectlessContext ensure;
        private @Nullable SubjectlessContext actual;

        private final List<Property> properties = new ArrayList<>();

        @Override
        public TestContextBuilder subject(Object subject) {
            this.subject = subject;
            return this;
        }

        @Override
        public TestContextBuilder require(SubjectlessContext context) {
            this.require = context;
            return this;
        }

        @Override
        public TestContextBuilder ensure(SubjectlessContext context) {
            this.ensure = context;
            return this;
        }

        @Override
        public TestContextBuilder actual(SubjectlessContext context) {
            this.actual = context;
            return this;
        }

        @Override
        public TestContext build() {
            return new TestContext(subject, require, ensure, actual);
        }
    }
}

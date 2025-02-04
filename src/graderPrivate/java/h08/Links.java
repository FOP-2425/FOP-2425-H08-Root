package h08;

import org.opentest4j.AssertionFailedError;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions3;
import org.tudalgo.algoutils.tutor.general.match.BasicStringMatchers;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.match.MatcherFactories;
import org.tudalgo.algoutils.tutor.general.reflections.BasicPackageLink;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.Arrays;
import java.util.List;

/**
 * Provides utility methods for obtaining links to classes, fields, and methods for an assignment.
 *
 * @author Nhan Huynh
 */
public final class Links {

    /**
     * Matcher factory for string matchers.
     */
    private static final MatcherFactories.StringMatcherFactory STRING_MATCHER_FACTORY = BasicStringMatchers::identical;

    /**
     * Prevent instantiation of this utility class.
     */
    private Links() {
    }

    /**
     * Returns the link to the class with the given name.
     *
     * @param clazz the class to get the link for
     *
     * @return the link to the class with the given name
     * @throws AssertionFailedError if the class does not exist
     */
    public static TypeLink getType(Class<?> clazz) {
        return getType(clazz.getPackageName(), clazz.getSimpleName());
    }

    /**
     * Returns the link to the class with the given name.
     *
     * @param packageName the package name of the class
     * @param className   the name of the class
     *
     * @return the link to the class with the given name
     * @throws AssertionFailedError if the class does not exist
     */
    public static TypeLink getType(String packageName, String className) {
        return Assertions3.assertTypeExists(BasicPackageLink.of(packageName), STRING_MATCHER_FACTORY.matcher(className));
    }

    /**
     * Returns the link to the field with the given name in the given class.
     *
     * @param type       the class to get the field from
     * @param methodName the name of the field
     * @param matchers   the matchers to apply to the field
     *
     * @return the link to the field with the given name in the given class
     * @throws AssertionFailedError if the field does not exist
     */
    @SafeVarargs
    public static FieldLink getField(TypeLink type, String methodName, Matcher<FieldLink>... matchers) {
        return Assertions3.assertFieldExists(type, Arrays.stream(matchers).reduce(STRING_MATCHER_FACTORY.matcher(methodName), Matcher::and));
    }

    /**
     * Returns the link to the method with the given name in the given class.
     *
     * @param type       the class to get the method from
     * @param methodName the name of the method
     * @param matchers   the matchers to apply to the method
     *
     * @return the link to the method with the given name in the given class
     * @throws AssertionFailedError if the method does not exist
     */
    @SafeVarargs
    public static MethodLink getMethod(TypeLink type, String methodName, Matcher<MethodLink>... matchers) {
        return Assertions3.assertMethodExists(type, Arrays.stream(matchers).reduce(STRING_MATCHER_FACTORY.matcher(methodName), Matcher::and));
    }

    /**
     * Returns the link to the method with the given name and parameter types in the given class.
     *
     * @param type           the class to get the method from
     * @param methodName     the name of the method
     * @param parameterTypes the parameter types of the method
     *
     * @return the link to the method with the given name and parameter types in the given class
     * @throws AssertionFailedError if the method does not exist
     */
    public static MethodLink getMethod(TypeLink type, String methodName, Class<?>... parameterTypes) {
        List<Class<?>> parameters = Arrays.stream(parameterTypes).toList();
        return Assertions3.assertMethodExists(type, STRING_MATCHER_FACTORY.<MethodLink>matcher(methodName).and(
            Matcher.of(method -> method.typeList().stream().map(TypeLink::reflection).toList().equals(parameters)))
        );
    }
}

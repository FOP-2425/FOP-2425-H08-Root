package h08.assertions;

import h08.TestConstants;
import org.opentest4j.AssertionFailedError;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions3;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.BasicPackageLink;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
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
     * Prevent instantiation of this utility class.
     */
    private Links() {
    }

    /**
     * Returns the link to the class with the given name in the given package.
     *
     * @param packageName the package name of the class
     * @param className   the name of the class
     *
     * @return the link to the class with the given name
     * @throws AssertionFailedError if the class could not be found
     */
    public static TypeLink getType(String packageName, String className) {
        try {
            return Assertions3.assertTypeExists(
                BasicPackageLink.of(packageName),
                TestConstants.STRING_MATCHER_FACTORY.matcher(className)
            );
        } catch (AssertionFailedError e) {
            // Only execute this if the class is not a project class e.g. stdlib class
            try {
                return BasicTypeLink.of(Class.forName(packageName + "." + className));
            } catch (ClassNotFoundException ex) {
                throw e;
            }
        }
    }

    /**
     * Returns the link to the class.
     *
     * @param clazz the class to get the link for
     *
     * @return the link to the class
     * @throws AssertionFailedError if the class could not be found
     */
    public static TypeLink getType(Class<?> clazz) {
        return getType(clazz.getPackageName(), clazz.getSimpleName());
    }

    /**
     * Returns the link to the field with the given name in the given class.
     *
     * @param type       the link to the class to get the field from
     * @param methodName the name of the field to get
     * @param matchers   the matchers to apply to the field which must all match
     *
     * @return the link to the field with the given name in the given class
     * @throws AssertionFailedError if the field could not be retrieved
     */
    @SafeVarargs
    public static FieldLink getField(TypeLink type, String methodName, Matcher<FieldLink>... matchers) {
        return Assertions3.assertFieldExists(
            type,
            Arrays.stream(matchers)
                .reduce(TestConstants.STRING_MATCHER_FACTORY.matcher(methodName), Matcher::and)
        );
    }

    /**
     * Returns the link to the method with the given name in the given class.
     *
     * @param type       the link to the class to get the method from
     * @param methodName the name of the method to get
     * @param matchers   the matchers to apply to the method which must all match
     *
     * @return the link to the method with the given name in the given class
     * @throws AssertionFailedError if the method could not be retrieved
     */
    @SafeVarargs
    public static MethodLink getMethod(TypeLink type, String methodName, Matcher<MethodLink>... matchers) {
        return Assertions3.assertMethodExists(
            type,
            Arrays.stream(matchers).reduce(TestConstants.STRING_MATCHER_FACTORY.matcher(methodName), Matcher::and)
        );
    }

    /**
     * Returns the link to the method with the given name and parameter types in the given class.
     *
     * @param type           the link to the class to get the method from
     * @param methodName     the name of the method to get
     * @param parameterTypes the parameter types of the method to get
     *
     * @return the link to the method with the given name and parameter types in the given class
     * @throws AssertionFailedError if the method could not be retrieved
     */
    public static MethodLink getMethod(TypeLink type, String methodName, Class<?>... parameterTypes) {
        List<Class<?>> parameters = Arrays.stream(parameterTypes).toList();
        return getMethod(
            type,
            methodName,
            Matcher.of(method -> method.typeList().stream()
                .map(TypeLink::reflection)
                .toList()
                .equals(parameters))
        );
    }

    /**
     * Returns the link to the method with the given name in the given class.
     *
     * @param type       the class to get the method from
     * @param methodName the name of the method to get
     *
     * @return the link to the method with the given name in the given class
     */
    public static MethodLink getMethod(TypeLink type, String methodName) {
        return getMethod(type, methodName, new Class<?>[0]);
    }
}

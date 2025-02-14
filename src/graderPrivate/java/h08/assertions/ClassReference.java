package h08.assertions;


import h08.Exceptions.BookingAlreadyCancelledException;
import h08.Exceptions.BookingManagementException;
import h08.Exceptions.BookingNotFoundException;
import h08.Exceptions.DuplicateBookingException;
import h08.Exceptions.FlightManagementException;
import h08.Exceptions.FlightNotFoundException;
import h08.Exceptions.InvalidBookingException;
import h08.Exceptions.NoSeatsAvailableException;
import h08.rubric.context.TestInformation;
import org.jetbrains.annotations.Nullable;
import org.opentest4j.AssertionFailedError;
import org.tudalgo.algoutils.tutor.general.reflections.BasicPackageLink;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.Link;
import org.tudalgo.algoutils.tutor.general.reflections.Modifier;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertArrayEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertTrue;

/**
 * A class reference represents a reference to a class in the project which may or may not be defined. It provides
 * operations to assert the existence and correctness of the class.
 *
 * @author <a href="https://github.com/FOP-2425/FOP-2425-H07-Root/blob/main/src/graderPrivate/java/h07/ClassReference.java">h07.ClassReference</a>
 */
public class ClassReference {

    /**
     * The predefined packages in this assignment.
     */
    public static final List<String> PREDEFINED_PACKAGES = List.of("h08", "h08.Exceptions", "h08/Exceptions");

    /**
     * The class reference for the {@link NoSeatsAvailableException} class.
     */
    public static final ClassReference NO_SEATS_AVAILABLE_EXCEPTION = new ClassReference(
        "h08",
        "NoSeatsAvailableException",
        new String[]{Exception.class.getSimpleName()},
        Link.Kind.CLASS,
        Modifier.PUBLIC,
        Modifier.NON_FINAL,
        Modifier.NON_ABSTRACT
    );

    /**
     * The class reference for the {@link FlightManagementException} class.
     */
    public static final ClassReference FLIGHT_MANAGEMENT_EXCEPTION = new ClassReference(
        "h08",
        "FlightManagementException",
        new String[]{Exception.class.getSimpleName()},
        Link.Kind.CLASS,
        Modifier.PUBLIC,
        Modifier.NON_FINAL,
        Modifier.NON_ABSTRACT
    );

    /**
     * The class reference for the {@link BookingManagementException} class.
     */
    public static final ClassReference BOOKING_MANAGEMENT_EXCEPTION = new ClassReference(
        "h08",
        "BookingManagementException",
        new String[]{Exception.class.getSimpleName()},
        Link.Kind.CLASS,
        Modifier.PUBLIC,
        Modifier.NON_FINAL,
        Modifier.NON_ABSTRACT
    );

    /**
     * The class reference for the {@link FlightNotFoundException} class.
     */
    public static final ClassReference FLIGHT_NOT_FOUND_EXCEPTION = new ClassReference(
        "h08",
        "FlightNotFoundException",
        new String[]{"FlightManagementException"},
        Link.Kind.CLASS,
        Modifier.PUBLIC,
        Modifier.NON_FINAL,
        Modifier.NON_ABSTRACT
    );

    /**
     * The class reference for the {@link BookingNotFoundException} class.
     */
    public static final ClassReference BOOKING_NOT_FOUND_EXCEPTION = new ClassReference(
        "h08",
        "BookingNotFoundException",
        new String[]{"BookingManagementException"},
        Link.Kind.CLASS,
        Modifier.PUBLIC,
        Modifier.NON_FINAL,
        Modifier.NON_ABSTRACT
    );

    /**
     * The class reference for the {@link InvalidBookingException} class.
     */
    public static final ClassReference INVALID_BOOKING_EXCEPTION = new ClassReference(
        "h08",
        "InvalidBookingException",
        new String[]{"BookingManagementException"},
        Link.Kind.CLASS,
        Modifier.PUBLIC,
        Modifier.NON_FINAL,
        Modifier.NON_ABSTRACT
    );

    /**
     * The class reference for the {@link BookingAlreadyCancelledException} class.
     */
    public static final ClassReference BOOKING_ALREADY_CANCELLED_EXCEPTION = new ClassReference(
        "h08",
        "BookingAlreadyCancelledException",
        new String[]{"FlightNotFoundException"},
        Link.Kind.CLASS,
        Modifier.PUBLIC,
        Modifier.NON_FINAL,
        Modifier.NON_ABSTRACT
    );

    /**
     * The class reference for the {@link DuplicateBookingException} class.
     */
    public static final ClassReference DUPLICATE_BOOKING_EXCEPTION = new ClassReference(
        "h08",
        "DuplicateBookingException",
        new String[]{"InvalidBookingException"},
        Link.Kind.CLASS,
        Modifier.PUBLIC,
        Modifier.NON_FINAL,
        Modifier.NON_ABSTRACT
    );

    /**
     * The package name of the class.
     */
    private final String packageName;

    /**
     * The class name of the class.
     */
    private final String className;

    private final String[] superClassNames;

    /**
     * The kind of the class.
     */
    private final Link.Kind kind;

    /**
     * The modifiers of the class.
     */
    private final Modifier[] modifiers;

    /**
     * The link to the class.
     */
    private @Nullable BasicTypeLink link;

    /**
     * Creates a new class reference with the given package name, class name, super classes and kind.
     *
     * @param packageName     the package name of the class
     * @param className       the class name of the class
     * @param superClassNames the super class names of the class
     * @param kind            the kind of the class
     */
    public ClassReference(String packageName, String className, String[] superClassNames, Link.Kind kind) {
        this(packageName, className, superClassNames, kind, new Modifier[0]);
    }

    /**
     * Creates a new class reference with the given package name, class name, super classes, kind and modifiers.
     *
     * @param packageName     the package name of the class
     * @param className       the class name of the class
     * @param superClassNames the super class names of the class
     * @param kind            the kind of the class
     * @param modifiers       the modifiers of the class
     */
    public ClassReference(String packageName, String className, String[] superClassNames, Link.Kind kind, Modifier... modifiers) {
        this.packageName = packageName;
        this.className = className;
        this.superClassNames = superClassNames;
        this.kind = kind;
        this.modifiers = modifiers;

        try {
            for (String predefinedPackageName : PREDEFINED_PACKAGES) {
                link = (BasicTypeLink) BasicPackageLink.of(predefinedPackageName).getType(Matchers.matchString(className));
                if (link != null && !link.reflection().getName().endsWith("Test")) {
                    return;
                }
            }
            link = null;

        } catch (Exception ignored) {
        }
    }

    /**
     * Returns the class name of the class.
     *
     * @return the class name of the class
     */
    public String getClassName() {
        return className;
    }

    /**
     * Returns {@code true} if the class is defined, {@code false} otherwise.
     *
     * @return {@code true} if the class is defined, {@code false} otherwise
     */
    public boolean isDefined() {
        return link != null;
    }

    /**
     * Asserts that the class is defined (exists).
     *
     * @throws AssertionFailedError if the class is not defined
     */
    public void assertDefined() {
        TestInformation info = TestInformation.builder()
            .subject(link)
            .expect(builder -> builder
                .add("Package", packageName)
                .add("Name", className)
                .add("Kind", kind)
                .add("Modifier", Arrays.stream(modifiers)
                    .map(Modifier::keyword)
                    .collect(Collectors.joining(", "))))
            .build();
        assertTrue(
            isDefined(),
            info,
            comment -> "Could not find class %s. Class is not defined or could not be found.".formatted(className)
        );
    }

    /**
     * Returns the test information for this class reference.
     *
     * @return the test information for this class reference
     */
    public TestInformation info() {
        assert link != null;
        return TestInformation.builder()
            .subject(link)
            .expect(builder -> builder
                .add("Package", packageName)
                .add("Name", className)
                .add("Kind", kind)
                .add("Modifier", Arrays.stream(modifiers)
                    .map(Modifier::keyword)
                    .toList()))
            .actual(builder -> builder
                .add("Package", link.reflection().getPackage().getName())
                .add("Name", link.name())
                .add("Kind", link.kind())
                .add("Modifier", Arrays.stream(Modifier.fromInt(link.modifiers()))
                    .map(Modifier::keyword)
                    .toList()))
            .build();
    }

    /**
     * Asserts that the class is correctly defined with all its specifications.
     *
     * @throws AssertionFailedError if the class is not defined or the specifications do not match
     */
    public void assertCorrectlyDefined() {
        assertDefined();
        assert link != null;
        TestInformation info = info();
        assertEquals(kind, link.kind(), info, comment -> "Kind does not match expected kind.");
        assertTrue(
            Arrays.stream(modifiers).allMatch(m -> m.is(link.modifiers())),
            info,
            r -> "The modifiers of the type do not match the expected modifiers."
        );
        String[] actualSuperClassNames = Stream.concat(Stream.of(link.superType()), link.interfaces().stream())
            .map(type -> type.reflection().getSimpleName())
            .toArray(String[]::new);
        assertArrayEquals(
            superClassNames,
            actualSuperClassNames,
            info,
            comment -> "The super classes of the type do not match the expected super classes.");
    }

    /**
     * Asserts that the class is named correctly.
     *
     * @throws AssertionFailedError if the class is not defined or the name does not match the expected name
     */
    public void assertNamedCorrectly() {
        if (!isDefined()) {
            return;
        }
        assert link != null;
        assertEquals(className, link.name(), info(), r -> "The name of the Type does not match the expected name.");
    }

    /**
     * Asserts that the class is defined in the correct package.
     *
     * @throws AssertionFailedError if the class is not defined or the package does not match the expected package
     */
    public void assertDefinedInCorrectPackage() {
        assertDefined();
        assert link != null;
        assertEquals(
            packageName.toLowerCase(),
            link.reflection().getPackage().getName().toLowerCase(),
            info(),
            comment -> "Package name does not match expected package name."
        );
    }

    /**
     * Returns the link to the class.
     *
     * @return the link to the class
     * @throws AssertionFailedError if the class is not defined
     */
    public BasicTypeLink getLink() {
        assertDefined();
        return link;
    }
}

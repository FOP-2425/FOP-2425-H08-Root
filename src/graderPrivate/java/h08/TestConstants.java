package h08;

import org.tudalgo.algoutils.tutor.general.match.BasicStringMatchers;
import org.tudalgo.algoutils.tutor.general.match.MatcherFactories;

/**
 * Constants for tests for configuring test behavior.
 *
 * @author Nhan Huynh
 */
public final class TestConstants {

    /**
     * Timeout for tests in seconds.
     */
    public static final int TEST_TIMEOUT_IN_SECONDS = 2;

    /**
     * Skip after the first failed test.
     */
    public static final boolean SKIP_AFTER_FIRST_FAILED_TEST = true;

    /**
     * Minimum similarity for string matching.
     */
    public static final double MINIMUM_SIMILARITY = .75;

    /**
     * Matcher factory for string matchers.
     */
    public static final MatcherFactories.StringMatcherFactory STRING_MATCHER_FACTORY = BasicStringMatchers::identical;

    /**
     * Prevent instantiation of this utility class.
     */
    private TestConstants() {
    }
}

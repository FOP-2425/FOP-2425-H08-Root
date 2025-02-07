package h08.assertions;

import h08.TestConstants;
import org.tudalgo.algoutils.tutor.general.match.Match;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.match.MatchingUtils;
import org.tudalgo.algoutils.tutor.general.match.Stringifiable;

/**
 * Utility class for creating matchers.
 *
 * @author Nhan Huynh
 */
public final class Matchers {

    /**
     * Prevent instantiation of this utility class.
     */
    private Matchers() {
    }

    /**
     * Creates a matcher that matches a string with a given string with a minimum similarity
     * {@value TestConstants#MINIMUM_SIMILARITY}%.
     *
     * @param string the string to match
     * @param <T>    the type of the object to match
     *
     * @return the matcher
     */
    public static <T extends Stringifiable> Matcher<T> matchString(String string) {

        return new Matcher<T>() {

            @Override
            public <ST extends T> Match<ST> match(ST object) {
                return new Match<>() {

                    final double similarity = MatchingUtils.similarity(object.string(), string);

                    @Override
                    public boolean matched() {
                        return similarity >= TestConstants.MINIMUM_SIMILARITY;
                    }

                    @Override
                    public ST object() {
                        return object;
                    }

                    @Override
                    public int compareTo(Match<ST> other) {
                        if (!other.matched()) {
                            return matched() ? 1 : 0;
                        } else if (!matched()) {
                            return -1;
                        }
                        double otherSimilarity = MatchingUtils.similarity(other.object().string(), string);
                        return Double.compare(similarity, otherSimilarity);
                    }
                };
            }
        };
    }
}

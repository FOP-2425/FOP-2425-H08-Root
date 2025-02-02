package h08;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Defines JSON converters for the H08 assignment.
 *
 * @author Nhan Huynh
 */
public final class JsonConverters extends org.tudalgo.algoutils.tutor.general.json.JsonConverters {

    /**
     * The date formatter for the date of birth.
     */
    private final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Prevent instantiation of this utility class.
     */
    private JsonConverters() {
    }

    /**
     * Converts a JSON node to a local date.
     *
     * @param node the JSON node to convert
     *
     * @return the local date represented by the JSON node
     */
    public static LocalDate toLocalDate(JsonNode node) {
        if (!node.isTextual()) {
            throw new IllegalArgumentException("Expected a textual value");
        }
        return LocalDate.parse(node.asText(), DATE_FORMATTER);
    }
}

package h08;

import h08.assertions.ClassReference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;

/**
 * Defines the private tests for the task H08.3.
 *
 * @author Nhan Huynh
 */
@TestForSubmission
@DisplayName("H08.3 | Exception Handling")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class H08_3_TestsPrivate extends H08_Tests {

    @DisplayName("Die FlightManagementException ist korrekt implementiert.")
    @Test
    void testFlightManagementException() {
        assertExceptionDefinedCorrectly(ClassReference.FLIGHT_MANAGEMENT_EXCEPTION);
    }

    @DisplayName("Die BookingManagementException ist korrekt implementiert.")
    @Test
    void testBookingManagementException() {
        assertExceptionDefinedCorrectly(ClassReference.BOOKING_MANAGEMENT_EXCEPTION);
    }

    @DisplayName("Die FlightNotFoundException ist korrekt implementiert.")
    @Test
    void testFlightNotFoundException() {
        assertExceptionDefinedCorrectly(ClassReference.FLIGHT_NOT_FOUND_EXCEPTION);
    }

    @DisplayName("Die BookingNotFoundException ist korrekt implementiert.")
    @Test
    void testBookingNotFoundException() {
        assertExceptionDefinedCorrectly(ClassReference.BOOKING_NOT_FOUND_EXCEPTION);
    }

    @DisplayName("Die InvalidBookingException ist korrekt implementiert.")
    @Test
    void testInvalidBookingException() {
        assertExceptionDefinedCorrectly(ClassReference.INVALID_BOOKING_EXCEPTION);
    }

    @DisplayName("Die BookingAlreadyCancelledException ist korrekt implementiert.")
    @Test
    void testBookingAlreadyCancelledException() {
        assertExceptionDefinedCorrectly(ClassReference.BOOKING_ALREADY_CANCELLED_EXCEPTION);
    }

    @DisplayName("Die DuplicateBookingException ist korrekt implementiert.")
    @Test
    void testDuplicateBookingException() {
        assertExceptionDefinedCorrectly(ClassReference.DUPLICATE_BOOKING_EXCEPTION);
    }
}

package h08;

import h08.assertions.Links;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

/**
 * Defines the private tests for the subtask H08.5.2.
 *
 * @author Nhan Huynh
 */
@TestForSubmission
@DisplayName("H08.5.2 | Flight and Booking Management")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class H08_5_2_TestsPrivate extends H08_Tests {

    @Test
    void testManageFlightCode() {
        FlightManagement management = Mockito.mock(FlightManagement.class, Mockito.CALLS_REAL_METHODS);
        TypeLink airportLink = Links.getType(FlightManagement.class);
        FieldLink airportsLink = airportLink.getField(Matcher.of(field -> field.name().equals("airports")));
        FieldLink sizeLink = airportLink.getField(Matcher.of(field -> field.name().equals("size")));
        Airport[] airports = new Airport[5];
        for (int i = 0; i < airports.length; i++) {
            airports[i] = Mockito.mock(Airport.class);
            Mockito.doReturn("Airport " + i).when(airports[i]).getAirportCode();
        }
        airportsLink.set(management, airports);
        sizeLink.set(management, airports.length);

        Flight flight = Mockito.mock(Flight.class);
        Airport airport = airports[0];
        management.manageFlight(airport.getAirportCode(), flight, true);
        Mockito.verify(airport, Mockito.times(1)).addFlight(Mockito.eq(flight), Mockito.anyBoolean());
    }
}

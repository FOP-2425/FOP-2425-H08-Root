package h08.mock;

import h08.Airport;
import h08.Flight;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * A mock class for testing the Airport class.
 *
 * @author Nhan Huynh
 */
public class MockAirport extends Airport {

    /**
     * The departing flights field link.
     */
    private final FieldLink departingFlightsLink;

    /**
     * The arriving flights field link.
     */
    private final FieldLink arrivingFlightsLink;

    /**
     * The departing size field link.
     */
    private final FieldLink departingSize;

    /**
     * The arriving size field link.
     */
    private final FieldLink arrivingSize;

    /**
     * Constructs a new mock airport with the specified airport code and initial capacity.
     *
     * @param airportCode     the code of the airport
     * @param initialCapacity the initial capacity of the airport
     */
    public MockAirport(
        String airportCode,
        int initialCapacity,
        List<MockFlight> departingFlights,
        List<MockFlight> arrivingFlights
    ) {
        super(airportCode, initialCapacity);
        TypeLink type = BasicTypeLink.of(Airport.class);
        departingFlightsLink = type.getField(Matcher.of(field -> field.name().equals("departingFlights")));
        arrivingFlightsLink = type.getField(Matcher.of(field -> field.name().equals("arrivingFlights")));
        departingSize = type.getField(Matcher.of(field -> field.name().equals("departingSize")));
        arrivingSize = type.getField(Matcher.of(field -> field.name().equals("arrivingSize")));

        copyFlights(departingFlightsLink, departingFlights);
        copyFlights(arrivingFlightsLink, arrivingFlights);
    }

    /**
     * Copies the flights to the specified field link.
     *
     * @param link    the field link to copy the flights to
     * @param flights the flights to copy
     */
    private void copyFlights(FieldLink link, List<MockFlight> flights) {
        Flight[] actual = link.get(this);
        if (flights.size() > actual.length) {
            actual = new Flight[flights.size()];
        }
        link.set(this, actual);
        System.arraycopy(flights.toArray(Flight[]::new), 0, actual, 0, flights.size());
    }

    /**
     * Gets the departing flights of the airport.
     *
     * @return the departing flights of the airport
     */
    public Flight[] getDepartingFlights() {
        return departingFlightsLink.get(this);
    }

    /**
     * Gets the arriving flights of the airport.
     *
     * @return the arriving flights of the airport
     */
    public Flight[] getArrivingFlights() {
        return arrivingFlightsLink.get(this);
    }

    /**
     * Gets the number of departing flights of the airport.
     *
     * @return the number of departing flights of the airport
     */
    public int getDepartingSize() {
        return departingSize.get(this);
    }

    /**
     * Gets the number of arriving flights to the airport.
     *
     * @return the number of arriving flights to the airport
     */
    public int getArrivingSize() {
        return arrivingSize.get(this);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Airport that)) return false;
        return Objects.equals(getAirportCode(), that.getAirportCode())
            && Arrays.equals(getDepartingFlights(), departingFlightsLink.<Flight[]>get(that))
            && Arrays.equals(getArrivingFlights(), arrivingFlightsLink.<Flight[]>get(that))
            && getDepartingSize() == departingSize.<Integer>get(that)
            && getArrivingSize() == arrivingSize.<Integer>get(that);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departingFlightsLink, arrivingFlightsLink, getDepartingSize(), getArrivingSize());
    }

    /**
     * Converts the flights to a string representation.
     *
     * @param flights the flights to convert
     *
     * @return the string representation of the flights
     */
    private String flightsToString(Flight[] flights) {
        return Arrays.stream(flights).map(it -> it == null ? null : it.getFlightNumber()).toList().toString();
    }

    @Override
    public String toString() {
        return "Airport{"
            + "departingFlights="
            + flightsToString(getDepartingFlights())
            + ", arrivingFlights="
            + flightsToString(getArrivingFlights())
            + '}';
    }
}

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
    private final FieldLink departingSizeLink;

    /**
     * The arriving size field link.
     */
    private final FieldLink arrivingSizeLink;

    /**
     * Constructs a new mock airport with the specified airport code, initial capacity, and flights.
     *
     * @param airportCode      the code of the airport
     * @param initialCapacity  the initial capacity of the airport
     * @param departingFlights the departing flights of the airport
     * @param arrivingFlights  the arriving flights of the airport
     */
    public MockAirport(
        String airportCode,
        int initialCapacity,
        List<MockFlight> departingFlights,
        List<MockFlight> arrivingFlights
    ) {
        this(airportCode, initialCapacity, departingFlights, arrivingFlights, departingFlights.size(), arrivingFlights.size());
    }

    /**
     * Constructs a new mock airport with the specified airport code, initial capacity, and flights.
     *
     * @param airportCode      the code of the airport
     * @param initialCapacity  the initial capacity of the airport
     * @param departingFlights the departing flights of the airport
     * @param arrivingFlights  the arriving flights of the airport
     * @param departingSize    the number of departing flights
     * @param arrivingSize     the number of arriving flights
     */
    public MockAirport(
        String airportCode,
        int initialCapacity,
        List<MockFlight> departingFlights,
        List<MockFlight> arrivingFlights,
        int departingSize,
        int arrivingSize
    ) {
        super(airportCode, initialCapacity);
        TypeLink type = BasicTypeLink.of(Airport.class);
        departingFlightsLink = type.getField(Matcher.of(field -> field.name().equals("departingFlights")));
        arrivingFlightsLink = type.getField(Matcher.of(field -> field.name().equals("arrivingFlights")));
        departingSizeLink = type.getField(Matcher.of(field -> field.name().equals("departingSize")));
        arrivingSizeLink = type.getField(Matcher.of(field -> field.name().equals("arrivingSize")));
        departingSizeLink.set(this, departingSize);
        arrivingSizeLink.set(this, arrivingSize);
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
    public int getDepartingSizeLink() {
        return departingSizeLink.get(this);
    }

    /**
     * Gets the number of arriving flights to the airport.
     *
     * @return the number of arriving flights to the airport
     */
    public int getArrivingSizeLink() {
        return arrivingSizeLink.get(this);
    }

    /**
     * Since we are using Mockito to mock the Flight class, we need to override the equals method to compare the
     * properties of the Flight class.
     *
     * @param flights1 the first array of flights
     * @param flights2 the second array of flights
     *
     * @return true if the flights are equal, false otherwise
     */
    private boolean flightsEqual(Flight[] flights1, Flight[] flights2) {
        if (flights1.length != flights2.length) {
            return false;
        }
        for (int i = 0; i < flights1.length; i++) {
            Flight flight1 = flights1[i];
            Flight flight2 = flights2[i];
            if (flight1 == null && flight2 == null) {
                continue;
            }
            if (flight1 == null) {
                return false;
            }
            if (!Objects.equals(flight1.getFlightNumber(), flight2.getFlightNumber())
                || !Objects.equals(flight1.getDeparture(), flight2.getDeparture())
                || !Objects.equals(flight1.getDestination(), flight2.getDestination())
                || !Objects.equals(flight1.getDepartureTime(), flight2.getDepartureTime())
                || flight1.getAvailableSeats() != flight2.getAvailableSeats()
            ) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Airport that)) return false;
        return Objects.equals(getAirportCode(), that.getAirportCode())
            && flightsEqual(getDepartingFlights(), departingFlightsLink.<Flight[]>get(that))
            && flightsEqual(getArrivingFlights(), arrivingFlightsLink.<Flight[]>get(that))
            && getDepartingSizeLink() == departingSizeLink.<Integer>get(that)
            && getArrivingSizeLink() == arrivingSizeLink.<Integer>get(that);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departingFlightsLink, arrivingFlightsLink, getDepartingSizeLink(), getArrivingSizeLink());
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

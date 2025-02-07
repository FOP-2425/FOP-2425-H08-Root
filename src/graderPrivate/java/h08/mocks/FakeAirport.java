package h08.mocks;

import h08.Airport;
import h08.Flight;
import h08.TutorUtils;
import h08.assertions.Links;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.Arrays;
import java.util.Objects;

/**
 * Extension of Airport to make testing easier.
 *
 * @author Nhan Huynh
 */
public class FakeAirport extends Airport {

    /**
     * The field link to the flights departing from this airport.
     */
    private final FieldLink departingFlightsLink;

    /**
     * The field link to the flights arriving at this airport.
     */
    private final FieldLink arrivingFlightsLink;

    /**
     * The field link to the number of departing flights.
     */
    private final FieldLink departingSizeLink;

    /**
     * The field link to the number of arriving flights.
     */
    private final FieldLink arrivingSizeLink;


    /**
     * Constructs a new airport with the specified airport code and initial capacity.
     *
     * @param airportCode     the code of the airport
     * @param initialCapacity the initial capacity of the airport
     */
    public FakeAirport(String airportCode, int initialCapacity) {
        this(airportCode, initialCapacity, new Flight[initialCapacity], new Flight[initialCapacity], 0, 0);
    }

    /**
     * Constructs a new airport with the specified airport code, initial capacity, and flights.
     *
     * @param airportCode      the code of the airport
     * @param initialCapacity  the initial capacity of the airport
     * @param departingFlights the flights departing from this airport
     * @param arrivingFlights  the flights arriving at this airport
     * @param departingSize    the number of departing flights
     * @param arrivingSize     the number of arriving flights
     */
    public FakeAirport(
        String airportCode,
        int initialCapacity,
        Flight[] departingFlights,
        Flight[] arrivingFlights,
        int departingSize,
        int arrivingSize
    ) {
        super(airportCode, initialCapacity);
        TypeLink typeLink = Links.getType(Airport.class);
        this.departingFlightsLink = typeLink.getField(Matcher.of(field -> field.name().equals("departingFlights")));
        this.arrivingFlightsLink = typeLink.getField(Matcher.of(field -> field.name().equals("arrivingFlights")));
        this.departingSizeLink = typeLink.getField(Matcher.of(field -> field.name().equals("departingSize")));
        this.arrivingSizeLink = typeLink.getField(Matcher.of(field -> field.name().equals("arrivingSize")));

        copyFlights(this.departingFlightsLink, departingFlights);
        copyFlights(this.arrivingFlightsLink, arrivingFlights);
        this.departingSizeLink.set(this, departingSize);
        this.arrivingSizeLink.set(this, arrivingSize);
    }

    /**
     * Sets the capacity of flights departing from this airport.
     *
     * @param link    the field link to set the capacity of flights departing from this airport
     * @param flights the capacity of flights departing from this airport
     */
    private void copyFlights(FieldLink link, Flight[] flights) {
        Flight[] actual = link.get(this);
        if (flights.length > actual.length) {
            actual = new Flight[flights.length];
        }
        link.set(this, actual);
        System.arraycopy(flights, 0, actual, 0, flights.length);
    }

    /**
     * Returns the capacity of flights departing from this airport.
     *
     * @return the capacity of flights departing from this airport
     */
    public Flight[] getDepartingFlights() {
        return departingFlightsLink.get(this);
    }

    /**
     * Returns the capacity of flights arriving from this airport.
     *
     * @return the capacity of flights arriving from this airport
     */
    public Flight[] getArrivingFlights() {
        return arrivingFlightsLink.get(this);
    }

    /**
     * Returns the number of departing flights.
     *
     * @return the number of departing flights
     */
    public int getDepartingSize() {
        return departingSizeLink.get(this);
    }

    /**
     * Returns the number of arriving flights.
     *
     * @return the number of arriving flights
     */
    public int getArrivingSize() {
        return arrivingSizeLink.get(this);
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Airport that)) {
            return false;
        }
        return Objects.equals(getAirportCode(), that.getAirportCode())
            && TutorUtils.equalFlights(getDepartingFlights(), departingFlightsLink.<Flight[]>get(that))
            && TutorUtils.equalFlights(getArrivingFlights(), arrivingFlightsLink.<Flight[]>get(that))
            && getDepartingSize() == departingSizeLink.<Integer>get(that)
            && getArrivingSize() == arrivingSizeLink.<Integer>get(that);
    }

    /**
     * Returns a string representation of the flights.
     *
     * @param flights the flights to convert to a string
     *
     * @return a string representation of the flights
     */
    private String flightsToString(Flight[] flights) {
        return Arrays.stream(flights).map(it -> it == null ? null : it.getFlightNumber()).toList().toString();
    }

    @Override
    public String toString() {
        return "Airport{"
            + "departingFlights=" + flightsToString(getDepartingFlights())
            + ", departingSize=" + getDepartingSize()
            + ", arrivingFlights=" + flightsToString(getArrivingFlights())
            + ", arrivingSize=" + getArrivingSize()
            + '}';
    }
}

package h08;

import h08.Exceptions.FlightNotFoundException;

import java.util.Arrays;

/**
 * Represents an airport. An airport manages departing and arriving flights, allowing for their addition, removal, and retrieval based on the airport code.
 */
public class Airport {

    /**
     * The code of the airport.
     */
    private String airportCode;

    /**
     * The departing flights of the airport.
     */
    private Flight[] departingFlights;

    /**
     * The arriving flights to the airport.
     */
    private Flight[] arrivingFlights;

    /**
     * The number of departing flights of the airport.
     */
    private int departingSize;

    /**
     * The number of arriving flights to the airport.
     */
    private int arrivingSize;

    /**
     * Constructs a new airport with the specified airport code and initial capacity.
     *
     * @param airportCode     the code of the airport
     * @param initialCapacity the initial capacity of the airport
     */
    public Airport(String airportCode, int initialCapacity) {
        this.airportCode = airportCode;
        this.departingFlights = new Flight[initialCapacity];
        this.arrivingFlights = new Flight[initialCapacity];
        this.departingSize = 0;
        this.arrivingSize = 0;
    }

    /**
     * Adds a departing flight.
     *
     * @param flight the departing flight to add
     * @throws IllegalArgumentException if the flight's departure airport code doesn't match the airport's code
     */
    public void addDepartingFlight(Flight flight) throws IllegalArgumentException {
        if (!flight.getDeparture().equals(airportCode)) {
            throw new IllegalArgumentException("Flight's departure airport code does not match this airport's code");
        }
        if (departingSize >= departingFlights.length) {
            departingFlights = Arrays.copyOf(departingFlights, departingFlights.length * 2);
        }
        departingFlights[departingSize++] = flight;
    }

    /**
     * Adds an arriving flight.
     *
     * @param flight the arrving flight to add
     */
    public void addArrivingFlight(Flight flight){
        if (!flight.getDestination().equals(airportCode)) {
            throw new IllegalArgumentException("Flight's arrival airport code does not match this airport's code");
        }
        if (arrivingSize >= arrivingFlights.length) {
            arrivingFlights = Arrays.copyOf(arrivingFlights, arrivingFlights.length * 2);
        }
        arrivingFlights[arrivingSize++] = flight;
    }

    /**
     * Removes a departing flight by flight number.
     *
     * @param flightNumber the departing flight number
     * @throws FlightNotFoundException if the departing flight is not found
     */
    public void removeDepartingFlight(String flightNumber) throws FlightNotFoundException {
        for (int i = 0; i < departingSize; i++) {
            if (departingFlights[i].getFlightNumber().equals(flightNumber)) {
                departingFlights[i] = departingFlights[--departingSize];
                departingFlights[departingSize] = null;
                return;
            }
        }
        throw new FlightNotFoundException("Departing flight not found: " + flightNumber);
    }

    /**
     * Removes an arriving flight by flight number.
     *
     * @param flightNumber the arriving flight number
     * @throws FlightNotFoundException if the arriving flight is not found
     */
    public void removeArrivingFlight(String flightNumber) throws FlightNotFoundException {
        for (int i = 0; i < arrivingSize; i++) {
            if (arrivingFlights[i].getFlightNumber().equals(flightNumber)) {
                arrivingFlights[i] = arrivingFlights[--arrivingSize];
                arrivingFlights[arrivingSize] = null;
                return;
            }
        }
        throw new FlightNotFoundException("Arriving flight not found: " + flightNumber);
    }

    /**
     * Returns a departing flight by flight number.
     *
     * @return a departing flight by flight number
     * @throws FlightNotFoundException if the departing flight is not found
     */
    public Flight getDepartingFlight(String flightNumber) throws FlightNotFoundException {
        for (int i = 0; i < departingSize; i++) {
            if (departingFlights[i].getFlightNumber().equals(flightNumber)) {
                return departingFlights[i];
            }
        }
        throw new FlightNotFoundException("Departing flight not found: " + flightNumber);
    }

    /**
     * Returns an arriving flight by flight number.
     *
     * @return an arriving flight by flight number
     * @throws FlightNotFoundException if the arriving flight is not found
     */
    public Flight getArrivingFlight(String flightNumber) throws FlightNotFoundException {
        for (int i = 0; i < arrivingSize; i++) {
            if (arrivingFlights[i].getFlightNumber().equals(flightNumber)) {
                return arrivingFlights[i];
            }
        }
        throw new FlightNotFoundException("Arriving flight not found: " + flightNumber);
    }

    /**
     * Returns the departing flights of the airport.
     *
     * @return the departing flights of the airport
     */
    public Flight[] getAllDepartingFlights() {
        return Arrays.copyOf(departingFlights, departingSize);
    }

    /**
     * Returns the arriving flights to the airport.
     *
     * @return the arriving flights to the airport
     */
    public Flight[] getAllArrivingFlights() {
        return Arrays.copyOf(arrivingFlights, arrivingSize);
    }

    /**
     * Returns the airport code.
     *
     * @return the airport code
     */
    public String getAirportCode() {
        return airportCode;
    }
}

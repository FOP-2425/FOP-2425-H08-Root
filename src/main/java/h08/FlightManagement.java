package h08;

import h08.Exceptions.FlightNotFoundException;

import java.util.Arrays;

/**
 * Represents a flight management. A flight management oversees the management of flights and airports.
 */
public class FlightManagement {

    /**
     * The airports whose flights are managed.
     */
    private Airport[] airports;

    /**
     * The current number of airports whose flights are managed.
     */
    private int size;

    /**
     * Constructs a new flight management with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity
     */
    public FlightManagement(int initialCapacity) {
        this.airports = new Airport[initialCapacity];
        this.size = 0;
    }

    /**
     * Adds an airport to the flight management.
     *
     * @param airport the airport to be added
     */
    public void addAirport(Airport airport) {
        if (size >= airports.length) {
            airports = Arrays.copyOf(airports, airports.length * 2);
        }
        airports[size++] = airport;
    }

    /**
     * Manages the addition or removal of a flight for a specific airport.
     *
     * @param airportCode    the airport code where the flight should be managed
     * @param flight         the flight to be added or removed
     * @param isAddOperation if true, the flight will be added; if false, the flight will be removed
     */
    public void manageFlight(String airportCode, Flight flight, boolean isAddOperation) {
        try {
            Airport airport = searchAirport(airportCode);
            String flightNumber = flight.getFlightNumber();
            boolean operationSuccessful = false;
            if (isAddOperation) {
                try {
                    airport.addFlight(flight, true);
                    operationSuccessful = true;
                } catch (Exception ignored) {
                }
                if (!operationSuccessful) {
                    try {
                        airport.addFlight(flight, false);
                    } catch (Exception e2) {
                        throw new IllegalArgumentException("Flight's airport codes do not match the provided airport code");
                    }
                }
            } else {
                try {
                    airport.removeFlight(flightNumber, true);
                    operationSuccessful = true;
                } catch (Exception ignored) {
                }
                if (!operationSuccessful) {
                    try {
                        airport.removeFlight(flightNumber, false);
                    } catch (Exception e2) {
                        System.out.println("Error removing flight: " + e2.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error managing flight: " + e.getMessage());
        }
    }


    /**
     * Returns a flight from a specific airport.
     *
     * @param airportCode  the airport code from which the flight should be returned
     * @param flightNumber the flight number of the flight
     *
     * @return a flight from a specific airport
     */
    public Flight getFlight(String airportCode, String flightNumber) {
        try {
            Airport airport = searchAirport(airportCode);
            Flight flight = searchFlight(airport, flightNumber);
            if (flight == null) {
                throw new FlightNotFoundException("Flight not found: " + flightNumber);
            }
            return flight;
        } catch (Exception e) {
            System.out.println("Error retrieving flight: " + e.getMessage());
            return null;
        }
    }

    /**
     * Returns a flight with a specified flight number.
     *
     * @param flightNumber the flight number of the flight
     *
     * @return a flight with a specified flight number
     */
    public Flight getFlight(String flightNumber) {
        for (int i = 0; i < size; i++) {
            Flight flight = searchFlight(airports[i], flightNumber);
            if (flight != null) {
                return flight;
            }
        }
        System.out.println("Error retrieving flight: Flight not found: " + flightNumber);
        return null;
    }

    /**
     * Searches for an airport by airport code.
     *
     * @param airportCode the airport code
     *
     * @return an airport by airport code
     * @throws Exception if the airport ist not found
     */
    private Airport searchAirport(String airportCode) throws Exception {
        for (int i = 0; i < size; i++) {
            if (airports[i].getAirportCode().equals(airportCode)) {
                return airports[i];
            }
        }
        throw new Exception("Airport not found: " + airportCode);
    }

    /**
     * Searches for a flight in departing or arriving flights.
     *
     * @param airport      the airport in which the flight should be searched
     * @param flightNumber the flight number of the flight
     *
     * @return a flight in departing or arriving flights
     */
    private Flight searchFlight(Airport airport, String flightNumber) {
        try {
            return airport.getFlight(flightNumber, true);
        } catch (FlightNotFoundException ignored) {
        }
        try {
            return airport.getFlight(flightNumber, false);
        } catch (FlightNotFoundException ignored) {
        }
        return null;
    }
}

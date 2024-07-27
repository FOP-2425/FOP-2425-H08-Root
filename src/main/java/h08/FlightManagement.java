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
     * Adds a flight to a specific airport.
     *
     * @param airportCode the airport code to which the flight should be added
     * @param flight      the flight to be added
     * @throws IllegalArgumentException if the flight's airport codes do not match the provided airport code
     */
    public void addFlight(String airportCode, Flight flight) {
        try {
            Airport airport = searchAirport(airportCode);
            boolean added = false;
            try {
                airport.addDepartingFlight(flight);
                added = true;
            } catch (Exception e1) {
                try {
                    airport.addArrivingFlight(flight);
                    added = true;
                } catch (Exception e2) {
                    if (!added) {
                        throw new IllegalArgumentException("Flight's airport codes do not match the provided airport code");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error adding flight: " + e.getMessage());
        }
    }

    /**
     * Removes a flight from a specific airport.
     *
     * @param airportCode  the airport code from which the flight should be removed
     * @param flightNumber the flight number of the flight
     * @throws FlightNotFoundException if the flight ist not found
     */
    public void removeFlight(String airportCode, String flightNumber) {
        try {
            Airport airport = searchAirport(airportCode);
            boolean removed = removeDepartingOrArrivingFlight(airport, flightNumber);
            if (!removed) {
                throw new FlightNotFoundException("Flight not found: " + flightNumber);
            }
        } catch (Exception e) {
            System.out.println("Error removing flight: " + e.getMessage());
        }
    }

    /**
     * Returns a flight from a specific airport.
     *
     * @param airportCode  the airport code from which the flight should be returned
     * @param flightNumber the flight number of the flight
     * @return a flight from a specific airport
     * @throws FlightNotFoundException if the flight ist not found
     */
    public Flight getFlight(String airportCode, String flightNumber) {
        try {
            Airport airport = searchAirport(airportCode);
            Flight flight = findDepartingOrArrivingFlight(airport, flightNumber);
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
     * @return a flight with a specified flight number
     */
    public Flight getFlight(String flightNumber){
        for (int i = 0; i < size; i++) {
            try {
                return airports[i].getDepartingFlight(flightNumber);
            } catch (FlightNotFoundException e) {
                try {
                    return airports[i].getArrivingFlight(flightNumber);
                } catch (FlightNotFoundException ignored) {
                }
            }
        }
        System.out.println("Error retrieving flight: Flight not found: " + flightNumber);
        return null;
    }

    /**
     * Searches for an airport by airport code.
     *
     * @param airportCode the airport code
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
     * @return a flight in departing or arriving flights
     */
    private Flight findDepartingOrArrivingFlight(Airport airport, String flightNumber) {
        try {
            return airport.getDepartingFlight(flightNumber);
        } catch (FlightNotFoundException e) {
            try {
                return airport.getArrivingFlight(flightNumber);
            } catch (FlightNotFoundException ignored) {
                return null;
            }
        }
    }

    /**
     * Removes a flight from departing or arriving flights.
     *
     * @param airport      the airport from which the flight should be removed
     * @param flightNumber the flight number of the flight
     * @return {@code true} if the flight has been removed from departing or arriving flights
     */
    private boolean removeDepartingOrArrivingFlight(Airport airport, String flightNumber) {
        try {
            airport.removeDepartingFlight(flightNumber);
            return true;
        } catch (FlightNotFoundException e) {
            try {
                airport.removeArrivingFlight(flightNumber);
                return true;
            } catch (FlightNotFoundException ignored) {
                return false;
            }
        }
    }
}

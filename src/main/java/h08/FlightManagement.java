package h08;

import java.util.Arrays;

public class FlightManagement {
    private Airport[] airports;
    private int size;

    // Constructor to initialize the flight management system with a capacity
    public FlightManagement(int initialCapacity) {
        this.airports = new Airport[initialCapacity];
        this.size = 0;
    }

    // Method to add an airport
    public void addAirport(Airport airport) {
        if (size >= airports.length) {
            airports = Arrays.copyOf(airports, airports.length * 2);
        }
        airports[size++] = airport;
    }

    // Method to add a flight to a specific airport
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

    // Method to remove a flight from a specific airport
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

    // Method to get a flight from a specific airport
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

    // Helper method to search for an airport by airport code
    private Airport searchAirport(String airportCode) throws Exception {
        for (int i = 0; i < size; i++) {
            if (airports[i].getAirportCode().equals(airportCode)) {
                return airports[i];
            }
        }
        throw new Exception("Airport not found: " + airportCode);
    }

    // Helper method to find a flight in departing or arriving flights
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

    // Helper method to remove a flight from departing or arriving flights
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




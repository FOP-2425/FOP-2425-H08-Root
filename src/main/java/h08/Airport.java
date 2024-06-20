package h08;

import h08.Flight;

import java.util.Arrays;

public class Airport {
    private String airportCode;
    private Flight[] departingFlights;
    private Flight[] arrivingFlights;
    private int departingSize;
    private int arrivingSize;

    // Constructor to initialize the airport attributes
    public Airport(String airportCode, int initialCapacity) {
        this.airportCode = airportCode;
        this.departingFlights = new Flight[initialCapacity];
        this.arrivingFlights = new Flight[initialCapacity];
        this.departingSize = 0;
        this.arrivingSize = 0;
    }

    // Method to add a departing flight
    public void addDepartingFlight(Flight flight) throws IllegalArgumentException {
        if (!flight.getDeparture().equals(airportCode)) {
            throw new IllegalArgumentException("Flight's departure airport code does not match this airport's code");
        }
        if (departingSize >= departingFlights.length) {
            departingFlights = Arrays.copyOf(departingFlights, departingFlights.length * 2);
        }
        departingFlights[departingSize++] = flight;
    }

    // Method to add an arriving flight
    public void addArrivingFlight(Flight flight){
        if (!flight.getDestination().equals(airportCode)) {
            throw new IllegalArgumentException("Flight's arrival airport code does not match this airport's code");
        }
        if (arrivingSize >= arrivingFlights.length) {
            arrivingFlights = Arrays.copyOf(arrivingFlights, arrivingFlights.length * 2);
        }
        arrivingFlights[arrivingSize++] = flight;
    }

    // Method to remove a departing flight by flight number
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

    // Method to remove an arriving flight by flight number
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

    // Method to get a departing flight by flight number
    public Flight getDepartingFlight(String flightNumber) throws FlightNotFoundException {
        for (int i = 0; i < departingSize; i++) {
            if (departingFlights[i].getFlightNumber().equals(flightNumber)) {
                return departingFlights[i];
            }
        }
        throw new FlightNotFoundException("Departing flight not found: " + flightNumber);
    }

    // Method to get an arriving flight by flight number
    public Flight getArrivingFlight(String flightNumber) throws FlightNotFoundException {
        for (int i = 0; i < arrivingSize; i++) {
            if (arrivingFlights[i].getFlightNumber().equals(flightNumber)) {
                return arrivingFlights[i];
            }
        }
        throw new FlightNotFoundException("Arriving flight not found: " + flightNumber);
    }

    // Method to get all departing flights
    public Flight[] getAllDepartingFlights() {
        return Arrays.copyOf(departingFlights, departingSize);
    }

    // Method to get all arriving flights
    public Flight[] getAllArrivingFlights() {
        return Arrays.copyOf(arrivingFlights, arrivingSize);
    }

    // Getter for airport code
    public String getAirportCode() {
        return airportCode;
    }
}

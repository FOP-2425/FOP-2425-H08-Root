package h08;


import java.time.LocalDateTime;

public class Flight {
    private String flightNumber;
    private String departure;
    private String destination;
    private LocalDateTime departureTime;
    private int availableSeats;


    public Flight(String flightNumber, String departure, String destination, LocalDateTime departureTime, int availableSeats) {
        assert flightNumber != null;
        assert departure != null;
        assert destination != null;
        assert departureTime != null;
        assert availableSeats >= 0;
        this.flightNumber = flightNumber;
        this.departure = departure;
        this.destination = destination;
        this.departureTime = departureTime;
        this.availableSeats = availableSeats;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getDeparture() {
        return departure;
    }

    public String getDestination() {
        return destination;
    }

    public String getDepartureTime() {
        return departureTime.toString();
    }


    public int getavailableSeats() {
        return availableSeats;
    }

    // Method to book a seat
    public void bookSeat() throws NoSeatsAvailableException {
        if (availableSeats <= 0) {
            throw new NoSeatsAvailableException("No seats available for flight: " + flightNumber);
        }
        availableSeats--;
    }

    // Method to cancel a seat
    public void cancelSeat() {
        availableSeats++;
    }

    @Override
    public String toString() {
        return "Flight{" +
            "flightNumber='" + flightNumber + '\'' +
            ", departure='" + departure + '\'' +
            ", destination='" + destination + '\'' +
            ", departureTime='" + departureTime + '\'' +
            '}';
    }
}

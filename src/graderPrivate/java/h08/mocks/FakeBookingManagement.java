package h08.mocks;

import h08.Booking;
import h08.BookingManagement;
import h08.FlightManagement;
import h08.assertions.Links;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.ArrayList;
import java.util.List;

public class FakeBookingManagement extends BookingManagement {

    private final List<Booking> bookings = new ArrayList<>();
    private final FieldLink bookingsLink;
    private final FieldLink sizeLink;
    private final FieldLink flightManagementLink;

    /**
     * Constructs a new booking management with the specified initial capacity and flight management.
     *
     * @param initialCapacity  the initial number of bookings that can be managed
     * @param flightManagement the flight management for the bookings
     */
    public FakeBookingManagement(int initialCapacity, FlightManagement flightManagement) {
        super(initialCapacity, flightManagement);
        TypeLink typeLink = Links.getType(BookingManagement.class);
        this.bookingsLink = Links.getField(typeLink, "bookings");
        this.sizeLink = Links.getField(typeLink, "size");
        this.flightManagementLink = Links.getField(typeLink, "flightManagement");
    }

    public FakeBookingManagement(FlightManagement flightManagement, List<Booking> bookings) {
        this(bookings.size(), flightManagement);
        setBookings(bookings);
    }

    public void update() {
        setBookings(getBookings());
    }

    public Booking[] getBookings() {
        return bookingsLink.get(this);
    }

    public int size() {
        return sizeLink.get(this);
    }

    private void setSize(int size) {
        sizeLink.set(this, size);
    }

    public FlightManagement getFlightManagement() {
        return flightManagementLink.get(this);
    }

    public void setBookings(Booking[] bookings) {
        setBookings(List.of(bookings));
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings.clear();
        this.bookings.addAll(bookings);
        this.bookingsLink.set(this, this.bookings.toArray(Booking[]::new));
        setSize(bookings.size());
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
        update();
    }

    @Override
    public String toString() {
        return "FakeBookingManagement{" +
            "bookings=" + bookings +
            ", flightManagement=" + getFlightManagement() +
            '}';
    }
}

package h08;

import java.time.LocalDate;

public class Passenger {
    private String passengerID;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    public Passenger(String firstName, String lastName, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.passengerID = generatePassengerID(firstName, lastName, dateOfBirth);
    }

    private String generatePassengerID(String firstName, String lastName, LocalDate dateOfBirth) {
        String initials = firstName.charAt(0) + String.valueOf(lastName.charAt(0));
        return initials + dateOfBirth.hashCode();
    }

    public String getPassengerID() {
        return passengerID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public String toString() {
        return "Passenger{" +
            "passengerID='" + passengerID + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", dateOfBirth=" + dateOfBirth +
            '}';
    }
}

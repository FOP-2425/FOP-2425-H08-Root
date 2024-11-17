package h08;

import org.sourcegrade.jagr.api.rubric.*;

import static org.tudalgo.algoutils.tutor.general.jagr.RubricUtils.criterion;

public class H08_RubricProvider implements RubricProvider {

    private static final Criterion H8_1 = Criterion.builder()
            .shortDescription("H8.1 | Have your ID ready")
            .maxPoints(2)
            .addChildCriteria(
                    criterion("Die Methode generatePassengerID stellt sicher, dass die ersten zwei Zeichen der ID die Initialen des Vornamens und Nachnamens sind."),
                    criterion("Die Methode generatePassengerID stellt sicher, dass der Hash-Code des Datums korrekt im zweiten Teil der ID enthalten ist.")
            )
            .build();

    private static final Criterion H8_2_1 = Criterion.builder()
            .shortDescription("H8.2.1 | Let’s get in shape")
            .maxPoints(2)
            .addChildCriteria(
                    criterion("Die Methode validateFlightNumber überprüft die Flugnummer korrekt."),
                    criterion("Der Konstruktor der Klasse Flight enthält assert-Anweisungen, die die Eingaben überprüfen.")
            )
            .build();

    private static final Criterion H8_2_2 = Criterion.builder()
            .shortDescription("H8.2.2 | Fasten your seatbelts")
            .maxPoints(2)
            .addChildCriteria(
                    criterion("Die Methode bookSeat() reserviert korrekt Sitzplätze."),
                    criterion("Die Methode wirft korrekt eine NoSeatsAvailableException, wenn keine Plätze mehr verfügbar sind.")
            )
            .build();

    private static final Criterion H8_2 = Criterion.builder()
            .shortDescription("H8.2 | Flight Zone")
            .maxPoints(4)
            .addChildCriteria(
                    H8_2_1,
                    H8_2_2
            )
            .build();
    private static final Criterion H8_3 = Criterion.builder()
            .shortDescription("H8.3 | Exception Handling")
            .maxPoints(4)
            .addChildCriteria(
                    criterion("Die FlightManagementException ist korrekt implementiert."),
                    criterion("Die BookingManagementException ist korrekt implementiert."),
                    criterion("Die FlightNotFoundException, BookingNotFoundException und InvalidBookingException sind korrekt."),
                    criterion("Die BookingAlreadyCancelledException und DuplicateBookingException sind korrekt.")
            )
            .build();

    private static final Criterion H8_4_1 = Criterion.builder()
            .shortDescription("H8.4.1 | Adding a Flight")
            .maxPoints(2)
            .addChildCriteria(
                    criterion("Die Methode addFlight fügt Flüge korrekt zu abgehenden oder ankommenden Flügen hinzu."),
                    criterion("Die Methode prüft und behandelt korrekt falsche Flughafencodes.")
            )
            .build();

    private static final Criterion H8_4_2 = Criterion.builder()
            .shortDescription("H8.4.2 | Removing a Flight")
            .maxPoints(2)
            .addChildCriteria(
                    criterion("Die Methode removeFlight entfernt Flüge korrekt aus den Listen."),
                    criterion("Die Methode wirft korrekt eine FlightNotFoundException, wenn der Flug nicht gefunden wird.")
            )
            .build();

    private static final Criterion H8_4_3 = Criterion.builder()
            .shortDescription("H8.4.3 | Getting a Flight")
            .maxPoints(2)
            .addChildCriteria(
                    criterion("Die Methode getFlight gibt Flüge korrekt zurück."),
                    criterion("Die Methode wirft korrekt eine FlightNotFoundException, wenn der Flug nicht existiert.")
            )
            .build();

    private static final Criterion H8_4_4 = Criterion.builder()
            .shortDescription("H8.4.4 | Removing a booking")
            .maxPoints(2)
            .addChildCriteria(
                    criterion("Die Methode cancelBooking() storniert eine Buchung korrekt."),
                    criterion("Die Methode wirft korrekt eine BookingAlreadyCancelledException, wenn die Buchung bereits storniert wurde.")
            )
            .build();

    private static final Criterion H8_4 = Criterion.builder()
            .shortDescription("H8.4 | Airport Command Center")
            .maxPoints(8)
            .addChildCriteria(
                    H8_4_1,
                    H8_4_2,
                    H8_4_3,
                    H8_4_4
            )
            .build();

    private static final Criterion H8_5_1 = Criterion.builder()
            .shortDescription("H8.5.1 | Airport and Flight Lookup")
            .maxPoints(3)
            .addChildCriteria(
                    criterion("Die Methode searchAirport findet Flughäfen korrekt."),
                    criterion("Die Methode searchFlight durchsucht Flüge korrekt."),
                    criterion("Die Methode getFlight gibt Flüge korrekt zurück.")
            )
            .build();

    private static final Criterion H8_5_2 = Criterion.builder()
            .shortDescription("H8.5.2 | Flight and Booking Management")
            .maxPoints(4)
            .addChildCriteria(
                    criterion("Die Methode manageFlight verwaltet Flüge korrekt (Hinzufügen oder Entfernen)."),
                    criterion("Die Methode manageFlight prüft korrekt die Flughafencodes."),
                    criterion("Die Methode validateAndCheckBooking validiert Buchungsdetails korrekt."),
                    criterion("Die Methode validateAndCheckBooking prüft korrekt auf doppelte Buchungen.")
            )
            .build();

    private static final Criterion H8_5_3 = Criterion.builder()
            .shortDescription("H8.5.3 | Searching a Booking")
            .maxPoints(2)
            .addChildCriteria(
                    criterion("Die Methode searchBooking durchsucht Buchungen korrekt."),
                    criterion("Die Methode getBooking gibt Buchungen korrekt zurück.")
            )
            .build();

    private static final Criterion H8_5_4 = Criterion.builder()
            .shortDescription("H8.5.4 | Cancelling a Booking")
            .maxPoints(2)
            .addChildCriteria(
                    criterion("Die Methode cancelBooking storniert Buchungen korrekt."),
                    criterion("Die Methode gibt die richtigen Fehlermeldungen oder Bestätigungen aus.")
            )
            .build();

    private static final Criterion H8_5_5 = Criterion.builder()
            .shortDescription("H8.5.5 | Creating a Booking")
            .maxPoints(3)
            .addChildCriteria(
                    criterion("Die Methode createBooking erstellt Buchungen korrekt."),
                    criterion("Die Methode behandelt alle relevanten Ausnahmen korrekt."),
                    criterion("Die Methode validiert Buchungsdetails und reserviert Sitzplätze korrekt.")
            )
            .build();

    private static final Criterion H8_5 = Criterion.builder()
            .shortDescription("H8.5 | Booking and Flight Management")
            .maxPoints(14)
            .addChildCriteria(
                    H8_5_1,
                    H8_5_2,
                    H8_5_3,
                    H8_5_4,
                    H8_5_5
            )
            .build();

    private static final Rubric RUBRIC = Rubric.builder()
            .title("H08 | Flight Control: Navigating the Exceptions")
            .addChildCriteria(
                    H8_1,
                    H8_2,
                    H8_3,
                    H8_4,
                    H8_5
            )
            .build();

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }
}

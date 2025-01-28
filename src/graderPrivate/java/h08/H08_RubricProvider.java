package h08;

import h08.rubric.AtomicTask;
import h08.rubric.CompositeTask;
import h08.rubric.Subtask;
import h08.rubric.Task;
import h08.rubric.TaskRubricProvider;

import java.util.List;
import java.util.Map;

/**
 * Provides the rubrics for H08.
 *
 * @author Nhan Huynh
 */
public class H08_RubricProvider extends TaskRubricProvider {

    private static final Task H08_1 = AtomicTask.builder()
        .description("H08.1 | Have your ID ready")
        .testClassName("H08_1_Tests")
        .criterion("Die Methode generatePassengerID stellt sicher, dass die ersten zwei Zeichen der ID die Initialen des Vornamens und Nachnamens sind.", "testGeneratePassengerIDNameInitials")
        .criterion("Die Methode generatePassengerID stellt sicher, dass der Hash-Code des Datums korrekt im zweiten Teil der ID enthalten ist.", "testGeneratePassengerIDDateHash")
        .build();

    private static final Subtask H08_2_1 = Subtask.builder()
        .description("H8.2.1 | Let’s get in shape.")
        .testClassName("H08_2_1_Tests")
        .criterion("Die Methode validateFlightNumber überprüft die Flugnummer korrekt.", "testValidateFlightNumber")
        .criterion("Der Konstruktor der Klasse Flight enthält assert-Anweisungen, die die Eingaben überprüfen.", "testFlightConstructor")
        .build();

    private static final Subtask H08_2_2 = Subtask.builder()
        .description("H08.2.2 | Fasten your seatbelts")
        .testClassName("H08_2_2_Tests")
        .criterion("Die Methode bookSeat() reserviert korrekt Sitzplätze.", "testBookSeat")
        .criterion("Die Methode wirft korrekt eine NoSeatsAvailableException, wenn keine Plätze mehr verfügbar sind.", "testBookSeatNoSeatsAvailableException")
        .build();

    private static final Task H08_2 = CompositeTask.builder()
        .description("H08.2 | Flight Zone")
        .subtasks(H08_2_1, H08_2_2)
        .build();

    private static final Task H08_3 = AtomicTask.builder()
        .description("H08.3 | Exception Handling")
        .testClassName("H08_3_Tests")
        .criterion("Die FlightManagementException ist korrekt implementiert.", "testFlightManagementException")
        .criterion("Die BookingManagementException ist korrekt implementiert.", "testBookingManagementException")
        .criterion("Die FlightNotFoundException, BookingNotFoundException und InvalidBookingException sind korrekt.", Map.of(
            "testFlightNotFoundException", List.of(),
            "testBookingNotFoundException", List.of(),
            "testInvalidBookingException", List.of())
        )
        .criterion("Die BookingAlreadyCancelledException und DuplicateBookingException sind korrekt.", Map.of(
            "testBookingAlreadyCancelledException", List.of(),
            "testDuplicateBookingException", List.of())
        ).build();

    private static final Subtask H08_4_1 = Subtask.builder()
        .description("H08.4.1 | Adding a Flight")
        .testClassName("H08_4_1_Tests")
        .criterion("Die Methode addFlight fügt Flüge korrekt zu abgehenden oder ankommenden Flügen hinzu.", "testAddFlight")
        .criterion("Die Methode prüft und behandelt korrekt falsche Flughafencodes.", "testAddFlightInvalidAirportCode")
        .build();

    private static final Subtask H08_4_2 = Subtask.builder()
        .description("H08.4.2 | Removing a Flight")
        .testClassName("H08_4_2_Tests")
        .criterion("Die Methode removeFlight entfernt Flüge korrekt aus den Listen.", "testRemoveFlight")
        .criterion("Die Methode wirft korrekt eine FlightNotFoundException, wenn der Flug nicht gefunden wird.", "testRemoveFlightFlightNotFoundException")
        .build();

    private static final Subtask H08_4_3 = Subtask.builder()
        .description("H08.4.3 | Getting a Flight")
        .testClassName("H08_4_3_Tests")
        .criterion("Die Methode getFlight gibt Flüge korrekt zurück.", "testGetFlight")
        .criterion("Die Methode wirft korrekt eine FlightNotFoundException, wenn der Flug nicht existiert.", "testGetFlightFlightNotFoundException")
        .build();

    private static final Subtask H08_4_4 = Subtask.builder()
        .description("H8.4.4 | Removing a booking")
        .testClassName("H08_4_4_Tests")
        .criterion("Die Methode cancelBooking() storniert eine Buchung korrekt.", "testCancelBooking")
        .criterion("Die Methode wirft korrekt eine BookingAlreadyCancelledException, wenn die Buchung bereits storniert wurde.", "testCancelBookingBookingAlreadyCancelledException")
        .build();

    private static final Task H08_4 = CompositeTask.builder()
        .description("H08.4 | Airport Command Center")
        .subtasks(H08_4_1, H08_4_2, H08_4_3, H08_4_4)
        .build();

    private static final Subtask H08_5_1 = Subtask.builder()
        .description("H8.5.1 | Airport and Flight Lookup")
        .testClassName("H08_5_1_Tests")
        .criterion("Die Methode searchAirport findet Flughäfen korrekt.", "testSearchAirport")
        .criterion("Die Methode searchFlight durchsucht Flüge korrekt.", "testSearchFlight")
        .criterion("Die Methode getFlight gibt Flüge korrekt zurück.", "testGetFlight")
        .build();

    private static final Subtask H08_5_2 = Subtask.builder()
        .description("H08.5.2 | Flight and Booking Management")
         .testClassName("H08_5_2_Tests")
        .criterion("Die Methode manageFlight verwaltet Flüge korrekt (Hinzufügen oder Entfernen).", "testManageFlight")
        .criterion("Die Methode manageFlight prüft korrekt die Flughafencodes.", "testManageFlightInvalidAirportCode")
        .criterion("Die Methode validateAndCheckBooking validiert Buchungsdetails korrekt.", "testValidateAndCheckBooking")
        .criterion("Die Methode validateAndCheckBooking prüft korrekt auf doppelte Buchungen.", "testValidateAndCheckBookingDuplicateBooking")
        .build();

    private static final Subtask H08_5_3 = Subtask.builder()
        .description("H8.5.3 | Searching a Booking")
         .testClassName("H08_5_3_Tests")
        .criterion("Die Methode searchBooking durchsucht Buchungen korrekt.", "testSearchBooking")
        .criterion("Die Methode getBooking gibt Buchungen korrekt zurück.", "testGetBooking")
        .build();

    private static final Subtask H08_5_4 = Subtask.builder()
        .description("H08.5.4 | Cancelling a Booking")
         .testClassName("H08_5_4_Tests")
        .criterion("Die Methode cancelBooking storniert Buchungen korrekt.", "testCancelBooking")
        .criterion("Die Methode gibt die richtigen Fehlermeldungen oder Bestätigungen aus.", "testCancelBookingMessages")
        .build();

    private static final Subtask H08_5_5 = Subtask.builder()
        .description("H08.5.5 | Creating a Booking")
        .testClassName("H08_5_5_Tests")
        .criterion("Die Methode createBooking erstellt Buchungen korrekt.", "testCreateBooking")
        .criterion("Die Methode behandelt alle relevanten Ausnahmen korrekt.", "testCreateBookingExceptions")
        .criterion("Die Methode validiert Buchungsdetails und reserviert Sitzplätze korrekt.", "testCreateBookingValidation")
        .build();

    private static final Task H08_5 = CompositeTask.builder()
        .description("H08.5 | Booking and Flight Management")
        .subtasks(H08_5_1, H08_5_2, H08_5_3, H08_5_4, H08_5_5)
        .build();

    public H08_RubricProvider() {
        super(8, "Flight Control: Navigating the Exceptions", false);
    }

    @Override
    public List<Task> getTasks() {
        return List.of(H08_1, H08_2, H08_3, H08_4, H08_5);
    }
}

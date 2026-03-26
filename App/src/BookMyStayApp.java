import java.util.*;

/**
 * ============================================================
 * MAIN CLASS - UseCase10BookingCancellation
 * ============================================================
 *
 * Use Case 10: Booking Cancellation
 *
 * @version 10.0
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Booking Cancellation");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize cancellation service
        CancellationService cancellationService = new CancellationService();

        // Simulate confirmed booking
        String reservationId = "Single-1";
        String roomType = "Single";

        cancellationService.registerBooking(reservationId, roomType);

        // Cancel booking
        cancellationService.cancelBooking(reservationId, inventory);

        // Show rollback history
        System.out.println("\nRollback History (Most Recent First):");
        cancellationService.showRollbackHistory();

        // Show updated availability
        System.out.println("\nUpdated Single Room Availability: "
                + inventory.getRoomAvailability().get("Single"));
    }
}


/**
 * ============================================================
 * CLASS - CancellationService
 * ============================================================
 */
class CancellationService {

    // Stack for rollback (LIFO)
    private Stack<String> releasedRoomIds;

    // Map reservationId → roomType
    private Map<String, String> reservationRoomTypeMap;

    public CancellationService() {
        releasedRoomIds = new Stack<>();
        reservationRoomTypeMap = new HashMap<>();
    }

    // Register confirmed booking
    public void registerBooking(String reservationId, String roomType) {
        reservationRoomTypeMap.put(reservationId, roomType);
    }

    // Cancel booking
    public void cancelBooking(String reservationId, RoomInventory inventory) {

        // Validate booking exists
        if (!reservationRoomTypeMap.containsKey(reservationId)) {
            System.out.println("Invalid reservation ID.");
            return;
        }

        String roomType = reservationRoomTypeMap.get(reservationId);

        // Restore inventory
        Map<String, Integer> availability = inventory.getRoomAvailability();
        availability.put(roomType, availability.get(roomType) + 1);

        // Push into stack (rollback tracking)
        releasedRoomIds.push(reservationId);

        // Remove booking
        reservationRoomTypeMap.remove(reservationId);

        System.out.println("Booking cancelled successfully. Inventory restored for room type: " + roomType);
    }

    // Show rollback history
    public void showRollbackHistory() {

        Stack<String> temp = (Stack<String>) releasedRoomIds.clone();

        while (!temp.isEmpty()) {
            System.out.println("Released Reservation ID: " + temp.pop());
        }
    }
}


/**
 * ============================================================
 * CLASS - RoomInventory
 * ============================================================
 */
class RoomInventory {

    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        roomAvailability.put("Single", 5);
        roomAvailability.put("Double", 3);
        roomAvailability.put("Suite", 2);
    }

    public Map<String, Integer> getRoomAvailability() {
        return roomAvailability;
    }
}
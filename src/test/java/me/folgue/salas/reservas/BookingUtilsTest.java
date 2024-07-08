package me.folgue.salas.reservas;

import me.folgue.salas.bookings.BookingUtils;
import java.time.LocalDateTime;
import java.time.Month;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author folgue
 */
public class BookingUtilsTest {

    @Test
    public void testDateRangeInRange_SameDates() {
        // A Dates
        LocalDateTime startDateA = LocalDateTime.of(2024, Month.JULY, 21, 20, 20),
                endDateA = LocalDateTime.of(2024, Month.JULY, 21, 21, 21);

        // B Dates 
        LocalDateTime startDateB = LocalDateTime.of(2024, Month.JULY, 21, 20, 20),
                endDateB = LocalDateTime.of(2024, Month.JULY, 21, 21, 21);

        assertTrue(BookingUtils.isDateRangeInRange(startDateA, endDateA, startDateB, endDateB));
    }

    @Test
    public void testDateRangeInRange_SingleOverlappingDate() {
        // A Dates
        LocalDateTime startDateA = LocalDateTime.of(2024, Month.JULY, 21, 19, 00),
                endDateA = LocalDateTime.of(2024, Month.JULY, 21, 20, 20);

        // B Dates 
        LocalDateTime startDateB = LocalDateTime.of(2024, Month.JULY, 21, 20, 20),
                endDateB = LocalDateTime.of(2024, Month.JULY, 21, 21, 21);

        assertTrue(BookingUtils.isDateRangeInRange(startDateA, endDateA, startDateB, endDateB));
    }

    @Test
    public void testDateRangeInRange_ConflictedDates() {
        // A Dates
        LocalDateTime startDateA = LocalDateTime.of(2024, Month.JULY, 21, 20, 20),
                endDateA = LocalDateTime.of(2024, Month.JULY, 21, 21, 21);

        // B Dates 
        LocalDateTime startDateB = LocalDateTime.of(2024, Month.JULY, 21, 19, 20),
                endDateB = LocalDateTime.of(2024, Month.JULY, 21, 20, 22);

        assertTrue(BookingUtils.isDateRangeInRange(startDateA, endDateA, startDateB, endDateB));
    }

    @Test
    public void testDateRangeInRange_SeparateDates() {
        // A Dates
        LocalDateTime startDateA = LocalDateTime.of(2024, Month.JULY, 21, 20, 20),
                endDateA = LocalDateTime.of(2024, Month.JULY, 21, 21, 21);

        // B Dates 
        LocalDateTime startDateB = LocalDateTime.of(2024, Month.JULY, 21, 17, 20),
                endDateB = LocalDateTime.of(2024, Month.JULY, 21, 18, 22);

        assertFalse(BookingUtils.isDateRangeInRange(startDateA, endDateA, startDateB, endDateB));
    }

}

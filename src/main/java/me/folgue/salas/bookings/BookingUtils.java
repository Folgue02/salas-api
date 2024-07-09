package me.folgue.salas.bookings;

import java.time.LocalDateTime;

public class BookingUtils {

    /**
     * Checks if a date range has a conflict with another date range.
     * <br>
     * <b>Note: </b>This is inclusive, meaning that a date range that ends on
     * <i>...xx-xx-xxxx 21:00</i> would have a conflict with a date range that
     * starts on
     * <i>xx-xx-xxxx 21:00...</i>.
     *
     * @param startDateA Start of the first time range.
     * @param endDateA End of the first time range.
     * @param startDateB Start of the second time range
     * @param endDateB End of the second time range.
     * @return {@code true} if there are any conflicts between the ranges,
     * {@code false} otherwise.
     */
    public static boolean isDateRangeInRange(LocalDateTime startDateA, LocalDateTime endDateA, LocalDateTime startDateB, LocalDateTime endDateB) {
        return !startDateA.isAfter(endDateB) && !endDateA.isBefore(startDateB);
    }

    public static boolean isValidDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return !startDate.isEqual(endDate) && startDate.isBefore(endDate);
    }
}

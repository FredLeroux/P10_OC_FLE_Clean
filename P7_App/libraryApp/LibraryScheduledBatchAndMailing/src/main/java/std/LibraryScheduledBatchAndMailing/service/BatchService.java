package std.LibraryScheduledBatchAndMailing.service;

import java.util.List;

import std.LibraryScheduledBatchAndMailing.dto.LoanBatchMailInfoDTO;
import std.LibraryScheduledBatchAndMailing.dto.ReservationToCancelInfoDTO;
import std.LibraryScheduledBatchAndMailing.entities.ReservationBatch;

public interface BatchService {

    public List<LoanBatchMailInfoDTO> sortLateLoansList();

    // -------- Ticket-1 issue#5 --------//
    public List<ReservationBatch> toNotifieds(Integer priority);

    public void updateNotificationDate(String customerEmail, String bookTitle);

    public List<ReservationToCancelInfoDTO> reservationsToCancelInfo(List<ReservationBatch> reservations);

    /**
     *
     * @param reservations waiting for
     *                     {@link #reservationsToCancelListDelayExceeded}
     *
     * @param toAdd        the number to add to book numberOf reservation
     *                     {@link #updateBooksNumberOfreservation}
     * @apiNote Method which set Reservation.CancelStatus to true,
     *          Book.numberOfReservation + toAdd and save updated Reservations
     *          linked Books and Reservations List
     *
     */
    public void updateAndSaveReservationAndLinkedBookOnExceedDelay(List<ReservationBatch> reservations, Integer toAdd);

    /**
     *
     * @param reservations a list of ReservationBatch
     * @param delayInDays  accorded delay in days
     * @return a list of ReservationBatch where notification date is before
     *         LocalDate.now().minus(delayInDays)
     */
    public List<ReservationBatch> reservationsToCancelListDelayExceeded(List<ReservationBatch> reservations,
	    Long delayInDays);

    /**
     *
     * @param priority the priority to extract from db
     * @return a list of reservations where priority = priority, notification date
     *         not null and canceled status = false
     */
    public List<ReservationBatch> reservationsToCheckCancelation(Integer priority);
}

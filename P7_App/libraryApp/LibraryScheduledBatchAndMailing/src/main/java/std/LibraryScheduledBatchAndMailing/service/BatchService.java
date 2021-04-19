package std.LibraryScheduledBatchAndMailing.service;

import java.util.List;

import std.LibraryScheduledBatchAndMailing.dto.LoanBatchMailInfoDTO;
import std.LibraryScheduledBatchAndMailing.dto.ReservationToNotifiedInfoDTO;
import std.LibraryScheduledBatchAndMailing.entities.ReservationBatch;

public interface BatchService {

    public List<LoanBatchMailInfoDTO> sortLateLoansList();

    // -------- Ticket-1 issue#5 --------//

    /**
     *
     * @param priority the priority to extract
     * @return a ReservationBatch list where ReservationBatch.priority == priority
     *         and ReservationBatch.canceledStatus == false
     */
    public List<ReservationBatch> toNotifieds(Integer priority);

    /**
     *
     * @param customerEmail the Reservation CustomerEmail to find in db
     * @param bookTitle     The REservations BookTitle to find in db
     * @apiNote Find and update One reservation where
     *          Reservation.Customer.customerEmail = customerEmail and
     *          Reservation.Book.title = bookTitle
     */
    public void updateNotificationDate(String customerEmail, String bookTitle);

    /**
     *
     * @param reservations the ReservationsBatch list where to update priority
     * @apiNote Set each ReservationBatch.notificationDate to LocalDate.now()
     */
    public void updateNotificationDate(List<ReservationBatch> reservations);

    public List<ReservationToNotifiedInfoDTO> reservationsToCancelInfo(List<ReservationBatch> reservations);

    public List<ReservationToNotifiedInfoDTO> reservationsToNotifiedBookAvailableInfo(
	    List<ReservationBatch> reservations);

    /**
     *
     * @param reservations           waiting for
     *                               {@link #reservationsToCancelListDelayExceeded}
     * @param toNotifiedReservations waiting for {@link #toNotifieds(Integer)}
     * @param priority               the priority to extract
     * @param toAdd                  the number to add to book numberOf reservation
     *                               {@link #updateBooksNumberOfreservation}
     * @apiNote Method which set Reservation.CancelStatus to true, set next priority
     *          reservation Book.numberOfReservation + toAdd and save updated
     *          Reservations linked Books and Reservations List
     *
     */
    public void updateAndSaveReservationAndLinkedBookOnExceedDelay(List<ReservationBatch> canceledReservations,
	    List<ReservationBatch> toNotifiedReservations, Integer toAdd);

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
     * @return a list of reservations where notification date not null and canceled
     *         status = false
     */
    public List<ReservationBatch> reservationsToCheckCancelation(List<ReservationBatch> reservations);

    /**
     *
     * @param reservations            waiting for the list from
     *                                {@link #toNotifieds(Integer)}
     * @param canceledReservationList waiting for the list from
     *                                {@link #reservationsToCancelListDelayExceeded}
     * @param priority                the priority to extract
     * @return a ReservationBatch list where forEach
     *         reservations.ReservationBatch.bookId is contained in
     *         canceledReservationList.
     */
    public List<ReservationBatch> reservationsListToUpdatePriority(List<ReservationBatch> reservations,
	    List<ReservationBatch> canceledReservationList, Integer priority);

    /**
     *
     * @param reservations the ReservationsBatch list where to update priority
     * @param priority     the priority to extract
     * @apiNote Set each ReservationBatch.priority to priority
     */
    public void updateReservationsPriority(List<ReservationBatch> reservations, Integer priority);

}

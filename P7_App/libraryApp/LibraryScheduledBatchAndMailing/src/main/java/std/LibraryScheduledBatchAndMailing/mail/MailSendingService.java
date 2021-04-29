package std.LibraryScheduledBatchAndMailing.mail;

public interface MailSendingService {

    /**
     * @apiNote inform all customer which have a book not returned in time
     */
    public void getCustomerInformedOnLate();

    /**
     *
     * @param customerEmail the customer to contact
     * @param bookTitle     the title
     * @param buildingName  the building
     * @param ref           the reservation reference
     * @apiNote send a book available notification to a customer
     */
    public void sendNotificationBookAvailable(String customerEmail, String bookTitle, String buildingName, Integer ref);

    /**
     *
     * @param priority the priority to extract
     * @param toAdd    the number to add to book numberOfReservation (use negative
     *                 value in order to subtract to the current value)
     * @apiNote Send a Notification to customer if Reservation.notificationDate is
     *          more than a delay of two days(by default) before LocalDate.now() and
     *          notified the next priority customer that a book is available, if
     *          needed the delay can be changed via setDelayIndays() methods
     */
    public void sendNotificationCanceledReservationAndUpdateDataBase(Integer priority, Integer toAdd);

    /**
     *
     * @param customerEmail the customer to contact
     * @param bookTitle     the title
     * @param buildingName  the building
     * @param ref           the reservation reference
     * @apiNote send a cancel notification to a customer
     */
    public void sendNotificationReservationCancel(String customerEmail, String bookTitle, String buildingName,
	    Integer ref);

    public void sendNotificationBookAvailableOnCustomerCancelReservation(Integer bookId, Integer Priority);
}

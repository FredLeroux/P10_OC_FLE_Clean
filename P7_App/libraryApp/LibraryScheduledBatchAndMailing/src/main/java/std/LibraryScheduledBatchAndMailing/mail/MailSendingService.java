package std.LibraryScheduledBatchAndMailing.mail;

public interface MailSendingService {

    public void getCustomerInformedOnLate();

    public void sendNotificationBookAvailable(String customerEmail, String bookTitle, String buildingName, Integer ref);

    public void sendNotificationCanceledReservationAndUpdateDataBase(Integer priority, Long delayInDays, Integer toAdd);

    public void sendNotificationReservationCancel(String customerEmail, String bookTitle, String buildingName,
	    Integer ref);
}

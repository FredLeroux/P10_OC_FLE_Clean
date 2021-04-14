package std.LibraryScheduledBatchAndMailing.mail;

public interface MailSendingService {

    public void getCustomerInformedOnLate();

    public void sendNotificationBookAvailable(String customerEmail, String bookTitle, String buildingName);

}

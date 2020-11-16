package std.LibraryScheduledBatchAndMailing.mail;

public interface MailSendingService {

	public void sendSimpleMessage(String to, String subject, String text);

}

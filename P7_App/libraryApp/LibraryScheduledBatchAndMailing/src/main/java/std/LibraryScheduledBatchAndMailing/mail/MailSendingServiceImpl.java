package std.LibraryScheduledBatchAndMailing.mail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import std.LibraryScheduledBatchAndMailing.dto.LoanBatchMailInfoDTO;
import std.LibraryScheduledBatchAndMailing.entities.ReservationBatch;
import std.LibraryScheduledBatchAndMailing.mailMessagesComponents.MailLateMessageElmt;
import std.LibraryScheduledBatchAndMailing.mailMessagesComponents.NotificationBookAvailableMessageElmt;
import std.LibraryScheduledBatchAndMailing.mailMessagesComponents.NotificationCancelReservationMessageElmt;
import std.LibraryScheduledBatchAndMailing.service.BatchService;

@Service
public class MailSendingServiceImpl implements MailSendingService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private BatchService batchService;

    @Autowired
    private MailLateMessageElmt lateMessage;

    @Autowired
    private NotificationBookAvailableMessageElmt notifAvailableBook;

    @Autowired
    private NotificationCancelReservationMessageElmt notifCancelReservation;

    private final String ln = "\n";

    @Override
    public void getCustomerInformedOnLate() {
	List<LoanBatchMailInfoDTO> list = batchService.sortLateLoansList();
	if (!list.isEmpty()) {
	    customerBooksMap(list).forEach((k, v) -> sendSimpleMessage(createLateMessage(k, v)));
	}
    }

    private HashMap<String, ArrayList<String>> customerBooksMap(List<LoanBatchMailInfoDTO> list) {
	HashMap<String, ArrayList<String>> map = new HashMap<>();
	list.forEach(o -> fillCustomerBookMap(o.getCustomer().getCustomerEmail(), o.getBook().getTitle(), map));
	return map;

    }

    private void fillCustomerBookMap(String key, String value, HashMap<String, ArrayList<String>> map) {
	if (map.containsKey(key)) {
	    map.get(key).add(value);
	} else {
	    ArrayList<String> arrayList = new ArrayList<>();
	    arrayList.add(value);
	    map.put(key, arrayList);
	}
    }

    private void sendSimpleMessage(SimpleMailMessage message) {
	emailSender.send(message);
    }

    private SimpleMailMessage createLateMessage(String customerEmail, ArrayList<String> booksTitleList) {
	return createMessage(lateMessage.getFrom(), customerEmail, lateMessage.getSubject(),
		lateBodyMessage(booksTitleList));
    }

    private SimpleMailMessage createMessage(String from, String to, String subject, String bodyText) {
	SimpleMailMessage message = new SimpleMailMessage();
	message.setFrom(from);
	message.setTo(to);
	message.setSubject(subject);
	message.setText(bodyText);
	return message;
    }

    private String lateBodyMessage(ArrayList<String> booksTitleList) {
	StringBuilder stb = new StringBuilder();
	stb.append(lateMessage.getGreeting());
	stb.append(ln);
	stb.append(lateMessage.getThebook());
	appendBooksTitleList(stb, booksTitleList);
	stb.append(lateMessage.getMessage());
	stb.append(ln);
	stb.append(lateMessage.getThanks());
	stb.append(ln);
	stb.append(lateMessage.getEnd());
	return stb.toString();

    }

    private void appendBooksTitleList(StringBuilder stb, ArrayList<String> booksTitleList) {
	for (String str : booksTitleList) {
	    stb.append(str);
	    stb.append(ln);
	}
    }

    // -------- Ticket-1 issue#5 --------//

    public void reservationAvailableNotification(Integer Priority) {
	// TODO implements if scheduled automation on available book notification is
	// wanted
    }

    @Override
    public void sendNotificationBookAvailable(String customerEmail, String bookTitle, String buildingName,
	    Integer ref) {
	sendSimpleMessage(createMessage(notifAvailableBook.getFrom(), customerEmail, notifAvailableBook.getSubject(),
		notificationBookAvailable(bookTitle, buildingName, ref)));
	batchService.updateNotificationDate(customerEmail, bookTitle);
    }

    private String notificationBookAvailable(String bookTitle, String buildingName, Integer ref) {
	StringBuilder stb = new StringBuilder();
	stb.append(notifAvailableBook.getGreeting());
	stb.append(ln);
	stb.append(notifAvailableBook.getThebook());
	stb.append(bookTitle + ".");
	stb.append(ln);
	stb.append(notifAvailableBook.getMessage());
	stb.append(buildingName + ".");
	stb.append(ln);
	stb.append(notifAvailableBook.getRef());
	stb.append(ref + ".");
	stb.append(ln);
	stb.append(notifAvailableBook.getTime());
	stb.append(ln);
	stb.append(notifAvailableBook.getEnd());
	return stb.toString();

    }

    @Override
    public void sendNotificationCanceledReservationAndUpdateDataBase(Integer priority, Long delayInDays,
	    Integer toAdd) {
	List<ReservationBatch> list = batchService.reservationsToCancelListDelayExceeded(
		batchService.reservationsToCheckCancelation(priority), delayInDays);
	batchService.updateAndSaveReservationAndLinkedBookOnExceedDelay(list, toAdd);
	batchService.reservationsToCancelInfo(list).forEach(o -> sendNotificationReservationCancel(o.getCustomerEmail(),
		o.getBookTitle(), o.getBuilding(), o.getReference()));

    }

    @Override
    public void sendNotificationReservationCancel(String customerEmail, String bookTitle, String buildingName,
	    Integer ref) {
	sendSimpleMessage(createMessage(notifCancelReservation.getFrom(), customerEmail,
		notifCancelReservation.getSubject(), notificationReservationCancel(bookTitle, buildingName, ref)));
	batchService.updateNotificationDate(customerEmail, bookTitle);
    }

    private String notificationReservationCancel(String bookTitle, String buildingName, Integer ref) {
	StringBuilder stb = new StringBuilder();
	stb.append(notifCancelReservation.getGreeting());
	stb.append(ln);
	stb.append(notifCancelReservation.getThebook());
	stb.append(bookTitle + ".");
	stb.append(ln);
	stb.append(notifCancelReservation.getMessage());
	stb.append(buildingName + ".");
	stb.append(ln);
	stb.append(notifCancelReservation.getRef());
	stb.append(ref + ".");
	stb.append(ln);
	stb.append(notifCancelReservation.getCause());
	stb.append(ln);
	stb.append(notifCancelReservation.getEnd());
	return stb.toString();
    }

}

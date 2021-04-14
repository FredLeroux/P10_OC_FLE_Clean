package std.LibraryScheduledBatchAndMailing.mail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import std.LibraryScheduledBatchAndMailing.dto.LoanBatchMailInfoDTO;
import std.LibraryScheduledBatchAndMailing.service.LoanBatchService;

@Service
public class MailSendingServiceImpl implements MailSendingService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private LoanBatchService LoanBatchService;

    @Autowired
    private MailLateMessageElmt lateMessage;

    @Autowired
    private NotificationBookAvailableMessageElmt notifAvailableBook;

    private final String ln = "\n";

    @Override
    public void getCustomerInformedOnLate() {
	List<LoanBatchMailInfoDTO> list = LoanBatchService.sortLateLoansList();
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
    public void sendNotificationBookAvailable(String customerEmail, String bookTitle, String buildingName) {
	sendSimpleMessage(createMessage(notifAvailableBook.getFrom(), customerEmail, notifAvailableBook.getSubject(),
		notificationBookAvailable(bookTitle, buildingName)));
	LoanBatchService.updateNotificationDate(customerEmail, bookTitle);
    }

    private String notificationBookAvailable(String bookTitle, String buildingName) {
	StringBuilder stb = new StringBuilder();
	stb.append(notifAvailableBook.getGreeting());
	stb.append(ln);
	stb.append(notifAvailableBook.getThebook());
	stb.append(bookTitle + ".");
	stb.append(ln);
	stb.append(notifAvailableBook.getMessage());
	stb.append(buildingName + ".");
	stb.append(ln);
	stb.append(notifAvailableBook.getTime());
	stb.append(ln);
	stb.append(notifAvailableBook.getEnd());
	return stb.toString();

    }

}

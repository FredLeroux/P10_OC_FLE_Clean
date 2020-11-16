package std.LibraryScheduledBatchAndMailing.mail;

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
	private MailMessageElmt messageElmt;
	
	private final String ln = "\n";




	@Override
	public void getCustomerInformedOnLate() {
		List<LoanBatchMailInfoDTO> list = LoanBatchService.sortLateLoansList();
		if(!list.isEmpty()) {
			list.forEach(o->sendSimpleMessage(createLateMessage(o)));
		}
	}

	private void sendSimpleMessage(SimpleMailMessage message) {
		emailSender.send(message);
	}

	private SimpleMailMessage createLateMessage(LoanBatchMailInfoDTO dto) {
		return createMessage(messageElmt.getFrom(), dto.getCustomer().getCustomerEmail(), messageElmt.getSubject(),
				lateBodyMessage(dto));
	}

	private SimpleMailMessage createMessage(String from, String to, String subject, String bodyText) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(bodyText);
		return message;
	}

	 private String lateBodyMessage(LoanBatchMailInfoDTO dto) {
		 StringBuilder stb = new StringBuilder();
		 stb.append(messageElmt.getGreeting());
		 stb.append(ln);
		 stb.append(messageElmt.getThebook());
		 stb.append(dto.getBook().getTitle());
		 stb.append(ln);
		 stb.append(messageElmt.getLateMess());
		 stb.append(ln);
		 stb.append(messageElmt.getThanks());
		 stb.append(ln);
		 stb.append(messageElmt.getEnd());
		 return stb.toString();

	 }




}

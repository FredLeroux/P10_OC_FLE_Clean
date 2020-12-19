package std.LibraryScheduledBatchAndMailing.mail;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "mail")
@Getter
@Setter
public class MailMessageElmt {

	private String from;
	private String subject;
	private String greeting;
	private String thebook;
	private String lateMess;
	private String thanks;
	private String end;

}

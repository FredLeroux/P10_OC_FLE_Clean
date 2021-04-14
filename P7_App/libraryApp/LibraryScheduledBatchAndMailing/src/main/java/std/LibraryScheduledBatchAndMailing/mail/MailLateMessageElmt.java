package std.LibraryScheduledBatchAndMailing.mail;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "mail")
@Getter
@Setter
public class MailLateMessageElmt extends MailComponents {

}

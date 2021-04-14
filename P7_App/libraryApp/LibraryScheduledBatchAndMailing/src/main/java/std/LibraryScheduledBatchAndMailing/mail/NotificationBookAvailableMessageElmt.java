package std.LibraryScheduledBatchAndMailing.mail;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "notificationavailable")
@Getter
@Setter
public class NotificationBookAvailableMessageElmt extends MailComponents {

    private String time;

}

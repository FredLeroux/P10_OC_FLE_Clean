package std.LibraryScheduledBatchAndMailing.mailMessagesComponents;

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
    private String ref;
    private String timeunit;

}

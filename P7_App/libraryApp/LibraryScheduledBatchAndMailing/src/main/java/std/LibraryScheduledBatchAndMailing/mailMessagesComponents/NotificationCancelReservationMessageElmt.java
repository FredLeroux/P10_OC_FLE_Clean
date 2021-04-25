package std.LibraryScheduledBatchAndMailing.mailMessagesComponents;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "notificationcancel")
@Getter
@Setter
public class NotificationCancelReservationMessageElmt extends MailComponents {

    private String cause;
    private String ref;

}

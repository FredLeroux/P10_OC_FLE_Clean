package std.LibraryScheduledBatchAndMailing.mailMessagesComponents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class MailComponents {

    private String from;
    private String subject;
    private String greeting;
    private String thebook;
    private String message;
    private String thanks;
    private String end;
}

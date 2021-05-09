package std.LibraryScheduledBatchAndMailing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import std.LibraryScheduledBatchAndMailing.mail.MailSendingService;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class LibraryScheduledBatchAndMailingApplication {
    private static final Integer PRIORITY = 1;
    private static final Integer NUMBER_OF_RESERVATIONS_UPDATE = -1;

    @Autowired
    MailSendingService service;

    public static void main(String[] args) {
	SpringApplication.run(LibraryScheduledBatchAndMailingApplication.class, args);
    }

    @Scheduled(cron = "${cronNotif}")
    public void scheduling() {
	service.getCustomerInformedOnLate();
    }

    /*
     * cron set to at each hour start (presentation matter) from MONDAY to FRIDAY no
     * message the week-end cron ="0 0 * * * MON-FRI"
     *
     */
    /**
     *
     */
    @Scheduled(cron = "${cronDelay}")
    public void cancelResevationDelayExceeded() {
	service.sendNotificationCanceledReservationAndUpdateDataBase(PRIORITY, NUMBER_OF_RESERVATIONS_UPDATE);
	;
    }
}

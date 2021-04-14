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

    @Autowired
    MailSendingService service;

    public static void main(String[] args) {
	SpringApplication.run(LibraryScheduledBatchAndMailingApplication.class, args);
    }

    @Scheduled(fixedRate = 3600000)
    public void scheduling() {
	service.getCustomerInformedOnLate();
    }
}

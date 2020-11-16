package std.LibraryScheduledBatchAndMailing;

import java.time.LocalDateTime;

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
	MailSendingService mail;

	public static void main(String[] args) {
		SpringApplication.run(LibraryScheduledBatchAndMailingApplication.class, args);
	}

	@Scheduled(fixedRate = 30000)
	public void scheduling() {
		LocalDateTime date = LocalDateTime.now();
		System.out.println("shedule time = " + date.getHour() +":"+date.getMinute());
		mail.sendSimpleMessage("me", "yop", "blabla");
	}
}

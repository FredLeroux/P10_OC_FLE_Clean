package std.LibraryScheduledBatchAndMailing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import std.LibraryScheduledBatchAndMailing.mail.MailSendingService;

@RestController
public class LibraryScheduledBatchAndMailingController {

    @Autowired
    MailSendingService service;

    @PostMapping(value = "/sendNotificationBookAvailable")
    public void sendNotificationBookAvailable(@RequestParam(value = "customerEmail") String customerEmail,
	    @RequestParam(value = "bookTitle") String bookTitle,
	    @RequestParam(value = "buildingName") String buildingName,
	    @RequestParam(value = "reference") Integer reference) {
	service.sendNotificationBookAvailable(customerEmail, bookTitle, buildingName, reference);
    }

    @PostMapping(value = "/sendNotificationBookAvailableAfterCustomerCancel")
    public void sendNotificationBookAvailableAfterCustomerCancel(@RequestParam(value = "bookId") Integer bookId,
	    @RequestParam(value = "priority") Integer priority) {
	service.sendNotificationBookAvailableOnCustomerCancelReservation(bookId, priority);
	;
    }

}

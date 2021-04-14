package std.LibraryScheduledBatchAndMailing.service;

import java.util.List;

import std.LibraryScheduledBatchAndMailing.dto.LoanBatchMailInfoDTO;
import std.LibraryScheduledBatchAndMailing.entities.ReservationBatch;

public interface LoanBatchService {

    public List<LoanBatchMailInfoDTO> sortLateLoansList();

    // -------- Ticket-1 issue#5 --------//
    public List<ReservationBatch> toNotifieds(Integer priority);

    public void updateNotificationDate(String customerEmail, String bookTitle);

}

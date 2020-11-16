package std.LibraryScheduledBatchAndMailing.service;

import java.util.List;

import std.LibraryScheduledBatchAndMailing.dto.LoanBatchMailInfoDTO;

public interface LoanBatchService {

	List<LoanBatchMailInfoDTO> sortLateLoansList();

}

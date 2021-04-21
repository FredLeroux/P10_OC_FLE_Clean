package std.libraryBookLoans.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import std.libraryBookLoans.dto.LoanInfoDTO;
import std.libraryBookLoans.dto.ReservableBookExamplaryDatedDTO;
import std.libraryBookLoans.dto.ReservableBookLinkedLoanDTO;
import std.libraryBookLoans.service.LoanService;

@RestController
public class LibraryBookLoansController {

    @Autowired
    LoanService loan;

    @GetMapping(value = "/loansList")
    public List<LoanInfoDTO> loansList(@RequestParam(value = "userName") String userName) {
	return loan.customerLoans(userName);
    }

    /**
     *
     * @param loanId
     * @param postponeDaysNumber
     * @param userName
     * @param unitNumber
     * @param unit
     * @apiNote - period is defined by unitNumber x unit(ChronoUnit only
     *          days/weeks/months/years) <br>
     *          -The daysOffList and holidays as to be managed in the method owner
     *          micro-service id not used set them to null
     *
     */
    @PostMapping(value = "/postpone")
    public void postpone(@RequestParam(value = "loanId") Integer loanId,
	    @RequestParam(value = "userName") String userName, @RequestParam(value = "unitNumber") Integer unitNumber,
	    @RequestParam(value = "unit") String unit) {
	loan.postponeLoan(loanId, userName, unitNumber, unit, null, null);

    }

    @PostMapping(value = "/createLoan")
    public void createLoan(@RequestParam(value = "customerId") Integer customerId,
	    @RequestParam(value = "bookId") Integer bookId, @RequestParam(value = "unitNumber") Integer unitNumber,
	    @RequestParam(value = "unit") String unit) {

	loan.createLoan(customerId, bookId, unitNumber, unit);
    }

    @PostMapping(value = "/createLoanFromReservation")
    public void createLoanFromReservation(@RequestParam(value = "reservationId") Integer reservationId,
	    @RequestParam(value = "customerId") Integer customerId,
	    @RequestParam(value = "unitNumber") Integer unitNumber, @RequestParam(value = "unit") String unit) {
	loan.createLoanFromReservation(reservationId, customerId, unitNumber, unit);

    }

    @PostMapping(value = "/returnLoan")
    public void returnLoan(@RequestParam(value = "customerId") Integer customerId,
	    @RequestParam(value = "bookId") Integer bookId) {
	loan.returnLoan(bookId, customerId);
    }

    @GetMapping(value = "/reservableBookLinkedLoans")
    List<ReservableBookLinkedLoanDTO> getReservableBookLinkedLoans(
	    @RequestParam(value = "bookIdList") List<Integer> bookIdList) {
	return loan.getReservableBookLinkedLoans(bookIdList);
    }

    @GetMapping(value = "/reservableBookExamplary")
    public List<ReservableBookExamplaryDatedDTO> reservableBookExamplaryDTOs(
	    @RequestParam(value = "bookIdList") List<Integer> bookIdList,
	    @RequestParam(value = "numberChronoOfUnit") Integer numberChronoOfUnit,
	    @RequestParam(value = "unit") String unit) {
	return loan.reservableBookExamplaryDTOs(bookIdList, numberChronoOfUnit, unit);
    }

}

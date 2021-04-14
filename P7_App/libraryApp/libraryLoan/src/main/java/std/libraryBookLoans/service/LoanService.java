package std.libraryBookLoans.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import std.libraryBookLoans.dto.LoanInfoDTO;
import std.libraryBookLoans.dto.ReservableBookExamplaryDatedDTO;
import std.libraryBookLoans.dto.ReservableBookLinkedLoanDTO;

public interface LoanService {

    public List<LoanInfoDTO> customerLoans(String authUserName);

    /**
     *
     * @param custommerId
     * @param bookId
     * @param unitNumber
     * @param unit
     * @apiNote a loan period is defined by unitNumber x unit(ChronoUnit expect only
     *          days/weeks/months/years)
     */
    public void createLoan(Integer custommerId, Integer bookId, Integer unitNumber, ChronoUnit unit);

    public void returnLoan(Integer loanId, Integer custommerId);

    /**
     *
     * @param loanId
     * @param postponeDays
     * @param unitNumber
     * @param unit
     * @param daysOffList  wait for a String[] containing DayOfWeek.toString(),can
     *                     be null
     * @param holidays     wait for a String[] containing LocalDate.toString(),can
     *                     be null
     *
     * @apiNote -A loan period is defined by unitNumber x unit(ChronoUnit expect
     *          only days/weeks/months/years) <br>
     *          -The daysOffList and holidays as to be managed in the method owner
     *          micro-service this two parameter allow to one day postpone if days
     *          is an holiday or a day off Conclusion done under app not under
     *          Ribbon and eureka try again to pass null parameters (holiday and
     *          daysoff ) from ui microservice
     */
    public void postponeLoan(Integer loanId, String userName, Integer unitNumber, ChronoUnit unit,
	    ArrayList<DayOfWeek> daysOffList, ArrayList<LocalDate> holidays);

    public List<ReservableBookLinkedLoanDTO> getReservableBookLinkedLoans(List<Integer> bookIdList);

    public List<ReservableBookExamplaryDatedDTO> reservableBookExamplaryDTOs(List<Integer> bookIdList,
	    Integer numberChronoOfUnit, ChronoUnit unit);

    public void createLoanFromReservation(Integer reservationId, Integer customerId, Integer unitNumber,
	    ChronoUnit unit);

}

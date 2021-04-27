package std.libraryLoan;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import std.libraryBookLoans.dao.CustomerLoanDAO;
import std.libraryBookLoans.dao.LibraryBookLoanDAO;
import std.libraryBookLoans.dao.LibraryBuildingLoanDAO;
import std.libraryBookLoans.dao.LibraryReservationForLoanDAO;
import std.libraryBookLoans.dao.LibraryRoleLoanDAO;
import std.libraryBookLoans.dao.LoanDAO;

@SpringBootTest
@TestPropertySource(value = "application-unitTest.properties")
@ExtendWith(MockitoExtension.class)
class LibraryLoanApplicationTests {

    @MockBean
    LoanDAO loanDAO;

    @MockBean
    CustomerLoanDAO customerDAO;

    @MockBean
    LibraryRoleLoanDAO roleDAO;

    @MockBean
    LibraryBookLoanDAO bookDAO;

    @MockBean
    LibraryBuildingLoanDAO buildingDAO;

    @MockBean
    LibraryReservationForLoanDAO reservationDAO;

}

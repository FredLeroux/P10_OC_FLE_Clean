package std.libraryUi.controller;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import std.libraryUi.beans.ReservableBookExamplaryDatedBean;
import std.libraryUi.controller.controllerMethods.ControllerMethods;
import std.libraryUi.exceptions.EmptyListException;
import std.libraryUi.proxies.LibraryBookCaseProxy;
import std.libraryUi.proxies.LibraryBuildingsProxy;

@Controller
public class LibraryUiController {

    private final static Integer MAX_RESERVATION_NUMBER = 2;

    @Autowired
    private LibraryBookCaseProxy libraryCaseProxy;

    @Autowired
    private LibraryBuildingsProxy libraryBuildingsProxy;

    @Autowired
    private ControllerMethods methods;

    @GetMapping(value = { "/", "home" })
    public ModelAndView home(ModelAndView model, HttpServletRequest request, HttpSession session) {
	model.setViewName("home");
	if (request.getHeader("isKnown").equals("true")) {
	    session.setAttribute("logged", "true");
	}
	return model;
    }

    @GetMapping(value = "/loanTracking")
    public ModelAndView welcome(ModelAndView model, HttpServletRequest request) {
	model.setViewName("loanTracking");
	listAndDatepickerInfo(model, request);
	return model;
    }

    @PostMapping(value = "/postPone")
    public ModelAndView postPone(ModelAndView model, Integer loanId, HttpServletRequest request) {
	model.setViewName("loanTracking");
	methods.postPoneLoan(request, "token", loanId, 4, ChronoUnit.WEEKS);
	listAndDatepickerInfo(model, request);
	return model;
    }

    @GetMapping(value = "/login")
    public ModelAndView login() {
	return new ModelAndView("login");
    }

    @GetMapping(value = "/booksList")
    public ModelAndView allBook(ModelAndView model, HttpServletRequest request, HttpSession session) {
	model.setViewName("booksList");
	model.addObject("list", libraryCaseProxy.books(MAX_RESERVATION_NUMBER));
	model.addObject("buildings", libraryBuildingsProxy.getBuildings());
	model.addObject("kinds", libraryCaseProxy.kinds());
	model.addObject("selectedBuilding", 0);
	if (request.getHeader("isKnown").equals("true")) {
	    session.setAttribute("logged", "true");
	}
	return model;
    }

    @GetMapping(value = "/bookListFiltering")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public @ResponseBody void filtering() {
    }

    @GetMapping(value = "/booksListFiltered")
    public String filter(Model model, @RequestParam(name = "libraryBuilding") Integer libraryBuilding,
	    @RequestParam(name = "kinds") List<String> kinds, HttpServletRequest request, HttpSession session) {
	if (libraryBuilding == 0 && kinds.size() == 0) {
	    model.addAttribute("list", libraryCaseProxy.books(MAX_RESERVATION_NUMBER));
	} else if (libraryBuilding == 0 && kinds.size() != 0) {
	    model.addAttribute("list", libraryCaseProxy.booksKindsFiltered(kinds, MAX_RESERVATION_NUMBER));
	} else if (libraryBuilding != 0 && kinds.size() == 0) {
	    model.addAttribute("list", libraryCaseProxy.booksBuildingFiltered(libraryBuilding, MAX_RESERVATION_NUMBER));
	} else if (libraryBuilding != 0 && kinds.size() != 0) {
	    model.addAttribute("list",
		    libraryCaseProxy.booksBuildingAndKindsFiltered(libraryBuilding, kinds, MAX_RESERVATION_NUMBER));
	}
	bookListTableSessionAttribut(request, session);
	return "bookListTable";
    }

    @GetMapping(value = "/bookListTable")
    public String table(Model model, HttpServletRequest request, HttpSession session) {
	model.addAttribute("list", libraryCaseProxy.books(MAX_RESERVATION_NUMBER));
	bookListTableSessionAttribut(request, session);
	return "bookListTable";
    }

    private void bookListTableSessionAttribut(HttpServletRequest request, HttpSession session) {
	if (request.getHeader("isKnown").equals("true")) {
	    session.setAttribute("logged", "true");
	    session.setAttribute("resa", "true");
	}
    }

    private void listAndDatepickerInfo(ModelAndView model, HttpServletRequest request) {
	methods.addSortedLoansListAndDatePickerLoansInfoToModel(model, "list", "datepickerInfo", request, "token",
		"fr");
    }

    @GetMapping(value = "/doReservation")
    public ModelAndView doReservation(ModelAndView model, HttpServletRequest request) {
	model.setViewName("doReservation");
	String title = request.getParameter("title");
	String buildingName = request.getParameter("building");
	List<ReservableBookExamplaryDatedBean> list = new ArrayList<>();
	try {
	    list = methods.getReservableExamplaryBeans(title, buildingName, 2, 4, ChronoUnit.WEEKS, "fr");
	} catch (Exception e) {
	    throw new EmptyListException();
	}
	if (list.isEmpty()) {
	    throw new EmptyListException("Examplaries List is empty");
	}
	model.addObject("title", title);
	model.addObject("building", buildingName);
	model.addObject("examplariesList", list);
	return model;
    }

    @PostMapping(value = "/createLoan")
    public ModelAndView createLoan(@RequestParam(value = "customerId") Integer customerId,
	    @RequestParam(value = "bookId") Integer bookId) {
	methods.createLoan(customerId, bookId);
	return new ModelAndView("confirmationPage");
    }

    @PostMapping(value = "/createLoanFromReservation")
    public ModelAndView createLoanFromReservation(@RequestParam(value = "customerId") Integer customerId,
	    @RequestParam(value = "reservationId") Integer reservationId) {
	methods.createLoanFromReservation(customerId, reservationId);
	return new ModelAndView("confirmationPage");
    }

    @PostMapping(value = "/returnLoan")
    public ModelAndView returnLoan(@RequestParam(value = "customerId") Integer customerId,
	    @RequestParam(value = "bookId") Integer bookId) {
	methods.returnLoan(customerId, bookId);
	return new ModelAndView("confirmationPage");

    }

    @GetMapping(value = "/errorPage")
    public ModelAndView errorPage(ModelAndView model, HttpServletRequest request) {
	model.setViewName("errorPage");
	String errorCode = request.getParameter("errorCode");
	model.addObject("errorCode", errorCode);
	return model;

    }

    @PostMapping(value = "/reserveBook")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public @ResponseBody Boolean reserveBook(ModelAndView model, Integer bookRef, HttpServletRequest request) {
	try {
	    methods.createReservation(request, "token", bookRef);
	} catch (Exception e) {
	    return false;
	}
	return true;

    }

    @GetMapping(value = "/onGoingReservations")
    public ModelAndView onGoingReservation(ModelAndView model, HttpServletRequest request, HttpSession session) {
	model.setViewName("onGoingReservations");
	if (request.getHeader("isKnown").equals("true")) {
	    session.setAttribute("logged", "true");
	}
	return model;
    }

    @GetMapping(value = "/myReservationsList")
    public ModelAndView myReservationsList(ModelAndView model, HttpServletRequest request) {
	model.setViewName("reservationsList");
	methods.customerReservations(model, request, "token");
	return model;
    }

    @PostMapping(value = "/cancelReservation")
    public ModelAndView cancelReservation(ModelAndView model, HttpServletRequest request,
	    Integer reservationReference) {
	model.setViewName("reservationsList");
	methods.cancelReservation(reservationReference);
	return model;
    }

}

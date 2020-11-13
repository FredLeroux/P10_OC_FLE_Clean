package std.libraryUi.controller;

import java.security.Principal;
import java.time.temporal.ChronoUnit;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.ModelAndView;

import std.libraryUi.beans.LoanInfoBean;
import std.libraryUi.controller.controllerMethods.ControllerMethods;
import std.libraryUi.proxies.LibraryBookCaseProxy;
import std.libraryUi.proxies.LibraryBookLoansProxy;
import std.libraryUi.proxies.LibraryBuildingsProxy;

@Controller
public class LibraryUiController {

	@Autowired
	private ApplicationContext appContext;

	@Autowired
	LibraryBookCaseProxy libraryCaseProxy;

	@Autowired
	HttpServletRequest request;

	@Autowired
	LibraryBuildingsProxy libraryBuildingsProxy;

	@Autowired
	LibraryBookLoansProxy libraryBookLoansProxy;

	@Autowired
	private ControllerMethods methods;

	@GetMapping(value = "/")
	public ModelAndView home(ModelAndView model, Principal principal, Locale timezone) {
		model.setViewName("home");

		return model;
	}


	@GetMapping(value = "/loanTracking")
	public ModelAndView welcome(ModelAndView model, Principal principal,HttpServletRequest request) {
		model.setViewName("loanTracking");
		//HttpSession session = request.getSession();
		//System.out.println(session.getAttribute("code"));
		//if (methods.isUserAuthenticated()) {
			List<LoanInfoBean> list = libraryBookLoansProxy.loansList("mail"/*principal.getName()*/);
			model.addObject("list", /* new ArrayList<String>() */methods.loanInfoDTOList(list, "fr"));
			model.addObject("datepickerInfo", methods.loanInfoToDatepicker(list));
			System.out.println("backtoLibraryUi api");
		//}
		return model;
	}

	@PostMapping(value = "/postPone")
	public ModelAndView postPone(ModelAndView model, Integer loanId, Principal principal) {
		methods.postPoneLoan(loanId,principal.getName(), 4,ChronoUnit.WEEKS);
		return new ModelAndView("redirect:/loanTracking");
	}

	@GetMapping(value = "/login")
	public ModelAndView login() {
		return new ModelAndView("login");
	}

	@GetMapping(value = "/booksList")
	public ModelAndView allBook(ModelAndView model, Principal principal) {
		model.setViewName("booksList");
		model.addObject("list", libraryCaseProxy.books());
		model.addObject("buildings", libraryBuildingsProxy.getBuildings());
		model.addObject("kinds", libraryCaseProxy.kinds());
		model.addObject("selectedBuilding", 0);

		return model;
	}

	@GetMapping(value = "/bookListFiltering")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public @ResponseBody void filtering() {
	}

	@GetMapping(value = "/booksListFiltered")
	public String filter(Model model, @RequestParam(name = "libraryBuilding") Integer libraryBuilding,
			@RequestParam(name = "kinds") List<String> kinds) {
		if (libraryBuilding == 0 && kinds.size() == 0) {
			model.addAttribute("list", libraryCaseProxy.books());
		} else if (libraryBuilding == 0 && kinds.size() != 0) {
			model.addAttribute("list", libraryCaseProxy.booksKindsFiltered(kinds));
		} else if (libraryBuilding != 0 && kinds.size() == 0) {
			model.addAttribute("list", libraryCaseProxy.booksBuildingFiltered(libraryBuilding));
		} else if (libraryBuilding != 0 && kinds.size() != 0) {
			model.addAttribute("list", libraryCaseProxy.booksBuildingAndKindsFiltered(libraryBuilding, kinds));
		}
		return "bookListTable";
	}

	@GetMapping(value = "/bookListTable")
	public String table(Model model) {
		model.addAttribute("list", libraryCaseProxy.books());
		return "bookListTable";
	}

	@GetMapping(value = "/getLoan")
	public ModelAndView loanInfo(ModelAndView model) {
		model.setViewName("loan");
		// model.addObject("list",
		// methods.loanInfoDTOList(libraryBookLoansProxy.loansList(),"fr"));
		return model;
	}

}

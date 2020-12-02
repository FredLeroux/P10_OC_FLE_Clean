package std.libraryUi.controller;

import java.security.Principal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

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

import std.libraryUi.controller.controllerMethods.ControllerMethods;
import std.libraryUi.exceptions.UnknowErrorException;
import std.libraryUi.proxies.LibraryBookCaseProxy;
import std.libraryUi.proxies.LibraryBuildingsProxy;

@Controller
public class LibraryUiController {

	@Autowired
	private LibraryBookCaseProxy libraryCaseProxy;

	@Autowired
	private LibraryBuildingsProxy libraryBuildingsProxy;

	@Autowired
	private ControllerMethods methods;

	@GetMapping(value = "/")
	public ModelAndView home(ModelAndView model, Principal principal, Locale timezone, HttpServletRequest request,
			HttpSession session) {
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

	private void listAndDatepickerInfo(ModelAndView model, HttpServletRequest request) {
		methods.addSortedLoansListAndDatePickerLoansInfoToModel(model, "list", "datepickerInfo", request, "token",
				"fr");
	}

	@GetMapping(value = "/errorPage")
	public  ModelAndView errorPage(ModelAndView model, HttpServletRequest request) {
		model.setViewName("errorPage");
		String errorCode = request.getParameter("errorCode");
		model.addObject("errorCode", errorCode);
		return model;

	}

}

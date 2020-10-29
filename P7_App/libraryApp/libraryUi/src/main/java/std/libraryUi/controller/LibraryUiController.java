package std.libraryUi.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import std.libraryUi.controller.controllerMethods.ControllerMethods;
import std.libraryUi.proxies.LibraryBookCaseProxy;
import std.libraryUi.proxies.LibraryBookLoansProxy;
import std.libraryUi.proxies.LibraryBuildingsProxy;

@Controller
public class LibraryUiController {

	@Autowired
	LibraryBookCaseProxy libraryCaseProxy;

	@Autowired
	LibraryBuildingsProxy libraryBuildingsProxy;

	@Autowired
	LibraryBookLoansProxy libraryBookLoansProxy;

	private ControllerMethods methods = new ControllerMethods();
	private Boolean bool = false;

	@GetMapping(value = "/")
	public ModelAndView home(ModelAndView model,Principal principal) {
		model.setViewName("home");
		LocalDate date = LocalDate.now().plusDays(14);
		System.out.println(date.getDayOfWeek());
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
		System.out.println(principal.getName());
		bool = true;

		}else {
			bool = false;
		}
		return model;
	}

	@GetMapping(value="/welcome")
	public ModelAndView welcome(ModelAndView model) {
		model.setViewName("welcome");
		if(bool) {
		model.addObject("list",libraryBookLoansProxy.loan());
		}
		return model;
	}

	@GetMapping(value = "login")
	public ModelAndView login() {
		return new ModelAndView("login");
	}



	@GetMapping(value = "/booksList")
	public  ModelAndView allBook(ModelAndView model, Principal principal) {
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
	public String filter(Model model,
			@RequestParam(name = "libraryBuilding") Integer libraryBuilding,
			@RequestParam(name = "kinds") List<String> kinds) {
		if (libraryBuilding == 0 && kinds.size() == 0) {
			model.addAttribute("list", libraryCaseProxy.books());
		}else if (libraryBuilding == 0 && kinds.size() != 0) {
			model.addAttribute("list", libraryCaseProxy.booksKindsFiltered(kinds));
		}else if (libraryBuilding != 0 && kinds.size() == 0) {
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

	@GetMapping(value ="/getLoan")
	public ModelAndView loanInfo(ModelAndView model){
		model.setViewName("loan");
		model.addObject("list", methods.loanInfoDTOList(libraryBookLoansProxy.loan()));
		return model;
	}


}

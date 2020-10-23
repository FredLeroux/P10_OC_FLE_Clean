package std.libraryUi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import std.libraryUi.proxies.LibraryBookCaseProxy;
import std.libraryUi.proxies.LibraryBuildingsProxy;

@Controller
public class LibraryUiController {

	@Autowired
	LibraryBookCaseProxy libraryCaseProxy;

	@Autowired
	LibraryBuildingsProxy libraryBuildingsProxy;

	@GetMapping(value = "/")
	public ModelAndView home() {
		return new ModelAndView("home");
	}

	@GetMapping(value="/welcome")
	public ModelAndView welcome() {
		return new ModelAndView("welcome");
	}

	@GetMapping(value = "login")
	public ModelAndView login() {
		return new ModelAndView("login");
	}

/*	@GetMapping(value ="logout")
	public ModelAndView logout() {
		return new ModelAndView("home");
	}*/

	@GetMapping(value = "/booksList")
	public  ModelAndView allBook(ModelAndView model) {
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

}

package std.libraryUi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LibraryUiController {
	
	@GetMapping(value = "/")
	public String home() {
		return "home";
	}

}

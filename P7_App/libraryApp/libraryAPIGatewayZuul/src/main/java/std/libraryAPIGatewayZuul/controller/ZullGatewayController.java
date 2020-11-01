package std.libraryAPIGatewayZuul.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ZullGatewayController {

	@GetMapping("/")
	public ModelAndView home() {
		return new ModelAndView("redirect:/libraryUi/");
	}

	@GetMapping("/welcome")
	public ModelAndView welcome() {
		return new ModelAndView("redirect:/libraryUi/welcome");
	}

}

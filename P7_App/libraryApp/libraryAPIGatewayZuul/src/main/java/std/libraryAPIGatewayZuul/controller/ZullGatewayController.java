package std.libraryAPIGatewayZuul.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ZullGatewayController {

	@GetMapping("/")
	public String home() {
		return "ui";
	}

	@GetMapping("/loanTracking")
	public String loanTracking() {
		//System.out.println("auth= "+ isUserAuthenticated());
		return "/ui/loanTracking";
	}

	@GetMapping("/login")
	public String login() {
		return "/ui/login";
	}

	@GetMapping("/bookList")
	public String bookList() {
		return "/ui/bookList";
	}

	@PostMapping("/letsGo")
	public void performLogin() {
		//return "/ui/letsGo";
	}

}

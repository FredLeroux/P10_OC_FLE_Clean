package std.libraryAPIGatewayZuul.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;

//@Controller
public class ZullGatewayController {

	@GetMapping("/")
	public String home() {
		System.out.println("auth= "+ isUserAuthenticated());
		return "ui";
	}

	@GetMapping("loanTracking")
	public String loanTracking() {
		System.out.println("auth= "+ isUserAuthenticated());
		return "/ui/loanTracking";
	}

	@GetMapping("/login")
	public String login() {
		return "/ui/login";
	}

	@GetMapping("/booksList")
	public String bookList() {
		return "/ui/booksList";
	}

	/*@PostMapping("/letsGo")
	public String performLogin() {
		return "/ui/letsGo";
	}*/

	public Boolean isUserAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			return true;
		} else {
			return false;
		}
	}

}

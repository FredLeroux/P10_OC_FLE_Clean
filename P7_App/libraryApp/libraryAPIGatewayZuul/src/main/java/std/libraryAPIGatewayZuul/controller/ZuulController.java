package std.libraryAPIGatewayZuul.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ZuulController {


	@GetMapping("/")
	public String home() {
		return "/ui";
	}

}

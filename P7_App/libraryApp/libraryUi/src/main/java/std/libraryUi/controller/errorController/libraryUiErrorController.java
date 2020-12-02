package std.libraryUi.controller.errorController;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class libraryUiErrorController implements ErrorController {


	@GetMapping(value = "/error")
	public ModelAndView errorController(ModelAndView model,HttpServletResponse response) {
		model.setViewName("errorPageTrigger");
		model.addObject("errorCode", response.getStatus());
		return model;
	}



	@Override
	public String getErrorPath() {
		return "/error";
	}


}

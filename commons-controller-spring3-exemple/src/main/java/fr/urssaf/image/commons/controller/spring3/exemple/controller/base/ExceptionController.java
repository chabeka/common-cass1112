package fr.urssaf.image.commons.controller.spring3.exemple.controller.base;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/exception")
public class ExceptionController {

	@RequestMapping
	public String getDefaultView() {

		return "exception/exceptionView";
	}

}

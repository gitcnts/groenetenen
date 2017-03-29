package be.vdab.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//als om in het even welke Controller een exception optreedt, zoekt Spring in een class
//voorzien van @ControllerAdvice, een method die de opgetreden exception verwerkt
@ControllerAdvice
public class FoutController {
	
	private static final String VIEW = "fout";

	// je geeft type exception mee dat deze method moet verwerken (ook
	// afgeleide)
	@ExceptionHandler(Exception.class)
	public String foutPagina() {
		return VIEW;
	}
	
}
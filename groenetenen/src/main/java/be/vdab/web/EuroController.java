package be.vdab.web;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import be.vdab.exceptions.KanKoersNietLezenException;
import be.vdab.services.EuroService;

@Controller
@RequestMapping("euro")
class EuroController {

	private final static String VIEW = "euro/naardollar";
	private final EuroService euroService;

	EuroController(EuroService euroService) {
		this.euroService = euroService;
	}

	@GetMapping("{euro}/naardollar")
	ModelAndView naarDollar(@PathVariable BigDecimal euro) {
		ModelAndView modelAndView = new ModelAndView(VIEW);
		try {
			modelAndView.addObject("dollar", euroService.naarDollar(euro));
		} catch (KanKoersNietLezenException ex) {
			modelAndView.addObject("fout", "Kan koers niet lezen");
		}
		return modelAndView;
	}

}

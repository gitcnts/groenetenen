package be.vdab.web;

import java.util.logging.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import be.vdab.entities.Offerte;

@Controller
@RequestMapping("/offertes")
@SessionAttributes("offerte")
class OfferteController {
	private static final String STAP1_VIEW = "offertes/stap1";
	private static final String STAP2_VIEW = "offertes/stap2";
	private static final String REDIRECT_URL_NA_TOEVOEGEN = "redirect:/";
	private static final Logger LOGGER = Logger.getLogger(OfferteController.class.getName());

	@GetMapping("aanvraag")
	ModelAndView createForm1() {
		return new ModelAndView(STAP1_VIEW).addObject(new Offerte());
	}

	@PostMapping(params = "volgende")
	String createForm1Naar2(@Validated(Offerte.Stap1.class) Offerte offerte, BindingResult bindingResult) {
		return bindingResult.hasErrors() ? STAP1_VIEW : STAP2_VIEW;
	}

	@PostMapping(params = "vorige")
	String createForm2Naar1(Offerte offerte) {
		return STAP1_VIEW;
	}

	@PostMapping(params = "bevestigen")
	String create(@Validated(Offerte.Stap2.class) Offerte offerte, BindingResult bindingResult,
			SessionStatus sessionStatus) {
		if ( ! offerte.getGazontypes().values().contains(true)) {
			bindingResult.reject("minstensEenGazonType");
		}
		if (bindingResult.hasErrors()) {
			return STAP2_VIEW;
		}
		LOGGER.info("offerte versturen via e-mail");
		sessionStatus.setComplete();
		return REDIRECT_URL_NA_TOEVOEGEN;
	}

	@PostMapping(params = "nogeennummer")
	String nogEenNummer(Offerte offerte) {
		offerte.nogEenTelefoonNr();
		return STAP1_VIEW;
	}

}
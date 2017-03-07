package be.vdab.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import be.vdab.entities.Filiaal;
import be.vdab.services.FiliaalService;

public class FiliaalControllerTest {
	private FiliaalController filiaalController;
	private List<Filiaal> filialen;
	private FiliaalService filiaalService;

	@Before
	public void before() {
		filialen = Collections.emptyList();
		// Mockito geeft je een object waarvan de class FiliaalService
		// implementeert
		filiaalService = mock(FiliaalService.class);
		// De dummy method findAll() geeft standaard null terug. Je zorgt ervoor
		// dat de lege lijst in de // variabele filialen wordt teruggegeven.
		when(filiaalService.findAll()).thenReturn(filialen);
		filiaalController = new FiliaalController(filiaalService);
	}

	@Test
	public void findAllActiveertJuisteView() {
		// Je test of een controller method de juiste view activeert
		assertEquals("filialen/filialen", filiaalController.findAll().getViewName());
	}

	@Test
	public void findAllMaaktRequestAttribuutFilialen() {
		// Je test of de ModelAndView een attribuut met de naam filialen bevat
		// en of de inhoud gelijk is aan de return waarde van de FiliaalService
		// dummy method findAll
		assertSame(filialen, filiaalController.findAll().getModelMap().get("filialen"));
	}
}

package be.vdab.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import be.vdab.entities.Filiaal;
import be.vdab.services.FiliaalService;
import be.vdab.valueobjects.Adres;

public class FiliaalControllerTest {
	private FiliaalController filiaalController;
	private List<Filiaal> filialen;
	private FiliaalService filiaalService;
	private Filiaal filiaal;

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
		// Je maakt een dummy filiaal aan
		filiaal = new Filiaal("naam1", true, BigDecimal.ONE, LocalDate.now(),
				new Adres("straat1", "huisnr1", 1, "gemeente1"));
		// Als je op mock filiaalService de method read oproept met parameter 1
		// dan krijg je het dummy filaal. Gebruik je parameter 666 dan krijg je
		// null terug.
		when(filiaalService.read(1)).thenReturn(Optional.of(filiaal));
		when(filiaalService.read(666)).thenReturn(Optional.empty());

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

	@Test
	public void readActiveertJuisteView() {
		assertEquals("filialen/filiaal", filiaalController.read(1L).getViewName());
	}

	@Test
	public void readMetBestaandeIDGeeftFiliaalTerug() {
		assertSame(filiaal, filiaalController.read(1L).getModelMap().get("filiaal"));
	}

	@Test
	public void readMetOnbestaandeIDGeeftNullTerug() {
		assertNull(filiaalController.read(666L).getModelMap().get("filiaal"));
	}

}

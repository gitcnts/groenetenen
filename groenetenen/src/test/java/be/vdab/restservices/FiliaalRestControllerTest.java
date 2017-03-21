package be.vdab.restservices;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.WebApplicationContext;

import be.vdab.datasource.TestDataSourceConfig;
import be.vdab.entities.Filiaal;
import be.vdab.repositories.TestRepositoriesConfig;
import be.vdab.restclients.RestClientsConfig;
import be.vdab.services.FiliaalService;
import be.vdab.services.ServicesConfig;
import be.vdab.valueobjects.Adres;
import be.vdab.web.ControllersConfig;

@RunWith(SpringRunner.class)
// je vult de IOC container van SpringJUnit4ClassRunner met de REST controller
// beans
// en hun dependencies
@ContextConfiguration(classes = { TestDataSourceConfig.class, TestRepositoriesConfig.class, ServicesConfig.class,
		ControllersConfig.class, RestControllersConfig.class, RestClientsConfig.class })
@WebAppConfiguration // zodat REST controllers in de IOC container HTTP requests
						// kunnen verwerken
@Transactional // zodat elke test één database transactie met aan het einde een
				// rollback
public class FiliaalRestControllerTest {

	@Autowired
	private WebApplicationContext context; // injecteert IOC container als een
											// WebApplicationContext
	@Autowired
	private FiliaalService filiaalService; // injecteert bean die interface
											// FiliaalService implementeert
	private Filiaal filiaal;
	private MockMvc mvc; // MockMvc object kan HTTP request versturen in
							// integration test

	@Before
	public void before() {
		// je voegt in elke test een filiaal toe aan de db (op het einde
		// rollback)
		filiaal = new Filiaal("naam", true, BigDecimal.TEN, LocalDate.now(),
				new Adres("straat", "huisNr", 1000, "gemeente"));
		filiaalService.create(filiaal);
		// je verbindt de MockMVC met de WebApplicationContext zodat de HTTP
		// requests naar de REST // controllers in die WebApplicationContext
		// worden gestuurd door Spring
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void filiaalLezenDatNietBestaat() throws Exception {
		mvc.perform(get("/filialen/-666") // GET request testen via mvc
				.accept(MediaType.APPLICATION_XML))// request header accept op
													// xml
				.andExpect(status().isNotFound()); // controle response status =
													// 404 (not found)
	}

	@Test
	public void filiaalDatBestaatLezenInXMLFormaat() throws Exception {
		mvc.perform(get("/filialen/" + filiaal.getId()) // GET request testen
														// via mvc
				.accept(MediaType.APPLICATION_XML)) // request header accept op
													// xml
				.andExpect(status().isOk())// controle response status = 200
											// (ok)
				.andExpect(content() // controle response header content-type is
										// xml
						.contentTypeCompatibleWith(MediaType.APPLICATION_XML))
				// zoekt in XML data in response body in element
				// filiaalResource, childelement filiaal, // childelement id en
				// controleert of gelijk aan id van toegevoegd filiaal
				.andExpect(xpath("/filiaalResource/filiaal/id").string(String.valueOf(filiaal.getId())));
	}

	@Test
	public void filiaalDatBestaatLezenInJSONFormaat() throws Exception {
		mvc.perform(get("/filialen/" + filiaal.getId()) // GET request testen
														// via mvc
				.accept(MediaType.APPLICATION_JSON))// request header accept op
													// json
				.andExpect(status().isOk()) // controle response status = 200
											// (ok)
				.andExpect(content() // controle response header content-type is
										// json
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				// zoekt in response body in JSON object ($ stelt volledige
				// response body voor) de // eigenschap filiaal en daarin de
				// eigenschap id en controleert gelijk aan id toegevoegd filiaal
				.andExpect(jsonPath("$.filiaal.id").value((int) filiaal.getId()));
	}

	@Test
	public void correctFiliaalToevoegen() throws Exception {
		// je vult een String met de inhoud van het bestand
		// nieuwCorrectFiliaal.xml. De method copyToString // sluit zelf de
		// Stream die je als parameter aanbiedt
		String nieuwCorrectFiliaal = FileCopyUtils.copyToString(
				new InputStreamReader(context.getResource("classpath:nieuwCorrectFiliaal.xml").getInputStream()));
		mvc.perform(post("/filialen") // POST request testen via mvc
				.contentType(MediaType.APPLICATION_XML) // accept op xml zetten
				.content(nieuwCorrectFiliaal)) // vult request body met string
				.andExpect(status().isCreated()); // controle response status =
													// 201 (created)
	}

	@Test
	public void filiaalMetFoutToevoegen() throws Exception {
		// je vult een String met de inhoud van het bestand
		// nieuwFiliaalMetFout.xml. CopyToString sluit zelf // de Stream die je
		// als parameter aanbiedt
		String nieuwFiliaalMetFout = FileCopyUtils.copyToString(
				new InputStreamReader(context.getResource("classpath:nieuwFiliaalMetFout.xml").getInputStream()));
		mvc.perform(post("/filialen") // POST request testen via mvc
				.contentType(MediaType.APPLICATION_XML) // accept op xml zetten
				.content(nieuwFiliaalMetFout)) // vult request body met string
				.andExpect(status().isBadRequest());// controle response status
													// = 400 (bad request)
	}
}

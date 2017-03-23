package be.vdab.restservices;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import be.vdab.entities.Filiaal;
import be.vdab.exceptions.FiliaalHeeftNogWerknemersException;
import be.vdab.exceptions.FiliaalNietGevondenException;
import be.vdab.services.FiliaalService;

@RestController // bij controller die niet-browser requests verwerkt (andere dan
				// text/html)
@RequestMapping("/filialen")
@ExposesResourceFor(Filiaal.class)
class FiliaalRestController {

	private final FiliaalService filiaalService;
	private final EntityLinks entityLinks;

	FiliaalRestController(FiliaalService filiaalService, EntityLinks entityLinks) {
		this.filiaalService = filiaalService;
		this.entityLinks = entityLinks; // injecteren van bean
	}

	@GetMapping("{filiaal}")
	FiliaalResource read(@PathVariable Filiaal filiaal) {
		if (filiaal == null) {
			throw new FiliaalNietGevondenException();
		}
		return new FiliaalResource(filiaal, entityLinks);
	}

	@GetMapping
	FilialenResource findAll() {
		return new FilialenResource(filiaalService.findAll(), entityLinks);
	}

	@DeleteMapping("{filiaal}")
	void delete(@PathVariable Filiaal filiaal) {
		if (filiaal == null) {
			throw new FiliaalNietGevondenException();
		}
		filiaalService.delete(filiaal.getId());
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	HttpHeaders create(@RequestBody @Valid Filiaal filiaal, HttpServletRequest request) {
		filiaalService.create(filiaal, request.getRequestURL().toString());
		HttpHeaders headers = new HttpHeaders();
		Link link = entityLinks.linkToSingleResource(Filiaal.class, filiaal.getId());
		headers.setLocation(URI.create(link.getHref()));
		return headers;
	}

	@PutMapping("{id}")
	void update(@RequestBody @Valid Filiaal filiaal) {
		filiaalService.update(filiaal);
	}

	@RequestMapping(path = "{filiaal}", method = RequestMethod.OPTIONS)
	HttpHeaders options(@PathVariable Filiaal filiaal) {
		if (filiaal == null) {
			throw new FiliaalNietGevondenException();
		}
		Set<HttpMethod> allowedMethods = new HashSet<>();
		allowedMethods.add(HttpMethod.GET);
		allowedMethods.add(HttpMethod.PUT);
		if (filiaal.getWerknemers().isEmpty()) {
			allowedMethods.add(HttpMethod.DELETE);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setAllow(allowedMethods);
		return headers;
	}

	@ExceptionHandler(FiliaalNietGevondenException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	void filiaalNietGevonden() {
	}

	@ExceptionHandler(FiliaalHeeftNogWerknemersException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	String filiaalHeeftNogWerknemers() {
		return "filiaal heeft nog werknemers";
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String filiaalMetVerkeerdeProperties(MethodArgumentNotValidException ex) {
		StringBuilder fouten = new StringBuilder();
		ex.getBindingResult().getFieldErrors().forEach(
				error -> fouten.append(error.getField()).append(':').append(error.getDefaultMessage()).append('\n'));
		fouten.deleteCharAt(fouten.length() - 1);
		return fouten.toString();
	}

}

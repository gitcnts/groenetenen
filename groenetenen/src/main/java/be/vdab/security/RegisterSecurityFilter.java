package be.vdab.security;

import javax.servlet.ServletContext;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;

// de AbstractSecurityWebApplicationInitializer bevat code om de Spring security filter
// te associeren met alle browser requests
public class RegisterSecurityFilter extends AbstractSecurityWebApplicationInitializer {

	// deze method wordt uitgevoerd vóór hij de Spring security filter toevoegt
	// aan de website
	@Override
	protected void beforeSpringSecurityFilterChain(ServletContext context) {
		// Vóór Spring security een browser request verwerkt moet de
		// CharacterEncodingFilter filter die request verwerken (speciale
		// tekens)
		super.insertFilters(context, new CharacterEncodingFilter("UTF-8"));
	}

}

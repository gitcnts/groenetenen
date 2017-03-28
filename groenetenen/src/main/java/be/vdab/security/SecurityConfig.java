package be.vdab.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // integreert Spring security en Spring MVC
@EnableGlobalMethodSecurity(prePostEnabled = true)
// WebSecurityConfiguerAdapter maakt de serlet filter van Spring security en de
// bijhorende Spring beans
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private static final String MANAGER = "manager";
	private static final String HELPDESKMEDEWERKER = "helpdeskmedewerker";
	private static final String MAGAZIJNIER = "magazijnier";
	private final DataSource dataSource;

	private static final String USERS_BY_USERNAME = "select naam as username, paswoord as password, actief as enabled"
			+ " from gebruikers where naam = ?";
	private static final String AUTHORITIES_BY_USERNAME = "select gebruikers.naam as username, rollen.naam as authorities"
			+ " from gebruikers inner join gebruikersrollen" + " on gebruikers.id = gebruikersrollen.gebruikerid"
			+ " inner join rollen" + " on rollen.id = gebruikersrollen.rolid" + " where gebruikers.naam= ?";

	public SecurityConfig(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	// je injecteert de standaard gemaakte AuthenticationManagerBuilder bean
	// en gebruikt deze om de authenticatie te definieren
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery(USERS_BY_USERNAME)
				.authoritiesByUsernameQuery(AUTHORITIES_BY_USERNAME).passwordEncoder(new BCryptPasswordEncoder());
	}

	// je configureert de web eigenschappen van Spring security
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
				// geen beveiliging op URL’s die passen bij images, css en
				// javascripts en hun onderliggende folders
				.antMatchers("/images/**").antMatchers("/styles/**").antMatchers("/scripts/**");
	}

	// je configureert de HTTP beveiliging van Spring security
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// gebruiker authenticeert zich door zijn gebruikersnaam en paswoord te
		// typen in een HTML form
		http.formLogin().loginPage("/login").and().logout().logoutSuccessUrl("/").and().authorizeRequests()
				// je definieert authorisatie: enkel ingelogde gebruikers met de
				// authority manager kunnen deze URL’s aanspreken
				.antMatchers("/filialen/toevoegen", "/filialen/*/wijzigen", "/filialen/*/verwijderen")
				.hasAuthority(MANAGER)
				// enkel ingelogde gebruikers met de authority manager kunnen
				// een POST request versturen naar deze URL
				.antMatchers(HttpMethod.POST, "/filialen").hasAuthority(MANAGER)
				// enkel ingelogde gebruikers met de authority magazijnier of
				// helpdeskmedewerker kunnen deze URL aanspreken
				.antMatchers("/werknemers").hasAnyAuthority(MAGAZIJNIER, HELPDESKMEDEWERKER)
				// niet-browser requests PUT en DELETE beveiligen
				.antMatchers(HttpMethod.PUT, "/filialen/*").hasAuthority(MANAGER)
				.antMatchers(HttpMethod.DELETE, "/filialen/*").hasAuthority(MANAGER)
				// je geeft alle (ook anonieme) gebruikers toegang tot de
				// welkompagina en inlogpagina
				.antMatchers("/", "/login").permitAll()
				// voor alle andere URL’s moet de gebruiker minstens ingelogd
				// zijn
				.antMatchers("/**").authenticated()
				// eigen forbidden pagina indien fout 403
				.and().exceptionHandling().accessDeniedPage("/WEB-INF/JSP/forbidden.jsp");
		// activatie basic authentication voor niet-browser requests
		http.httpBasic();
	}

}

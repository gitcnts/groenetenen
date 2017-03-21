package be.vdab.restclients;

import java.math.BigDecimal;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import be.vdab.exceptions.KanKoersNietLezenException;

@Component
class YahooKoersenClient implements KoersenClient {
	
	private final static Logger LOGGER = Logger.getLogger(YahooKoersenClient.class.getName());
	private final URI yahooURL;
	private final RestTemplate restTemplate;

	YahooKoersenClient(@Value("${yahooKoersenURL}") URI yahooURL, RestTemplate restTemplate) {
		this.yahooURL = yahooURL;
		this.restTemplate = restTemplate;
	}

	@Override
	public BigDecimal getDollarKoers() {
		try {
			Query query = restTemplate.getForObject(yahooURL, Query.class);
			return query.results.rate.rate;
		} catch (RestClientException ex) {
			LOGGER.log(Level.SEVERE, "kan koers niet lezen", ex);
			throw new KanKoersNietLezenException();
		}
	}
	
}

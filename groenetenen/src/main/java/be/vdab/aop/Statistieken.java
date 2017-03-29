package be.vdab.aop;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
class Statistieken {

	private final static Logger LOGGER = Logger.getLogger(Statistieken.class.getName());
	// key=signature jointpoint value=aantal keer dit jointpoint opgeroepen
	private final ConcurrentHashMap<String, AtomicInteger> statistieken = new ConcurrentHashMap<>();

	// toegepast op alle methods van package be.vdab.services
	@After("be.vdab.aop.PointcutExpressions.services()")
	void statistiekBijwerken(JoinPoint joinPoint) {
		String joinPointSignatuur = joinPoint.getSignature().toLongString();
		// je voegt een entry toe met value 1 indien de signature nog niet
		// voorkwam
		AtomicInteger vorigAantalOproepen = statistieken.putIfAbsent(joinPointSignatuur, new AtomicInteger(1));
		// je verhoogt de value indien de signature al wel voorkwam
		int aantalOproepen = vorigAantalOproepen == null ? 1 : vorigAantalOproepen.incrementAndGet();
		LOGGER.log(Level.INFO, "{0} werd {1} opgeroepen", new Object[] { joinPointSignatuur, aantalOproepen });
	}

}

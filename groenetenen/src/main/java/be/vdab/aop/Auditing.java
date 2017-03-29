package be.vdab.aop;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect // je definieert een class als aspect met @Aspect
@Component // een aspect moet zelf ook een Spring bean zijn
@Order(1)
class Auditing {

	// je schrijft met deze Logger auditing informatie naar de webserver
	// logbestanden
	private final static Logger LOGGER = Logger.getLogger(Auditing.class.getName());

	// after returning advice toegepast op alle methods uit de package
	// be.vdab.services
	// parameter “returnValue” wordt opgevuld met returnwaarde van jointpoint
	// (optioneel)
	@AfterReturning(pointcut = "be.vdab.aop.PointcutExpressions.services()", returning = "returnValue")
	// extra parameter voor de returnwaarde van het jointpoint
	void schrijfAudit(JoinPoint joinPoint, Object returnValue) {

		// je bouwt de auditing info op in een StringBuilder object
		// je voegt het tijdstip toe waarop een service method werd uitgevoerd
		StringBuilder builder = new StringBuilder("\nTijdstip\t").append(LocalDateTime.now());
		// je haalt een Authentication object met info over ingelogde gebruiker
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && !"anonymousUser".equals(authentication.getName())) {
			builder.append("\nGebruiker\t").append(authentication.getName());
		}
		// je haalt de package-, interface- en methodnaam op van de jointpoint
		builder.append("\nMethod\t\t").append(joinPoint.getSignature().toLongString());
		// getArgs geeft een Object array met parameterwaarden van jointpoint
		Arrays.stream(joinPoint.getArgs()).forEach(object -> builder.append("\nParameter\t").append(object));
		if (returnValue != null) {
			builder.append("\nReturn\t\t");
			if (returnValue instanceof Collection) {
				builder.append(((Collection<?>) returnValue).size()).append(" objects");
			} else {
				builder.append(returnValue.toString());
			}
		}
		// info naar Logger
		LOGGER.info(builder.toString());
	}

}

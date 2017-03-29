package be.vdab.aop;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
class Logging {
	
	private final static Logger LOGGER = Logger.getLogger(Logging.class.getName());

	// methods uit packages be.vdab.services en springframework.transaction
	// je geeft parameter throwing mee als je de exception van jointpoint wilt
	// weten
	@AfterThrowing(pointcut = "be.vdab.aop.PointcutExpressions.servicesEnTransacties()", throwing = "ex")
	// de extra Throwable parameter wordt opgevuld met de exception van
	// jointpoint
	void schrijfException(JoinPoint joinPoint, Throwable ex) {
		StringBuilder builder = new StringBuilder("\nTijdstip\t").append(LocalDateTime.now());
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && !"anonymousUser".equals(authentication.getName())) {
			builder.append("\nGebruiker\t").append(authentication.getName());
		}
		builder.append("\nMethod\t\t").append(joinPoint.getSignature().toLongString());
		Arrays.stream(joinPoint.getArgs()).forEach(object -> builder.append("\nParameter\t").append(object));
		LOGGER.log(Level.SEVERE, builder.toString(), ex);
	}
	
}
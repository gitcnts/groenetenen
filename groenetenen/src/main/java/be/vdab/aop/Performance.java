package be.vdab.aop;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(2)
class Performance {

	private final static Logger LOGGER = Logger.getLogger(Performance.class.getName());

	// toegepast op alle methods van de package be.vdab.services
	@Around("be.vdab.aop.PointcutExpressions.services()")
	// ProceedingJointPoint erft van JointPoint en heeft extra functionaliteit
	// alle fouten die het jointpoint werpt, worden verder geworpen naar de code
	// die oorspronkelijk het jointpoint opgeroepen heeft
	Object schrijfPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
		// vóór je het jointpoint uitvoert, haal je de nanotime op
		long voor = System.nanoTime();
		// het is jou verantwoordelijkheid om het joint point uit te voeren of
		// niet je voert het uit met proceed() in een try blok omdat ze een fout
		// kan werpen. De returnwaarde van de proceed bevat de returnwaarde van
		// het joint point zelf en geeft deze door aan de code die de joint
		// point oorspronkelijk heeft opgeroepen
		try {
			return joinPoint.proceed();
			// finally blok wordt altijd uitgevoerd, ook indien return in try
			// blok
		} finally {
			// je berekent de duurtijd van het uitvoeren van het joint point
			long duurtijd = System.nanoTime() - voor;
			LOGGER.log(Level.INFO, "{0} duurde {1} nanoseconden",
					new Object[] { joinPoint.getSignature().toLongString(), duurtijd });
		}
	}

}

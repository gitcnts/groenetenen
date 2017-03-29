package be.vdab.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

//je zet @Aspect vóór de class die de gecentraliseerde pointcuts bevatten
@Aspect
class PointcutExpressions {
	// je definieert een gecentraliseerde pointcut expressie
	// de method naam is de naam die je associeert met de pointcut expressie
	// de method heeft geen parameters en geeft void terug
	@Pointcut("execution(* be.vdab.services.*.*(..))")
	void services() {}

	@Pointcut("execution(* be.vdab.services.*.*(..)) " + "|| execution(* org.springframework.transaction.*.*(..))")
	void servicesEnTransacties() {}
	
}

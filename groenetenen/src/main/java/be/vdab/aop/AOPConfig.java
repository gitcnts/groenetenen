package be.vdab.aop;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
// Spring maakt één bean per class voorzien van @Component in de huidige package
@ComponentScan
// Activeren van AOP
@EnableAspectJAutoProxy
public class AOPConfig {
}

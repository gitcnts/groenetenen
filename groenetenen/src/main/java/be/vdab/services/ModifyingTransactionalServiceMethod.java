package be.vdab.services;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Target(ElementType.METHOD) // om vóór een method te zetten
@Retention(RetentionPolicy.RUNTIME)
@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
@interface ModifyingTransactionalServiceMethod {

}

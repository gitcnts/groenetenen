package be.vdab.services;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Target(ElementType.TYPE) // om vóór een class te zetten
@Retention(RetentionPolicy.RUNTIME)
@Service
@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
@interface ReadOnlyTransactionalService {

}

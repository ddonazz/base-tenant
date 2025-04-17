package it.andrea.start.annotation;

import it.andrea.start.constants.AuditActivity;
import it.andrea.start.constants.AuditTypeOperation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Audit {

    AuditActivity activity();

    AuditTypeOperation type();

    String description() default "";
}

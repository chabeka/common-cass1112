package fr.urssaf.image.commons.controller.spring3.exemple.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.ElementType;


@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {InfValidator.class})
@Documented
public @interface Inf {

	double borneInf();
	
	String message() default "{exception.infNumber}";
	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
	
}

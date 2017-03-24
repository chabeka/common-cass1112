package fr.urssaf.image.commons.controller.spring3.exemple.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {NotEmptyValidator.class})
@Documented
public @interface NotEmpty {

	String message() default "{org.hibernate.validator.constraints.NotEmpty.message}";
	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}

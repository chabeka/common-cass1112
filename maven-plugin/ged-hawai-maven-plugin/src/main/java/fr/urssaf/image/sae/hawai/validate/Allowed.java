package fr.urssaf.image.sae.hawai.validate;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Allowed {
    String[] values() default {};

}

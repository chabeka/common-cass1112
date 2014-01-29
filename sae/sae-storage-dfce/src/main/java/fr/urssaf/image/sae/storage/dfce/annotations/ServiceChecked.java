package fr.urssaf.image.sae.storage.dfce.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marque une méthode pour que ces paramètres soient vérifié par aspect.<BR />
 * Ceci pour exclure la vérification des paramètres des services des façades.
 */
@Retention(RUNTIME)
@Target(METHOD)
@Documented
public @interface ServiceChecked {

}

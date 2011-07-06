package fr.urssaf.image.sae.storage.dfce.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import fr.urssaf.image.sae.storage.dfce.messages.LogLevel;
/**
 * Marque une méthode pour qu'elle soit trace par aspect.
 *
 * 
 * @author akenore
 *
 */
@Retention(RUNTIME)
@Target(METHOD)
@Documented
public @interface Loggable {
	/**
	 * 
	 *  Le niveau de logging
	 */
	LogLevel value();
}

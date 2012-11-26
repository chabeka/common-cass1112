/**
 * 
 */
package fr.urssaf.image.sae.trace.service.validation;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.trace.model.TraceToCreate;

/**
 * Classe de validation des implémentations des méthodes de l'interface
 * RegService
 * 
 */
@Aspect
public class DispatcheurServiceValidation {

   private static final String MESSAGE_ERREUR = "l'argument {0} est obligatoire";

   private static final String CLASS_NAME = "fr.urssaf.image.sae.trace.service.DispatcheurService.";
   private static final String TRACER_METHOD = "execution(void " + CLASS_NAME
         + "ajouterTrace(*))" + " && args(trace)";

   /**
    * Réalise la validation de la méthode ajouterTrace de l'interface
    * DispatcheurService
    * 
    * @param trace
    *           trace à créer
    */
   @Before(TRACER_METHOD)
   public final void testTracer(TraceToCreate trace) {

      if (trace == null) {
         throw new IllegalArgumentException(StringUtils.replace(MESSAGE_ERREUR,
               "{0}", "trace"));
      }
   }
}

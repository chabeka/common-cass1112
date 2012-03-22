/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats.validation;

import java.io.File;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireFormatValidationException;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;

/**
 * Validation des arguments passés en entrée de l'implémentation du service
 * {@link fr.urssaf.image.sae.services.capturemasse.support.resultats.ResultatsFileEchecBloquantSupport}. La validation est basée sur la
 * programmation Aspect
 * 
 */
@Aspect
public class ResultatsFileEchecBloquantSupportValidation {

   private static final String CHECK_METHOD = "execution(void fr.urssaf.image.sae.services.capturemasse.support.resultats.ResultatsFileEchecBloquantSupport.writeResultatsFile(*,*))"
         + " && args(ecdeDirectory,erreur)";

   /**
    * Vérification de la présence de tous les arguments de la méthode
    * writeResultatsFile
    * 
    * @param ecdeDirectory
    *           chemin absolu du répertoire de traitement du traitement de masse
    * @param erreur
    *           erreur bloquante mère
    */
   @Before(CHECK_METHOD)
   public final void checkWriteResultatsFile(final File ecdeDirectory,
         final CaptureMasseSommaireFormatValidationException erreur) {

      if (ecdeDirectory == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "ecdeDirectory"));
      }

      if (erreur == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "erreur"));
      }
   }
}

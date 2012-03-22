/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats.validation;

import java.io.File;
import java.util.List;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.services.capturemasse.model.CaptureMasseIntegratedDocument;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;

/**
 * Validation des arguments en entrée des implémentations du service
 * {@link fr.urssaf.image.sae.services.capturemasse.support.resultats.ResultatFileSuccessSupport}. La validation est basée sur la
 * programmation orientée Aspect
 * 
 */
@Aspect
public class ResultatFileSuccessSupportValidation {

   private static final String CHECK_METHOD = "execution(void fr.urssaf.image.sae.services.capturemasse.support.resultats.ResultatFileSuccessSupport.writeResultatsFile(*,*,*))"
         + " && args(ecdeDirectory,integDocs,documentsCount)";

   /**
    * permet de vérifier que les éléments suivants sont présents :<br>
    * <ul>
    * <li>ecdeDirectory</li>
    * <li>documentsCount</li>
    * </ul>
    * 
    * @param ecdeDirectory
    *           répertoire de traitement du traitement de masse
    * @param integDocs
    *           liste des documents intégrés
    * @param documentsCount
    *           nombre de documents intégrés
    */
   @Before(CHECK_METHOD)
   public final void checkWriteResultats(final File ecdeDirectory,
         final List<CaptureMasseIntegratedDocument> integDocs,
         final int documentsCount) {

      if (ecdeDirectory == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "ecdeDirectory"));
      }

      if (documentsCount < 0) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "documentsCount"));
      }
   }

}

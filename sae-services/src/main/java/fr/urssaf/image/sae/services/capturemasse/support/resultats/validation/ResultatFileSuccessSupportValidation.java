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

   private static final String WRITE_CONTROLES_METHOD = "execution(void fr.urssaf.image.sae.services.capturemasse.support.resultats.ResultatFileSuccessSupport.writeResultatsFile(*,*,*))"
         + " && args(ecdeDirectory,integratedDocuments,documentsCount)";

   /**
    * permet de vérifier que les éléments suivants sont présents :<br>
    * <ul>
    * <li>ecdeDirectory</li>
    * <li>documentsCount</li>
    * </ul>
    * 
    * @param ecdeDirectory
    *           répertoire de traitement du traitement de masse
    * @param integratedDocuments
    *           liste des documents intégrés
    * @param documentsCount
    *           nombre de documents intégrés
    */
   @Before(WRITE_CONTROLES_METHOD)
   public final void checkWriteResultats(File ecdeDirectory,
         List<CaptureMasseIntegratedDocument> integratedDocuments,
         int documentsCount) {

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

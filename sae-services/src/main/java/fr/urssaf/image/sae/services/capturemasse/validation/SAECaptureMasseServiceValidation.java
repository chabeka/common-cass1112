/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.validation;

import java.net.URI;
import java.util.UUID;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;

/**
 * Classe de validation des arguments en entrée des implémentations du service
 * {@link fr.urssaf.image.sae.services.capturemasse.SAECaptureMasseService}. La validation est basée sur la programmation
 * Aspect
 * 
 */
@Aspect
public class SAECaptureMasseServiceValidation {

   private static final String CAPTURE_CONTROLES_METHOD = "execution(void fr.urssaf.image.sae.services.capturemasse.SAECaptureMasseService.captureMasse(*,*))"
         + " && args(sommaireURL, idTraitement)";

   /**
    * permet de vérifier que l'ensemble des paramètres de la méthode
    * captureMasse de SAECaptureMasseService possède tous les arguments
    * renseignés
    * 
    * @param sommaireURL
    *           URL du fichier sommaire.xml
    * @param idTraitement
    *           identifiant du traitement
    */
   @Before(CAPTURE_CONTROLES_METHOD)
   public final void checkCaptureMasse(URI sommaireURL, UUID idTraitement) {

      if (sommaireURL == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "sommaireURL"));
      }

      if (idTraitement == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "idTraitement"));
      }
   }

}

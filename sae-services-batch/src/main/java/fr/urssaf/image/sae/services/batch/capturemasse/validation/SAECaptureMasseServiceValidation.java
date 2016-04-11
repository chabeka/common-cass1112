/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.validation;

import java.net.URI;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Classe de validation des arguments en entrée des implémentations du service
 * {@link fr.urssaf.image.sae.services.batch.capturemasse.SAECaptureMasseService}. La
 * validation est basée sur la programmation Aspect
 * 
 */
@Aspect
public class SAECaptureMasseServiceValidation {

   private static final String ARGUMENT_REQUIRED = "L'argument '%s' doit être renseigné ou être non null.";

   private static final String CAPTURE_METHOD = "execution(fr.urssaf.image.sae.services.batch.common.model.ExitTraitement fr.urssaf.image.sae.services.batch.capturemasse.SAECaptureMasseService.captureMasse(*,*))"
         + " && args(sommaireURL, idTraitement)";

   private static final String CAPTURE_METHOD_HASH = "execution(fr.urssaf.image.sae.services.batch.common.model.ExitTraitement fr.urssaf.image.sae.services.batch.capturemasse.SAECaptureMasseService.captureMasse(*,*,*,*))"
         + " && args(sommaireURL, idTraitement, hash, typeHash)";

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
   @Before(CAPTURE_METHOD)
   public final void captureMasse(final URI sommaireURL, final UUID idTraitement) {

      if (sommaireURL == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "sommaireURL"));
      }

      if (idTraitement == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "idTraitement"));
      }
   }

   /**
    * permet de vérifier que l'ensemble des paramètres de la méthode
    * captureMasse de SAECaptureMasseService possède tous les arguments
    * renseignés
    * 
    * @param sommaireURL
    *           URL du fichier sommaire.xml
    * @param idTraitement
    *           identifiant du traitement
    * @param hash
    *           le hash du fichier sommaire.xml
    * @param typeHash
    *           Algorithme de hash utilisé
    */
   @Before(CAPTURE_METHOD_HASH)
   public final void captureMasse(final URI sommaireURL,
         final UUID idTraitement, final String hash, final String typeHash) {

      captureMasse(sommaireURL, idTraitement);
      // controle des paramètres supplémentaires
      if (hash != null && StringUtils.isBlank(typeHash)) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "typeHash"));
      }
   }

}

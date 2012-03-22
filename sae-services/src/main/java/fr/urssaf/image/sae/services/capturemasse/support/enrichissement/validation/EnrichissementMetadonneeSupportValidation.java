/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.enrichissement.validation;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;

/**
 * Validation des arguments passés en paramètre des implémentations de
 * {@link fr.urssaf.image.sae.services.capturemasse.support.enrichissement.EnrichissementMetadonneeSupport}
 * . Validation basée sur la programmation Aspect
 * 
 */
@Aspect
public class EnrichissementMetadonneeSupportValidation {

   private static final String CHECK_ENRICHMENT_METHOD = "execution(void fr.urssaf.image.sae.services.capturemasse.support.enrichissement.EnrichissementMetadonneeSupport.enrichirMetadonnee(*))"
         + " && args(document)";

   /**
    * permet de vérifier que l'ensemble des paramètres de la méthode
    * enrichirMetadonnee possède tous les arguments renseignés
    * 
    * @param document
    *           modèle métier du document
    */
   @Before(CHECK_ENRICHMENT_METHOD)
   public final void checkWrite(SAEDocument document) {

      if (document == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "document"));
      }
   }
}

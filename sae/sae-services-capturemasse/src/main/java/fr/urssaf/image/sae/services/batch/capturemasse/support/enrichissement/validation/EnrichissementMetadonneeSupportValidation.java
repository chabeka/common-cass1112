/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.enrichissement.validation;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.bo.SAEVirtualDocument;

/**
 * Validation des arguments passés en paramètre des implémentations de
 * {@link fr.urssaf.image.sae.services.batch.capturemasse.support.enrichissement.EnrichissementMetadonneeSupport}
 * . Validation basée sur la programmation Aspect
 * 
 */
@Aspect
public class EnrichissementMetadonneeSupportValidation {

   private static final String ARGUMENT_REQUIRED = "L'argument '%s' doit être renseigné ou être non null.";

   private static final String ENRICHMENT = "execution(void fr.urssaf.image.sae.services.batch.capturemasse.support.enrichissement.EnrichissementMetadonneeSupport.enrichirMetadonnee(*))"
         + " && args(document)";

   private static final String ENRICHMENT_VRTL = "execution(void fr.urssaf.image.sae.services.batch.capturemasse.support.enrichissement.EnrichissementMetadonneeSupport.enrichirMetadonneesVirtuelles(*))"
         + " && args(document)";

   /**
    * permet de vérifier que l'ensemble des paramètres de la méthode
    * enrichirMetadonnee possède tous les arguments renseignés
    * 
    * @param document
    *           modèle métier du document
    */
   @Before(ENRICHMENT)
   public final void checkWrite(final SAEDocument document) {
      if (document == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "document"));
      }
   }

   /**
    * permet de vérifier que l'ensemble des paramètres de la méthode
    * enrichirMetadonnee possède tous les arguments renseignés
    * 
    * @param document
    *           modèle métier du document
    */
   @Before(ENRICHMENT_VRTL)
   public final void checkWriteVrtl(final SAEVirtualDocument document) {
      if (document == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "document virtuel"));
      }
   }
}

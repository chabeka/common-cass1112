/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.enrichissement.validation;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument;

/**
 * Validation des arguments passés en paramètre des implémentations de
 * {@link fr.urssaf.image.sae.services.capturemasse.support.enrichissement.EnrichissementMetadonneeSupport}
 * . Validation basée sur la programmation Aspect
 * 
 */
@Aspect
public class EnrichissementStorageDocumentSupportValidation {

   private static final String ARGUMENT_REQUIRED = "argument.required";

   private static final String ENRICHMENT = "execution(fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument fr.urssaf.image.sae.services.capturemasse.support.enrichissement.EnrichissementStorageDocumentSupport.enrichirDocument(*,*))"
         + " && args(document,uuid)";

   private static final String ENRICHMENT_VRTL = "execution(fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument fr.urssaf.image.sae.services.capturemasse.support.enrichissement.EnrichissementStorageDocumentSupport.enrichirVirtualDocument(*,*))"
         + " && args(document,uuid)";

   /**
    * permet de vérifier que l'ensemble des paramètres de la méthode
    * enrichirMetadonnee possède tous les arguments renseignés
    * 
    * @param document
    *           modèle métier du document
    * @param uuid
    *           : uuid
    */
   @Before(ENRICHMENT)
   public final void checkEnrichissement(final StorageDocument document,
         String uuid) {

      if (document == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARGUMENT_REQUIRED, "document"));
      }

      if (StringUtils.isBlank(uuid)) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARGUMENT_REQUIRED, "uuid"));
      }
   }

   /**
    * permet de vérifier que l'ensemble des paramètres de la méthode
    * enrichirVirualDocument possède tous les arguments renseignés
    * 
    * @param document
    *           modèle métier du document
    * @param uuid
    *           : uuid
    */
   @Before(ENRICHMENT_VRTL)
   public final void checkEnrichissementVirtuel(
         final VirtualStorageDocument document, String uuid) {

      if (document == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARGUMENT_REQUIRED, "document virtuel"));
      }
      
      if (StringUtils.isEmpty(uuid)) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               ARGUMENT_REQUIRED, "identifiant unique du process"));
      }
   }
}

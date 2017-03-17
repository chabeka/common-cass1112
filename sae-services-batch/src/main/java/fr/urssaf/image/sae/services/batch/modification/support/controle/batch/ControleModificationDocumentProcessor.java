/**
 * 
 */
package fr.urssaf.image.sae.services.batch.modification.support.controle.batch;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.listener.AbstractListener;
import fr.urssaf.image.sae.services.batch.modification.support.controle.ModificationMasseControleSupport;
import fr.urssaf.image.sae.services.batch.modification.support.controle.model.ModificationMasseControlResult;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;

/**
 * ItemProcessor pour le contrôle des documents au stockage
 * 
 */
@Component
public class ControleModificationDocumentProcessor extends AbstractListener
      implements ItemProcessor<UntypedDocument, StorageDocument> {

   private static final Logger LOGGER = LoggerFactory.getLogger(ControleModificationDocumentProcessor.class);
   
   @Autowired
   private ModificationMasseControleSupport support;

   /**
    * {@inheritDoc}
    */
   @Override
   public final StorageDocument process(final UntypedDocument item) throws Exception {

      List<StorageMetadata> listeMetadataDocument = this.retrieveListMetadatasToMap(item);
      
      StorageDocument document = support.controleSAEDocumentModification(item, listeMetadataDocument);

      this.deleteListMetadatasToMap(item);

      return document;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   protected final ExitStatus specificAfterStepOperations() {
      return getStepExecution().getExitStatus();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void specificInitOperations() {
      // rien à faire
   }
   
   /**
    * Récupérer la liste des metadonnées pour le document en cours de traitement.
    * @param item Document
    * @return la liste des metadatas gardées en mémoire.
    */
   private List<StorageMetadata> retrieveListMetadatasToMap(final UntypedDocument item) {
      String trcPrefix = "retrieveListMetadatasToMap";
      List<StorageMetadata> listeMetadataDocument = null;
      LOGGER.debug("{} - début", trcPrefix);
      if (getStepExecution() != null) {
         // récupére la map de resultat de controle de capture de masse
         @SuppressWarnings("unchecked")
         Map<String, ModificationMasseControlResult> map = (Map<String, ModificationMasseControlResult>) getStepExecution().getJobExecution().getExecutionContext().get("mapModificationControlResult");
         if (map == null) {
            LOGGER.debug("{} - Map des résultat de controle non présente dans le contexte d'éxécution",
                        trcPrefix);
         } else {
            LOGGER.debug("{} - Map des résultat de controle récupéré", trcPrefix);
            // Recupére le résultat de controle de capture de masse du document archive
            ModificationMasseControlResult resultat = map.get(item.getUuid().toString());
            if (resultat == null) {
               LOGGER.debug("{} - Résultat de controle non présent dans la map pour la key {}",
                           trcPrefix, item.getUuid().toString());
            } else {
               LOGGER.debug("{} - Récupération OK du résultat de controle pour le document",
                           trcPrefix);
               listeMetadataDocument = resultat.getStorageMetadatasList();
            }
         }
      }
      LOGGER.debug("{} - fin", trcPrefix);
      
      return listeMetadataDocument;
   }

   /**
    * Nettoyage de la map pour ne pas garder trop d'information en mémoire.
    * 
    * @param item
    */
   private void deleteListMetadatasToMap(UntypedDocument item) {
      String trcPrefix = "deleteListMetadatasToMap";
      LOGGER.debug("{} - début", trcPrefix);
      if (getStepExecution() != null) {
         // récupére la map de resultat de controle de capture de masse
         @SuppressWarnings("unchecked")
         Map<String, ModificationMasseControlResult> map = (Map<String, ModificationMasseControlResult>) getStepExecution()
               .getJobExecution().getExecutionContext()
               .get("mapModificationControlResult");
         if (map == null) {
            LOGGER.debug(
                  "{} - Map des résultat de controle non présente dans le contexte d'éxécution",
                  trcPrefix);
         } else {
            LOGGER.debug("{} - Map des résultat de controle récupéré",
                  trcPrefix);
            // Recupére le résultat de controle de capture de masse du document
            // archive
            String uuid = item.getUuid().toString();
            ModificationMasseControlResult resultat = map.get(uuid);
            if (resultat == null) {
               LOGGER.debug(
                     "{} - Résultat de controle non présent dans la map pour la key {}",
                     trcPrefix, item.getUuid().toString());
            } else {
               LOGGER.debug(
                     "{} - Nettoyage de la map des metadonnées.",
                     trcPrefix);
               map.remove(uuid);
            }
         }
      }
      LOGGER.debug("{} - fin", trcPrefix);

   }
}

package fr.urssaf.image.sae.services.batch;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.ecde.modele.resultats.Resultats;
import fr.urssaf.image.sae.ecde.modele.sommaire.Sommaire;
import fr.urssaf.image.sae.services.jmx.CommonIndicator;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.jmx.JmxIndicator;
import fr.urssaf.image.sae.storage.model.storagedocument.BulkInsertionResults;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;

/**
 * Cette classe a pour rôle de contrôler, de convertir et d’archiver les
 * documents contenus dans le sommaire en mode « tout ou rien ».<br>
 * Le sommaire est passé en paramètre de la méthode bulkCapture.
 * 
 * @author Rhofir
 */
@Component
@Qualifier("bulkCaptureJob")
@SuppressWarnings( { "PMD.LongVariable" })
public class BulkCaptureJob extends CommonIndicator {
   private static final Logger LOGGER = LoggerFactory
         .getLogger(BulkCaptureJob.class);
   @Autowired
   @Qualifier("storageServiceProvider")
   private StorageServiceProvider storageServiceProvider;
   @Autowired
   @Qualifier("bulkCaptureHelper")
   private BulkCaptureHelper bulkCaptureHelper;

   /**
    * Service pour l'opération <b>capture en Masse</b>.
    * 
    * @param sommaire
    *           Un objet de type {@link Sommaire}.
    * @param treatementId
    *           : UUID du traitement en masse
    * @return Résultats Un objet résultat de type {@link Résultats}.
    */
   public final Resultats bulkCapture(Sommaire sommaire, String treatementId) {
      String prefixeTrc = "bulkCapture()";
      LOGGER.debug("{} - Début", prefixeTrc);
      LOGGER.debug("{} - Nombre de documents à archiver : {}", prefixeTrc,
            sommaire.getDocuments().size());
      LOGGER.debug("{} - Mode de la capture de masse : {}", prefixeTrc,
            sommaire.getBatchMode());
      Resultats resultats = null;
      boolean hasError = false;
      BulkInsertionResults bulkInsertionResults = null;
      bulkCaptureHelper.setIndicator(getIndicator());
      List<UntypedDocument> untypedDocs = sommaire.getDocuments();
      // Construire un ensemble de StorageDocument
      List<StorageDocument> storageDocs = bulkCaptureHelper
            .buildStorageDocuments(untypedDocs);
      // Cas lors qu'il y a un problème lors de la construction de la liste
      // StorageDocument
      if (!CollectionUtils.isEmpty(bulkCaptureHelper
            .getUntypedDocumentsOnError())) {
         resultats = bulkCaptureHelper.buildResultatsError(untypedDocs.size(),
               sommaire, bulkCaptureHelper.getUntypedDocumentsOnError());
         hasError = true;
      }
      if (resultats == null) {  
         // Enrichissement des SAEMetadata par un Id traitement en masse
         storageDocs = bulkCaptureHelper.addIdTreatementToStorageDoc(
               storageDocs, treatementId);
         StorageDocuments storageDocuments = new StorageDocuments(storageDocs);
         bulkInsertionResults = bulkCaptureInsert(storageDocuments);
      }
      // Cas lors qu'il y a un problème lors de l'insetion dans DFCE.
      if (!CollectionUtils.isEmpty(bulkCaptureHelper
            .getUntypedDocumentsOnError())
            && !hasError) {
         resultats = bulkCaptureHelper.buildResultatsError(untypedDocs.size(),
               sommaire, bulkCaptureHelper.getUntypedDocumentsOnError());
      }
      if (resultats == null) {
         resultats = bulkCaptureHelper.buildResultatsSuccess(
               bulkInsertionResults, untypedDocs.size(), sommaire);
      }
      LOGGER.debug("{} - Sortie", prefixeTrc);
      // Fin des traces debug - sortie méthode
      return resultats;
   }

   /**
    * Lancer l'archivage en masse.
    * 
    * @param storageDocuments
    *           : Document de type {@link StorageDocument}
    * @throws ConnectionServiceEx
    *            Exception de type {@link ConnectionServiceEx}
    * @throws InsertionServiceEx
    *            Exception de type {@link InsertionServiceEx}
    */
   private BulkInsertionResults bulkCaptureInsert(
         StorageDocuments storageDocuments) {
      BulkInsertionResults bulkInsertionResults = null;
      try {
         storageServiceProvider.openConnexion();
         StorageDocumentService storageService = storageServiceProvider
               .getStorageDocumentService();
         storageService.setJmxIndicator(getIndicator());
         bulkInsertionResults = storageService.bulkInsertStorageDocument(
               storageDocuments, true);
         storageServiceProvider.closeConnexion();
      } catch (ConnectionServiceEx except) {
         logBulkCaptureException(except);
         bulkCaptureHelper.buildTechnicalErrors(storageDocuments, except);
      } catch (InsertionServiceEx except) {
         logBulkCaptureException(except);
         bulkCaptureHelper.buildTechnicalErrors(storageDocuments, except);
      } catch (Exception except) {
         logBulkCaptureException(except);
         bulkCaptureHelper.buildTechnicalErrors(storageDocuments, except);
      }
      return bulkInsertionResults;
   }

   // code provisoir pour bénificier d'un log en cas d'erreur dans l'insertion
   // en masse
   @Deprecated
   private void logBulkCaptureException(Exception exception) {

      LOGGER.warn("bulkCapture() - erreur lors de l'insertion en masse ",
            exception);
   }

   /**
    * 
    * @return Les indicateurs du job de la capture.
    */
   public JmxIndicator retrieveJmxBulkCaptureJobIndicator() {
      storageServiceProvider.getStorageDocumentService().setJmxIndicator(
            getIndicator());
      return storageServiceProvider.getStorageDocumentService()
            .retrieveJmxStorageIndicator();

   }

}

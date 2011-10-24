package fr.urssaf.image.sae.services.batch;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.ecde.modele.resultats.Resultats;
import fr.urssaf.image.sae.ecde.modele.sommaire.Sommaire;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.BulkInsertionResults;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

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
public class BulkCaptureJob {

   @Autowired
   @Qualifier("storageServiceProvider")
   private StorageServiceProvider storageServiceProvider;

   @Autowired
   @Qualifier("bulkCaptureHelper")
   private BulkCaptureHelper bulkCaptureHelper;

   // private ExceptionDispatcher dispatcher = new ExceptionDispatcher();

   /**
    * Service pour l'opération <b>capture en Masse</b>.
    * 
    * @param sommaire
    *           Un objet de type {@link Sommaire}.
    * @return Resultats Un objet résultat de type {@link Resultats}.
    */
   public final Resultats bulkCapture(Sommaire sommaire) {
      Resultats resultats = null;
      boolean hasError = false;
      BulkInsertionResults bulkInsertionResults = null;
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
         storageDocs = bulkCaptureHelper
               .addIdTreatementToStorageDoc(storageDocs);
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
         bulkInsertionResults = storageServiceProvider
               .getStorageDocumentService().bulkInsertStorageDocument(
                     storageDocuments, true);
         storageServiceProvider.closeConnexion();
      } catch (ConnectionServiceEx except) {
         bulkCaptureHelper.buildTechnicalErrors(storageDocuments, except);
      } catch (InsertionServiceEx except) {
         bulkCaptureHelper.buildTechnicalErrors(storageDocuments, except);
      } catch (Exception except) {
         bulkCaptureHelper.buildTechnicalErrors(storageDocuments, except);
      }
      return bulkInsertionResults;
   }
}

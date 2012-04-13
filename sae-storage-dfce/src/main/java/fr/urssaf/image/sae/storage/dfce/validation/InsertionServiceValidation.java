package fr.urssaf.image.sae.storage.dfce.validation;

import org.apache.commons.lang.Validate;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.storage.dfce.messages.StorageMessageHandler;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;

/**
 * Fournit des méthodes de validation des arguments des services d'insertion par
 * aspect.
 * 
 * @author akenore
 * 
 */

@Aspect
public class InsertionServiceValidation {
   // Code erreur.
   private static final String CODE_ERROR = "insertion.code.message";

   /**
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.InsertionServiceImpl#insertStorageDocument(StorageDocument)
    * insertStorageDocument}
    * 
    * @param storageDocument
    *           : Le document à insérer.
    */
   @Before(value = "execution( fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument  fr.urssaf.image.sae.storage.services.storagedocument..InsertionService.insertStorageDocument(..)) && @annotation(fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked) && args(storageDocument)")
   public final void insertStorageDocumentValidation(
         final StorageDocument storageDocument) {
      // ici on valide que le document n'est pas null
      Validate.notNull(storageDocument, StorageMessageHandler.getMessage(CODE_ERROR,
            "insertion.document.required", "insertion.impact",
            "insertion.action"));
//      Validate.notNull(storageDocument.getContent(), StorageMessageHandler.getMessage(
//            CODE_ERROR, "insertion.document.required", "insertion.impact",
//            "insertion.action"));
   }
   
   
   /**
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.InsertionServiceImpl#insertBinaryStorageDocument(StorageDocument)
    * insertBinaryStorageDocument}
    * 
    * @param storageDocument
    *           : Le document à insérer.
    */
   @Before(value = "execution( fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument  fr.urssaf.image.sae.storage.services.storagedocument..InsertionService.insertBinaryStorageDocument(..)) && @annotation(fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked) && args(storageDocument)")
   public final void insertBinaryStorageDocumentValidation(
         final StorageDocument storageDocument) {
      // ici on valide que le document n'est pas null
      Validate.notNull(storageDocument, StorageMessageHandler.getMessage(CODE_ERROR,
            "insertion.document.required", "insertion.impact",
            "insertion.action"));
   }
   

   /**
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.InsertionServiceImpl#bulkInsertStorageDocument(StorageDocuments, boolean)
    * bulkInsertStorageDocument}.
    * 
    * @param storageDocuments
    *           : La liste des documents
    * @param allOrNothing
    *           : Paramètre qui indique si l'insertion sera réalisée en mode
    *           "Tout ou rien".
    */
   @Before(value = "execution( fr.urssaf.image..BulkInsertionResults  fr.urssaf.image.sae.storage.services.storagedocument..InsertionService.bulkInsertStorageDocument(..)) && @annotation(fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked)  && args(storageDocuments,allOrNothing)")
   public final void bulkInsertStorageDocumentValidation(
         final StorageDocuments storageDocuments, final boolean allOrNothing) {
      // ici on valide que le document n'est pas null
      Validate.notNull(storageDocuments, StorageMessageHandler.getMessage(CODE_ERROR,
            "bulk.insertion.document.required",
            "bulk.insertion.allOrNothing.impact",
            "bulk.insertion.allOrNothing.action"));
      // ici on vérifie que tous les composants du document ne sont pas null.
      Validate.notNull(storageDocuments.getAllStorageDocuments(), StorageMessageHandler
            .getMessage(CODE_ERROR,
                  "bulk.insertion.document.component.required",
                  "bulk.insertion.allOrNothing.impact",
                  "bulk.insertion.allOrNothing.action"));

   }
}

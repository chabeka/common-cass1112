package fr.urssaf.image.sae.storage.dfce.validation;

import org.apache.commons.lang.Validate;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.storage.dfce.messages.StorageMessageHandler;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;

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
   

}

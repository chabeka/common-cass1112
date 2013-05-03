package fr.urssaf.image.sae.storage.dfce.validation;

import org.apache.commons.lang.Validate;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.storage.dfce.messages.StorageMessageHandler;
import fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.InsertionServiceImpl;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageReference;

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
      Validate.notNull(storageDocument, StorageMessageHandler.getMessage(
            CODE_ERROR, "insertion.document.required", "insertion.impact",
            "insertion.action"));
      // Validate.notNull(storageDocument.getContent(),
      // StorageMessageHandler.getMessage(
      // CODE_ERROR, "insertion.document.required", "insertion.impact",
      // "insertion.action"));
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
      Validate.notNull(storageDocument, StorageMessageHandler.getMessage(
            CODE_ERROR, "insertion.document.required", "insertion.impact",
            "insertion.action"));
   }

   /**
    * Valide l'argument de la méthode
    * {@link InsertionServiceImpl#insertStorageReference(fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageReference)}
    * 
    * @param storageReference
    *           : la référence à insérer.
    */
   @Before(value = "execution( fr.urssaf.image.sae.storage.model.storagedocument.StorageReferenceFile  fr.urssaf.image.sae.storage.services.storagedocument..InsertionService.insertStorageReference(..)) && @annotation(fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked) && args(storageReference)")
   public final void insertStorageReferenceValidation(
         final VirtualStorageReference storageReference) {
      // ici on valide que le document n'est pas null
      Validate.notNull(storageReference, StorageMessageHandler.getMessage(
            CODE_ERROR, "insertion.reference.required", "insertion.impact",
            "insertion.action"));
   }

   /**
    * Valide l'argument de la méthode
    * {@link InsertionServiceImpl#insertVirtualStorageDocument(VirtualStorageDocument)}
    * 
    * @param storageDocument
    *           : le document virtuel à insérer
    */
   @Before(value = "execution( java.util.UUID  fr.urssaf.image.sae.storage.services.storagedocument..InsertionService.insertVirtualStorageDocument(..)) && @annotation(fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked) && args(storageDocument)")
   public final void insertVirtualStorageDocumentValidation(
         final VirtualStorageDocument storageDocument) {
      // ici on valide que le document n'est pas null
      Validate.notNull(storageDocument, StorageMessageHandler.getMessage(
            CODE_ERROR, "insertion.document.required", "insertion.impact",
            "insertion.action"));
   }

}

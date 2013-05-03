package fr.urssaf.image.sae.mapping.validation;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.bo.SAEVirtualDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.mapping.messages.MappingMessageHandler;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;

/**
 * Fournit des méthodes de validation des arguments des services de l'interface
 * {@link fr.urssaf.image.sae.mapping.services.MappingDocumentService
 * MappingDocumentService}
 * 
 * @author akenore
 * 
 */
@Aspect
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class MappingDocumentServiceValidation {

   /**
    * 
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.mapping.services.MappingDocumentService#saeDocumentToStorageDocument(fr.urssaf.image.sae.bo.model.bo.SAEDocument)
    * saeDocumentToStorageDocument}.<br>
    * 
    * @param saeDoc
    *           : Le document métier.
    */
   @Before(value = "execution(fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument  fr.urssaf.image.sae.mapping.services.MappingDocumentService.saeDocumentToStorageDocument(..)) && args(saeDoc)")
   public final void saeDocumentToStorageDocumentValidation(
         final SAEDocument saeDoc) {
      Validate.notNull(saeDoc, MappingMessageHandler.getMessage(
            "mapping.document.required", SAEDocument.class.getName()));
      Validate.notNull(saeDoc.getMetadatas(), MappingMessageHandler.getMessage(
            "mapping.document.metadata.required", SAEDocument.class.getName()));
   }

   /**
    * 
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.mapping.services.MappingDocumentService#saeDocumentToUntypedDocument(SAEDocument)
    * saeDocumentToUntypedDocument}.<br>
    * 
    * @param saeDoc
    *           : Le document métier.
    */
   @Before(value = "execution(fr.urssaf.image.sae.bo.model.untyped.UntypedDocument  fr.urssaf.image.sae.mapping.services.MappingDocumentService.saeDocumentToUntypedDocument(..)) && args(saeDoc)")
   public final void saeDocumentToUntypedDocumentValidation(
         final SAEDocument saeDoc) {
      Validate.notNull(saeDoc, MappingMessageHandler.getMessage(
            "mapping.document.required", SAEDocument.class.getName()));
      Validate.notNull(saeDoc.getMetadatas(), MappingMessageHandler.getMessage(
            "mapping.document.required", SAEDocument.class.getName()));
   }

   /**
    * 
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.mapping.services.MappingDocumentService#storageDocumentToSaeDocument(StorageDocument)
    * storageDocumentToSaeDocument }.<br>
    * 
    * @param storageDoc
    *           : Le document de stockage.
    */
   @Before(value = "execution(fr.urssaf.image.sae.bo.model.bo.SAEDocument  fr.urssaf.image.sae.mapping.services.MappingDocumentService.storageDocumentToSaeDocument(..)) && args(storageDoc)")
   public final void storageDocumentToSaeDocumentValidation(
         final StorageDocument storageDoc) {
      Validate.notNull(storageDoc, MappingMessageHandler.getMessage(
            "mapping.document.required", StorageDocument.class.getName()));
      Validate.notNull(storageDoc.getMetadatas(), MappingMessageHandler
            .getMessage("mapping.document.required", StorageDocument.class
                  .getName()));
   }

   /**
    * 
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.mapping.services.MappingDocumentService#storageDocumentToUntypedDocument(StorageDocument)
    * storageDocumentToSaeDocument }.<br>
    * 
    * @param storageDoc
    *           : Le document de stockage.
    */
   @Before(value = "execution(fr.urssaf.image.sae.bo.model.untyped.UntypedDocument  fr.urssaf.image.sae.mapping.services.MappingDocumentService.storageDocumentToUntypedDocument(..)) && args(storageDoc)")
   public final void storageDocumentToUntypedDocumentValidation(
         final StorageDocument storageDoc) {
      Validate.notNull(storageDoc, MappingMessageHandler.getMessage(
            "mapping.document.required", StorageDocument.class.getName()));
      Validate.notNull(storageDoc.getMetadatas(), MappingMessageHandler
            .getMessage("mapping.document.required", StorageDocument.class
                  .getName()));
   }

   /**
    * 
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.mapping.services.MappingDocumentService#untypedDocumentToSaeDocument(UntypedDocument)
    * untypedDocumentToSaeDocument }.<br>
    * 
    * @param untyped
    *           : Le document non typée.
    */
   @Before(value = "execution(fr.urssaf.image.sae.bo.model.bo.SAEDocument  fr.urssaf.image.sae.mapping.services.MappingDocumentService.untypedDocumentToSaeDocument(..)) && args(untyped)")
   public final void untypedDocumentToSaeDocumentValidation(
         final UntypedDocument untyped) {
      Validate.notNull(untyped, MappingMessageHandler.getMessage(
            "mapping.document.required", UntypedDocument.class.getName()));
      Validate.notNull(untyped.getUMetadatas(), MappingMessageHandler
            .getMessage("mapping.document.required", UntypedDocument.class
                  .getName()));
   }

   /**
    * 
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.mapping.services.MappingDocumentService#untypedMetadatasToSaeMetadatas(java.util.List)}
    * 
    * @param metadatas
    *           : Le document non typée.
    */
   @Before(value = "execution(java.util.List  fr.urssaf.image.sae.mapping.services.MappingDocumentService.untypedMetadatasToSaeMetadatas(..)) && args(metadatas)")
   public final void untypedMetadatasToSaeMetadatasValidation(
         final List<UntypedMetadata> metadatas) {
      Validate.notNull(metadatas, MappingMessageHandler
            .getMessage("mapping.metadata.required"));
   }

   /**
    * 
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.mapping.services.MappingDocumentService#saeVirtualDocumentToVirtualStorageDocument(SAEVirtualDocument)}
    * <br />
    * 
    * @param document
    *           : Le document à transformer
    */
   @Before(value = "execution(fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument fr.urssaf.image.sae.mapping.services.MappingDocumentService.saeVirtualDocumentToVirtualStorageDocument(..)) && args(document)")
   public final void saeVirtualDocumentToVirtualStoragDocument(
         final SAEVirtualDocument document) {
      Validate.notNull(document, MappingMessageHandler.getMessage(
            "mapping.document.required", SAEVirtualDocument.class.getName()));

      Validate.notNull(document.getMetadatas(), MappingMessageHandler
            .getMessage("mapping.document.required", SAEVirtualDocument.class
                  .getName()));
      Validate.notNull(document.getReference(), MappingMessageHandler
            .getMessage("mapping.reference.required", SAEVirtualDocument.class
                  .getName()));
   }

}

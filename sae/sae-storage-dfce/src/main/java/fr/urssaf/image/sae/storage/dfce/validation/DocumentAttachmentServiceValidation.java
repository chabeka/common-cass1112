package fr.urssaf.image.sae.storage.dfce.validation;

import java.util.UUID;

import javax.activation.DataHandler;

import org.apache.commons.lang.Validate;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.storage.dfce.messages.StorageMessageHandler;

/**
 * Fournit des méthodes de validation des arguments des services de gestion des
 * documents attachés par aspect.
 */
@Aspect
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class DocumentAttachmentServiceValidation {
   // Code erreur.
   private static final String CODE_ERROR = "gestiondocatt.code.message";

   /**
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.DocumentAttachmentServiceImpl#addDocumentAttachment(UUID,String,String,DataHandler)}
    * 
    * @param docUuid
    *           L'identifiant du document
    * @param docName
    *           Le nom du document
    * @param extension
    *           L'extension du document
    * @param contenu
    *           Contenu du document
    */
   @Before(value = "execution(void fr.urssaf.image.sae.storage.services.storagedocument.DocumentAttachmentService.addDocumentAttachment(*,*,*,*)) && @annotation(fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked) && args(docUuid, docName, extension, contenu)")
   public final void addDocumentAttachment(final UUID docUuid,
         final String docName, final String extension, final DataHandler contenu) {
      Validate.notNull(docUuid, StorageMessageHandler.getMessage(CODE_ERROR,
            "add.document.attache.docuuid.required", "ajoutdocattache.impact",
            "ajoutdocattache.docuuid.action"));

      Validate.notNull(docUuid.toString(), StorageMessageHandler.getMessage(
            CODE_ERROR, "add.document.attache.docuuid.required",
            "ajoutdocattache.impact", "ajoutdocattache.docuuid.action"));

      Validate.notEmpty(docName, StorageMessageHandler.getMessage(CODE_ERROR,
            "add.document.attache.docname.required", "ajoutdocattache.impact",
            "ajoutdocattache.docName.action"));

      Validate.notEmpty(extension, StorageMessageHandler.getMessage(CODE_ERROR,
            "add.document.attache.extension.required",
            "ajoutdocattache.impact", "ajoutdocattache.extension.action"));

      Validate.notNull(contenu, StorageMessageHandler.getMessage(CODE_ERROR,
            "add.document.attache.contenu.required", "ajoutdocattache.impact",
            "ajoutdocattache.contenu.action"));

   }

   /**
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.DocumentAttachmentServiceImpl#getDocumentAttachment(UUID)}
    * 
    * @param docUuid
    *           L'identifiant du document pour lequel on cherche le document attaché
    */
   @Before(value = "execution(fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentAttachment fr.urssaf.image.sae.storage.services.storagedocument.DocumentAttachmentService.getDocumentAttachment(*)) && @annotation(fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked) && args(docUuid)")
   public final void getDocumentAttachment(final UUID docUuid) {
      Validate.notNull(docUuid, StorageMessageHandler.getMessage(CODE_ERROR,
            "add.document.attache.docuuid.required", "ajoutdocattache.impact",
            "ajoutdocattache.docuuid.action"));

      Validate.notNull(docUuid.toString(), StorageMessageHandler.getMessage(
            CODE_ERROR, "add.document.attache.docuuid.required",
            "ajoutdocattache.impact", "ajoutdocattache.docuuid.action"));

   }
   

}

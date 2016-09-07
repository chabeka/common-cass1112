package fr.urssaf.image.sae.storage.dfce.validation;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.storage.dfce.messages.StorageMessageHandler;

/**
 * Fournit des méthodes de validation des arguments des services de gestion des
 * notes par aspect.
 */
@Aspect
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class DocumentNoteServiceValidation {
   // Code erreur.
   private static final String CODE_ERROR = "gestionnote.code.message";

   /**
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.DocumentNoteServiceImpl#addDocumentNote(UUID,String,String,Date,UUID)}
    * 
    * 
    * @param docUuid
    *           L'identifiant du document
    * @param contenu
    *           Le contenu de la note
    * @param login
    *           Login
    * @param dateCreation
    *           Date de création de la note
    * @param noteUuid
    *           UUID de la note
    */
   @Before(value = "execution(void  fr.urssaf.image.sae.storage.services.storagedocument.DocumentNoteService.addDocumentNote(*,*,*,*,*)) && @annotation(fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked) && args(docUuid, contenu, login, dateCreation, noteUuid)")
   public final void addDocumentNoteValidation(final UUID docUuid,
         final String contenu, final String login, final Date dateCreation,
         final UUID noteUuid) {
      Validate.notNull(docUuid, StorageMessageHandler.getMessage(CODE_ERROR,
            "add.document.note.docuuid.required", "ajoutnote.impact",
            "ajoutnote.docuuid.action"));

      Validate.notNull(docUuid.toString(), StorageMessageHandler.getMessage(
            CODE_ERROR, "add.document.note.docuuid.required",
            "ajoutnote.impact", "ajoutnote.docuuid.action"));

      Validate.notEmpty(contenu, StorageMessageHandler.getMessage(CODE_ERROR,
            "add.document.note.contenu.required", "ajoutnote.impact",
            "ajoutnote.contenu.action"));

   }

   /**
    * Valide l'argument de la méthode
    * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.DocumentNoteServiceImpl#getDocumentNote(UUID)}
    * 
    * 
    * @param docUuid
    *           L'identifiant du document
    */
   @Before(value = "execution(java.util.List<fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentNote>  fr.urssaf.image.sae.storage.services.storagedocument.DocumentNoteService.getDocumentNotes(*)) && @annotation(fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked) && args(docUuid)")
   public final void getDocumentNotesValidation(final UUID docUuid) {
      Validate.notNull(docUuid, StorageMessageHandler.getMessage(CODE_ERROR,
            "get.document.notes.docuuid.required", "getnote.impact",
            "getnote.docuuid.action"));
   }

}

package fr.urssaf.image.sae.storage.services.storagedocument;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import fr.urssaf.image.sae.storage.exception.DocumentNoteServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentNote;

/**
 * Interface pour accès à la base du service de gestion des notes
 */
public interface DocumentNoteService {

   /**
    * 
    * @param <T>
    *           : Le type générique.
    * @param parameter
    *           : Le paramètre du service {@link DeletionService}
    */
   <T> void setDeletionServiceParameter(T parameter);

   /**
    * Ajoute une note sur un document
    * 
    * @param docUuid
    *           L'identifiant du document concerné par la note
    * @param contenu
    *           Contenu de la note
    * @param login
    *           Login utilisateur
    * @param dateCreation
    *           Date de création de la note
    * @param noteUuid
    *           UUID de la note
    * @throws DocumentNoteServiceEx
    *            Une erreur s'est produite lors de l'ajout d'une note au
    *            document
    */
   void addDocumentNote(UUID docUuid, String contenu, String login, Date dateCreation, UUID noteUuid)
         throws DocumentNoteServiceEx;

   /**
    * Récupération de la liste des notes d'un document
    * 
    * @param docUuid
    *           UUID du document dont on souhaite récupérer les notes
    * @return La liste des notes rattachées à ce document
    * @throws DocumentNoteServiceEx
    *            Une erreur s'est produite lors de la récupération des notes
    */
   List<StorageDocumentNote> getDocumentNotes(UUID docUuid);

}

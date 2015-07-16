package fr.urssaf.image.sae.services.document;

import java.util.List;
import java.util.UUID;

import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.SAEDocumentNoteException;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentNote;

/**
 * Interface du servic
 */
public interface SAENoteService {

   /**
    * Permet de rajouter une note à un document
    * 
    * @param docUuid
    *           UUID du document concerné
    * @param contenu
    *           Contenu de la note
    * @param login
    *           Login de l'utilisateur
    * @return Objet Note ajouté à un document
    * @throws SAEDocumentNoteException
    *            Une exception s’est produite lors de l’ajout d’une note au
    *            document
    * @throws ArchiveInexistanteEx
    *            La note ne peut pas être ajoutée car le document n'existe pas
    */
   void addDocumentNote(UUID docUuid, String contenu, String login)
         throws SAEDocumentNoteException, ArchiveInexistanteEx;

   /**
    * Méthode de récupération de la liste des notes d’un document
    * 
    * @param docUuid
    *           UUID du document concerné
    * @return Liste d’objets Note ajouté du document
    * @throws SAEDocumentNoteException
    *            Une exception s’est produite lors de la récupération des notes
    */
   List<StorageDocumentNote> getDocumentNotes(UUID docUuid)
         throws SAEDocumentNoteException;

}

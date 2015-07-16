package fr.urssaf.image.sae.storage.services.storagedocument;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.DocumentNoteServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentNote;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;

/**
 * 
 * Classe de gestion des opérations sur les documents de la GNS
 * 
 */
public interface StorageTransfertService {

   /**
    * Permet de faire une recherche de document par UUID.
    * 
    * @param uUIDCriteria
    *           : L'UUID du document à rechercher
    * 
    * @return un strorageDocument
    * 
    * @throws SearchingServiceEx
    *            Exception lévée lorsque la recherche ne se déroule pas bien.
    */
   StorageDocument searchStorageDocumentByUUIDCriteria(
         final UUIDCriteria uUIDCriteria) throws SearchingServiceEx;

   /**
    * Permet d'insérer un document unique avec piece jointe
    * 
    * @param storageDocument
    *           : Le document à stocker
    * 
    * @return Le document
    * 
    * @throws InsertionServiceEx
    *            Exception lévée lorsque l'insertion d'un document ne se déroule
    *            pas bien.
    */
   StorageDocument insertBinaryStorageDocument(
         final StorageDocument storageDocument) throws InsertionServiceEx;

   /**
    * Permet de supprimer un StorageDocument à partir du critère « UUIDCriteria
    * ».
    * 
    * @param uuid
    *           : L'identifiant unique du document
    * 
    * 
    * @throws DeletionServiceEx
    *            en cas d'erreur de suppression
    */
   void deleteStorageDocument(final UUID uuid) throws DeletionServiceEx;

   /**
    * Permet d'ajouter une note à un document
    * 
    * @param docUuid
    *           Identifiant du document auquel on souhaite ajouter une note
    * @param contenu
    *           Le contenu de la note
    * @param login
    *           Login utilisateur
    * @param dateCreation
    *           La date de création de la note
    * @param noteUuid
    *           L'UUID de la note
    * @throws DocumentNoteServiceEx
    *            Une erreur s'est produite lors de l'ajout d'une note au
    *            document
    */
   void addDocumentNote(final UUID docUuid, String contenu, String login,
         Date dateCreation, UUID noteUuid) throws DocumentNoteServiceEx;

   /**
    * Récupération de la liste des notes d'un document
    * 
    * @param docUuid
    *           Identifiant du document pour lequel on souhaite récupérer la
    *           liste des notes
    * @return La liste des notes rattachées au document
    */
   List<StorageDocumentNote> getDocumentNotes(UUID docUuid);

   /**
    * 
    * Ouverture de la connection DFCE de transfert
    * 
    * 
    * @throws ConnectionServiceEx
    *            en cas d'erreur de connection
    * 
    */
   void openConnexion() throws ConnectionServiceEx;

   /**
    * 
    * Fermeture de la connection DFCE de transfert
    * 
    * 
    */
   void closeConnexion();
}

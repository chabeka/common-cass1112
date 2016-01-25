package fr.urssaf.image.sae.storage.services.storagedocument;

import java.util.UUID;

import javax.activation.DataHandler;

import fr.urssaf.image.sae.storage.exception.StorageDocAttachmentServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentAttachment;

/**
 * Interface pour accès aux services de gestion des documents attachés
 */
public interface DocumentAttachmentService {

   /**
    * 
    * @param <T>
    *           : Le type générique.
    * @param parameter
    *           : Le paramètre du service {@link DeletionService}
    */
   <T> void setDocumentAttachmentServiceParameter(T parameter);

   /**
    * Permet de joindre un « document attaché » à un document
    * 
    * @param docUuid
    *           UUID du document parent
    * @param docName
    *           le nom de la pièce jointe
    * @param extenxion
    *           l’extension de la pièce jointe
    * @param contenu
    *           Contenu de la pièce jointe
    * @throws StorageDocAttachmentServiceEx
    *            Une erreur s’est produite lors de l’ajout d’un document attaché
    *            au document
    */
   void addDocumentAttachment(UUID docUuid, String docName, String extenxion,
         DataHandler contenu) throws StorageDocAttachmentServiceEx;

   /**
    * Méthode de récupération des documents attachés
    * 
    * @param docUuid
    *           UUID du document concerné
    * @throws StorageDocAttachmentServiceEx
    *            Une erreur s’est produite lors de la récupération du document
    *            attaché
    * @return Le document attaché
    */
   StorageDocumentAttachment getDocumentAttachments(UUID docUuid)
         throws StorageDocAttachmentServiceEx;

}

package fr.urssaf.image.sae.services.document;

import java.net.URI;
import java.util.UUID;

import javax.activation.DataHandler;

import org.springframework.security.access.prepost.PreAuthorize;

import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.SAEDocumentAttachmentEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureBadEcdeUrlEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyFileNameEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentAttachment;

/**
 * Interface du service de gestion des pièces attachées d’un document
 */
public interface SAEDocumentAttachmentService {

   /**
    * Permet de rajouter un document attaché (Binaire) à un document
    * 
    * @param docUuid
    *           UUID du document concerné
    * @param docName
    *           le nom de la pièce jointe
    * @param extension
    *           l’extension de la pièce jointe
    * @param contenu
    *           Contenu du doc à attacher
    * @throws SAEDocumentAttachmentEx
    *            Une exception s’est produite lors de l’ajout d’une pièce au
    *            document
    * @throws ArchiveInexistanteEx
    *            L'archive sur laquelle on souhaite ajouter un document attaché
    *            n'existe pas
    * @throws EmptyDocumentEx
    *            Le contenu du document attaché est vide
    * @throws EmptyFileNameEx
    *            Le nom du fichier est vide
    */
   @PreAuthorize("hasRole('ajout_doc_attache')")
   void addDocumentAttachmentBinaire(UUID docUuid, String docName,
         String extension, DataHandler contenu) throws SAEDocumentAttachmentEx,
         ArchiveInexistanteEx, EmptyDocumentEx, EmptyFileNameEx;

   /**
    * Permet de rajouter un document attaché (Binaire) à un document. Si il y a
    * une erreur lors de l'ajout du document, le document parent sera supprimé
    * (il s'agit du cas de l'ajout de document au moment du stockage unitaire du
    * document parent)
    * 
    * @param docUuid
    *           UUID du document concerné
    * @param docName
    *           le nom de la pièce jointe
    * @param extension
    *           l’extension de la pièce jointe
    * @param contenu
    *           Contenu du doc à attacher
    * @throws SAEDocumentAttachmentEx
    *            Une exception s’est produite lors de l’ajout d’une pièce au
    *            document
    * @throws ArchiveInexistanteEx
    *            L'archive sur laquelle on souhaite ajouter un document attaché
    *            n'existe pas
    * @throws EmptyDocumentEx
    *            Le contenu du document attaché est vide
    * @throws EmptyFileNameEx
    *            Le nom du fichier est vide
    */
   @PreAuthorize("hasRole('ajout_doc_attache')")
   void addDocumentAttachmentBinaireRollbackParent(UUID docUuid, String docName,
         String extension, DataHandler contenu) throws SAEDocumentAttachmentEx,
         ArchiveInexistanteEx, EmptyDocumentEx, EmptyFileNameEx;

   /**
    * Permet de rajouter un document attaché (par URL) à un document
    * 
    * @param docUuid
    *           UUID du document concerné
    * @param ecdeURL
    *           URL du document attaché
    * @throws SAEDocumentAttachmentEx
    *            Une exception s’est produite lors de l’ajout d’une pièce au
    *            document
    * @throws ArchiveInexistanteEx
    *            L'archive sur laquelle on souhaite ajouter un document attaché
    *            n'existe pas
    * @throws EmptyDocumentEx
    *            Le contenu du document attaché est vide
    * @throws EmptyFileNameEx
    *            Le nom du fichier est vide
    * @throws CaptureBadEcdeUrlEx
    *            L'URL du document est incorrecte
    */
   @PreAuthorize("hasRole('ajout_doc_attache')")
   void addDocumentAttachmentUrl(UUID docUuid, URI ecdeURL)
         throws SAEDocumentAttachmentEx, ArchiveInexistanteEx, EmptyDocumentEx,
         EmptyFileNameEx, CaptureBadEcdeUrlEx;

   /**
    * Méthode de récupération du document attaché
    * 
    * @param docUuid
    *           UUID du document concerné
    * @return Le document attaché
    * @throws SAEDocumentAttachmentEx
    *            Une exception s’est produite lors de l’ajout d’une pièce au
    *            document
    * @throws ArchiveInexistanteEx
    */
   @PreAuthorize("hasRole('consultation')")
   StorageDocumentAttachment getDocumentAttachment(UUID docUuid)
         throws SAEDocumentAttachmentEx, ArchiveInexistanteEx;

   
   /**
    * Permet de rajouter un document attaché (par URL) à un document. Si il y a
    * une erreur lors de l'ajout du document, le document parent sera supprimé
    * (il s'agit du cas de l'ajout de document au moment du stockage unitaire du
    * document parent)
    * 
    * @param docUuid
    *           UUID du document concerné
    * @param ecdeURL
    *           URL du document attaché
    * @throws SAEDocumentAttachmentEx
    *            Une exception s’est produite lors de l’ajout d’une pièce au
    *            document
    * @throws ArchiveInexistanteEx
    *            L'archive sur laquelle on souhaite ajouter un document attaché
    *            n'existe pas
    * @throws EmptyDocumentEx
    *            Le contenu du document attaché est vide
    * @throws EmptyFileNameEx
    *            Le nom du fichier est vide
    * @throws CaptureBadEcdeUrlEx
    *            L'URL du document est incorrecte
    */
   @PreAuthorize("hasRole('ajout_doc_attache')")
   void addDocumentAttachmentUrlRollbackParent(UUID docUuid, URI ecdeURL)
         throws SAEDocumentAttachmentEx, ArchiveInexistanteEx, EmptyDocumentEx,
         EmptyFileNameEx, CaptureBadEcdeUrlEx;

}

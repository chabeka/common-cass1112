package fr.urssaf.image.sae.services.transfert;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;

import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.transfert.ArchiveAlreadyTransferedException;
import fr.urssaf.image.sae.services.exception.transfert.TransfertException;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;

/**
 * 
 * Service permettant de réaliser le transfert de documents
 * 
 */
public interface SAETransfertService {

   /**
    * Supprime le document donné
    * 
    * @param idArchive
    *           identifiant unique du document à transférer
    * @throws TransfertException
    *            Erreur levée lorsqu'un erreur survient pendant le transfert du
    *            doc
    * @throws ArchiveAlreadyTransferedException
    *            Erreur levée lorsque le document a déjà été transféré
    * @throws ArchiveInexistanteEx
    *            Erreur levée lorsque l'archive n'a pas été trouvée
    */
   @PreAuthorize("hasRole('transfert')")
   void transfertDoc(UUID idArchive) throws TransfertException,
         ArchiveAlreadyTransferedException, ArchiveInexistanteEx;

   void transfertDocMasse(StorageDocument document) throws TransfertException,
         ArchiveAlreadyTransferedException, ArchiveInexistanteEx,
         ReferentialException, RetrievalServiceEx, InvalidSAETypeException,
         MappingFromReferentialException;

   void controleDroitTransfert(final UUID idArchive)
         throws ReferentialException, RetrievalServiceEx,
         InvalidSAETypeException, MappingFromReferentialException;

   StorageDocument transfertControlePlateforme(StorageDocument document,
         final UUID idArchive) throws ArchiveAlreadyTransferedException,
         SearchingServiceEx, ReferentialException, ArchiveInexistanteEx,
         TransfertException, ConnectionServiceEx;

   void transfertDocument(StorageDocument document) throws TransfertException;

   void deleteDocApresTransfert(final UUID idArchive)
         throws SearchingServiceEx, ReferentialException, TransfertException;

   StorageDocument recupererDocMetaTransferable(final UUID idArchive)
         throws ReferentialException, SearchingServiceEx;

   StorageDocument updateMetaDocumentForTransfertMasse(
         StorageDocument document, final List<StorageMetadata> listeMeta)
         throws ReferentialException, TransfertException;

   StorageDocument controleDocumentTransfertMasse(final UUID idArchive,
         final List<StorageMetadata> storageMetas) throws TransfertException,
         ArchiveAlreadyTransferedException, ArchiveInexistanteEx;

}

package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.activation.DataHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.storage.dfce.model.AbstractServices;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.DocumentNoteServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionIdGedExistantEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.exception.StorageDocAttachmentServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentAttachment;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentNote;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageTransfertService;
import fr.urssaf.image.sae.storage.services.storagedocument.TransfertService;

/**
 * {@inheritDoc}
 */
public class StorageTransfertServiceImpl extends AbstractServices
implements StorageTransfertService {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(StorageTransfertServiceImpl.class);

   /**
    * Service de transfert
    */
   private final TransfertService transfertService;

   /**
    * Constructeur
    *
    * @param transfertService
    *           service de transfert
    */
   public StorageTransfertServiceImpl(final TransfertService transfertService) {
      this.dfceServices = null;     // le dfceServices pointe vers la GNT, mais n'est pas utilisé
      this.transfertService = transfertService;
   }

   /**
    * {@inheritDoc}
    * @throws InsertionIdGedExistantEx
    */
   @Override
   public StorageDocument insertBinaryStorageDocument(
                                                      final StorageDocument storageDocument) throws InsertionServiceEx, InsertionIdGedExistantEx {
      return transfertService.insertBinaryStorageDocument(storageDocument);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public StorageDocument searchStorageDocumentByUUIDCriteria(
                                                              final UUIDCriteria uUIDCriteria) throws SearchingServiceEx {
      return transfertService.searchStorageDocumentByUUIDCriteria(uUIDCriteria);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void deleteStorageDocument(final UUID uuid) throws DeletionServiceEx {
      transfertService.deleteStorageDocument(uuid);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void addDocumentNote(final UUID docUuid, final String contenu, final String login,
                               final Date dateCreation, final UUID noteUuid) throws DocumentNoteServiceEx {
      transfertService.addDocumentNote(docUuid, contenu, login, dateCreation,
                                       noteUuid);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<StorageDocumentNote> getDocumentNotes(final UUID docUuid) {
      return transfertService.getDocumentNotes(docUuid);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void openConnexion() throws ConnectionServiceEx {
      dfceServices.connectTheFistTime();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void closeConnexion() {
      dfceServices.closeConnexion();
   }

   @Override
   public void addDocumentAttachment(final UUID docUuid, final String docName,
                                     final String extension, final DataHandler contenu)
                                           throws StorageDocAttachmentServiceEx {
      transfertService.addDocumentAttachment(docUuid, docName, extension,
                                             contenu);
   }

   @Override
   public StorageDocumentAttachment getDocumentAttachment(final UUID docUuid)
         throws StorageDocAttachmentServiceEx {
      return transfertService.getDocumentAttachment(docUuid);
   }

}

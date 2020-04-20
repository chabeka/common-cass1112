/**
 *
 */
package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.crud;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.activation.DataHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.sae.storage.dfce.bo.DocumentsTypeList;
import fr.urssaf.image.sae.storage.dfce.support.StorageDocumentServiceSupport;
import fr.urssaf.image.sae.storage.dfce.support.TracesDfceSupport;
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
import fr.urssaf.image.sae.storage.services.storagedocument.TransfertService;

/**
 * Classe d'implémentation de l'interface {@link TransfertService}. Cette classe
 * est un singleton et peut être accessible via le mécanisme d'injection IOC et
 * l'annotation @Autowired
 *
 */
@Component
public class TransfertServiceImpl implements
TransfertService {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(TransfertServiceImpl.class);

   /**
    * On injecte DocumentsTypeList manuellement(xml) paramétré avec la
    * connexion DFCE de transfert pour éviter les conflits avec celle existante.
    */
   private DocumentsTypeList typeList;

   private TracesDfceSupport tracesSupport;

   /**
    * Il s'agit des services DFCE du DFCE DISTANT (cible du transfert, GNS)
    */
   protected DFCEServices dfceServices;

   @Autowired
   protected StorageDocumentServiceSupport storageDocumentServiceSupport;


   /**
    * Constructeur
    */
   public TransfertServiceImpl(final DocumentsTypeList typeList,
                               final TracesDfceSupport tracesSupport, final DFCEServices dfceServices) {
      this.typeList = typeList;
      this.tracesSupport = tracesSupport;
      this.dfceServices = dfceServices;
   }

   /**
    * Constructeur
    */
   public TransfertServiceImpl() {
      super();
   }

   public DFCEServices getDfceServices() {
      return dfceServices;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public StorageDocument searchStorageDocumentByUUIDCriteria(
                                                              final UUIDCriteria uUIDCriteria) throws SearchingServiceEx {
      return storageDocumentServiceSupport
            .searchStorageDocumentByUUIDCriteriaWithoutDocContent(
                                                                  dfceServices, uUIDCriteria, false, LOGGER);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public StorageDocument insertBinaryStorageDocument(
                                                      final StorageDocument storageDocument) throws InsertionServiceEx,
   InsertionIdGedExistantEx {
      return storageDocumentServiceSupport.insertBinaryStorageDocument(dfceServices, typeList, storageDocument, LOGGER,
                                                                       tracesSupport);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void deleteStorageDocument(final UUID uuid) throws DeletionServiceEx {
      // -- Suppression du document
      storageDocumentServiceSupport.deleteStorageDocument(dfceServices,
                                                          uuid, LOGGER, tracesSupport);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void addDocumentNote(final UUID docUuid, final String contenu, final String login,
                               final Date dateCreation, final UUID noteUuid) throws DocumentNoteServiceEx {
      storageDocumentServiceSupport.addDocumentNote(dfceServices, docUuid,
                                                    contenu, login, dateCreation, noteUuid, LOGGER);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<StorageDocumentNote> getDocumentNotes(final UUID docUuid) {
      return storageDocumentServiceSupport.getDocumentNotes(dfceServices,
                                                            docUuid, LOGGER);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void addDocumentAttachment(final UUID docUuid, final String docName,
                                     final String extension, final DataHandler contenu)
                                           throws StorageDocAttachmentServiceEx {
      storageDocumentServiceSupport.addDocumentAttachment(dfceServices,
                                                          docUuid, docName, extension, contenu, LOGGER,
                                                          tracesSupport);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public StorageDocumentAttachment getDocumentAttachment(final UUID docUuid)
         throws StorageDocAttachmentServiceEx {
      return storageDocumentServiceSupport.getDocumentAttachment(dfceServices, docUuid, LOGGER);
   }

}

package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.storage.dfce.annotations.Loggable;
import fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked;
import fr.urssaf.image.sae.storage.dfce.messages.LogLevel;
import fr.urssaf.image.sae.storage.dfce.model.AbstractServices;
import fr.urssaf.image.sae.storage.dfce.support.StorageDocumentServiceSupport;
import fr.urssaf.image.sae.storage.dfce.support.TracesDfceSupport;
import fr.urssaf.image.sae.storage.exception.RecycleBinServiceEx;
import fr.urssaf.image.sae.storage.exception.StorageException;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.RecycleBinService;
import net.docubase.toolkit.model.document.Document;

/**
 * Implémente les services de l'interface {@link RecycleBinService}.
 */
@Service
@Qualifier("recycleBinService")
public class RecycleBinServiceImpl extends AbstractServices implements
RecycleBinService {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(RecycleBinServiceImpl.class);

   @Autowired
   private TracesDfceSupport tracesSupport;

   /**
    * {@inheritDoc}
    */
   @Override
   @Loggable(LogLevel.TRACE)
   @ServiceChecked
   public void moveStorageDocumentToRecycleBin(final UUID uuid)
         throws RecycleBinServiceEx {
      // -- Mise a la corbeille du document
      storageDocumentServiceSupport.moveStorageDocumentToRecycleBin(
                                                                    getDfceServices(), uuid, LOGGER, tracesSupport);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @Loggable(LogLevel.TRACE)
   @ServiceChecked
   public void restoreStorageDocumentFromRecycleBin(final UUID uuid)
         throws RecycleBinServiceEx {
      // -- Restore de la corbeille du document
      storageDocumentServiceSupport.restoreStorageDocumentFromRecycleBin(
                                                                         getDfceServices(), uuid, LOGGER, tracesSupport);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @Loggable(LogLevel.TRACE)
   @ServiceChecked
   public void deleteStorageDocumentFromRecycleBin(final UUID uuid)
         throws RecycleBinServiceEx {
      // -- Suppression de la corbeille du document
      storageDocumentServiceSupport.deleteStorageDocumentFromRecycleBin(
                                                                        getDfceServices(), uuid, LOGGER, tracesSupport);
   }

   @Override
   @Loggable(LogLevel.TRACE)
   public StorageDocument getStorageDocumentFromRecycleBin(
                                                           final UUIDCriteria uuidCriteria) throws StorageException, IOException {

      // Rechercher le document dans la corbeille
      final Document doc = storageDocumentServiceSupport.getDocumentFromRecycleBin(
                                                                                   getDfceServices(), uuidCriteria.getUuid(),
                                                                                   LOGGER, tracesSupport);

      if(doc != null){
         return storageDocumentServiceSupport.getStorageDocument(doc,
                                                                 uuidCriteria.getDesiredStorageMetadatas(), getDfceServices(), false);
      }else {
         return null;
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public StorageDocument getStorageDocumentFromRecycleBin(
                                                           final UUIDCriteria uuidCriteria, final boolean forConsultion)
                                                                 throws StorageException, IOException {

      // Rechercher le document dans la corbeille
      final Document doc = storageDocumentServiceSupport.getDocumentFromRecycleBin(
                                                                                   getDfceServices(), uuidCriteria.getUuid(),
                                                                                   LOGGER, tracesSupport);

      return storageDocumentServiceSupport.getStorageDocument(doc,
                                                              uuidCriteria.getDesiredStorageMetadatas(), getDfceServices(),
                                                              forConsultion);
   }

   /**
    * Setter pour storageDocumentServiceSupport
    *
    * @param storageDocumentServiceSupport
    *           the storageDocumentServiceSupport to set
    */
   public void setStorageDocumentServiceSupport(
                                                final StorageDocumentServiceSupport storageDocumentServiceSupport) {
      this.storageDocumentServiceSupport = storageDocumentServiceSupport;
   }

   /**
    * Setter pour tracesSupport
    *
    * @param tracesSupport
    *           the tracesSupport to set
    */
   public void setTracesSupport(final TracesDfceSupport tracesSupport) {
      this.tracesSupport = tracesSupport;
   }


}

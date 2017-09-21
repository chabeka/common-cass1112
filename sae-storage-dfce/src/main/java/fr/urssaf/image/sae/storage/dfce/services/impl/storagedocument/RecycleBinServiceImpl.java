package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.io.IOException;
import java.util.UUID;

import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.service.ServiceProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.storage.dfce.annotations.Loggable;
import fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked;
import fr.urssaf.image.sae.storage.dfce.mapping.BeanMapper;
import fr.urssaf.image.sae.storage.dfce.messages.LogLevel;
import fr.urssaf.image.sae.storage.dfce.model.AbstractServices;
import fr.urssaf.image.sae.storage.dfce.support.StorageDocumentServiceSupport;
import fr.urssaf.image.sae.storage.dfce.support.TracesDfceSupport;
import fr.urssaf.image.sae.storage.exception.RecycleBinServiceEx;
import fr.urssaf.image.sae.storage.exception.StorageException;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.RecycleBinService;

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

   @Autowired
   private StorageDocumentServiceSupport storageDocumentServiceSupport;

   /**
    * {@inheritDoc}
    */
   @Override
   @Loggable(LogLevel.TRACE)
   @ServiceChecked
   public void moveStorageDocumentToRecycleBin(UUID uuid)
         throws RecycleBinServiceEx {
      // -- Mise a la corbeille du ducument
      storageDocumentServiceSupport.moveStorageDocumentToRecycleBin(
            getDfceService(), getCnxParameters(), uuid, LOGGER, tracesSupport);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @Loggable(LogLevel.TRACE)
   @ServiceChecked
   public void restoreStorageDocumentFromRecycleBin(UUID uuid)
         throws RecycleBinServiceEx {
      // -- Restore de la corbeille du ducument
      storageDocumentServiceSupport.restoreStorageDocumentFromRecycleBin(
            getDfceService(), getCnxParameters(), uuid, LOGGER, tracesSupport);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @Loggable(LogLevel.TRACE)
   @ServiceChecked
   public void deleteStorageDocumentFromRecycleBin(UUID uuid)
         throws RecycleBinServiceEx {
      // -- Suppression de la corbeille du ducument
      storageDocumentServiceSupport.deleteStorageDocumentFromRecycleBin(
            getDfceService(), getCnxParameters(), uuid, LOGGER, tracesSupport);
   }

   @Override
   @Loggable(LogLevel.TRACE)
   public StorageDocument getStorageDocumentFromRecycleBin(
         UUIDCriteria uuidCriteria) throws StorageException, IOException {

      // Rechercher le document dans la corbeille
      Document doc = storageDocumentServiceSupport.getDocumentFromRecycleBin(
            getDfceService(), getCnxParameters(), uuidCriteria.getUuid(),
            LOGGER, tracesSupport);

      StorageDocument storageDocument = BeanMapper
            .dfceDocumentFromRecycleBinToStorageDocument(doc,
            uuidCriteria.getDesiredStorageMetadatas(), getDfceService(), false);

      return storageDocument;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public StorageDocument getStorageDocumentFromRecycleBin(
         UUIDCriteria uuidCriteria, boolean forConsultion)
               throws StorageException, IOException {

      // Rechercher le document dans la corbeille
      Document doc = storageDocumentServiceSupport.getDocumentFromRecycleBin(
            getDfceService(), getCnxParameters(), uuidCriteria.getUuid(),
            LOGGER, tracesSupport);

      StorageDocument storageDocument = BeanMapper
            .dfceDocumentFromRecycleBinToStorageDocument(doc,
                  uuidCriteria.getDesiredStorageMetadatas(), getDfceService(),
                  forConsultion);

      return storageDocument;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public <T> void setRecycleBinServiceParameter(T parameter) {
      setDfceService((ServiceProvider) parameter);
   }

   /**
    * Setter pour storageDocumentServiceSupport
    * 
    * @param storageDocumentServiceSupport
    *           the storageDocumentServiceSupport to set
    */
   public void setStorageDocumentServiceSupport(
         StorageDocumentServiceSupport storageDocumentServiceSupport) {
      this.storageDocumentServiceSupport = storageDocumentServiceSupport;
   }

   /**
    * Setter pour tracesSupport
    * 
    * @param tracesSupport
    *           the tracesSupport to set
    */
   public void setTracesSupport(TracesDfceSupport tracesSupport) {
      this.tracesSupport = tracesSupport;
   }

}

package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.storage.dfce.annotations.Loggable;
import fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked;
import fr.urssaf.image.sae.storage.dfce.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.messages.LogLevel;
import fr.urssaf.image.sae.storage.dfce.messages.StorageMessageHandler;
import fr.urssaf.image.sae.storage.dfce.model.AbstractServices;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.RetrievalService;
import fr.urssaf.image.sae.storage.services.storagedocument.SearchingService;

/**
 * Implémente les services de l'interface {@link RetrievalService}.
 */
@Service
@Qualifier("retrievalService")
public class RetrievalServiceImpl extends AbstractServices implements
RetrievalService {
   private static final Logger LOG = LoggerFactory
         .getLogger(RetrievalServiceImpl.class);
   @Autowired
   @Qualifier("searchingService")
   private SearchingService searchingService;

   /**
    * {@inheritDoc}
    */
   @Override
   @Loggable(LogLevel.TRACE)
   @ServiceChecked
   public final StorageDocument retrieveStorageDocumentByUUID(
                                                              final UUIDCriteria uUIDCriteria) throws RetrievalServiceEx {
      try {
         // Traces debug - entrée méthode
         final String prefixeTrc = "retrieveStorageDocumentByUUID()";
         LOG.debug("{} - Début", prefixeTrc);
         LOG.debug("{} - UUIDCriteria du document à consulter: {}", prefixeTrc,
                   uUIDCriteria.toString());
         // Fin des traces debug - entrée méthode
         final StorageDocument storageDoc = searchingService
               .searchStorageDocumentByUUIDCriteria(uUIDCriteria, true);
         if (storageDoc == null) {
            LOG.debug("{} - Le document n'a pas été trouvé dans le stockage",
                      prefixeTrc);

         } else {
            LOG.debug("{} - Le document a été trouvé dans le stockage",
                      prefixeTrc);
         }

         LOG.debug("{} - Sortie", prefixeTrc);
         return storageDoc;

      } catch (final SearchingServiceEx srcSerEx) {
         throw new RetrievalServiceEx(StorageMessageHandler
                                      .getMessage(Constants.RTR_CODE_ERROR), srcSerEx.getMessage(),
                                      srcSerEx);
      } catch (final Exception exc) {
         throw new RetrievalServiceEx(StorageMessageHandler
                                      .getMessage(Constants.RTR_CODE_ERROR), exc.getMessage(), exc);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @Loggable(LogLevel.TRACE)
   @ServiceChecked
   public final byte[] retrieveStorageDocumentContentByUUID(
                                                            final UUIDCriteria uUIDCriteria) throws RetrievalServiceEx {
      try {
         return searchingService
               .searchStorageDocumentContentByUUIDCriteria(uUIDCriteria);
      } catch (final IOException except) {
         throw new RetrievalServiceEx(StorageMessageHandler
                                      .getMessage(Constants.RTR_CODE_ERROR), except.getMessage(),
                                      except);
      } catch (final Exception except) {
         throw new RetrievalServiceEx(StorageMessageHandler
                                      .getMessage(Constants.SRH_CODE_ERROR), except.getMessage(),
                                      except);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @Loggable(LogLevel.TRACE)
   @ServiceChecked
   public final List<StorageMetadata> retrieveStorageDocumentMetaDatasByUUID(
                                                                             final UUIDCriteria uUIDCriteria) throws RetrievalServiceEx {
      try {
         return searchingService.searchMetaDatasByUUIDCriteria(uUIDCriteria)
               .getMetadatas();
      } catch (final SearchingServiceEx srcSerEx) {
         throw new RetrievalServiceEx(srcSerEx.getMessage(), srcSerEx);
      } catch (final Exception except) {
         throw new RetrievalServiceEx(StorageMessageHandler
                                      .getMessage(Constants.RTR_CODE_ERROR), except.getMessage(),
                                      except);
      }
   }

   /**
    * @param searchingService
    *           the searchingService to set
    */
   public final void setSearchingService(final SearchingService searchingService) {
      this.searchingService = searchingService;
   }

   /**
    * @return the searchingService
    */
   public final SearchingService getSearchingService() {
      return searchingService;
   }

}

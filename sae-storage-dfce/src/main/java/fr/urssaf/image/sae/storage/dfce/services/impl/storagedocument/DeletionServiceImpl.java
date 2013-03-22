package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.util.UUID;

import net.docubase.toolkit.service.ServiceProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.docubase.dfce.exception.FrozenDocumentException;

import fr.urssaf.image.sae.storage.dfce.annotations.Loggable;
import fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked;
import fr.urssaf.image.sae.storage.dfce.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.messages.LogLevel;
import fr.urssaf.image.sae.storage.dfce.messages.StorageMessageHandler;
import fr.urssaf.image.sae.storage.dfce.model.AbstractServices;
import fr.urssaf.image.sae.storage.dfce.support.TracesDfceSupport;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.QueryParseServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.LuceneCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.DeletionService;
import fr.urssaf.image.sae.storage.services.storagedocument.SearchingService;

/**
 * Implémente les services de l'interface {@link DeletionService}.
 * 
 * @author Akenore, Rhofir
 * 
 */
@Service
@Qualifier("deletionService")
public class DeletionServiceImpl extends AbstractServices implements
      DeletionService {
   private static final Logger LOGGER = LoggerFactory
         .getLogger(DeletionServiceImpl.class);
   @Autowired
   @Qualifier("searchingService")
   private SearchingService searchingService;

   @Autowired
   private TracesDfceSupport tracesSupport;

   /**
    * @param searchingService
    *           : Le service de recherche.
    */
   public final void setSearchingService(final SearchingService searchingService) {
      this.searchingService = searchingService;
   }

   /**
    * @return Le service de recherche.
    */
   public final SearchingService getSearchingService() {
      return searchingService;
   }

   /**
    * {@inheritDoc}
    */
   @Loggable(LogLevel.TRACE)
   @ServiceChecked
   public final void deleteStorageDocument(final UUID uuid)
         throws DeletionServiceEx {
      // Traces debug - entrée méthode
      String prefixeTrc = "deleteStorageDocument()";
      LOGGER.debug("{} - Début", prefixeTrc);
      // Fin des traces debug - entrée méthode
      try {
         LOGGER.debug("{} - UUID à supprimer : {}", prefixeTrc, uuid);
         getDfceService().getStoreService().deleteDocument(uuid);

         // Trace l'événement "Suppression d'un document de DFCE"
         tracesSupport.traceSuppressionDocumentDeDFCE(uuid);

         LOGGER.debug("{} - Sortie", prefixeTrc);

      } catch (FrozenDocumentException frozenExcept) {
         LOGGER
               .debug(
                     "{} - Une exception a été levée lors de la suppression du document : {}",
                     prefixeTrc, frozenExcept.getMessage());
         throw new DeletionServiceEx(StorageMessageHandler
               .getMessage(Constants.DEL_CODE_ERROR),
               frozenExcept.getMessage(), frozenExcept);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Loggable(LogLevel.TRACE)
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   @ServiceChecked
   public final void rollBack(final String processId) throws DeletionServiceEx {
      final String lucene = String.format("%s:%s", "iti", processId);
      StorageDocuments storageDocuments;
      try {
         storageDocuments = searchingService
               .searchStorageDocumentByLuceneCriteria(new LuceneCriteria(
                     lucene, Integer.parseInt(StorageMessageHandler
                           .getMessage("max.lucene.results")), null));
         for (StorageDocument storageDocument : storageDocuments
               .getAllStorageDocuments()) {
            deleteStorageDocument(storageDocument.getUuid());
         }
      } catch (NumberFormatException numberExcept) {
         throw new DeletionServiceEx(StorageMessageHandler
               .getMessage(Constants.DEL_CODE_ERROR),
               numberExcept.getMessage(), numberExcept);
      } catch (SearchingServiceEx searchingExcept) {
         new DeletionServiceEx(StorageMessageHandler
               .getMessage(Constants.DEL_CODE_ERROR), searchingExcept
               .getMessage(), searchingExcept);
      } catch (QueryParseServiceEx searchingExcept) {
         throw new DeletionServiceEx(StorageMessageHandler
               .getMessage(Constants.DEL_CODE_ERROR), searchingExcept
               .getMessage(), searchingExcept);
      }
   }

   /**
    * {@inheritDoc}
    */
   public final <T> void setDeletionServiceParameter(final T parameter) {
      setDfceService((ServiceProvider) parameter);
   }
}

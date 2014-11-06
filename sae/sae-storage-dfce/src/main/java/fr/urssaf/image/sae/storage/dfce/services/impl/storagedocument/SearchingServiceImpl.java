package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchResult;
import net.docubase.toolkit.model.search.SortedSearchQuery;
import net.docubase.toolkit.model.search.impl.SortedQueryImpl;
import net.docubase.toolkit.service.ServiceProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.docubase.dfce.exception.ExceededSearchLimitException;
import com.docubase.dfce.exception.SearchQueryParseException;

import fr.urssaf.image.sae.storage.dfce.annotations.Loggable;
import fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked;
import fr.urssaf.image.sae.storage.dfce.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.mapping.BeanMapper;
import fr.urssaf.image.sae.storage.dfce.messages.LogLevel;
import fr.urssaf.image.sae.storage.dfce.messages.StorageMessageHandler;
import fr.urssaf.image.sae.storage.dfce.model.AbstractServices;
import fr.urssaf.image.sae.storage.dfce.support.StorageDocumentServiceSupport;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;
import fr.urssaf.image.sae.storage.exception.QueryParseServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.exception.StorageException;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.LuceneCriteria;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.SearchingService;

/**
 * Implémente les services de l'interface {@link SearchingService} .
 * 
 * 
 */
@Service
@Qualifier("searchingService")
public class SearchingServiceImpl extends AbstractServices implements
      SearchingService {
   private static final Logger LOG = LoggerFactory
         .getLogger(SearchingServiceImpl.class);

   @Autowired
   private StorageDocumentServiceSupport storageServiceSupport;
   
   /**
    * {@inheritDoc}
    */
   @Loggable(LogLevel.TRACE)
   @ServiceChecked
   public final StorageDocuments searchStorageDocumentByLuceneCriteria(
         final LuceneCriteria luceneCriteria) throws SearchingServiceEx,
         QueryParseServiceEx {
      String prefixTrace = "searchStorageDocumentByLuceneCriteria()";
      final List<StorageDocument> storageDocuments = new ArrayList<StorageDocument>();
      try {

         LOG.debug("{} - Requête Lucene envoyée à DFCE: \"{}\"", prefixTrace,
               luceneCriteria.getLuceneQuery());

         SortedSearchQuery paramSearchQuery = new SortedQueryImpl(
               luceneCriteria.getLuceneQuery(), luceneCriteria.getLimit(), 0,
               getBaseDFCE());
         SearchResult searchResult = getDfceService().getSearchService()
               .search(paramSearchQuery);

         for (Document document : Utils.nullSafeIterable(searchResult
               .getDocuments())) {

            storageDocuments.add(BeanMapper.dfceDocumentToStorageDocument(
                  document, luceneCriteria.getDesiredStorageMetadatas(),
                  getDfceService(), false));
         }
      } catch (SearchQueryParseException except) {
         throw new QueryParseServiceEx(StorageMessageHandler
               .getMessage(Constants.SRH_CODE_ERROR), except.getMessage(),
               except);
      } catch (StorageException srcSerEx) {
         throw new SearchingServiceEx(StorageMessageHandler
               .getMessage(Constants.SRH_CODE_ERROR), srcSerEx.getMessage(),
               srcSerEx);
      } catch (IOException ioExcept) {
         throw new SearchingServiceEx(StorageMessageHandler
               .getMessage(Constants.SRH_CODE_ERROR), ioExcept.getMessage(),
               ioExcept);
      } catch (ExceededSearchLimitException exceedSearchEx) {
         throw new SearchingServiceEx(StorageMessageHandler
               .getMessage(Constants.SRH_CODE_ERROR), exceedSearchEx
               .getMessage(), exceedSearchEx);
      } catch (Exception except) {
         throw new SearchingServiceEx(StorageMessageHandler
               .getMessage(Constants.SRH_CODE_ERROR), except.getMessage(),
               except);
      }
      return new StorageDocuments(storageDocuments);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @Loggable(LogLevel.TRACE)
   @ServiceChecked
   public StorageDocument searchStorageDocumentByUUIDCriteria(UUIDCriteria uUIDCriteria) 
      throws SearchingServiceEx {
      
      //-- Recherche du document
      return storageServiceSupport.searchStorageDocumentByUUIDCriteria(getDfceService(), 
            getCnxParameters(), uUIDCriteria, LOG);
   }


   /**
    * {@inheritDoc}
    */
   @ServiceChecked
   public final StorageDocument searchMetaDatasByUUIDCriteria(
         final UUIDCriteria uuidCriteria) throws SearchingServiceEx {
      try {
         final Document docDfce = getDfceService().getSearchService()
               .getDocumentByUUID(getBaseDFCE(), uuidCriteria.getUuid());
         return BeanMapper.dfceMetaDataToStorageDocument(docDfce, uuidCriteria
               .getDesiredStorageMetadatas(), getDfceService());
      } catch (StorageException srcSerEx) {
         throw new SearchingServiceEx(StorageMessageHandler
               .getMessage(Constants.SRH_CODE_ERROR), srcSerEx.getMessage(),
               srcSerEx);
      } catch (IOException ioExcept) {
         throw new SearchingServiceEx(StorageMessageHandler
               .getMessage(Constants.SRH_CODE_ERROR), ioExcept.getMessage(),
               ioExcept);
      } catch (Exception except) {
         throw new SearchingServiceEx(StorageMessageHandler
               .getMessage(Constants.SRH_CODE_ERROR), except.getMessage(),
               except);
      }
   }

   /**
    * {@inheritDoc}
    */
   public final <T> void setSearchingServiceParameter(final T parameter) {
      setDfceService((ServiceProvider) parameter);
   }

}

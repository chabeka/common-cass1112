package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.ChainedFilter;
import net.docubase.toolkit.model.search.SearchQuery;
import net.docubase.toolkit.model.search.SearchResult;
import net.docubase.toolkit.model.search.SortedSearchQuery;
import net.docubase.toolkit.model.search.ChainedFilter.ChainedFilterOperator;
import net.docubase.toolkit.service.ServiceProvider;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.docubase.dfce.exception.ExceededSearchLimitException;
import com.docubase.dfce.exception.SearchQueryParseException;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.droit.service.PrmdService;
import fr.urssaf.image.sae.droit.utils.ResourceMessagesUtils;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.metadata.exceptions.LongCodeNotFoundException;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;
import fr.urssaf.image.sae.metadata.referential.services.SAEConvertMetadataService;
import fr.urssaf.image.sae.storage.dfce.annotations.Loggable;
import fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked;
import fr.urssaf.image.sae.storage.dfce.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.exception.MetadonneeInexistante;
import fr.urssaf.image.sae.storage.dfce.mapping.BeanMapper;
import fr.urssaf.image.sae.storage.dfce.messages.LogLevel;
import fr.urssaf.image.sae.storage.dfce.messages.StorageMessageHandler;
import fr.urssaf.image.sae.storage.dfce.model.AbstractServices;
import fr.urssaf.image.sae.storage.dfce.support.StorageDocumentServiceSupport;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;
import fr.urssaf.image.sae.storage.exception.QueryParseServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.exception.StorageException;
import fr.urssaf.image.sae.storage.model.storagedocument.PaginatedStorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.filters.AbstractFilter;
import fr.urssaf.image.sae.storage.model.storagedocument.filters.NotRangeFilter;
import fr.urssaf.image.sae.storage.model.storagedocument.filters.NotValueFilter;
import fr.urssaf.image.sae.storage.model.storagedocument.filters.RangeFilter;
import fr.urssaf.image.sae.storage.model.storagedocument.filters.ValueFilter;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.LuceneCriteria;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.PaginatedLuceneCriteria;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.SearchingService;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

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

   private static final String SEPARATOR_STRING = ", ";

   private static final int LIMITE = 1000;

   @Autowired
   private StorageDocumentServiceSupport storageServiceSupport;
   @Autowired
   private MetadataReferenceDAO referenceDAO;
   @Autowired
   private SAEConvertMetadataService convertService;

   @Autowired
   private PrmdService prmdService;

   @Autowired
   private MappingDocumentService mappingService;

   public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat(
         "yyyyMMddHHmmssSSS");
   public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
         "yyyyMMdd");

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

         SortedSearchQuery paramSearchQuery = ToolkitFactory.getInstance()
               .createMonobaseSortedQuery(luceneCriteria.getLuceneQuery(),
                     getBaseDFCE());
         paramSearchQuery.setPageSize(luceneCriteria.getLimit());
         paramSearchQuery.setOffset(0);
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
   public final StorageDocument searchStorageDocumentByUUIDCriteria(
         UUIDCriteria uUIDCriteria) throws SearchingServiceEx {

      // -- Recherche du document
      return storageServiceSupport.searchStorageDocumentByUUIDCriteria(
            getDfceService(), getCnxParameters(), uUIDCriteria, LOG);
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

   /**
    * {@inheritDoc}
    */
   @Override
   public final PaginatedStorageDocuments searchPaginatedStorageDocuments(
         PaginatedLuceneCriteria paginatedLuceneCriteria)
         throws SearchingServiceEx, QueryParseServiceEx {

      PaginatedStorageDocuments paginatedStorageDocuments = new PaginatedStorageDocuments();
      try {
         String prefixeTrc = "searchPaginatedStorageDocuments()";

         final List<StorageDocument> storageDocuments = new ArrayList<StorageDocument>();

         // Création de la SearchQuery
         SearchQuery searchQuery = ToolkitFactory.getInstance()
               .createMonobaseQuery(paginatedLuceneCriteria.getLuceneQuery(),
                     getBaseDFCE());
         // Récupération du référentiel des métadonnés pour les différentes
         // vérifications
         Map<String, MetadataReference> referentielMeta = referenceDAO
               .getAllMetadataReferences();

         // Création de la chainedFilter
         ChainedFilter chainedFilter = createChaineFilter(
               paginatedLuceneCriteria, referentielMeta);
         // On l'ajoute à la searchQuery
         searchQuery.setChainedFilter(chainedFilter);
         // On fixe le pas d'execution de l'itérateur
         searchQuery.setSearchLimit(LIMITE);

         // Recherche des documents par l'itérateur DFCE
         Iterator<Document> iterateur = getDfceService().getSearchService()
               .createDocumentIterator(searchQuery);

         Integer limite = paginatedLuceneCriteria.getLimit();
         Integer compteur = 0;

         // On récupère la liste complète des métadonnées du référentiel afin de
         // pouvoir tester les droits
         List<String> metadonneesRef = new ArrayList<String>(referentielMeta
               .keySet());
         List<StorageMetadata> allMeta = convertToStorageMeta(metadonneesRef);

         // Si appel au service de recherche avec passage de l'UUID du dernier
         // document, on boucle sur l'itérateur jusqu'à ce qu'on ait atteint le
         // dernier document
         if (paginatedLuceneCriteria.getLastIdDoc() != null) {
            String lastUuid = paginatedLuceneCriteria.getLastIdDoc().toString();
            while (iterateur.hasNext()) {
               Document doc = iterateur.next();
               if (lastUuid.equals(doc.getUuid().toString())) {
                  break;
               }
            }
         }

         // Ensuite, on continue l'itération pour récupérer les documents
         // souhaités
         while (iterateur.hasNext()) {
            Document doc = iterateur.next();
            StorageDocument storageDocument = BeanMapper
                  .dfceDocumentToStorageDocument(doc, allMeta,
                        getDfceService(), false);
            UntypedDocument untypedDocument = null;
            // Vérification des droits
            if (storageDocument != null) {
               untypedDocument = mappingService
                     .storageDocumentToUntypedDocument(storageDocument);
            }

            if (compteur < limite) {
               // On vérifie que le document courant est autorisé par le
               // périmètre de donnée
               LOG.debug("{} - Récupération des droits", prefixeTrc);
               AuthenticationToken token = (AuthenticationToken) AuthenticationContext
                     .getAuthenticationToken();
               List<SaePrmd> saePrmds = token.getSaeDroits().get(
                     "recherche_iterateur");
               LOG.debug("{} - Vérification des droits", prefixeTrc);
               boolean isPermitted = prmdService.isPermitted(untypedDocument
                     .getUMetadatas(), saePrmds);

               if (isPermitted) {
                  storageDocuments.add(BeanMapper
                        .dfceDocumentToStorageDocument(doc,
                              paginatedLuceneCriteria
                                    .getDesiredStorageMetadatas(),
                              getDfceService(), false));
                  compteur++;
               }
            } else {
               paginatedStorageDocuments
                     .setAllStorageDocuments(storageDocuments);
               paginatedStorageDocuments.setLastPage(false);
               break;
            }
         }

         // Si il ne reste plus de documents
         if (paginatedStorageDocuments.getLastPage() == null) {
            paginatedStorageDocuments.setAllStorageDocuments(storageDocuments);
            paginatedStorageDocuments.setLastPage(true);
         }

         String codeCourtVaryingMeta = paginatedLuceneCriteria
               .getCodeCourtVaryingMeta();

         // Si la recherche renvoie des documents, on récupère la valeur de la
         // métadonnée variable afin de pouvoir la transmettre au client dans
         // l'identifiant de la dernière page
         if (storageDocuments.size() > 0) {
            String valeurMetaLastPage = recupererValeurMetaLastPage(
                  storageDocuments, codeCourtVaryingMeta);
            paginatedStorageDocuments.setValeurMetaLastPage(valeurMetaLastPage);
         }

      } catch (ReferentialException e) {
         throw new SearchingServiceEx(StorageMessageHandler
               .getMessage(Constants.SRH_CODE_ERROR), e.getMessage(), e);
      } catch (MetadonneeInexistante e) {
         throw new SearchingServiceEx(StorageMessageHandler
               .getMessage(Constants.SRH_CODE_ERROR), e.getMessage(), e);
      } catch (StorageException srcSerEx) {
         throw new SearchingServiceEx(StorageMessageHandler
               .getMessage(Constants.SRH_CODE_ERROR), srcSerEx.getMessage(),
               srcSerEx);
      } catch (IOException ioExcept) {
         throw new SearchingServiceEx(StorageMessageHandler
               .getMessage(Constants.SRH_CODE_ERROR), ioExcept.getMessage(),
               ioExcept);
      } catch (InvalidSAETypeException e) {
         throw new SearchingServiceEx(StorageMessageHandler
               .getMessage(Constants.SRH_CODE_ERROR), e.getMessage(), e);
      } catch (MappingFromReferentialException e) {
         throw new SearchingServiceEx(StorageMessageHandler
               .getMessage(Constants.SRH_CODE_ERROR), e.getMessage(), e);
      } catch (SearchQueryParseException e) {
         throw new QueryParseServiceEx(StorageMessageHandler
               .getMessage(Constants.SRH_CODE_ERROR), e.getMessage(), e);
      }
      return paginatedStorageDocuments;
   }

   private String recupererValeurMetaLastPage(
         final List<StorageDocument> storageDocuments,
         String codeCourtVaryingMeta) {
      StorageDocument storageDocument = storageDocuments.get(storageDocuments
            .size() - 1);
      List<StorageMetadata> listeSMeta = storageDocument.getMetadatas();
      String valeurMetaLastPage = "";
      for (StorageMetadata storageMetadata : listeSMeta) {
         if (storageMetadata.getShortCode().equals(codeCourtVaryingMeta)) {
            if (storageMetadata.getValue() instanceof Date) {
               if (codeCourtVaryingMeta.equals("SM_ARCHIVAGE_DATE")) {
                  // Convert Local Time to UTC (Works Fine)
                  DATE_TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
                  valeurMetaLastPage = DATE_TIME_FORMAT.format(storageMetadata
                        .getValue());
               } else {
                  valeurMetaLastPage = DATE_FORMAT.format(storageMetadata
                        .getValue());
               }
            } else {
               valeurMetaLastPage = storageMetadata.getValue().toString();
            }
            break;
         }
      }
      return valeurMetaLastPage;
   }

   /**
    * Création de la chainedFilter à partir des filtres de la requête
    * 
    * @param paginatedLuceneCriteria
    * @param referentielMeta
    * @return la chainedFilter
    */
   private ChainedFilter createChaineFilter(
         PaginatedLuceneCriteria paginatedLuceneCriteria,
         Map<String, MetadataReference> referentielMeta) {
      ChainedFilter chainedFilter = ToolkitFactory.getInstance()
            .createChainedFilter();

      List<AbstractFilter> listeFiltres = paginatedLuceneCriteria.getFilters();
      // On parcourt les filtres pour les ajouter à la chainedFilter
      for (AbstractFilter filtre : listeFiltres) {
         if (filtre instanceof ValueFilter) {
            Object value = ((ValueFilter) filtre).getValue();
            chainedFilter.addTermFilter(filtre.getShortCode(), (String) value,
                  ChainedFilterOperator.AND);
         } else if (filtre instanceof RangeFilter) {
            Object minValue = ((RangeFilter) filtre).getMinValue();
            Object maxValue = ((RangeFilter) filtre).getMaxValue();
            String typeMeta = referentielMeta.get(
                  ((RangeFilter) filtre).getLongCode()).getType();
            if ("Integer".equals(typeMeta) || "Long".equals(typeMeta)) {
               chainedFilter.addIntRangeFilter(filtre.getShortCode(), Integer
                     .parseInt((String) minValue), Integer
                     .parseInt((String) maxValue), true, true,
                     ChainedFilterOperator.AND);
            } else {
               chainedFilter.addTermRangeFilter(filtre.getShortCode(),
                     (String) minValue, (String) maxValue, true, true,
                     ChainedFilterOperator.AND);
            }
         } else if (filtre instanceof NotValueFilter) {
            Object value = ((NotValueFilter) filtre).getValue();
            if (value instanceof String) {
               chainedFilter.addTermFilter(filtre.getShortCode(),
                     (String) value, ChainedFilterOperator.ANDNOT);
            } else if (value instanceof Integer) {
               chainedFilter.addTermFilter(filtre.getShortCode(), Integer
                     .toString((Integer) value), ChainedFilterOperator.ANDNOT);
            }
         } else if (filtre instanceof NotRangeFilter) {
            Object minValue = ((NotRangeFilter) filtre).getMinValue();
            Object maxValue = ((NotRangeFilter) filtre).getMaxValue();
            String typeMeta = referentielMeta.get(
                  ((NotRangeFilter) filtre).getLongCode()).getType();
            if ("Integer".equals(typeMeta) || "Long".equals(typeMeta)) {
               chainedFilter.addIntRangeFilter(filtre.getShortCode(), Integer
                     .parseInt((String) minValue), Integer
                     .parseInt((String) maxValue), true, true,
                     ChainedFilterOperator.ANDNOT);
            } else {
               chainedFilter.addTermRangeFilter(filtre.getShortCode(),
                     (String) minValue, (String) maxValue, true, true,
                     ChainedFilterOperator.ANDNOT);
            }

         }
      }
      return chainedFilter;
   }

   /**
    * Convertion d'une liste de métadonnées String en liste de SorageMetadata
    * 
    * @param listeMeta
    *           La liste des métadonnées à convertir
    * @return La liste des métadonnées convertie
    * @throws MetadonneeInexistante
    *            Exception si la métadonnée n'existe pas
    */
   private List<StorageMetadata> convertToStorageMeta(List<String> listeMeta)
         throws MetadonneeInexistante {
      List<StorageMetadata> metadatas = new ArrayList<StorageMetadata>();
      List<String> keyList = new ArrayList<String>();
      Map<String, String> mapShortCode = null;
      try {
         mapShortCode = convertService.longCodeToShortCode(listeMeta);
      } catch (LongCodeNotFoundException longExcept) {
         String message = ResourceMessagesUtils.loadMessage(
               "consultation.metadonnees.inexistante", StringUtils.join(
                     longExcept.getListCode(), SEPARATOR_STRING));
         throw new MetadonneeInexistante(message);
      }
      keyList.addAll(mapShortCode.keySet());

      for (String shortCode : keyList) {
         metadatas.add(new StorageMetadata(shortCode));
      }
      return metadatas;
   }

}

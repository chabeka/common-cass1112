package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.crud;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.ChainedFilter;
import net.docubase.toolkit.model.search.ChainedFilter.ChainedFilterOperator;
import net.docubase.toolkit.model.search.SearchQuery;
import net.docubase.toolkit.model.search.SearchResult;
import net.docubase.toolkit.model.search.SortedSearchQuery;

/**
 * Implémente les services de l'interface {@link SearchingService} .
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
   private MetadataReferenceDAO referenceDAO;

   @Autowired
   private SAEConvertMetadataService convertService;

   @Autowired
   private PrmdService prmdService;

   @Autowired
   private MappingDocumentService mappingService;

   @Value("${sae.nom.instance.plateforme}")
   private String nomPlateforme;

   public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat(
                                                                                "yyyyMMddHHmmssSSS");

   public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
                                                                           "yyyyMMdd");

   /**
    * {@inheritDoc}
    */
   @Override
   @Loggable(LogLevel.TRACE)
   @ServiceChecked
   public StorageDocuments searchStorageDocumentByLuceneCriteria(
                                                                 final LuceneCriteria luceneCriteria)
         throws SearchingServiceEx,
         QueryParseServiceEx {
      final String prefixTrace = "searchStorageDocumentByLuceneCriteria()";
      final List<StorageDocument> storageDocuments = new ArrayList<>();
      try {

         LOG.debug("{} - Requête Lucene envoyée à DFCE: \"{}\"",
                   prefixTrace,
                   luceneCriteria.getLuceneQuery());

         final SortedSearchQuery paramSearchQuery = ToolkitFactory.getInstance()
                                                                  .createMonobaseSortedQuery(luceneCriteria.getLuceneQuery(),
                                                                                             getBaseDFCE());
         paramSearchQuery.setPageSize(luceneCriteria.getLimit());
         paramSearchQuery.setOffset(0);
         final SearchResult searchResult = getDfceServices().search(paramSearchQuery);

         for (final Document document : Utils.nullSafeIterable(searchResult
                                                                           .getDocuments())) {

            storageDocuments.add(BeanMapper.dfceDocumentToStorageDocument(
                                                                          document,
                                                                          luceneCriteria.getDesiredStorageMetadatas(),
                                                                          getDfceServices(),
                                                                          nomPlateforme,
                                                                          true,
                                                                          false));
         }
      }
      catch (final SearchQueryParseException except) {
         throw new QueryParseServiceEx(StorageMessageHandler
                                                            .getMessage(Constants.SRH_CODE_ERROR),
                                       except.getMessage(),
                                       except);
      }
      catch (final StorageException srcSerEx) {
         throw new SearchingServiceEx(StorageMessageHandler
                                                           .getMessage(Constants.SRH_CODE_ERROR),
                                      srcSerEx.getMessage(),
                                      srcSerEx);
      }
      catch (final IOException ioExcept) {
         throw new SearchingServiceEx(StorageMessageHandler
                                                           .getMessage(Constants.SRH_CODE_ERROR),
                                      ioExcept.getMessage(),
                                      ioExcept);
      }
      catch (final ExceededSearchLimitException exceedSearchEx) {
         throw new SearchingServiceEx(StorageMessageHandler
                                                           .getMessage(Constants.SRH_CODE_ERROR),
                                      exceedSearchEx
                                                    .getMessage(),
                                      exceedSearchEx);
      }
      catch (final Exception except) {
         throw new SearchingServiceEx(StorageMessageHandler
                                                           .getMessage(Constants.SRH_CODE_ERROR),
                                      except.getMessage(),
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
   public StorageDocument searchStorageDocumentByUUIDCriteria(
                                                              final UUIDCriteria uUIDCriteria, final boolean forConsultation)
         throws SearchingServiceEx {

      // -- Recherche du document
      return storageDocumentServiceSupport.searchStorageDocumentByUUIDCriteria(
                                                                               getDfceServices(),
                                                                               uUIDCriteria,
                                                                               forConsultation,
                                                                               LOG);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @ServiceChecked
   public StorageDocument searchMetaDatasByUUIDCriteria(
                                                        final UUIDCriteria uuidCriteria)
         throws SearchingServiceEx {
      try {
         final Document docDfce = getDfceServices().getDocumentByUUID(uuidCriteria.getUuid());

         return BeanMapper.dfceMetaDataToStorageDocument(docDfce,
                                                         uuidCriteria
                                                                     .getDesiredStorageMetadatas(),
                                                         getDfceServices());

      }
      catch (final StorageException srcSerEx) {
         throw new SearchingServiceEx(StorageMessageHandler
                                                           .getMessage(Constants.SRH_CODE_ERROR),
                                      srcSerEx.getMessage(),
                                      srcSerEx);
      }
      catch (final IOException ioExcept) {
         throw new SearchingServiceEx(StorageMessageHandler
                                                           .getMessage(Constants.SRH_CODE_ERROR),
                                      ioExcept.getMessage(),
                                      ioExcept);
      }
      catch (final Exception except) {
         throw new SearchingServiceEx(StorageMessageHandler
                                                           .getMessage(Constants.SRH_CODE_ERROR),
                                      except.getMessage(),
                                      except);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public PaginatedStorageDocuments searchPaginatedStorageDocuments(
                                                                    final PaginatedLuceneCriteria paginatedLuceneCriteria)
         throws SearchingServiceEx, QueryParseServiceEx {

      return searchByIterator(paginatedLuceneCriteria, null, false, true);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public PaginatedStorageDocuments searchPaginatedStorageDocumentsWithBestIndex(final PaginatedLuceneCriteria paginatedLuceneCriteria,
                                                                                 final List<String> bestIndex)
         throws SearchingServiceEx, QueryParseServiceEx {

      return searchByIterator(paginatedLuceneCriteria, bestIndex, false, true);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public byte[] searchStorageDocumentContentByUUIDCriteria(
                                                            final UUIDCriteria uUIDCriteria)
         throws IOException {
      final Document docDfce = getDfceServices().getDocumentByUUID(uUIDCriteria.getUuid());
      final InputStream docContent = getDfceServices().getDocumentFile(docDfce);
      return IOUtils.toByteArray(docContent);
   }

   /**
    * Methode de rechercher par iterateur.
    *
    * @param paginatedLuceneCriteria
    *           requete lucene que l'on veut paginer
    * @param searchInRecycleBean
    *           boolean indiquant si l'on recherche dans le stockage par defaut
    *           ou la corbeille
    * @param useChainedFilter
    *           boolean indiquant si la requete peut utiliser les filtres
    * @return PaginatedStorageDocuments
    * @throws SearchingServiceEx
    *            exception levée lors d'une erreur de recherche
    * @throws QueryParseServiceEx
    *            exception levée lorsque la syntaxe de la recherche n'est pas
    *            valide
    */
   private PaginatedStorageDocuments searchByIterator(
                                                      final PaginatedLuceneCriteria paginatedLuceneCriteria,
                                                      final List<String> indexOrderPreferenceList,
                                                      final boolean searchInRecycleBean,
                                                      final boolean useChainedFilter)
         throws SearchingServiceEx, QueryParseServiceEx {
      final PaginatedStorageDocuments paginatedStorageDocuments = new PaginatedStorageDocuments();
      try {
         final String prefixeTrc = "searchByIterator()";

         final List<StorageDocument> storageDocuments = new ArrayList<>();

         // Création de la SearchQuery
         final SearchQuery searchQuery = ToolkitFactory.getInstance()
                                                       .createMonobaseQuery(paginatedLuceneCriteria.getLuceneQuery(),
                                                                            getBaseDFCE());
         // Récupération du référentiel des métadonnés pour les différentes
         // vérifications
         final Map<String, MetadataReference> referentielMeta = referenceDAO
                                                                            .getAllMetadataReferencesPourVerifDroits();

         if (useChainedFilter) {
            // Création de la chainedFilter
            final ChainedFilter chainedFilter = createChaineFilter(
                                                                   paginatedLuceneCriteria,
                                                                   referentielMeta);
            // On l'ajoute à la searchQuery
            searchQuery.setChainedFilter(chainedFilter);
         }
         // On fixe le pas d'execution de l'itérateur
         searchQuery.setSearchLimit(LIMITE);

         // charger l'index à utliser dans l'objet searchQuery
         if (indexOrderPreferenceList != null && !indexOrderPreferenceList.isEmpty()) {
            searchQuery.setIndexOrderPreference(indexOrderPreferenceList);
         }

         // Recherche des documents par l'itérateur DFCE
         Iterator<Document> iterateur;
         if (searchInRecycleBean) {
            iterateur = getDfceServices().createDocumentIteratorFromRecycleBin(searchQuery);
         } else {
            iterateur = getDfceServices().createDocumentIterator(searchQuery);
         }

         final Integer limite = paginatedLuceneCriteria.getLimit();
         Integer compteur = 0;

         // On récupère la liste complète des métadonnées du référentiel afin de
         // pouvoir tester les droits
         final List<String> metadonneesRef = new ArrayList<>(referentielMeta
                                                                            .keySet());

         List<StorageMetadata> allMeta = null;
         // dans le cas de la recherche dans la corbeille, on ne vérifie pas les droits pour éviter de récupérer le flag GEL dans le mauvais index
         if (!searchInRecycleBean) {
            allMeta = convertToStorageMeta(metadonneesRef);
         }

         // Si appel au service de recherche avec passage de l'UUID du dernier
         // document, on boucle sur l'itérateur jusqu'à ce qu'on ait atteint le
         // dernier document
         if (paginatedLuceneCriteria.getLastIdDoc() != null) {
            final String lastUuid = paginatedLuceneCriteria.getLastIdDoc().toString();
            while (iterateur.hasNext()) {
               final Document doc = iterateur.next();
               if (lastUuid.equals(doc.getUuid().toString())) {
                  break;
               }
            }
         }

         // Ensuite, on continue l'itération pour récupérer les documents
         // souhaités
         while (iterateur.hasNext()) {
            final Document doc = iterateur.next();
            final StorageDocument storageDocument = BeanMapper
                                                              .dfceDocumentToStorageDocument(doc,
                                                                                             allMeta,
                                                                                             getDfceServices(),
                                                                                             nomPlateforme,
                                                                                             true,
                                                                                             false);
            UntypedDocument untypedDocument = null;
            // Vérification des droits
            if (storageDocument != null) {
               untypedDocument = mappingService
                                               .storageDocumentToUntypedDocument(storageDocument);
            }

            if (compteur < limite) {
               // On vérifie que le document courant est autorisé par le
               // périmètre de donnée
               boolean isPermitted = true;
               // dans le cas de la recherche dans la corbeille, on ne verifie pas les droits pour eviter de recuperer le flag GEL dans le mauvais index
               if (!searchInRecycleBean) {
                  LOG.debug("{} - Récupération des droits", prefixeTrc);
                  final AuthenticationToken token = AuthenticationContext
                                                                         .getAuthenticationToken();
                  final List<SaePrmd> saePrmds = token.getSaeDroits()
                                                      .get(
                                                           "recherche_iterateur");
                  LOG.debug("{} - Vérification des droits", prefixeTrc);
                  isPermitted = prmdService.isPermitted(untypedDocument
                                                                       .getUMetadatas(),
                                                        saePrmds);
               }

               if (isPermitted) {
                  storageDocuments.add(BeanMapper
                                                 .dfceDocumentToStorageDocument(doc,
                                                                                paginatedLuceneCriteria
                                                                                                       .getDesiredStorageMetadatas(),
                                                                                getDfceServices(),
                                                                                nomPlateforme,
                                                                                true,
                                                                                false));
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

         final String codeCourtVaryingMeta = paginatedLuceneCriteria
                                                                    .getCodeCourtVaryingMeta();

         // Si la recherche renvoie des documents, on récupère la valeur de la
         // métadonnée variable afin de pouvoir la transmettre au client dans
         // l'identifiant de la dernière page
         if (storageDocuments.size() > 0) {
            final String valeurMetaLastPage = recupererValeurMetaLastPage(
                                                                          storageDocuments,
                                                                          codeCourtVaryingMeta);
            paginatedStorageDocuments.setValeurMetaLastPage(valeurMetaLastPage);
         }

      }
      catch (final ReferentialException e) {
         throw new SearchingServiceEx(StorageMessageHandler
                                                           .getMessage(Constants.SRH_CODE_ERROR),
                                      e.getMessage(),
                                      e);
      }
      catch (final MetadonneeInexistante e) {
         throw new SearchingServiceEx(StorageMessageHandler
                                                           .getMessage(Constants.SRH_CODE_ERROR),
                                      e.getMessage(),
                                      e);
      }
      catch (final StorageException srcSerEx) {
         throw new SearchingServiceEx(StorageMessageHandler
                                                           .getMessage(Constants.SRH_CODE_ERROR),
                                      srcSerEx.getMessage(),
                                      srcSerEx);
      }
      catch (final IOException ioExcept) {
         throw new SearchingServiceEx(StorageMessageHandler
                                                           .getMessage(Constants.SRH_CODE_ERROR),
                                      ioExcept.getMessage(),
                                      ioExcept);
      }
      catch (final InvalidSAETypeException e) {
         throw new SearchingServiceEx(StorageMessageHandler
                                                           .getMessage(Constants.SRH_CODE_ERROR),
                                      e.getMessage(),
                                      e);
      }
      catch (final MappingFromReferentialException e) {
         throw new SearchingServiceEx(StorageMessageHandler
                                                           .getMessage(Constants.SRH_CODE_ERROR),
                                      e.getMessage(),
                                      e);
      }
      catch (final SearchQueryParseException e) {
         throw new QueryParseServiceEx(StorageMessageHandler
                                                            .getMessage(Constants.SRH_CODE_ERROR),
                                       e.getMessage(),
                                       e);
      }
      return paginatedStorageDocuments;
   }

   private String recupererValeurMetaLastPage(
                                              final List<StorageDocument> storageDocuments,
                                              final String codeCourtVaryingMeta) {
      final StorageDocument storageDocument = storageDocuments.get(storageDocuments
                                                                                   .size()
            - 1);
      final List<StorageMetadata> listeSMeta = storageDocument.getMetadatas();
      String valeurMetaLastPage = "";
      for (final StorageMetadata storageMetadata : listeSMeta) {
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
                                            final PaginatedLuceneCriteria paginatedLuceneCriteria,
                                            final Map<String, MetadataReference> referentielMeta) {
      final ChainedFilter chainedFilter = ToolkitFactory.getInstance()
                                                        .createChainedFilter();

      final List<AbstractFilter> listeFiltres = paginatedLuceneCriteria.getFilters();
      // On parcourt les filtres pour les ajouter à la chainedFilter
      for (final AbstractFilter filtre : listeFiltres) {
         if (filtre instanceof ValueFilter) {
            final Object value = ((ValueFilter) filtre).getValue();
            chainedFilter.addTermFilter(filtre.getShortCode(),
                                        (String) value,
                                        ChainedFilterOperator.AND);
         } else if (filtre instanceof RangeFilter) {
            final Object minValue = ((RangeFilter) filtre).getMinValue();
            final Object maxValue = ((RangeFilter) filtre).getMaxValue();
            final String typeMeta = referentielMeta.get(
                                                        ((RangeFilter) filtre).getLongCode())
                                                   .getType();
            if ("Integer".equals(typeMeta) || "Long".equals(typeMeta)) {
               chainedFilter.addIntRangeFilter(filtre.getShortCode(),
                                               Integer
                                                      .parseInt((String) minValue),
                                               Integer
                                                      .parseInt((String) maxValue),
                                               true,
                                               true,
                                               ChainedFilterOperator.AND);
            } else {
               chainedFilter.addTermRangeFilter(filtre.getShortCode(),
                                                (String) minValue,
                                                (String) maxValue,
                                                true,
                                                true,
                                                ChainedFilterOperator.AND);
            }
         } else if (filtre instanceof NotValueFilter) {
            final Object value = ((NotValueFilter) filtre).getValue();
            if (value instanceof String) {
               chainedFilter.addTermFilter(filtre.getShortCode(),
                                           (String) value,
                                           ChainedFilterOperator.ANDNOT);
            } else if (value instanceof Integer) {
               chainedFilter.addTermFilter(filtre.getShortCode(),
                                           Integer
                                                  .toString((Integer) value),
                                           ChainedFilterOperator.ANDNOT);
            }
         } else if (filtre instanceof NotRangeFilter) {
            final Object minValue = ((NotRangeFilter) filtre).getMinValue();
            final Object maxValue = ((NotRangeFilter) filtre).getMaxValue();
            final String typeMeta = referentielMeta.get(
                                                        ((NotRangeFilter) filtre).getLongCode())
                                                   .getType();
            if ("Integer".equals(typeMeta) || "Long".equals(typeMeta)) {
               chainedFilter.addIntRangeFilter(filtre.getShortCode(),
                                               Integer
                                                      .parseInt((String) minValue),
                                               Integer
                                                      .parseInt((String) maxValue),
                                               true,
                                               true,
                                               ChainedFilterOperator.ANDNOT);
            } else {
               chainedFilter.addTermRangeFilter(filtre.getShortCode(),
                                                (String) minValue,
                                                (String) maxValue,
                                                true,
                                                true,
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
   private List<StorageMetadata> convertToStorageMeta(final List<String> listeMeta)
         throws MetadonneeInexistante {
      final List<StorageMetadata> metadatas = new ArrayList<>();
      final List<String> keyList = new ArrayList<>();
      Map<String, String> mapShortCode = null;
      try {
         mapShortCode = convertService.longCodeToShortCode(listeMeta);
      }
      catch (final LongCodeNotFoundException longExcept) {
         final String message = ResourceMessagesUtils.loadMessage(
                                                                  "consultation.metadonnees.inexistante",
                                                                  StringUtils.join(
                                                                                   longExcept.getListCode(),
                                                                                   SEPARATOR_STRING));
         throw new MetadonneeInexistante(message);
      }
      keyList.addAll(mapShortCode.keySet());

      for (final String shortCode : keyList) {
         metadatas.add(new StorageMetadata(shortCode));
      }
      return metadatas;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public PaginatedStorageDocuments searchStorageDocumentsInRecycleBean(
                                                                        final PaginatedLuceneCriteria paginatedLuceneCriteria)
         throws SearchingServiceEx, QueryParseServiceEx {

      return searchByIterator(paginatedLuceneCriteria, null, true, false);
   }

   /**
    * Setter pour referenceDAO
    *
    * @param referenceDAO
    *           the referenceDAO to set
    */
   public final void setReferenceDAO(final MetadataReferenceDAO referenceDAO) {
      this.referenceDAO = referenceDAO;
   }

   /**
    * Setter pour mappingService
    *
    * @param mappingService
    *           the mappingService to set
    */
   public final void setMappingService(final MappingDocumentService mappingService) {
      this.mappingService = mappingService;
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

}

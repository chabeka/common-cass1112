/**
 *
 */
package fr.urssaf.image.sae.services.batch.suppression.support.stockage.batch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.util.Version;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.building.services.BuildService;
import fr.urssaf.image.sae.metadata.exceptions.IndexCompositeException;
import fr.urssaf.image.sae.metadata.referential.services.IndexCompositeService;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.InterruptionTraitementMasseSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.model.InterruptionTraitementConfig;
import fr.urssaf.image.sae.services.batch.suppression.exception.SuppressionMasseSearchException;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.QueryParseServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.PaginatedStorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.filters.AbstractFilter;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.PaginatedLuceneCriteria;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

/**
 * Item reader permettant de récupérer les documents à partir de la requête
 * lucene.
 */
@Component
@Scope("step")
public class StorageDocumentReader implements ItemReader<StorageDocument> {

   private static final Logger LOGGER = LoggerFactory.getLogger(StorageDocumentReader.class);

   /**
    * Requête lucene récupérée dans le contexte du job d'exécution
    */
   @Value("#{jobExecutionContext['requeteFinale']}")
   private String requeteLucene;

   /**
    * Service de build permettant de générer les critères de la recherche
    * paginée.
    */
   @Autowired
   @Qualifier("buildService")
   private BuildService buildService;

   /**
    * Service permettant d'exécuter la recherche paginée
    */
   @Autowired
   @Qualifier("storageServiceProvider")
   private StorageServiceProvider storageServiceProvider;

   @Autowired
   private InterruptionTraitementMasseSupport support;

   @Autowired
   private InterruptionTraitementConfig config;

   @Autowired
   private IndexCompositeService indexCompositeService;

   /**
    * Nombre de documents par page d'itération.
    */
   private static final int MAX_PAR_PAGE = 500;

   /**
    * Itérateur de documents.
    */
   private Iterator<StorageDocument> iterateurDoc = null;

   /**
    * Flag indiquant s'il s'agit de la dernière itération.
    */
   private boolean lastIteration = false;

   /**
    * Dernier identifiant de document remonté à l'itération précédente.
    */
   private UUID lastIdDoc = null;

   /**
    * Liste des métadonnées que l'on souhaite récupérer
    */
   private static final List<SAEMetadata> DESIRED_METADATAS = new ArrayList<>();

   /**
    * Initialisation de la liste des métadonnées désirées.
    */
   static {
      // En l'occurrence, on veut savoir si le document est gelé ou non
      final SAEMetadata metadataGel = new SAEMetadata(StorageTechnicalMetadatas.GEL.getLongCode(),
                                                      StorageTechnicalMetadatas.GEL.getShortCode(),
                                                      null);
      DESIRED_METADATAS.add(metadataGel);
   }

   /**
    * Permet de verifier s'il y a un élément suivant.
    * 
    * @return boolean
    * @throws SuppressionMasseSearchException
    */
   private boolean hasNext() throws SuppressionMasseSearchException {
      boolean hasNext;
      if (iterateurDoc == null || !iterateurDoc.hasNext()) {
         // dans ce cas, on est soit à la première itération, soit on n'a
         // plus d'éléments dans notre itérateur 'local'
         // il faut donc aller récupérer des nouveaux éléments si on n'est
         // pas à la dernière itération
         if (lastIteration) {
            hasNext = false;
         } else {
            // avant de faire appel à la base pour récupérer les éléments,
            // on va tester qu'on est pas à l'heure
            // de l'interruption des serveurs
            gererInterruption();
            // on appelle la récupération des prochains éléments
            hasNext = fetchMore();
         }
      } else {
         // on a encore des éléments dans l'itérateur 'local'
         hasNext = true;
      }
      return hasNext;
   }

   /**
    * Récupère le prochain element.
    * 
    * @return StorageDocument
    */
   private StorageDocument next() {
      return iterateurDoc.next();
   }

   /**
    * Permet de récupérer les prochains éléments.
    * 
    * @return boolean
    * @throws SuppressionMasseSearchException
    */
   private boolean fetchMore() throws SuppressionMasseSearchException {
      boolean hasMore = false;
      try {
         String strIdDoc;
         if (lastIdDoc == null) {
            strIdDoc = "null";
         } else {
            strIdDoc = lastIdDoc.toString();
         }

         // On cherche l'index le plus pertinent à utiliser pour la requête
         String bestIndex = null;
         if (!StringUtils.containsIgnoreCase(requeteLucene, " OR ")) {
            // Comme in n'y a pas de OR dans la requête, on peut trouver l'index le plus pertinent à utiliser

            // Récupération des métadonnées utilisées dans la requête
            final Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
            final QueryParser luceneParser = new QueryParser(Version.LUCENE_CURRENT, "", analyzer);
            final Set<String> fieldsLuceneQuery = new TreeSet<>();
            calculateQueryFieldsRecursively(luceneParser.parse(requeteLucene), fieldsLuceneQuery);

            // On cherche le meilleur index à partir de ces métadonnées
            bestIndex = indexCompositeService.getBestIndexForQuery(fieldsLuceneQuery);
         }

         LOGGER.debug("fetchMore : {} - {}", requeteLucene, strIdDoc);

         final PaginatedLuceneCriteria paginatedLuceneCriteria = buildService.buildStoragePaginatedLuceneCriteria(
                                                                                                                  requeteLucene,
                                                                                                                  MAX_PAR_PAGE,
                                                                                                                  DESIRED_METADATAS,
                                                                                                                  new ArrayList<AbstractFilter>(),
                                                                                                                  lastIdDoc,
                                                                                                                  "");

         PaginatedStorageDocuments paginatedStorageDocuments;
         if (bestIndex != null && !bestIndex.isEmpty()) {
            LOGGER.debug("index sélectionné : {}", bestIndex);
            final ArrayList<String> indexOrderPreferenceList = new ArrayList<>();
            indexOrderPreferenceList.add(bestIndex);
            paginatedStorageDocuments = storageServiceProvider.getStorageDocumentService()
                                                              .searchPaginatedStorageDocumentsWithBestIndex(paginatedLuceneCriteria,
                                                                                                            indexOrderPreferenceList);
         } else {
            paginatedStorageDocuments = storageServiceProvider.getStorageDocumentService()
                                                              .searchPaginatedStorageDocuments(paginatedLuceneCriteria);
         }

         // récupère le résultat de la requête
         iterateurDoc = paginatedStorageDocuments.getAllStorageDocuments().iterator();
         lastIteration = paginatedStorageDocuments.getLastPage();
         if (!paginatedStorageDocuments.getAllStorageDocuments().isEmpty()) {
            final int nbElements = paginatedStorageDocuments.getAllStorageDocuments().size();
            lastIdDoc = paginatedStorageDocuments.getAllStorageDocuments().get(nbElements - 1).getUuid();
            hasMore = true;
         } else {
            hasMore = false;
         }

      }
      catch (final ConnectionServiceEx | SearchingServiceEx | QueryParseServiceEx | IndexCompositeException except) {
         throw new SuppressionMasseSearchException(except);
      }
      catch (final org.apache.lucene.queryParser.ParseException except) {
         throw new SuppressionMasseSearchException("ParseException - " + except.getMessage());
      }

      return hasMore;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public StorageDocument read() throws Exception {

      StorageDocument doc = null;
      if (hasNext()) {
         doc = next();
      }
      if (doc != null) {
         LOGGER.debug("Read du document : {}", doc.getUuid());
      }
      return doc;
   }

   /**
    * Méthode permettant de gérer la plage d'interruption.
    * 
    * @throws SuppressionMasseSearchException
    */
   private void gererInterruption() throws SuppressionMasseSearchException {
      // on vérifie que le traitement ne doit pas s'interrompre
      final DateTime currentDate = new DateTime();

      if (config != null && support.hasInterrupted(currentDate, config)) {

         try {
            support.interruption(currentDate, config);
         }
         catch (final InterruptionTraitementException e) {
            throw new SuppressionMasseSearchException(e);
         }
      }
   }

   /**
    * Retourne les critères de recherche a partir d'une requête lucene
    * 
    * @param query
    * @return
    * @throws ParseException
    */
   private void calculateQueryFieldsRecursively(final Query query, final Set<String> fieldsLuceneQuery) {

      final Set<String> fields = new TreeSet<>();
      if (query instanceof TermQuery) {
         final TermQuery tQuery = (TermQuery) query;
         final Term term = tQuery.getTerm();
         fields.add(term.field());

      } else if (query instanceof TermRangeQuery) {
         final TermRangeQuery trq = (TermRangeQuery) query;
         fields.add(trq.getField());
      } else if (query instanceof BooleanQuery) {
         final BooleanQuery bQuery = (BooleanQuery) query;
         final List<BooleanClause> clauses = bQuery.clauses();
         for (final BooleanClause clause : clauses) {
            final Query innerQuery = clause.getQuery();
            calculateQueryFieldsRecursively(innerQuery, fieldsLuceneQuery);
         }
      }
      // TODO support more lucene query types
      // else {
      // return null;
      // }

      if (!fields.isEmpty()) {
         fieldsLuceneQuery.addAll(fields);
      }

   }

}

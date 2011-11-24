package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import fr.urssaf.image.sae.storage.dfce.data.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.data.model.LuceneQueries;
import fr.urssaf.image.sae.storage.dfce.data.model.LuceneQuery;
import fr.urssaf.image.sae.storage.dfce.data.model.SaeDocument;
import fr.urssaf.image.sae.storage.dfce.mapping.DocumentForTestMapper;
import fr.urssaf.image.sae.storage.dfce.services.StorageServices;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.QueryParseServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.LuceneCriteria;

/**
 * Classe de test des services de recherche.
 * 
 * @author Rhofir, aknore.
 * 
 */

public class LuceneSimpleQueryTest extends StorageServices {
   /**
    * Test de recherche par requête Lucene.
    */
   @Test
   @Ignore
   public void luceneQueriesWithWildcard() throws SearchingServiceEx,
         IOException, ParseException, InsertionServiceEx, QueryParseServiceEx {
      createStorageDocument();
      final Map<String, LuceneCriteria> queries = buildQueries("wildcard");
      for (Map.Entry<String, LuceneCriteria> query : queries.entrySet()) {
         Assert.assertTrue(query.getKey(), getSearchingService()
               .searchStorageDocumentByLuceneCriteria(query.getValue())
               .getAllStorageDocuments().size() > 0);
      }
   }

   /**
    * 
    * @throws FileNotFoundException
    *            Exception lévée lorsque le fichier n'existe pas.
    * @throws IOException
    *            Exception lévée
    * @throws ParseException
    *            Exception lévée
    * @throws InsertionServiceEx
    *            Exception lévée
    */
   private void createStorageDocument() throws FileNotFoundException,
         IOException, ParseException, InsertionServiceEx {
      final SaeDocument saeDocument = getXmlDataService().saeDocumentReader(
            new File(Constants.XML_PATH_DOC_WITHOUT_ERROR[0]));
      final StorageDocument storageDocument = DocumentForTestMapper
            .saeDocumentXmlToStorageDocument(saeDocument);
      getInsertionService().insertStorageDocument(storageDocument);
   }

   /**
    * Test de recherche par requête Lucene.
    */
   @Test
   @Ignore
   public void luceneQueries() throws SearchingServiceEx, InsertionServiceEx,
         IOException, ParseException, QueryParseServiceEx {
      final Map<String, LuceneCriteria> queries = buildQueries("simple");
      createStorageDocument();
      for (Map.Entry<String, LuceneCriteria> query : queries.entrySet()) {
         Assert.assertTrue(query.getKey(), getSearchingService()
               .searchStorageDocumentByLuceneCriteria(query.getValue())
               .getAllStorageDocuments().size() > 0);
      }
   }

   /**
    * Test de recherche par requête Lucene.
    */
   @Test
   @Ignore
   public void luceneQueriesWithRange() throws SearchingServiceEx,
         InsertionServiceEx, IOException, ParseException, QueryParseServiceEx {
      createStorageDocument();
      final Map<String, LuceneCriteria> queries = buildQueries("range");
      for (Map.Entry<String, LuceneCriteria> query : queries.entrySet()) {
         Assert.assertTrue(query.getKey(), getSearchingService()
               .searchStorageDocumentByLuceneCriteria(query.getValue())
               .getAllStorageDocuments().size() > 0);
      }
   }

   /**
    * Test de recherche par requête Lucene.
    */
   @Test
   @Ignore
   public void luceneQueriesWithOperatorAnd() throws SearchingServiceEx,
         InsertionServiceEx, IOException, ParseException, QueryParseServiceEx {
      createStorageDocument();
      final Map<String, LuceneCriteria> queries = buildQueries("withOperatorAnd");
      for (Map.Entry<String, LuceneCriteria> query : queries.entrySet()) {
         Assert.assertTrue(query.getKey(), getSearchingService()
               .searchStorageDocumentByLuceneCriteria(query.getValue())
               .getAllStorageDocuments().size() > 0);
      }
   }

   /**
    * Test de recherche par requête Lucene avec l'opérateur OR.
    */
   @Test
   @Ignore
   public void luceneQueriesWithOperatorOr() throws SearchingServiceEx,
         InsertionServiceEx, IOException, ParseException, QueryParseServiceEx {
      createStorageDocument();
      final Map<String, LuceneCriteria> queries = buildQueries("withOperatorOr");
      for (Map.Entry<String, LuceneCriteria> query : queries.entrySet()) {
         Assert.assertTrue(query.getKey(), getSearchingService()
               .searchStorageDocumentByLuceneCriteria(query.getValue())
               .getAllStorageDocuments().size() > 0);
      }
   }

   /**
    * Test de recherche par requête Lucene avec l'opérateur OR.
    */
   @Test
   @Ignore
   public void luceneQueriesWithOperatorAndOr() throws SearchingServiceEx,
         InsertionServiceEx, IOException, ParseException, QueryParseServiceEx {
      createStorageDocument();
      final Map<String, LuceneCriteria> queries = buildQueries("withOperatorAndOr");
      for (Map.Entry<String, LuceneCriteria> query : queries.entrySet()) {
         Assert.assertTrue(query.getKey(), getSearchingService()
               .searchStorageDocumentByLuceneCriteria(query.getValue())
               .getAllStorageDocuments().size() > 0);
      }
   }

   /**
    * Test de recherche par requête Lucene avec l'opérateur OR.
    */
   @Test
   @Ignore
   public void luceneQueriesWithOperatorNot() throws SearchingServiceEx,
         InsertionServiceEx, IOException, ParseException, QueryParseServiceEx {
      createStorageDocument();
      final Map<String, LuceneCriteria> queries = buildQueries("withOperatorNot");
      for (Map.Entry<String, LuceneCriteria> query : queries.entrySet()) {
         Assert.assertTrue(query.getKey(), getSearchingService()
               .searchStorageDocumentByLuceneCriteria(query.getValue())
               .getAllStorageDocuments().size() > 0);
      }

   }

   /**
    * Permet de construire une serie de requête lucene à partir d'un type.
    * 
    * @param queryType
    *           : Le type de requête.
    * @return Une liste de requête suivant le type de requête en entrée.
    * @throws IOException
    *            Exception lévée
    * @throws ParseException
    *            Exception lévée
    * @throws InsertionServiceEx
    *            Exception lévée
    */
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   public Map<String, LuceneCriteria> buildQueries(final String queryType)
         throws InsertionServiceEx, IOException, ParseException {
      createStorageDocument();
      final LuceneQueries queries = getXmlDataService().queriesReader(
            new File(Constants.XML_FILE_QUERIES[0]));
      final Map<String, LuceneCriteria> luceneCriteria = new HashMap<String, LuceneCriteria>();
      for (LuceneQuery query : queries.getLuceneQuery()) {
         if (query.getQueryType().equals(queryType)) {
            final String saeQuery = query.getQuery();
            final List<String> metadatas = Arrays.asList(query
                  .getDesiredMetadata().split(","));
            final List<StorageMetadata> stMetadata = new ArrayList<StorageMetadata>();
            for (String metadata : metadatas) {
               stMetadata.add(new StorageMetadata(metadata.trim()));
            }
            luceneCriteria.put(query.getQueryId(), new LuceneCriteria(saeQuery,
                  100, stMetadata));
         }
      }
      return luceneCriteria;
   }
}

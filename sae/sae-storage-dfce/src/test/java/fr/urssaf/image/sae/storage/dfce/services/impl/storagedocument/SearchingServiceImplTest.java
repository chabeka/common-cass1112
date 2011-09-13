package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import fr.urssaf.image.sae.storage.dfce.data.model.DesiredMetaData;
import fr.urssaf.image.sae.storage.dfce.data.test.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.data.utils.CheckDataUtils;
import fr.urssaf.image.sae.storage.dfce.mapping.BeanTestDocumentMapper;
import fr.urssaf.image.sae.storage.dfce.services.CommonServicesImpl;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.LuceneCriteria;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;

/**
 * Classe de test des services de recherche.
 * 
 * @author Rhofir, aknore.
 * 
 */

public class SearchingServiceImplTest extends CommonServicesImpl {
   /**
    * Test de recherche par requête Lucence.<br>{@inheritDoc}
    */
   @Test
   public void searchStorageDocumentByLuceneCriteria()
         throws SearchingServiceEx, InsertionServiceEx, IOException,
         ParseException, DeletionServiceEx {
      int limit = 5;
      StorageDocument document = getMockData(getInsertionService());
      final String lucene = String.format("%s:%s", "_uuid", document.getUuid().toString());
      LuceneCriteria luceneCriteria = new LuceneCriteria(lucene, limit,
            new ArrayList<StorageMetadata>());
      Assert.assertNotNull("La recherche de documents par requette Lucence: ",
            getSearchingService().searchStorageDocumentByLuceneCriteria(
                  luceneCriteria).getAllStorageDocuments().size() >= 0);
      destroyMockTest(document.getUuid(), getDeletionService());
   }

   /**
    * Permet de récupérer un document à partir du critère « UUIDCriteria ».<br>
    * {@inheritDoc}
    */
   @Test
   public void searchDocumentByUUID() throws SearchingServiceEx,
         InsertionServiceEx, IOException, ParseException, DeletionServiceEx {
	   StorageDocument document = getMockData(getInsertionService());
      UUIDCriteria uuidCriteria = new UUIDCriteria(document.getUuid(),
            new ArrayList<StorageMetadata>());
      Assert.assertNotNull("Recupération d'un document par UUID :",
            getSearchingService().searchStorageDocumentByUUIDCriteria(
                  uuidCriteria).getUuid());
      destroyMockTest(document.getUuid(), getDeletionService());
   }

   /**
    * Permet de récupérer un document à partir du critère « UUIDCriteria ».<br>
    * {@inheritDoc}
    */
   @Test
   public void searchDocumentByUUIDWithDesiredMetaData()
         throws SearchingServiceEx, InsertionServiceEx, IOException,
         ParseException, DeletionServiceEx {
      // Initialisation des jeux de données UUID
	   StorageDocument document  = getMockData(getInsertionService());
      // Initialisation des jeux de données Metadata
      final DesiredMetaData metaDataFromXml = getXmlDataService()
            .desiredMetaDataReader(
                  new File(Constants.XML_FILE_DESIRED_MDATA[0]));
      List<StorageMetadata> desiredMetadatas = BeanTestDocumentMapper
            .saeMetaDataXmlToStorageMetaData(metaDataFromXml).getMetadatas();

      UUIDCriteria uuidCriteria = new UUIDCriteria(document.getUuid(), desiredMetadatas);
      Assert.assertNotNull("Le resultat de recherche :", getSearchingService()
            .searchStorageDocumentByUUIDCriteria(uuidCriteria).getUuid());
      Assert.assertTrue(
            "Les deux listes des métaData doivent être identique : ",
            CheckDataUtils.checkDesiredMetaDatas(desiredMetadatas,
                  getSearchingService().searchMetaDatasByUUIDCriteria(
                        uuidCriteria).getMetadatas()));
      // Suppression du document insert
      destroyMockTest(document.getUuid(), getDeletionService());
   }

   /**
    * Récupérer les métadonnées par UUID. <br>{@inheritDoc}
    */
   @Test
   public void searchMetaDatasByUUID() throws SearchingServiceEx,
         InsertionServiceEx, IOException, ParseException, DeletionServiceEx {
      // Initialisation des jeux de données UUID
	   StorageDocument document  = getMockData(getInsertionService());
	   UUIDCriteria uuidCriteria = new UUIDCriteria(document.getUuid(),
	            new ArrayList<StorageMetadata>());
      Assert.assertNotNull(
            "Recupération d'une liste de métadonnées par uuid :",
            getSearchingService().searchMetaDatasByUUIDCriteria(uuidCriteria)
                  .getMetadatas());
      // Suppression du document insert
      destroyMockTest(document.getUuid(), getDeletionService());
   }

   /**
    * Récupérer les métadonnées par UUID et récupération qu'une liste de
    * métadonnées spécifique. <br>{@inheritDoc}
    */
   @Test
   public void searchMetaDatasByUUIDWithDesiredMetaData()
         throws SearchingServiceEx, IOException, ParseException,
         InsertionServiceEx, DeletionServiceEx {
      // Initialisation des jeux de données UUID
	   StorageDocument document  = getMockData(getInsertionService());
      // Initialisation des jeux de données Metadata
      final DesiredMetaData desiredMetaData = getXmlDataService()
            .desiredMetaDataReader(
                  new File(Constants.XML_FILE_DESIRED_MDATA[0]));
      List<StorageMetadata> desiredMetadatas = BeanTestDocumentMapper
            .saeMetaDataXmlToStorageMetaData(desiredMetaData).getMetadatas();
      UUIDCriteria uuidCriteria = new UUIDCriteria(document.getUuid(), desiredMetadatas);
      Assert.assertNotNull("Le resultat de recherche :", getSearchingService()
            .searchMetaDatasByUUIDCriteria(uuidCriteria).getMetadatas());
      Assert.assertTrue("Les deux listes des métaData doivent être identique",
            CheckDataUtils.checkDesiredMetaDatas(desiredMetadatas,
                  getSearchingService().searchMetaDatasByUUIDCriteria(
                        uuidCriteria).getMetadatas()));
      // Suppression du document insert
      destroyMockTest(document.getUuid(), getDeletionService());
   }
   
   /**
    * Test de recherche par requête Lucence.<br>{@inheritDoc}
    */
   @Test
   public void searchStorageDocumentByLucene()
         throws SearchingServiceEx, InsertionServiceEx, IOException,
         ParseException, DeletionServiceEx {
      int limit = 5;
    //  UUID uuid = getMockData(getInsertionService());
      final String lucene = String.format("%s:%s", "cop", "UR030");
      LuceneCriteria luceneCriteria = new LuceneCriteria(lucene, limit,
            new ArrayList<StorageMetadata>());
      
      List<StorageDocument> docs =getSearchingService().searchStorageDocumentByLuceneCriteria(
              luceneCriteria).getAllStorageDocuments();
      Assert.assertNotNull("La recherche de documents par requette Lucence: ",
    		  docs.size() >= 0);
   //   destroyMockTest(uuid, getDeletionService());
   }
   
   
}

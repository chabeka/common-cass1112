package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.docubase.dfce.exception.ExceededSearchLimitException;
import com.docubase.dfce.exception.SearchQueryParseException;

import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.sae.storage.dfce.data.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.data.model.DesiredMetaData;
import fr.urssaf.image.sae.storage.dfce.data.utils.CheckDataUtils;
import fr.urssaf.image.sae.storage.dfce.mapping.DocumentForTestMapper;
import fr.urssaf.image.sae.storage.dfce.services.CommonsServices;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionIdGedExistantEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import junit.framework.Assert;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.search.SearchQuery;
import net.docubase.toolkit.model.search.SearchResult;

/**
 * Classe de test des services de recherche.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-storage-dfce-test.xml" })
public class SearchingServiceTest {

   @Autowired
   private CommonsServices commonsServices;

   /**
    * Permet de récupérer un document à partir du critère « UUIDCriteria ».<br>
    * {@inheritDoc}
    *
    * @throws ConnectionServiceEx
    * @throws InsertionIdGedExistantEx
    */
   @Test
   public void searchDocumentByUUID() throws SearchingServiceEx,
   InsertionServiceEx, IOException, ParseException, DeletionServiceEx,
   ConnectionServiceEx, InsertionIdGedExistantEx {
      final StorageDocument document = commonsServices.getMockData(commonsServices
                                                                   .getInsertionService());
      final UUIDCriteria uuidCriteria = new UUIDCriteria(document.getUuid(),
                                                         new ArrayList<StorageMetadata>());


      final StorageDocument storeDoc = commonsServices.getSearchingService()
            .searchStorageDocumentByUUIDCriteria(uuidCriteria, false);

      Assert.assertNotNull("Recupération d'un document par UUID :", storeDoc.getUuid());
      commonsServices.destroyMockTest(document.getUuid(), commonsServices
                                      .getDeletionService());
   }

   /**
    * Permet de récupérer un document à partir du critère « UUIDCriteria ».<br>
    * {@inheritDoc}
    *
    * @throws ConnectionServiceEx
    */
   @Test
   public void searchDocumentByUUIDWithDesiredMetaData()
         throws SearchingServiceEx, InsertionServiceEx, IOException,
         ParseException, DeletionServiceEx, ConnectionServiceEx {

      final DFCEServices dfceServices = commonsServices.getDfceServices();

      try {
         final SearchQuery query = ToolkitFactory.getInstance().createMultibaseQuery(
               "SM_FILE_UUID:2f52d1d4-297f-4ca5-8a85-7ca13d283dda");
         final SearchResult search = dfceServices.search(query);

         //         for (Document doc : search.getDocuments()) {
         //            System.out.println(doc.getUuid());
         //         }

      } catch (final ExceededSearchLimitException exception) {
         // TODO Auto-generated catch block
         exception.printStackTrace();
      } catch (final SearchQueryParseException exception) {
         // TODO Auto-generated catch block
         exception.printStackTrace();
      }

      // // Initialisation des jeux de données UUID
      // StorageDocument document = getMockData(getInsertionService());
      // // Initialisation des jeux de données Metadata
      // final DesiredMetaData metaDataFromXml = getXmlDataService()
      // .desiredMetaDataReader(
      // new File(Constants.XML_FILE_DESIRED_MDATA[0]));
      // List<StorageMetadata> desiredMetadatas = DocumentForTestMapper
      // .saeMetaDataXmlToStorageMetaData(metaDataFromXml)
      // .getMetadatas();
      //
      // UUIDCriteria uuidCriteria = new UUIDCriteria(document.getUuid(),
      // desiredMetadatas);
      // Assert.assertNotNull(
      // "Le resultat de recherche :",
      // getSearchingService().searchStorageDocumentByUUIDCriteria(
      // uuidCriteria).getUuid());
      // Assert.assertTrue(
      // "Les deux listes des métaData doivent être identique : ",
      // CheckDataUtils.checkDesiredMetaDatas(
      // desiredMetadatas,
      // getSearchingService().searchMetaDatasByUUIDCriteria(
      // uuidCriteria).getMetadatas()));
      // // Suppression du document insert
      // destroyMockTest(document.getUuid(), getDeletionService());
   }

   /**
    * Récupérer les métadonnées par UUID. <br>{@inheritDoc}
    *
    * @throws ConnectionServiceEx
    * @throws InsertionIdGedExistantEx
    */
   @Test
   public void searchMetaDatasByUUID() throws SearchingServiceEx,
   InsertionServiceEx, IOException, ParseException, DeletionServiceEx,
   ConnectionServiceEx, InsertionIdGedExistantEx {
      // Initialisation des jeux de données UUID
      final StorageDocument document = commonsServices.getMockData(commonsServices
                                                                   .getInsertionService());
      final UUIDCriteria uuidCriteria = new UUIDCriteria(document.getUuid(),
                                                         new ArrayList<StorageMetadata>());
      Assert.assertNotNull(
                           "Recupération d'une liste de métadonnées par uuid :",
                           commonsServices.getSearchingService()
                           .searchMetaDatasByUUIDCriteria(uuidCriteria).getMetadatas());
      // Suppression du document insert
      commonsServices.destroyMockTest(document.getUuid(), commonsServices
                                      .getDeletionService());
   }

   /**
    * Récupérer les métadonnées par UUID et récupération qu'une liste de
    * métadonnées spécifique. <br>{@inheritDoc}
    *
    * @throws ConnectionServiceEx
    * @throws InsertionIdGedExistantEx
    */
   @Test
   public void searchMetaDatasByUUIDWithDesiredMetaData()
         throws SearchingServiceEx, IOException, ParseException,
         InsertionServiceEx, DeletionServiceEx, ConnectionServiceEx, InsertionIdGedExistantEx {
      // Initialisation des jeux de données UUID
      final StorageDocument document = commonsServices.getMockData(commonsServices
                                                                   .getInsertionService());
      // Initialisation des jeux de données Metadata
      final DesiredMetaData desiredMetaData = commonsServices
            .getXmlDataService().desiredMetaDataReader(
                                                       new File(Constants.XML_FILE_DESIRED_MDATA[0]));
      final List<StorageMetadata> desiredMetadatas = DocumentForTestMapper
            .saeMetaDataXmlToStorageMetaData(desiredMetaData).getMetadatas();
      final UUIDCriteria uuidCriteria = new UUIDCriteria(document.getUuid(),
                                                         desiredMetadatas);
      Assert.assertNotNull("Le resultat de recherche :", commonsServices
                           .getSearchingService().searchMetaDatasByUUIDCriteria(uuidCriteria)
                           .getMetadatas());
      Assert.assertTrue("Les deux listes des métaData doivent être identique",
                        CheckDataUtils.checkDesiredMetaDatas(desiredMetadatas,
                                                             commonsServices.getSearchingService()
                                                             .searchMetaDatasByUUIDCriteria(uuidCriteria)
                                                             .getMetadatas()));
      // Suppression du document insert
      commonsServices.destroyMockTest(document.getUuid(), commonsServices
                                      .getDeletionService());
   }

}

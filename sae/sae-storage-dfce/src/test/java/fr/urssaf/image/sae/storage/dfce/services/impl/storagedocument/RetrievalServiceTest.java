package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.storage.dfce.data.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.data.model.SaeDocument;
import fr.urssaf.image.sae.storage.dfce.data.utils.CheckDataUtils;
import fr.urssaf.image.sae.storage.dfce.mapping.DocumentForTestMapper;
import fr.urssaf.image.sae.storage.dfce.services.StorageServices;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;

/**
 * Classe de test pour les services de consultation.
 * 
 * @author Rhofir, Kenore.
 * 
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class RetrievalServiceTest extends StorageServices {

   @Autowired
   private CassandraServerBean serverBean;

   /**
    * Permet d'initialiser le service RetrievalService
    * 
    * @throws ConnectionServiceEx
    */
   @Before
   public void init() throws ConnectionServiceEx {
      getDfceServicesManager().getConnection();
      getRetrievalService().setRetrievalServiceParameter(
            getDfceServicesManager().getDFCEService());

   }

   @After
   public void end() throws Exception {
      serverBean.resetData();
   }

   /**
    * Test de consultation par UUID
    * 
    * <br>{@inheritDoc}
    * 
    * @throws ConnectionServiceEx
    */
   @Test
   public void retrieveStorageDocumentByUUID() throws RetrievalServiceEx,
         InsertionServiceEx, IOException, ParseException, DeletionServiceEx,
         ConnectionServiceEx {
      getDfceServicesManager().getConnection();
      getInsertionService().setInsertionServiceParameter(
            getDfceServicesManager().getDFCEService());
      getRetrievalService().setRetrievalServiceParameter(
            getDfceServicesManager().getDFCEService());
      getDeletionService().setDeletionServiceParameter(
            getDfceServicesManager().getDFCEService());
      // Initialisation des jeux de données UUID
      final StorageDocument document = getMockData(getInsertionService());
      final UUIDCriteria uuidCriteria = new UUIDCriteria(document.getUuid(),
            new ArrayList<StorageMetadata>());
      Assert.assertNotNull("Récupération d'un StorageDocument par uuid :",
            getRetrievalService().retrieveStorageDocumentByUUID(uuidCriteria));
      // Suppression du document insert
      destroyMockTest(document.getUuid(), getDeletionService());
   }

   /**
    * Test de récupération du contenue par UUID.
    * 
    * 
    * @throws ConnectionServiceEx
    */
   @Test
   public void retrieveStorageDocumentContentByUUID()
         throws RetrievalServiceEx, IOException, InsertionServiceEx,
         ParseException, DeletionServiceEx, NoSuchAlgorithmException,
         ConnectionServiceEx {
      // Injection de jeu de donnée.
      final SaeDocument saeDocument = getXmlDataService().saeDocumentReader(
            new File(Constants.XML_PATH_DOC_WITHOUT_ERROR[1]));
      final StorageDocument storageDocument = DocumentForTestMapper
            .saeDocumentXmlToStorageDocument(saeDocument);
      final StorageDocument document = getInsertionService()
            .insertStorageDocument(storageDocument);

      final UUIDCriteria uuidCriteria = new UUIDCriteria(document.getUuid(),
            new ArrayList<StorageMetadata>());
      final byte[] content = getRetrievalService()
            .retrieveStorageDocumentContentByUUID(uuidCriteria);
      Assert.assertNotNull(
            "Le contenue du document récupérer doit être non null", content);
      destroyMockTest(document.getUuid(), getDeletionService());
   }

   /**
    * Test de récupération des Métadonnées par UUID.
    * 
    * <br>{@inheritDoc}
    * 
    * @throws ConnectionServiceEx
    */
   @Test
   public void retrieveStorageDocumentMetaDatasByUUID()
         throws RetrievalServiceEx, DeletionServiceEx, InsertionServiceEx,
         IOException, ParseException, ConnectionServiceEx {
      // Injection de jeu de donnée.
      final SaeDocument saeDocument = getXmlDataService().saeDocumentReader(
            new File(Constants.XML_PATH_DOC_WITHOUT_ERROR[1]));
      final StorageDocument storageDocument = DocumentForTestMapper
            .saeDocumentXmlToStorageDocument(saeDocument);
      final StorageDocument document = getInsertionService()
            .insertStorageDocument(storageDocument);
      final UUIDCriteria uuidCriteria = new UUIDCriteria(document.getUuid(),
            new ArrayList<StorageMetadata>());
      final List<StorageMetadata> storageMetadatas = getRetrievalService()
            .retrieveStorageDocumentMetaDatasByUUID(uuidCriteria);

      Assert.assertNotNull(
            "La liste des Métadonnées récupérer doit être non null : ",
            storageMetadatas);
      // Vérification que les deux liste des métadonnées sont identique du
      // document initial et document récupérer
      Assert.assertTrue(
            "Les deux listes des métaData doivent être identique : ",
            CheckDataUtils.checkMetaDatas(storageDocument.getMetadatas(),
                  storageMetadatas));
      // Suppression du document insert
      destroyMockTest(document.getUuid(), getDeletionService());
   }
}

package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.storage.dfce.services.CommonsServices;
import fr.urssaf.image.sae.storage.dfce.utils.TraceAssertUtils;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionIdGedExistantEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.RecycleBinServiceEx;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/**
 * Classe de test du service
 * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.RecycleBinServiceImpl
 * RecycleBinService}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-storage-dfce-test.xml" })
public class RecycleBinServiceTest {

   @Autowired
   private CommonsServices commonsServices;

   @Before
   public void init() throws ConnectionServiceEx {
      commonsServices.initServicesParameters();
   }

   @After
   public void end() {
      commonsServices.closeServicesParameters();
   }

   @Autowired
   private TraceAssertUtils traceAssertUtils;

   @Autowired
   private CassandraServerBean cassandraServerBean;

   @Before
   public void before() throws Exception {

      cassandraServerBean.resetData();

      // Initialisation des droits

      VIContenuExtrait viExtrait = new VIContenuExtrait();
      viExtrait.setCodeAppli("TESTS_UNITAIRES");
      viExtrait.setIdUtilisateur("UTILISATEUR TEST");
      viExtrait.setPagms(Arrays.asList("TU_PAGM1", "TU_PAGM2"));

      SaeDroits saeDroits = new SaeDroits();
      List<SaePrmd> saePrmds = new ArrayList<SaePrmd>();
      SaePrmd saePrmd = new SaePrmd();
      saePrmd.setValues(new HashMap<String, String>());
      Prmd prmd = new Prmd();
      prmd.setBean("permitAll");
      prmd.setCode("default");
      saePrmd.setPrmd(prmd);
      String[] roles = new String[] { "archivage_unitaire" };
      saePrmds.add(saePrmd);
      saeDroits.put("archivage_unitaire", saePrmds);

      viExtrait.setSaeDroits(saeDroits);

      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);

   }

   @After
   public void after() throws Exception {

      AuthenticationContext.setAuthenticationToken(null);

      cassandraServerBean.resetData();

   }

   /**
    * Test du service :
    * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.RecycleBinServiceImpl#moveStorageDocumentToRecycleBin(java.util.UUID)
    * moveStorageDocumentToRecycleBin}
    * 
    * @throws ConnectionServiceEx
    *            ConnectionServiceEx Exception lévée lorsque la connexion
    *            n'aboutie pas.
    * @throws RetrievalServiceEx
    * @throws InsertionIdGedExistantEx 
    */
   @Test
   public void moveStorageDocumentToRecycleBin() throws InsertionServiceEx, IOException,
         ParseException, RetrievalServiceEx, RecycleBinServiceEx, InsertionIdGedExistantEx {

      // Initialisation des jeux de données UUID
      final StorageDocument storageDoc = commonsServices
            .getMockData(commonsServices.getInsertionService());
      final UUIDCriteria uuidCriteria = new UUIDCriteria(storageDoc.getUuid(),
            new ArrayList<StorageMetadata>());
      try {
         commonsServices.getRecycleBinService().moveStorageDocumentToRecycleBin(
               uuidCriteria.getUuid());
      } catch (RecycleBinServiceEx e) {
         Assert.assertTrue("La mise a la corbeille a échoué " + e.getMessage(), true);
      }
      Assert.assertNull(commonsServices.getRetrievalService()
            .retrieveStorageDocumentByUUID(uuidCriteria));

      // Vérifie la traçabilité
      traceAssertUtils.verifieAucuneTraceDansRegistres();
      traceAssertUtils.verifieTraceDepotEtMiseEnCorbeilleDfceDansJournalSae(
            storageDoc.getUuid(), "a2f93f1f121ebba0faef2c0596f2f126eacae77b",
            "SHA-1");

      // supprime le doc
      commonsServices.destroyMockForRecycleBinTest(storageDoc.getUuid(), commonsServices.getRecycleBinService()); 
   }

   /**
    * Test du service :
    * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.RecycleBinServiceImpl#restoreStorageDocumentFromRecycleBin(java.util.UUID)
    * restoreStorageDocumentFromRecycleBin}
    * 
    * @throws ConnectionServiceEx
    *            ConnectionServiceEx Exception lévée lorsque la connexion
    *            n'aboutie pas.
    * @throws RetrievalServiceEx
    * @throws InsertionIdGedExistantEx 
    */
   @Test
   public void restoreStorageDocumentFromRecycleBin() throws InsertionServiceEx, IOException,
         ParseException, RetrievalServiceEx, DeletionServiceEx, RecycleBinServiceEx, InsertionIdGedExistantEx {

      // Initialisation des jeux de données UUID
      final StorageDocument storageDoc = commonsServices
            .getMockDataForRecycleBin(commonsServices.getInsertionService(),
                  commonsServices.getRecycleBinService());
      final UUIDCriteria uuidCriteria = new UUIDCriteria(storageDoc.getUuid(),
            new ArrayList<StorageMetadata>());
      try {
         commonsServices.getRecycleBinService().restoreStorageDocumentFromRecycleBin(
               uuidCriteria.getUuid());
      } catch (RecycleBinServiceEx e) {
         Assert.assertTrue("La restore de la corbeille a échoué " + e.getMessage(), true);
      }
      Assert.assertNotNull(commonsServices.getRetrievalService()
            .retrieveStorageDocumentByUUID(uuidCriteria));

      // Vérifie la traçabilité
      traceAssertUtils.verifieAucuneTraceDansRegistres();
      traceAssertUtils.verifieTraceDepotEtRestoreCorbeilleDfceDansJournalSae(
            storageDoc.getUuid(), "a2f93f1f121ebba0faef2c0596f2f126eacae77b",
            "SHA-1");

      // supprime le doc
      commonsServices.destroyMockTest(storageDoc.getUuid(), commonsServices.getDeletionService());
   }
   
   /**
    * Test du service :
    * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.RecycleBinServiceImpl#deleteStorageDocumentFromRecycleBin(java.util.UUID)
    * deleteStorageDocumentFromRecycleBin}
    * 
    * @throws ConnectionServiceEx
    *            ConnectionServiceEx Exception lévée lorsque la connexion
    *            n'aboutie pas.
    * @throws RetrievalServiceEx
    * @throws InsertionIdGedExistantEx 
    */
   @Test
   public void deleteStorageDocumentFromRecycleBin() throws InsertionServiceEx, IOException,
         ParseException, RetrievalServiceEx, RecycleBinServiceEx, InsertionIdGedExistantEx {

      // Initialisation des jeux de données UUID
      final StorageDocument storageDoc = commonsServices
            .getMockDataForRecycleBin(commonsServices.getInsertionService(),
                  commonsServices.getRecycleBinService());
      final UUIDCriteria uuidCriteria = new UUIDCriteria(storageDoc.getUuid(),
            new ArrayList<StorageMetadata>());
      try {
         commonsServices.getRecycleBinService().deleteStorageDocumentFromRecycleBin(
               uuidCriteria.getUuid());
      } catch (RecycleBinServiceEx e) {
         Assert.assertTrue("La mise a la corbeille a échoué " + e.getMessage(), true);
      }
      Assert.assertNull(commonsServices.getRetrievalService()
            .retrieveStorageDocumentByUUID(uuidCriteria));

      // Vérifie la traçabilité
      traceAssertUtils.verifieAucuneTraceDansRegistres();
      traceAssertUtils.verifieTraceDepotEtMiseEnCorbeilleEtSuppressionDfceDansJournalSae(
            storageDoc.getUuid(), "a2f93f1f121ebba0faef2c0596f2f126eacae77b",
            "SHA-1");

   }
}

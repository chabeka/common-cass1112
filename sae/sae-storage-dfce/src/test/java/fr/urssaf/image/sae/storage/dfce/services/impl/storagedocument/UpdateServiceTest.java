package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.storage.dfce.data.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.data.model.SaeDocument;
import fr.urssaf.image.sae.storage.dfce.mapping.DocumentForTestMapper;
import fr.urssaf.image.sae.storage.dfce.services.StorageServices;
import fr.urssaf.image.sae.storage.dfce.utils.TraceAssertUtils;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.exception.UpdateServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/**
 * Classe de test du service
 * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.InsertionServiceImpl
 * InsertionService}
 * 
 */
public class UpdateServiceTest extends StorageServices {

   @Autowired
   private TraceAssertUtils traceAssertUtils;

   @Autowired
   private CassandraServerBean cassandraServerBean;

   @Before
   public void before() {

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
            viExtrait.getIdUtilisateur(), viExtrait, roles, viExtrait
                  .getSaeDroits());
      AuthenticationContext.setAuthenticationToken(token);

      try {
         cassandraServerBean.resetData();
      } catch (Exception exception) {
         // rien à faire
      }
   }

   @After
   public void after() throws Exception {

      AuthenticationContext.setAuthenticationToken(null);

      cassandraServerBean.resetData();

   }

   /**
    * Test du service :
    * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.InsertionServiceImpl#insertStorageDocument(StorageDocument)
    * insertStorageDocument} <br>
    * Insérer deux fois le même document et vérifier que les UUIDs sont
    * différents.
    * 
    * @throws ConnectionServiceEx
    * @throws UpdateServiceEx
    * @throws SearchingServiceEx
    */
   @Test
   public void modifDocument() throws IOException, ParseException,
         InsertionServiceEx, ConnectionServiceEx, UpdateServiceEx,
         SearchingServiceEx {
      getDfceServicesManager().getConnection();
      getInsertionService().setInsertionServiceParameter(
            getDfceServicesManager().getDFCEService());
      final SaeDocument saeDocument = getXmlDataService().saeDocumentReader(
            new File(Constants.XML_PATH_DOC_WITHOUT_ERROR[0]));
      final StorageDocument storageDocument = DocumentForTestMapper
            .saeDocumentXmlToStorageDocument(saeDocument);
      final StorageDocument firstDocument = getInsertionService()
            .insertStorageDocument(storageDocument);

      Assert
            .assertNotNull("l'uuid doit etre non null", firstDocument.getUuid());

      List<StorageMetadata> delMetas = Arrays.asList(new StorageMetadata("itm",
            null));
      List<StorageMetadata> modifMetas = Arrays.asList(new StorageMetadata(
            "apr", "SAE"));

      getUpdateService().updateStorageDocument(firstDocument.getUuid(),
            modifMetas, delMetas);

      StorageDocument storedDoc = getSearchingService()
            .searchStorageDocumentByUUIDCriteria(
                  new UUIDCriteria(firstDocument.getUuid(), Arrays.asList(
                        new StorageMetadata("itm"), new StorageMetadata("apr"))));
      List<StorageMetadata> metadatas = storedDoc.getMetadatas();
      Assert.assertEquals("il doit y avoir deux métadonnées", 2, metadatas
            .size());
      StorageMetadata metadata = metadatas.get(0);
      checkMetadata(metadata);
      metadata = metadatas.get(1);
      checkMetadata(metadata);

      traceAssertUtils.verifieTraceModifDfceDansJournalSae(firstDocument
            .getUuid(), Arrays.asList("apr"), Arrays.asList("itm"));
   }

   private void checkMetadata(StorageMetadata metadata) {

      if ("apr".equals(metadata.getShortCode())) {
         Assert.assertEquals("la valeur de la métadonnée doit etre SAE", "SAE",
               metadata.getValue());
      } else if ("itm".equals(metadata.getShortCode())) {
         Assert.assertTrue("la valeur doit etre vide", StringUtils
               .isEmpty((String) metadata.getValue()));
      } else {
         Assert.fail("métadonnée non attendue");
      }

   }

}
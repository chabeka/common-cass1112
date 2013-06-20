package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.administration.BaseAdministrationService;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.storage.dfce.data.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.data.model.SaeDocument;
import fr.urssaf.image.sae.storage.dfce.mapping.DocumentForTestMapper;
import fr.urssaf.image.sae.storage.dfce.services.CommonsServices;
import fr.urssaf.image.sae.storage.dfce.utils.TraceAssertUtils;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.services.storagedocument.InsertionService;
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
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-storage-dfce-mock-test.xml" })
public class InsertionServiceTestCategoryException extends CommonsServices {

   @Autowired
   private TraceAssertUtils traceAssertUtils;

   @Autowired
   private CassandraServerBean cassandraServerBean;

   @Autowired
   @Qualifier("insertionService")
   private InsertionService insertionService;

   // Mocks
   @Autowired
   private Base base;
   @Autowired
   private BaseAdministrationService baseAdminService;
   @Autowired
   private ServiceProvider serviceProvider;

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

   }

   @After
   public void after() throws Exception {

      AuthenticationContext.setAuthenticationToken(null);

      cassandraServerBean.resetData();

      EasyMock.reset(base);
      EasyMock.reset(baseAdminService);
      EasyMock.reset(serviceProvider);

   }

   /**
    * Test du service :
    * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.InsertionServiceImpl#insertStorageDocument(StorageDocument)
    * insertStorageDocument} <br>
    * Insérer deux fois le même document et vérifier que les UUIDs sont
    * différents.
    * 
    * @throws ConnectionServiceEx
    *            Exception lévée lorsque la connexion n'aboutie pas.
    */
   @Test
   public void insertOneDocument() throws IOException, ParseException,
         ConnectionServiceEx {

      // Exception lors de la récupération de la version
      // EasyMock.expect(base.getBaseCategory("nouvelleMeta")).andReturn(null);

      EasyMock.expect(serviceProvider.getBaseAdministrationService())
            .andReturn(baseAdminService).anyTimes();
      EasyMock.replay(serviceProvider);

      EasyMock.expect(
            baseAdminService.getBase(EasyMock.anyObject(String.class)))
            .andReturn(base).anyTimes();
      EasyMock.replay(baseAdminService);

      EasyMock.expect(base.getBaseId()).andReturn("").anyTimes();
      UUID uuid = UUID.fromString("680F6020-31BC-41CE-9816-08217448C143");
      EasyMock.expect(base.getUuid()).andReturn(uuid).anyTimes();
      EasyMock.expect(base.getBaseCategory(EasyMock.anyObject(String.class)))
            .andReturn(null).anyTimes();
      EasyMock.replay(base);

      final SaeDocument saeDocument = getXmlDataService().saeDocumentReader(
            new File(Constants.XML_PATH_DOC_WITHOUT_ERROR[0]));

      final StorageDocument storageDocument = DocumentForTestMapper
            .saeDocumentXmlToStorageDocument(saeDocument);

      getDfceServicesManager().getConnection();

      insertionService.setInsertionServiceParameter(serviceProvider);

      try {
         insertionService.insertStorageDocument(storageDocument);
         Assert
               .fail("Une exception de type InsertionServiceEx doit être levée");
      } catch (Exception e) {
         Assert.assertEquals("Type d'exception incorrect",
               InsertionServiceEx.class, e.getClass());
      }
   }

}
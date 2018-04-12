package fr.urssaf.image.sae.batch.documents.executable.service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.document.impl.DocumentImpl;
import net.docubase.toolkit.model.search.SearchQuery;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.administration.BaseAdministrationService;
import net.docubase.toolkit.service.ged.SearchService;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.dfce.manager.DFCEConnectionFactory;
import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;
import fr.urssaf.image.sae.batch.documents.executable.bootstrap.ExecutableMain;
import fr.urssaf.image.sae.batch.documents.executable.model.ConfigurationEnvironnement;
import fr.urssaf.image.sae.batch.documents.executable.model.ConfigurationsEnvironnement;
import fr.urssaf.image.sae.batch.documents.executable.service.impl.ConfigurationServiceImpl;
import fr.urssaf.image.sae.batch.documents.executable.service.impl.DfceServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-test.xml" })
public class DfceServiceTest {
   
   private static DFCEConnection dfceConnection;
   
   @BeforeClass
   public static void init() throws IOException {
      ConfigurationServiceImpl configSce;
      configSce = new ConfigurationServiceImpl();
      File fichierConfEnv = new File("src/test/resources/environnements-test.xml");
      //FileInputStream confEnvInput = new FileInputStream(fichierConfEnv);
      
      //-- Liste liste des envirennements
      
      Properties dfceConfigProp;
      ConfigurationsEnvironnement envList;  
      
      ConfigurationEnvironnement destConfigEnv;
      envList = configSce.chargerConfiguration(fichierConfEnv);
      destConfigEnv = envList.getConfiguration("ENV_DEVELOPPEMENT");
      
      dfceConfigProp = ExecutableMain.getDfceConfiguration(destConfigEnv);
      
      dfceConnection = DFCEConnectionFactory
         .createDFCEConnectionByDFCEConfiguration(dfceConfigProp);
   }

   @Test
   public void ouvrirConnexion() {
      DfceService dfceService = new DfceServiceImpl(dfceConnection);
      // creation du mock
      DFCEConnectionService connectionService = EasyMock
            .createNiceMock(DFCEConnectionService.class);
      EasyMock.expect(connectionService.openConnection()).andReturn(
            ServiceProvider.newServiceProvider());
      EasyMock.replay(connectionService);
      ((DfceServiceImpl) dfceService)
            .setDfceConnectionService(connectionService);

      dfceService.ouvrirConnexion();
   }

   @Test
   public void fermerConnexion() {
      DfceService dfceService = new DfceServiceImpl(dfceConnection);
      // creation du mock
      ServiceProvider serviceProvider = EasyMock
            .createNiceMock(ServiceProvider.class);
      EasyMock.replay(serviceProvider);
      ((DfceServiceImpl) dfceService).setServiceProvider(serviceProvider);

      dfceService.fermerConnexion();
   }

   @Test
   public void executerRequete() throws Exception {
      String requeteLucene = "srt:41882050200023";
      String baseName = "TEST";
      DfceService dfceService = new DfceServiceImpl(dfceConnection);
      DFCEConnection dfceConnection = new DFCEConnection();
      dfceConnection.setBaseName(baseName);
      List<Document> listeDoc = new ArrayList<Document>();
      listeDoc.add(new DocumentImpl());
      // creation des mocks
      SearchService searchService = EasyMock
            .createNiceMock(SearchService.class);
      BaseAdministrationService baseAdministrationService = EasyMock
            .createNiceMock(BaseAdministrationService.class);
      Base base = EasyMock.createNiceMock(Base.class);
      ServiceProvider serviceProvider = EasyMock
            .createNiceMock(ServiceProvider.class);
      ToolkitFactory toolkit = EasyMock.createNiceMock(ToolkitFactory.class);
      SearchQuery searchQuery = EasyMock.createNiceMock(SearchQuery.class);
      EasyMock.expect(serviceProvider.getSearchService()).andReturn(
            searchService);
      EasyMock.expect(serviceProvider.getBaseAdministrationService())
            .andReturn(baseAdministrationService);
      EasyMock.expect(baseAdministrationService.getBase(baseName)).andReturn(
            base);

      setToolkit(toolkit);
      EasyMock.expect(toolkit.createMonobaseQuery(requeteLucene, base))
            .andReturn(searchQuery).anyTimes();
      EasyMock.expect(searchService.createDocumentIterator(searchQuery))
            .andReturn(listeDoc.iterator());
      EasyMock.replay(searchService);
      EasyMock.replay(baseAdministrationService);
      EasyMock.replay(base);
      EasyMock.replay(serviceProvider);
      EasyMock.replay(toolkit);
      ((DfceServiceImpl) dfceService).setDfceConnection(dfceConnection);
      ((DfceServiceImpl) dfceService).setServiceProvider(serviceProvider);

      Iterator<Document> resultat = dfceService.executerRequete(requeteLucene);

      Assert.assertNotNull(
            "La liste des documents retrouvés ne doit pas être null", resultat);
      Assert.assertTrue(
            "La liste des documents retrouvés ne contient pas d'éléments",
            resultat.hasNext());
      Assert.assertNotNull("Le document retrouvé ne doit pas être null",
            resultat.next());
   }

   private void setToolkit(ToolkitFactory toolkit) throws Exception {
      Field field = ToolkitFactory.class.getDeclaredField("toolkitFactory");
      field.setAccessible(true);
      field.set(null, toolkit);
   }
}

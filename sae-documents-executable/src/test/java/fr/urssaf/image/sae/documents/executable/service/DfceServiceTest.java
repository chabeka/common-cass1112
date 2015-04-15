package fr.urssaf.image.sae.documents.executable.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.document.impl.DocumentImpl;
import net.docubase.toolkit.model.search.SearchQuery;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.administration.BaseAdministrationService;
import net.docubase.toolkit.service.ged.SearchService;
import net.docubase.toolkit.service.ged.StoreService;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;
import fr.urssaf.image.sae.documents.executable.service.impl.DfceServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-documents-executable-test.xml" })
public class DfceServiceTest {

   private final File file = new File(
         "src/test/resources/identification/PdfaValide.pdf");

   @Test
   public void ouvrirConnexion() {
      DfceService dfceService = new DfceServiceImpl();
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
      DfceService dfceService = new DfceServiceImpl();
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
      DfceService dfceService = new DfceServiceImpl();
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

   @Test
   public void recupererContenu() throws FileNotFoundException {
      DfceService dfceService = new DfceServiceImpl();
      Document document = new DocumentImpl();
      document.setUuid(UUID.fromString("00000000-0000-0000-0000-000000000000"));
      FileInputStream fileStream = new FileInputStream(file);
      // creation des mocks
      StoreService storeService = EasyMock.createNiceMock(StoreService.class);
      ServiceProvider serviceProvider = EasyMock
            .createNiceMock(ServiceProvider.class);
      EasyMock.expect(serviceProvider.getStoreService())
            .andReturn(storeService);
      EasyMock.expect(storeService.getDocumentFile(document)).andReturn(
            fileStream);
      EasyMock.replay(storeService);
      EasyMock.replay(serviceProvider);
      ((DfceServiceImpl) dfceService).setServiceProvider(serviceProvider);

      InputStream stream = dfceService.recupererContenu(document);
      Assert.assertNotNull(
            "Le contenu du fichier récupéré ne doit pas être null", stream);

      try {
         stream.close();
      } catch (IOException e) {
         Assert.fail(e.getMessage());
      }
   }
}

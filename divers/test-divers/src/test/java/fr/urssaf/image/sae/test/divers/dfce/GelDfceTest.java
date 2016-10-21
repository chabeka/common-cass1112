package fr.urssaf.image.sae.test.divers.dfce;

import java.util.UUID;

import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.ged.SearchService;
import net.docubase.toolkit.service.ged.StoreService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-dev.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-interne.xml" })
public class GelDfceTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(GelDfceTest.class);
   
   /**
    * Service permettant de réaliser la connexion à DFCE.
    */
   @Autowired
   private DFCEConnectionService dfceConnectionService;
   
   @Test
   public void isFrozen() {
      UUID idDoc = UUID.fromString("af532e49-12fc-47a0-86bf-01c9c330bf50");
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      final SearchService searchService = serviceProvider.getSearchService();
      final Base base = serviceProvider.getBaseAdministrationService().getBase(
            "SAE-TEST");
      
      LOGGER.debug("Recuperation du document : {}", idDoc.toString());
      final Document doc = searchService.getDocumentByUUID(base, idDoc);
      
      final StoreService storeService = serviceProvider.getStoreService();
      LOGGER.debug("Document gele : {}", storeService.isFrozen(doc));
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   public void freezeDocument() {
      UUID idDoc = UUID.fromString("af532e49-12fc-47a0-86bf-01c9c330bf50");
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      final SearchService searchService = serviceProvider.getSearchService();
      final Base base = serviceProvider.getBaseAdministrationService().getBase(
            "SAE-TEST");
      
      LOGGER.debug("Recuperation du document : {}", idDoc.toString());
      final Document doc = searchService.getDocumentByUUID(base, idDoc);
      
      final StoreService storeService = serviceProvider.getStoreService();
      LOGGER.debug("Gel le document : {}", idDoc.toString());
      storeService.freezeDocument(doc);
      LOGGER.debug("Document gele : {}", storeService.isFrozen(doc));
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();  
   }
   
   @Test
   public void unfreezeDocument() {
      UUID idDoc = UUID.fromString("9492ae8b-6083-4c62-8257-083b1f712166");
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      final SearchService searchService = serviceProvider.getSearchService();
      final Base base = serviceProvider.getBaseAdministrationService().getBase(
            "SAE-TEST");
      
      LOGGER.debug("Recuperation du document : {}", idDoc.toString());
      final Document doc = searchService.getDocumentByUUID(base, idDoc);
      
      final StoreService storeService = serviceProvider.getStoreService();
      LOGGER.debug("Dégel le document : {}", idDoc.toString());
      storeService.unfreezeDocument(doc);
      LOGGER.debug("Document gele : {}", storeService.isFrozen(doc));
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();  
   }
}

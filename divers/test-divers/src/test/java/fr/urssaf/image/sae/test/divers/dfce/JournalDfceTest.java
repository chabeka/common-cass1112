package fr.urssaf.image.sae.test.divers.dfce;

import java.util.UUID;

import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.ged.SearchService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-dev.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-interne.xml" })
@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-pre-prod.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-prod.xml" })
public class JournalDfceTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(JournalDfceTest.class);
   
   /**
    * Service permettant de réaliser la connexion à DFCE.
    */
   @Autowired
   private DFCEConnectionService dfceConnectionService;
   
   @Test
   public void getFileSize() {
      UUID idDoc = UUID.fromString("5a945ca5-0f71-448b-be90-1ceafbc32e4e");
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      final SearchService searchService = serviceProvider
         .getSearchService();
      final Base base = serviceProvider.getBaseAdministrationService()
         .getBase("DAILY_LOG_ARCHIVE_BASE");
      
      LOGGER.debug("Recuperation du document : {}", idDoc.toString());
      final Document doc = searchService.getDocumentByUUID(base, idDoc);
      
      long tailleFichier = doc.getSize();
      long tailleKo = tailleFichier / 1024;
      long tailleMo = tailleKo / 1024;
      long tailleGo = tailleMo / 1024;
      LOGGER.debug("Taille du document : {} octets", tailleFichier);
      if (tailleKo > 0) {
         LOGGER.debug("Taille du document : {} ko", tailleKo);
      }
      if (tailleMo > 0) {
         LOGGER.debug("Taille du document : {} Mo", tailleMo);
      }
      if (tailleGo > 0) {
         LOGGER.debug("Taille du document : {} Go", tailleGo);
      }
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   
}

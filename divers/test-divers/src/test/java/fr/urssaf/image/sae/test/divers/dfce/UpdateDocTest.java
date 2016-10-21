package fr.urssaf.image.sae.test.divers.dfce;

import java.util.UUID;

import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Criterion;
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

import com.docubase.dfce.exception.FrozenDocumentException;
import com.docubase.dfce.exception.TagControlException;

import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-dev.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-interne.xml" })
@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-cliente-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-giin69-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-pre-prod.xml" })
public class UpdateDocTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(UpdateDocTest.class);
   
   /**
    * Service permettant de réaliser la connexion à DFCE.
    */
   @Autowired
   private DFCEConnectionService dfceConnectionService;
   
   @Test
   public void updateDocumentCesu() throws TagControlException, FrozenDocumentException {
      UUID idDoc = UUID.fromString("AE30F4EA-657A-4E1D-BF90-F4442DB4C2F9");
      // Base de dev
      //String nomBase = "SAE-TEST";
      
      // Base d'integration cliente gns
      String nomBase = "SAE-INT";
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      final SearchService searchService = serviceProvider.getSearchService();
      final Base base = serviceProvider.getBaseAdministrationService().getBase(
            nomBase);
      
      LOGGER.debug("Recuperation du document : {}", idDoc.toString());
      final Document doc = searchService.getDocumentByUUID(base, idDoc);
      
      final StoreService storeService = serviceProvider.getStoreService();
      
      Criterion metaApplicationTraitement = findMeta(doc, "atr");
      LOGGER.debug("Mise a jour du document : {}", idDoc.toString());
      if (metaApplicationTraitement != null) {
         metaApplicationTraitement.setWord("CESU");
      } else {
         BaseCategory category = base.getBaseCategory("atr");
         doc.addCriterion(category, "CESU");
      }
      storeService.updateDocument(doc);
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   private Criterion findMeta(final Document doc, final String nomMeta) {
      Criterion retour = null;
      for (Criterion meta : doc.getAllCriterions()) {
         if (meta.getCategoryName().equals(nomMeta)) {
            retour = meta;
            break;
         }
      }
      return retour;
   }
}

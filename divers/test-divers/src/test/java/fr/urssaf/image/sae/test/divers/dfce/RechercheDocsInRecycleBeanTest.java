package fr.urssaf.image.sae.test.divers.dfce;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Criterion;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchQuery;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.ged.RecycleBinService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.docubase.dfce.exception.SearchQueryParseException;

import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-dev.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-interne.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-interne-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-cliente-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-cliente-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-giin69-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-givn-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-prod.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-pre-prod.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-prod-gnt.xml" })
public class RechercheDocsInRecycleBeanTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(RechercheDocsInRecycleBeanTest.class);
   
   /**
    * Service permettant de réaliser la connexion à DFCE.
    */
   @Autowired
   private DFCEConnectionService dfceConnectionService;
   
   @Autowired
   private DFCEConnection dfceConnection;
   
   @Test
   //@Ignore
   public void rechercheDocParIdSuppression() throws SearchQueryParseException {
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      String requeteLucene = "isi:4bef36a0-dfae-11e5-b56a-f8b156992d8b";
      
      LOGGER.debug("Exécution de la requête lucène : {}", requeteLucene);
      final RecycleBinService recycleBeanService = serviceProvider
            .getRecycleBinService();
      final Base base = serviceProvider.getBaseAdministrationService()
            .getBase(dfceConnection.getBaseName());
      final SearchQuery searchQuery = ToolkitFactory.getInstance()
            .createMonobaseQuery(requeteLucene, base);
      
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
      
      // pas d'iteration
      searchQuery.setSearchLimit(500);
      
      Iterator<Document> iterateur = recycleBeanService.createDocumentIterator(searchQuery);
      long nbDocsTrouve = 0;
      
      // boucle de comptage
      while (iterateur.hasNext()) {
         Document doc = iterateur.next();
         nbDocsTrouve++;
         
         String id = doc.getUuid().toString();
         
         if (!doc.getCriterions("dmc").isEmpty()) {
            Criterion metaDmc = doc.getCriterions("dmc").get(0);
            Date dmc = (Date) metaDmc.getWord();
         
            LOGGER.debug("id: {} -> {}", new String[] { id, formatter.format(dmc)});
         }
      }
      LOGGER.debug("{} docs trouves", new Object[] { nbDocsTrouve});
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }

}

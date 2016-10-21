package fr.urssaf.image.sae.test.divers.dfce;

import java.util.List;

import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.index.IndexInformation;
import net.docubase.toolkit.model.index.RangeIndexInformation;
import net.docubase.toolkit.service.ServiceProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-dev.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-interne.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-pre-prod.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-prod-gnt.xml" })
@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-prod.xml" })
public class RangeManagerTest {

private static final Logger LOGGER = LoggerFactory.getLogger(RangeManagerTest.class);
   
   /**
    * Service permettant de réaliser la connexion à DFCE.
    */
   @Autowired
   private DFCEConnectionService dfceConnectionService;
   
   @Autowired
   private DFCEConnection dfceConnection;
   
   /**
    * Recuperation
    */
   @Autowired
   private CassandraServerBean cassandraServer;
   
   @Test
   public void getIndexByName() {
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      final Base base = serviceProvider.getBaseAdministrationService()
            .getBase(dfceConnection.getBaseName());
      
      String indexName = "SM_MODIFICATION_DATE";
      
      IndexInformation indexInformation = serviceProvider.getIndexAdministrationService().getIndexByNameInBase(indexName, base.getUuid());
      for (RangeIndexInformation rangeInfo : indexInformation.getRangeIndexes()) {
         LOGGER.info("[{} TO {}[ -> {} ({})", new Object[] { rangeInfo.getLowerBound(), rangeInfo.getUpperBound(), rangeInfo.getTotalIndexUseCount(), rangeInfo.getState()});
      }
      
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   @Test
   public void getIndexesLastRangeSize() {
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      int limite = 1000000;
      int alerteLimite = (int)(limite * 0.9);
      
      final Base base = serviceProvider.getBaseAdministrationService()
            .getBase(dfceConnection.getBaseName());
      
      List<IndexInformation> indexesInformation = serviceProvider.getIndexAdministrationService().getIndexesInBase(base.getUuid());
      for (IndexInformation indexInformation : indexesInformation) {
         RangeIndexInformation rangeInfo = getLastRange(indexInformation.getRangeIndexes());
         if (rangeInfo.getTotalIndexUseCount() > alerteLimite) {
            LOGGER.warn("{} : [{} TO {}[ -> {}", new Object[] { indexInformation.getIndexKey(), rangeInfo.getLowerBound(), rangeInfo.getUpperBound(), rangeInfo.getTotalIndexUseCount()});
         } else {
            LOGGER.info("{} : [{} TO {}[ -> {}", new Object[] { indexInformation.getIndexKey(), rangeInfo.getLowerBound(), rangeInfo.getUpperBound(), rangeInfo.getTotalIndexUseCount()});
         }
      }
      
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
   
   private RangeIndexInformation getLastRange(List<RangeIndexInformation> ranges) {
      RangeIndexInformation lastRange = null;
      for (RangeIndexInformation curRange : ranges) {
         if (curRange.getUpperBound().equals("max_upper_bound")) {
            lastRange = curRange;
            break;
         }
      }
      return lastRange;
   }
   
   @Test
   public void getIndexesAllRangeSize() {
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider serviceProvider = dfceConnectionService.openConnection();
      
      int limite = 1000000;
      int alerteLimite = (int)(limite * 0.9);
      
      final Base base = serviceProvider.getBaseAdministrationService()
            .getBase(dfceConnection.getBaseName());
      
      List<IndexInformation> indexesInformation = serviceProvider.getIndexAdministrationService().getIndexesInBase(base.getUuid());
      for (IndexInformation indexInformation : indexesInformation) {
         for (RangeIndexInformation curRange : indexInformation.getRangeIndexes()) {
            if (curRange.getTotalIndexUseCount() > alerteLimite) {
               LOGGER.warn("{} : [{} TO {}[ -> {}", new Object[] { indexInformation.getIndexKey(), curRange.getLowerBound(), curRange.getUpperBound(), curRange.getTotalIndexUseCount()});
            } else {
               LOGGER.info("{} : [{} TO {}[ -> {}", new Object[] { indexInformation.getIndexKey(), curRange.getLowerBound(), curRange.getUpperBound(), curRange.getTotalIndexUseCount()});
            }
         }
      }
      
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
   }
}

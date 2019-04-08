package fr.urssaf.image.sae.indexcounterupdater.dao;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;

public class IndexReferenceDAOTest {

   @Test
   /**
    * Pour les différents index : compare la valeur total_use_count avec la somme des COUNT des différents ranges
    * Ces deux valeurs sont censées être égales.
    */
   public void verifyTotalUseCountTest() {
      final String cassandraServers = "cnp69gntcas1.cer69.recouv";
      final String cassandraUsername = "root";
      final String cassandraPassword = "regina4932";
      final String cassandraLocalDC = "LYON_SP";
      final CqlSession session = CassandraSessionFactory.getSession(cassandraServers, cassandraUsername, cassandraPassword, cassandraLocalDC);

      final String baseName = BaseDAO.getBaseName(session);
      final UUID baseId = BaseDAO.getBaseUUID(session, baseName);
      final List<Row> rows = IndexReferenceDAO.getRows(session, baseId);
      for (final Row row : rows) {
         final int totalUseCount = row.getInt("total_use_count");
         final Map<Integer, String> ranges = row.getMap("index_ranges", Integer.class, String.class);
         int total = 0;
         for (final Integer rangeId : ranges.keySet()) {
            final String rangeAsJson = ranges.get(rangeId);
            final RangeIndexEntity rangeIndexEntity = IndexReferenceDAO.getRangeIndexEntityFromJson(rangeAsJson);
            final String state = rangeIndexEntity.getSTATE();
            if (!"NOMINAL".equals(state)) {
               // On ignore les ranges qui ne sont pas à l'état nominal
               continue;
            }
            total += rangeIndexEntity.getCOUNT();
         }
         final String indexName = row.getString("index_name");
         final float diff = 100f * (Math.max(total, totalUseCount) - Math.min(total, totalUseCount)) / Math.max(total, totalUseCount);
         final String status = total == totalUseCount ? "OK" : "KO !!!!!!!!!!!!!!!! " + diff + "%";
         System.out.println(String.format("Index %s : total_use_count=%d - total=%d - %s", indexName, totalUseCount, total, status));
         if (total != totalUseCount) {
            // Pour corriger :
            // IndexReferenceDAO.updateTotalUseCount(session, baseId, indexName, totalUseCount, total);
         }
      }
      session.close();
   }

   @Test
   /**
    * Exemple d'utilisation de updateIndexCounters
    */
   public void updateIndexCountersTest() {
      final String cassandraServers = "cnp69gntcas1.cer69.recouv";
      final String cassandraUsername = "root";
      final String cassandraPassword = "regina4932";
      final String cassandraLocalDC = "LYON_SP";
      final CqlSession session = CassandraSessionFactory.getSession(cassandraServers, cassandraUsername, cassandraPassword, cassandraLocalDC);

      final String baseName = BaseDAO.getBaseName(session);
      final UUID baseId = BaseDAO.getBaseUUID(session, baseName);
      final String index = "SM_IS_FROZEN";
      final int rangeId = 0;
      final String currentRangeAsJson = "{\"ID\":0,\"LOWER_BOUND\":\"min_lower_bound\",\"UPPER_BOUND\":\"max_upper_bound\",\"COUNT\":0,\"STATE\":\"NOMINAL\"}";
      final int currentTotalUseCount = -3072182;
      final int currentDistinctUseCount = -1;
      final int newCount = 0;
      final int newTotalUseCount = 0;
      final int newDistinctUseCount = 0;
      IndexReferenceDAO.updateIndexCounters(session,
                                            baseId,
                                            index,
                                            rangeId,
                                            currentRangeAsJson,
                                            currentTotalUseCount,
                                            currentDistinctUseCount,
                                            newCount,
                                            newTotalUseCount,
                                            newDistinctUseCount);
      session.close();
   }

   /**
    * Exemple de mise à jour de total_use_count, dans le cas où cette valeur des différente des COUNT des différents ranges
    */
   @Test
   public void updateTotalUseCountTest() {
      final String cassandraServers = "cnp69gntcas1.cer69.recouv";
      final String cassandraUsername = "root";
      final String cassandraPassword = "regina4932";
      final String cassandraLocalDC = "LYON_SP";
      final CqlSession session = CassandraSessionFactory.getSession(cassandraServers, cassandraUsername, cassandraPassword, cassandraLocalDC);

      final String baseName = BaseDAO.getBaseName(session);
      final UUID baseId = BaseDAO.getBaseUUID(session, baseName);
      final String index = "cot&cag&SM_CREATION_DATE&";
      final int currentTotalUseCount = -98;
      final int newTotalUseCount = -598;
      IndexReferenceDAO.updateTotalUseCount(session, baseId, index, currentTotalUseCount, newTotalUseCount);
      session.close();
   }
}

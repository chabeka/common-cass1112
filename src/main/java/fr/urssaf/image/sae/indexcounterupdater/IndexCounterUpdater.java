package fr.urssaf.image.sae.indexcounterupdater;

import java.math.BigInteger;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;

import fr.urssaf.image.sae.indexcounterupdater.dao.BaseDAO;
import fr.urssaf.image.sae.indexcounterupdater.dao.CassandraSessionFactory;
import fr.urssaf.image.sae.indexcounterupdater.dao.IndexReferenceDAO;
import fr.urssaf.image.sae.indexcounterupdater.dao.RangeIndexEntity;

/**
 * Classe principale du job de mise à jour des compteurs d'index DFCE
 */
public class IndexCounterUpdater {

   private static final Logger LOGGER = LoggerFactory.getLogger(IndexCounterUpdater.class);

   private final String cassandraServers;

   private final String cassandraUsername;

   private final String cassandraPassword;

   private final String cassandraLocalDC;

   private String dfceBaseName;

   private final int maxExecutionTime;

   private final boolean simulationMode;

   private long startTime;

   private CqlSession session;

   private UUID dfceBaseUUID;

   /**
    * Permet de cumuler le nombre total d'éléments parcourus dans l'index, au fur et à mesure du parcours des différents ranges de l'index
    * Clé : nom de l'index
    */
   private final Map<String, Integer> totalUseCountMap = new HashMap<>();

   /**
    * Permet de cumuler le nombre distincts d'éléments parcourus dans l'index, au fur et à mesure du parcours des différents ranges de l'index
    * Clé : nom de l'index
    */
   private final Map<String, Integer> distinctUseCountMap = new HashMap<>();

   public IndexCounterUpdater(final String cassandraServers, final String cassandraUsername, final String cassandraPassword, final String cassandraLocalDC,
                              final String dfceBaseName, final int maxExecutionTime, final boolean simulationMode) {

      this.cassandraServers = cassandraServers;
      this.cassandraUsername = cassandraUsername;
      this.cassandraPassword = cassandraPassword;
      this.cassandraLocalDC = cassandraLocalDC;
      this.dfceBaseName = dfceBaseName;
      this.maxExecutionTime = maxExecutionTime;
      this.simulationMode = simulationMode;
   }

   public void start() {
      startTime = System.currentTimeMillis();

      try {
         // Connexion à cassandra
         init();
         // Exécution
         executeJob();
      }
      finally {
         // Fini. On se déconnecte
         if (session != null) {
            session.close();
         }
      }
   }

   public void init() {
      session = CassandraSessionFactory.getSession(cassandraServers, cassandraUsername, cassandraPassword, cassandraLocalDC);
      findBaseUUID();
   }

   /**
    * Lance le job
    */
   private void executeJob() {
      // Détermination de la liste des ranges à parcourir
      final List<String> ranges = IndexReferenceDAO.getRanges(session, dfceBaseUUID);
      // On mélange, afin de traiter les ranges dans un ordre aléatoire. En effet, sur des grosses volumétries, on n'aura pas
      // le temps de traiter tous les ranges. On veut donc que les exécutions suivantes ne traitent pas forcément les mêmes ranges.
      Collections.shuffle(ranges);

      // Traitement des différents ranges
      int doneCounter = 0;
      final int rangesCount = ranges.size();
      for (final String range : ranges) {
         final String[] elements = StringUtils.split(range, "|");
         final String index = elements[0];
         final int rangeId = Integer.parseInt(elements[1]);
         LOGGER.info("Lancement du traitement du range {}|{} ({}/{})", index, rangeId, doneCounter + 1, rangesCount);
         processRange(index, rangeId);

         // On regarde s'il est l'heure de s'arrêter
         doneCounter++;
         final long currentTime = System.currentTimeMillis();
         final long currentDurationInSeconds = (currentTime - startTime) / 1000;
         if (currentDurationInSeconds >= maxExecutionTime) {
            LOGGER.info("On s'arrête car la durée max est dépassée.");
            break;
         }
      }
      LOGGER.info("Nombre de ranges traités : {}/{}", doneCounter, rangesCount);
   }

   /**
    * Traitement d'un range.
    * On parcours la ligne de la table term_info_range_xxx de ce range, afin de compter le nombre total et le nombre
    * d'éléments distincts de ce range.
    * Suite à ce parcours, on met à jours les compteurs dans la table index_reference
    * 
    * @param index
    *           nom de l'index concerné
    * @param rangeId
    *           id du range de l'index
    */
   public void processRange(final String index, final int rangeId) {

      final RangeIndexEntity rangeIndexEntity = IndexReferenceDAO.getRangeIndexEntity(session, dfceBaseUUID, index, rangeId);
      final String state = rangeIndexEntity.getSTATE();
      if (!"NOMINAL".equals(state)) {
         LOGGER.info("Le range {}|{} est à l'état {}. On s'arrête", index, rangeId, state);
      }
      final String indexType = BaseDAO.getIndexType(session, dfceBaseName, index);
      LOGGER.info("Type de l'index : {}", indexType);
      final String indexTable = getIndexTable(indexType);

      final SimpleStatement statement = SimpleStatement.builder("select metadata_value from dfce." + indexTable +
            " where index_code = '' and metadata_name =? and base_uuid=? and range_index_id=?")
                                                       .setConsistencyLevel(DefaultConsistencyLevel.ONE)
                                                       .setTimeout(Duration.ofSeconds(20))
                                                       .setTracing()
                                                       .addNamedValue("metadata_name", index)
                                                       .addNamedValue("base_uuid", dfceBaseUUID)
                                                       .addNamedValue("range_index_id", BigInteger.valueOf(rangeId))
                                                       .build();

      ResultSet rs;
      try {
         rs = session.execute(statement);
      }
      catch (final Exception e) {
         LOGGER.error("Erreur lors de l'exécution de la requête : {}", statement);
         throw e;
      }

      int totalCounter = 0;
      int distinctCounter = 0;
      String currentValue = "";
      for (final Row row : rs) {
         final String value = row.getString(0);
         if (!value.equals(currentValue)) {
            distinctCounter++;
            currentValue = value;
         }
         totalCounter++;
         if (totalCounter % 200000 == 0) {
            LOGGER.info("Progression : totalCounter={} distinctCounter={}", totalCounter, distinctCounter);
         }
      }

      LOGGER.info("Fin du parcours du range : totalCounter={} distinctCounter={}", totalCounter, distinctCounter);
      updateRange(index, rangeId, totalCounter, distinctCounter);
   }

   /**
    * Met à jour les compteurs dans index_reference pour un index, suite au parcours d'un range de l'index
    * 
    * @param index
    *           nom de l'index
    * @param rangeId
    *           id du range qu'on vient de parcourir
    * @param totalCounter
    *           nombre total de documents parcouru sur le range
    * @param distinctCounter
    *           nombre de valeurs distincts sur le range
    */
   private void updateRange(final String index, final int rangeId, final int totalCounter, final int distinctCounter) {
      // Pour évaluer distinctUseCount au mieux, on moyenne sur l'ensemble des ranges déjà parcourus pour cet index
      addInMap(totalUseCountMap, index, totalCounter);
      addInMap(distinctUseCountMap, index, distinctCounter);
      final float distinctRatio = (float) distinctUseCountMap.get(index) / totalUseCountMap.get(index);
      if (LOGGER.isDebugEnabled()) {
         LOGGER.debug("distinctRatio ={}/{}={}", distinctUseCountMap.get(index), totalUseCountMap.get(index), distinctRatio);
      }
      // On lit les compteurs actuels pour cet index et ce range
      final Row row = IndexReferenceDAO.getRow(session, dfceBaseUUID, index);
      final String rangeAsJson = IndexReferenceDAO.getRangeIndexAsJsonFromRow(row, rangeId);
      final RangeIndexEntity rangeIndexEntity = IndexReferenceDAO.getRangeIndexEntityFromJson(rangeAsJson);
      final int currentCount = (int) rangeIndexEntity.getCOUNT();
      final int currentTotalUseCount = row.getInt("total_use_count");
      final int currentDistinctUseCount = row.getInt("distinct_use_count");

      // On calcul les nouveaux compteurs
      final int totalDelta = totalCounter - currentCount;
      final int newTotalUseCount = currentTotalUseCount + totalDelta;
      int newDistinctUseCount = (int) (newTotalUseCount * distinctRatio);
      if (newTotalUseCount > 0) {
         // Le nombre d'éléments distinct est au minium 1 s'il existe au moins un élément
         newDistinctUseCount = Math.max(1, newDistinctUseCount);
      }

      // On met à jour de façon "atomique" en utilisant une lightweight transaction
      LOGGER.info("Mise à jour du compteur du range : {} -> {}", currentCount, totalCounter);
      LOGGER.info("Mise à jour de total_use_count sur l'index : {} -> {}", currentTotalUseCount, newTotalUseCount);
      LOGGER.info("Mise à jour du distinct_use_count sur l'index : {} -> {}", currentDistinctUseCount, newDistinctUseCount);

      if (!simulationMode) {
         IndexReferenceDAO.updateIndexCounters(session,
                                               dfceBaseUUID,
                                               index,
                                               rangeId,
                                               rangeAsJson,
                                               currentTotalUseCount,
                                               currentDistinctUseCount,
                                               totalCounter,
                                               newTotalUseCount,
                                               newDistinctUseCount);
      }
   }

   private void addInMap(final Map<String, Integer> map, final String key, final int numberToAdd) {
      if (map.containsKey(key)) {
         map.put(key, map.get(key) + numberToAdd);
      } else {
         map.put(key, numberToAdd);
      }
   }

   private static String getIndexTable(final String indexType) {
      switch (indexType) {
      case "INTEGER":
         return "term_info_range_integer";
      case "FLOAT":
         return "term_info_range_float";
      case "DOUBLE":
         return "term_info_range_double";
      case "LONG":
         return "term_info_range_long";
      case "DATE":
         return "term_info_range_date";
      case "DATETIME":
         return "term_info_range_datetime";
      case "STRING":
         return "term_info_range_string";
      case "BOOLEAN":
         return "term_info_range_string"; // A vérifier
      case "UUID":
         return "term_info_range_uuid";
      default:
         throw new RuntimeException("Type d'index inconnu : " + indexType);
      }
   }

   private void findBaseUUID() {
      if (StringUtils.isEmpty(dfceBaseName)) {
         dfceBaseName = BaseDAO.getBaseName(session);
         if (StringUtils.isEmpty(dfceBaseName)) {
            throw new RuntimeException("Base DFCE GNT/GNS non trouvée");
         }
      }
      dfceBaseUUID = BaseDAO.getBaseUUID(session, dfceBaseName);
      LOGGER.info("UUID base DFCE : {}", dfceBaseUUID);
   }

}

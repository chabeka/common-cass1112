package fr.urssaf.image.sae.dfcetools.dao;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.selectFrom;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.update;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.datastax.oss.driver.api.querybuilder.update.Assignment;
import com.datastax.oss.driver.api.querybuilder.update.Update;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Permet d'accéder à la table dfce.index_reference
 */
public class IndexReferenceDAO {

   private static final Logger LOGGER = LoggerFactory.getLogger(IndexReferenceDAO.class);

   private IndexReferenceDAO() {
      // Classe statique
   }

   /**
    * Pour une base DFCE : récupère la listes de tous les ranges de tous les index, sous la forme d'une liste de string "index|range"
    * 
    * @param session
    *           session cassandra
    * @param baseId
    *           identifiant de la base dfce
    * @return
    */
   public static List<String> getRanges(final CqlSession session, final UUID baseId) {
      final Select query = selectFrom("dfce", "index_reference").columns("index_name", "index_ranges", "base_id");
      final ResultSet rows = session.execute(query.build());
      final ArrayList<String> result = new ArrayList<>();
      for (final Row row : rows) {
         final UUID readBaseId = row.getUuid("base_id");
         if (!baseId.equals(readBaseId)) {
            continue;
         }
         final String indexName = row.getString("index_name");
         final Map<Integer, String> ranges = row.getMap("index_ranges", Integer.class, String.class);
         for (final Entry<Integer, String> entry : ranges.entrySet()) {
            final int rangeId = entry.getKey();
            result.add(indexName + "|" + rangeId);
         }
      }
      return result;
   }

   public static RangeIndexEntity getRangeIndexEntity(final CqlSession session, final UUID baseId, final String index, final int rangeId) {
      final Select query = selectFrom("dfce", "index_reference").columns("index_ranges")
                                                                .whereColumn("index_name")
                                                                .isEqualTo(literal(index))
                                                                .whereColumn("base_id")
                                                                .isEqualTo(literal(baseId));
      final SimpleStatement statement = query.build();
      final Row row = session.execute(statement).one();
      return getRangeIndexEntityFromRow(row, rangeId);
   }

   public static Row getRow(final CqlSession session, final UUID baseId, final String index) {
      final Select query = selectFrom("dfce", "index_reference").columns("index_name", "base_id", "distinct_use_count", "index_ranges", "total_use_count")
                                                                .whereColumn("index_name")
                                                                .isEqualTo(literal(index))
                                                                .whereColumn("base_id")
                                                                .isEqualTo(literal(baseId));
      final SimpleStatement statement = query.build();
      final Row row = session.execute(statement).one();
      return row;
   }

   public static List<Row> getRows(final CqlSession session, final UUID baseId) {
      final Select query = selectFrom("dfce", "index_reference").columns("index_name", "base_id", "distinct_use_count", "index_ranges", "total_use_count");

      final ResultSet rows = session.execute(query.build());
      final ArrayList<Row> result = new ArrayList<>();
      for (final Row row : rows) {
         final UUID readBaseId = row.getUuid("base_id");
         if (baseId.equals(readBaseId)) {
            result.add(row);
         }
      }
      return result;
   }

   public static String getRangeIndexAsJsonFromRow(final Row row, final int rangeId) {
      final Map<Integer, String> ranges = row.getMap("index_ranges", Integer.class, String.class);
      final String rangeAsJson = ranges.get(rangeId);
      return rangeAsJson;
   }

   public static RangeIndexEntity getRangeIndexEntityFromRow(final Row row, final int rangeId) {
      final String rangeAsJson = getRangeIndexAsJsonFromRow(row, rangeId);
      return getRangeIndexEntityFromJson(rangeAsJson);
   }

   public static RangeIndexEntity getRangeIndexEntityFromJson(final String rangeAsJson) {
      if (rangeAsJson == null) {
         return null;
      }
      final ObjectMapper jsonMapper = new ObjectMapper();
      try {
         final RangeIndexEntity rangeEntity = jsonMapper.readValue(rangeAsJson, RangeIndexEntity.class);
         return rangeEntity;
      }
      catch (final Exception e) {
         throw new RuntimeException(e);
      }
   }

   /**
    * Met à jour les compteurs sur un index.
    * On utilise des "lightweight transactions". On passe donc non seulement les nouveaux compteurs, mais aussi les anciens
    * 
    * @param session
    *           session cassandra
    * @param baseId
    *           identifiant de la base DFCE
    * @param index
    *           nom de l'index
    * @param rangeId
    *           id du range qu'on vient de parcourir
    * @param currentRangeAsJson
    *           chaîne json courante du range qu'on vient de parcourir
    * @param currentTotalUseCount
    *           valeur courante de "total_use_count"
    * @param currentDistinctUseCount
    *           valeur courante de "distinct_use_count"
    * @param newCount
    *           nouvelle valeur "COUNT" du range de l'index
    * @param newTotalUseCount
    *           nouvelle valeur "total_use_count" de l'index
    * @param newDistinctUseCount
    *           nouvelle valeur "distinct_use_count" de l'index
    */
   public static void updateIndexCounters(final CqlSession session, final UUID baseId, final String index, final int rangeId,
                                          final String currentRangeAsJson,
                                          final int currentTotalUseCount, final int currentDistinctUseCount, final int newCount, final int newTotalUseCount,
                                          final int newDistinctUseCount) {

      // On vérifie que le range est bien à l'état "nominal"
      final RangeIndexEntity rangeIndexEntity = getRangeIndexEntityFromJson(currentRangeAsJson);
      final String state = rangeIndexEntity.getSTATE();
      if (!"NOMINAL".equals(state)) {
         LOGGER.warn("Update abandonné pour le range {}|{} car l'état est {}", index, rangeId, state);
      }
      // On crée la nouvelle chaîne json contenant le compteur à jour
      String newJson;
      final ObjectMapper jsonMapper = new ObjectMapper();
      try {
         rangeIndexEntity.setCOUNT(newCount);
         newJson = jsonMapper.writeValueAsString(rangeIndexEntity);
      }
      catch (final JsonProcessingException e) {
         throw new RuntimeException("Erreur lors de la génération du json", e);
      }

      // On lance un update
      final Update query = update("dfce", "index_reference")
                                                            .set(Assignment.setColumn("distinct_use_count", literal(newDistinctUseCount)),
                                                                 Assignment.setColumn("total_use_count", literal(newTotalUseCount)),
                                                                 Assignment.setMapValue("index_ranges", literal(rangeId), literal(newJson)))
                                                            .whereColumn("index_name")
                                                            .isEqualTo(literal(index))
                                                            .whereColumn("base_id")
                                                            .isEqualTo(literal(baseId))
                                                            .ifColumn("distinct_use_count")
                                                            .isEqualTo(literal(currentDistinctUseCount))
                                                            .ifColumn("total_use_count")
                                                            .isEqualTo(literal(currentTotalUseCount))
                                                            .ifElement("index_ranges", literal(rangeId))
                                                            .isEqualTo(literal(currentRangeAsJson));
      final ResultSet result = session.execute(query.build());
      final List<String> warnings = result.getExecutionInfo().getWarnings();
      if (!warnings.isEmpty()) {
         LOGGER.warn("Warning lors de la tentative de mise à jour de l'index {}|{} : {}", index, rangeId, warnings);
      }
   }

   /**
    * Met à jour la propriété total_use_count d'un index
    * On utilise des "lightweight transactions". On passe donc non seulement les nouveaux compteurs, mais aussi les anciens
    * 
    * @param session
    *           session cassandra
    * @param baseId
    *           identifiant de la base DFCE
    * @param index
    *           nom de l'index
    * @param currentTotalUseCount
    *           valeur courante de "total_use_count"
    * @param newTotalUseCount
    */
   public static void updateTotalUseCount(final CqlSession session, final UUID baseId, final String index,
                                          final int currentTotalUseCount, final int newTotalUseCount) {

      // On lance un update
      final Update query = update("dfce", "index_reference")
                                                            .set(Assignment.setColumn("total_use_count", literal(newTotalUseCount)))
                                                            .whereColumn("index_name")
                                                            .isEqualTo(literal(index))
                                                            .whereColumn("base_id")
                                                            .isEqualTo(literal(baseId))
                                                            .ifColumn("total_use_count")
                                                            .isEqualTo(literal(currentTotalUseCount));
      final ResultSet result = session.execute(query.build());
      final List<String> warnings = result.getExecutionInfo().getWarnings();
      if (!warnings.isEmpty()) {
         LOGGER.warn("Warning lors de la tentative de mise à jour de total_use_count pour l'index {} : {}", index, warnings);
      }
   }
}

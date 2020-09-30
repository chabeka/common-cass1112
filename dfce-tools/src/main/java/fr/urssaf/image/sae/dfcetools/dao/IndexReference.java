package fr.urssaf.image.sae.dfcetools.dao;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.select.Select;

/**
 * 
 *
 */
public class IndexReference {

   private String boundaries[];

   private int boundaryIndexToRangeId[];

   private int maxRangeId;

   Map<Integer, RangeIndexEntity> entitiesById = new HashMap<>();

   Map<Integer, Integer> rangeIdToIndex = new HashMap<>();

   public void readIndexReference(final CqlSession session, final UUID baseUUID, final String meta, final String state) {
      readIndexReference(session, baseUUID, meta, new String[] {state});
   }

   public void readIndexReference(final CqlSession session, final UUID baseUUID, final String meta, final String[] states) {

      final Select query = QueryBuilder.selectFrom("dfce", "index_reference")
            .all()
            .whereColumn("index_name")
            .isEqualTo(literal(meta))
            .whereColumn("base_id")
            .isEqualTo(literal(baseUUID));
      final Row row = session.execute(query.build()).one();
      final Map<Integer, String> ranges = row.getMap("index_ranges", Integer.class, String.class);
      int rangeIndex = 0;
      // On crée une collection d'entitées (range) triée par la borne inférieure du range
      final TreeMap<String, RangeIndexEntity> entities = new TreeMap<>();

      for (final Integer rangeId : ranges.keySet()) {
         final String rangeAsJson = ranges.get(rangeId);
         final RangeIndexEntity entity = IndexReferenceDAO.getRangeIndexEntityFromJson(rangeAsJson);
         if (Arrays.asList(states).contains(entity.getSTATE())) {
            entitiesById.put(entity.getId(), entity.clone());
            rangeIdToIndex.put(entity.getId(), rangeIndex);
            if (Arrays.asList(states).contains(entity.getSTATE())) {
               if ("min_lower_bound".equals(entity.getLOWER_BOUND())) {
                  entity.setLOWER_BOUND("");
               }
               entities.put(entity.getLOWER_BOUND(), entity);
               if (entity.getId() > maxRangeId) {
                  maxRangeId = entity.getId();
               }
            }
            rangeIndex++;
         }
      }

      // On vérifie que tout est ok
      final Iterator<Entry<String, RangeIndexEntity>> it1 = entities.entrySet().iterator();
      RangeIndexEntity previousEntity = null;
      RangeIndexEntity lastEntity = null;
      while (it1.hasNext()) {
         final Entry<String, RangeIndexEntity> pair = it1.next();
         final RangeIndexEntity entity = pair.getValue();
         // System.out.println(entity.getLOWER_BOUND() + " - " + entity.getUPPER_BOUND());
         if (previousEntity == null) {
            if (!"".equals(entity.getLOWER_BOUND())) {
               throw new RuntimeException("Première borne min pas bonne : " + entity.getLOWER_BOUND());
            }
         } else {
            // on vérifie que les bornes sont contiguës
            if (!previousEntity.getUPPER_BOUND().equals(entity.getLOWER_BOUND())) {
               throw new RuntimeException("Bornes non contiguës : " + previousEntity.getUPPER_BOUND() + " et " + entity.getLOWER_BOUND());
            }
         }
         previousEntity = entity;
         lastEntity = entity;
      }
      if (!"max_upper_bound".equals(lastEntity.getUPPER_BOUND())) {
         throw new RuntimeException("Dernière borne max pas bonne : " + lastEntity.getUPPER_BOUND());
      }

      // Création des bornes
      boundaries = new String[entities.size()];
      boundaryIndexToRangeId = new int[entities.size()];
      final Iterator<Entry<String, RangeIndexEntity>> it = entities.entrySet().iterator();
      int currentRangeIndex = 0;
      while (it.hasNext()) {
         final Entry<String, RangeIndexEntity> pair = it.next();
         final RangeIndexEntity entity = pair.getValue();
         boundaries[currentRangeIndex] = entity.getLOWER_BOUND();
         boundaryIndexToRangeId[currentRangeIndex] = entity.getId();
         currentRangeIndex++;
      }

   }

   /**
    * Renvoie l'id du range qui contient la valeur de la méta passée en paramètre
    * 
    * @param metaValue
    * @return
    */
   public int metaToRangeId(final String metaValue) {
      int index = Arrays.binarySearch(boundaries, metaValue);
      if (index < 0) {
         index = -2 - index;
      }
      return boundaryIndexToRangeId[index];
   }

   /**
    * Retourne l'id de l'ensemble des ranges sur lesquels il faut requêter pour trouver les documents dont la méta indexée
    * est entre metaStartValue et metaEndValue
    * 
    * @param metaStartValue
    * @param metaEndValue
    * @return Les id des ranges
    */
   public int[] metaToRangeIds(final String metaStartValue, final String metaEndValue) {
      int index1 = Arrays.binarySearch(boundaries, metaStartValue);
      if (index1 < 0) {
         index1 = -2 - index1;
      }
      int index2 = Arrays.binarySearch(boundaries, metaEndValue);
      if (index2 < 0) {
         index2 = -2 - index2;
      }
      final int[] result = new int[index2 - index1 + 1];
      for (int index = index1; index <= index2; index++) {
         result[index - index1] = boundaryIndexToRangeId[index];
      }
      return result;
   }
}

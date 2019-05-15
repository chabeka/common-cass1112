package fr.urssaf.javaDriverTest.split;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.BuiltStatement;
import com.datastax.driver.core.querybuilder.QueryBuilder;

import fr.urssaf.javaDriverTest.dao.BaseDAO;
import fr.urssaf.javaDriverTest.dao.RangeIndexEntity;

/**
 * Classe qui permet de calculer les ranges d'index en avance de phase sur les index de type Date
 */
public class NewSplitsScheduler {

   private static final Logger LOGGER = LoggerFactory.getLogger(NewSplitsScheduler.class);

   /**
    * Analyse les ranges de l'index "index", et renvoie une chaine de caractères contenant, si besoin,
    * les nouveaux ranges à créer.
    * 
    * @param session
    * @param index
    * @return
    * @throws Exception
    */
   public static String scheduleNewSplits(final Session session, final String index) throws Exception {
      final UUID baseId = BaseDAO.getBaseUUID(session);
      final LocalDateTime currentTime = LocalDateTime.now();
      final List<RangeIndexEntity> entities = getRangeIndexEntities(session, baseId, index);
      return scheduleNewSplits(currentTime, entities);
   }

   /**
    * Analyse les ranges de l'index "index", et renvoie une chaine de caractères contenant, si besoin,
    * les nouveaux ranges à créer.
    * 
    * @param session
    * @param index
    * @return
    * @throws Exception
    */
   public static String scheduleNewSplits(final LocalDateTime currentTime, final List<RangeIndexEntity> entities) throws Exception {
      if (!isOnlyNominal(entities)) {
         LOGGER.debug("On s'arrête, car range non NOMINAL détecté");
         return "";
      }
      final long width = getWidth(currentTime, entities);
      LOGGER.debug("width={}", width);
      final String newRanges = createNewRanges(currentTime, entities, width);
      final int newRangesCount = StringUtils.countMatches(newRanges, "|");
      if (newRangesCount <= 1) {
         return "";
      }
      final DateTimeFormatter formatter = getDFCEFormatter();
      final RangeIndexEntity lastEntity = entities.get(entities.size() - 1);
      return rangeAsString(lastEntity, formatter) + "|" + newRanges.replace("|", "#");
   }

   private static String rangeAsString(final RangeIndexEntity entity, final DateTimeFormatter formatter) {
      return rangeAsString(stringToDate(entity.getLOWER_BOUND(), formatter), stringToDate(entity.getUPPER_BOUND(), formatter), formatter);
   }

   private static String rangeAsString(final LocalDateTime startDate, final LocalDateTime endDate, final DateTimeFormatter formatter) {
      return startRange(startDate, formatter) + " TO " + endRange(endDate, formatter);
   }

   private static String startRange(final LocalDateTime date, final DateTimeFormatter formatter) {
      return "[" + dateToString(date, formatter);
   }

   private static String endRange(final LocalDateTime date, final DateTimeFormatter formatter) {
      if (date.equals(LocalDateTime.MAX)) {
         return dateToString(date, formatter) + "]";
      } else {
         return dateToString(date, formatter) + "[";
      }
   }

   private static String dateToString(final LocalDateTime date, final DateTimeFormatter formatter) {
      if (date.equals(LocalDateTime.MAX)) {
         return "max_upper_bound";
      }
      if (date.equals(LocalDateTime.MIN)) {
         return "min_lower_bound";
      }
      return formatter.format(date);
   }

   private static LocalDateTime stringToDate(final String dateAsString, final DateTimeFormatter formatter) {
      if ("min_lower_bound".equals(dateAsString)) {
         return LocalDateTime.MIN;
      }
      if ("max_upper_bound".equals(dateAsString)) {
         return LocalDateTime.MAX;
      }
      return LocalDateTime.parse(dateAsString, formatter);
   }

   /**
    * Crée les ranges pour les 60 prochains jours
    * 
    * @param entities
    * @param rangeWidth
    *           : taille des ranges, en secondes
    * @return
    */
   private static String createNewRanges(final LocalDateTime currentTime, final List<RangeIndexEntity> entities, final long rangeWidth) {
      final RangeIndexEntity lastEntity = entities.get(entities.size() - 1);
      final DateTimeFormatter formatter = getDFCEFormatter();
      final LocalDateTime startDate = stringToDate(lastEntity.getLOWER_BOUND(), formatter);
      final LocalDateTime maxDate = currentTime.plusDays(60);
      LocalDateTime date = startDate;

      final StringBuilder result = new StringBuilder();
      while (date.isBefore(maxDate)) {
         LocalDateTime nextDate;
         if (result.length() > 0) {
            result.append("|");
         }
         if (date.equals(LocalDateTime.MIN)) {
            nextDate = currentTime.plusSeconds(rangeWidth);
         } else {
            nextDate = date.plusSeconds(rangeWidth);
         }
         if (!nextDate.isBefore(maxDate)) {
            nextDate = LocalDateTime.MAX;
         }
         result.append(rangeAsString(date, nextDate, formatter));
         date = nextDate;
      }
      return result.toString();
   }

   private static DateTimeFormatter getDFCEFormatter() {
      // Ce formater ne fonctionne pas en java 8 !!! cf https://stackoverflow.com/questions/22588051/is-java-time-failing-to-parse-fraction-of-second
      // final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuuMMddHHmmssSSS");
      return new DateTimeFormatterBuilder()
                                           .appendPattern("yyyyMMddHHmmss")
                                           .appendValue(ChronoField.MILLI_OF_SECOND, 3)
                                           .toFormatter();
   }

   /**
    * Renvoie la taille cible des ranges, en secondes, en se basant sur les statistiques des 45 derniers jours.
    * On vise des ranges avec 1 millions d'éléments
    * 
    * @param entities
    * @return
    */
   private static long getWidth(final LocalDateTime currentTime, final List<RangeIndexEntity> entities) {
      final DateTimeFormatter formatter = getDFCEFormatter();
      final long defaultWidth = Duration.ofDays(7).getSeconds();
      final LocalDateTime upperMargin = currentTime.minusDays(7);
      final LocalDateTime lowerDate = currentTime.minusDays(45);

      long totalWidth = 0;
      long totalElementsCount = 0;
      // On parcours les entités dans l'ordre inverse, en ignorant celle qui se termine par max_upper_bound et celle
      // commençant par min_lower_bount
      for (int i = entities.size() - 2; i > 0; i--) {
         final RangeIndexEntity entity = entities.get(i);
         final LocalDateTime lower = stringToDate(entity.getLOWER_BOUND(), formatter);
         final LocalDateTime upper = stringToDate(entity.getUPPER_BOUND(), formatter);
         if (upper.isAfter(upperMargin)) {
            // On ne prend en compte que les ranges terminés depuis plus de 7 jours. Ainsi, on sait que
            // les statistiques sont à jour sur ce range.
            continue;
         }
         if (totalWidth > 0) {
            if (lower.isBefore(lowerDate)) {
               LOGGER.debug("On s'arrête car lower={} < {}", lower, lowerDate);
               break;
            }
         }
         final Duration rangeDuration = Duration.between(lower, upper);
         final long elementsCount = entity.getCOUNT();
         if (elementsCount > 0) {
            final long width = rangeDuration.getSeconds();
            LOGGER.debug("Range pris en compte : {} to {} ({})", lower, upper, elementsCount);
            totalWidth += width;
            totalElementsCount += elementsCount;
         }
      }
      if (totalWidth == 0) {
         return defaultWidth;
      }
      return totalWidth * 1000000 / totalElementsCount;
   }

   private static List<RangeIndexEntity> getRangeIndexEntities(final Session session, final UUID baseId, final String index) throws Exception {
      final BuiltStatement query = QueryBuilder.select()
                                               .from("dfce", "index_reference")
                                               .where(QueryBuilder.eq("index_name", index))
                                               .and(QueryBuilder.eq("base_id", baseId));
      final Row row = session.execute(query).one();
      final ObjectMapper jsonMapper = new ObjectMapper();
      final Map<Integer, String> ranges = row.getMap("index_ranges", Integer.class, String.class);
      final List<RangeIndexEntity> result = new ArrayList<RangeIndexEntity>();
      for (final String rangeAsJson : ranges.values()) {
         final RangeIndexEntity rangeEntity = jsonMapper.readValue(rangeAsJson, RangeIndexEntity.class);
         result.add(rangeEntity);
      }
      Collections.sort(result);
      return result;
   }

   private static boolean isOnlyNominal(final List<RangeIndexEntity> entities) {
      for (final RangeIndexEntity entity : entities) {
         if (!"NOMINAL".equals(entity.getSTATE())) {
            return false;
         }
      }
      return true;
   }

}

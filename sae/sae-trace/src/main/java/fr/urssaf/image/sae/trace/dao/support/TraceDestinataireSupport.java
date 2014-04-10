/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyResultWrapper;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;

import fr.urssaf.image.commons.cassandra.helper.HectorIterator;
import fr.urssaf.image.commons.cassandra.helper.QueryResultConverter;
import fr.urssaf.image.sae.trace.dao.TraceDestinataireDao;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.dao.serializer.ListSerializer;
import fr.urssaf.image.sae.trace.exception.TraceRuntimeException;

/**
 * Support de la classe DAO {@link TraceDestinataireDao}
 * 
 */
@Component
public class TraceDestinataireSupport {

   private static final int LIFE_DURATION = 10;
   private static final int MAX_FIND_RESULT = 5000;

   private final TraceDestinataireDao dao;

   private final LoadingCache<String, TraceDestinataire> traces;

   /**
    * Constructeur
    * 
    * @param dao
    *           DAO des traces destinataires
    */
   @Autowired
   public TraceDestinataireSupport(TraceDestinataireDao dao) {
      this.dao = dao;
      traces = CacheBuilder.newBuilder().expireAfterWrite(LIFE_DURATION,
            TimeUnit.MINUTES).build(
            new CacheLoader<String, TraceDestinataire>() {

               @Override
               public TraceDestinataire load(String identifiant) {
                  return findById(identifiant);
               }

            });
   }

   /**
    * Création d'une colonne
    * 
    * @param trace
    *           trace à créer
    * @param clock
    *           horloge de la création
    */
   public final void create(TraceDestinataire trace, long clock) {
      ColumnFamilyTemplate<String, String> tmpl = dao.getCfTmpl();
      ColumnFamilyUpdater<String, String> updater = tmpl.createUpdater(trace
            .getCodeEvt());

      if (MapUtils.isNotEmpty(trace.getDestinataires())) {
         for (String key : trace.getDestinataires().keySet()) {

            insertColumn(updater, key, trace.getDestinataires().get(key), clock);

         }
      }

      tmpl.update(updater);
   }

   /**
    * Méthode de suppression d'une ligne
    * 
    * @param code
    *           identifiant de la ligne
    * @param clock
    *           horloge de suppression
    */
   public final void delete(String code, long clock) {
      Mutator<String> mutator = dao.createMutator();
      dao.mutatorSuppressionDestinataire(mutator, code, clock);
      mutator.execute();
   }

   /**
    * Recherche et retourne l'enregistrement de la trace destinataire en
    * fonction du code fourni
    * 
    * @param code
    *           code de la trace destinataire
    * @return l'enregistrement de la trace destinataire correspondante
    */
   public final TraceDestinataire find(String code) {
      try {
         return this.traces.getUnchecked(code);

      } catch (InvalidCacheLoadException exception) {
         throw new TraceRuntimeException(exception);
      }
   }

   private TraceDestinataire findById(String code) {
      ColumnFamilyTemplate<String, String> tmpl = dao.getCfTmpl();
      ColumnFamilyResult<String, String> result = tmpl.queryColumns(code);

      TraceDestinataire trace = getTraceDestinataireFromResult(result);

      return trace;
   }

   private void insertColumn(ColumnFamilyUpdater<String, String> updater,
         String key, List<String> list, long clock) {

      if (TraceDestinataireDao.COL_HIST_ARCHIVE.equals(key)) {
         dao.writeColumnHistArchive(updater, list, clock);

      } else if (TraceDestinataireDao.COL_HIST_EVT.equals(key)) {
         dao.writeColumnHistEvt(updater, list, clock);

      } else if (TraceDestinataireDao.COL_REG_EXPLOIT.equals(key)) {
         dao.writeColumnRegExploit(updater, list, clock);

      } else if (TraceDestinataireDao.COL_REG_SECURITE.equals(key)) {
         dao.writeColumnRegSecurite(updater, list, clock);

      } else if (TraceDestinataireDao.COL_REG_TECHNIQUE.equals(key)) {
         dao.writeColumnRegTechnique(updater, list, clock);

      } else if (TraceDestinataireDao.COL_JOURN_EVT.equals(key)) {
         dao.writeColumnJournalEvt(updater, list, clock);

      } else {
         throw new TraceRuntimeException(
               "Impossible de créer l'enregistrement demandé. " + "La clé "
                     + key + " n'est pas supportée");
      }
   }

   private TraceDestinataire getTraceDestinataireFromResult(
         ColumnFamilyResult<String, String> result) {

      TraceDestinataire trace = null;

      if (result != null && result.hasResults()) {
         trace = new TraceDestinataire();
         trace.setCodeEvt(result.getKey());

         List<String> colNames = Arrays.asList(
               TraceDestinataireDao.COL_HIST_ARCHIVE,
               TraceDestinataireDao.COL_HIST_EVT,
               TraceDestinataireDao.COL_REG_EXPLOIT,
               TraceDestinataireDao.COL_REG_SECURITE,
               TraceDestinataireDao.COL_REG_TECHNIQUE,
               TraceDestinataireDao.COL_JOURN_EVT);

         Map<String, List<String>> destinataires = new HashMap<String, List<String>>();
         List<String> values;
         for (String colName : colNames) {
            values = getListFromResult(colName, result);
            if (values != null) {
               destinataires.put(colName, values);
            }
         }

         trace.setDestinataires(destinataires);
      }

      return trace;

   }
   
   public final List<TraceDestinataire> findAll() {

      BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      RangeSlicesQuery<String, String, byte[]> rangeSlicesQuery = HFactory
            .createRangeSlicesQuery(dao.getKeyspace(),
                  StringSerializer.get(), StringSerializer.get(),
                  bytesSerializer);
      rangeSlicesQuery.setColumnFamily(dao.getColumnFamilyName());
      rangeSlicesQuery.setRange(StringUtils.EMPTY, StringUtils.EMPTY, false,
            MAX_FIND_RESULT);
      QueryResult<OrderedRows<String, String, byte[]>> queryResult = rangeSlicesQuery
            .execute();

      // On convertit le résultat en ColumnFamilyResultWrapper pour faciliter
      // son utilisation
      QueryResultConverter<String, String, byte[]> converter = new QueryResultConverter<String, String, byte[]>();
      ColumnFamilyResultWrapper<String, String> result = converter
            .getColumnFamilyResultWrapper(queryResult, StringSerializer.get(),
                  StringSerializer.get(), bytesSerializer);

      // On itère sur le résultat
      HectorIterator<String, String> resultIterator = new HectorIterator<String, String>(
            result);
      List<TraceDestinataire> list = new ArrayList<TraceDestinataire>();
      for (ColumnFamilyResult<String, String> row : resultIterator) {

         list.add(getTraceDestinataireFromResult(row));

      }
      return list;

   }

   private List<String> getListFromResult(String colName,
         ColumnFamilyResult<String, String> result) {

      List<String> value = null;
      byte[] bValue = result.getByteArray(colName);

      if (bValue != null) {
         value = ListSerializer.get().fromBytes(bValue);
      }

      return value;
   }
}

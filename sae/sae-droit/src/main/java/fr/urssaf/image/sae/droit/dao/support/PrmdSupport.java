/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.support;

import java.util.ArrayList;
import java.util.List;

import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyResultWrapper;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.helper.HectorIterator;
import fr.urssaf.image.commons.cassandra.helper.QueryResultConverter;
import fr.urssaf.image.sae.droit.dao.PrmdDao;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.serializer.MapSerializer;

/**
 * Classe de support de la classe {@link PrmdDao}
 * 
 */
@Component
public class PrmdSupport {

   @Autowired
   private PrmdDao dao;

   /**
    * Méthode de création d'un ligne
    * 
    * @param prmd
    *           propriétés du PRMD à créer
    * @param clock
    *           horloge de la création
    */
   public final void create(Prmd prmd, long clock) {
      ColumnFamilyUpdater<String, String> updater = dao.getPrmdTmpl()
            .createUpdater(prmd.getCode());

      dao.ecritDescription(updater, prmd.getDescription(), clock);
      dao.ecritLucene(updater, prmd.getLucene(), clock);
      dao.ecritBean(updater, prmd.getBean(), clock);
      dao.ecritMetaData(updater, prmd.getMetadata(), clock);

      dao.getPrmdTmpl().update(updater);
   }

   /**
    * Méthode de suppression d'une ligne
    * 
    * @param code
    *           identifiant du PRMD
    * @param clock
    *           horloge de suppression
    */
   public final void delete(String code, long clock) {

      Mutator<String> mutator = dao.createMutator();

      dao.mutatorSuppressionPrmd(mutator, code, clock);

      mutator.execute();
   }

   /**
    * Méthode de lecture d'une ligne
    * 
    * @param code
    *           identifiant du PRMD
    * @return un PAGM correpodant à l'identifiant passé en paramètre
    */
   public final Prmd find(String code) {

      ColumnFamilyResult<String, String> result = dao.getPrmdTmpl()
            .queryColumns(code);

      Prmd prmd = getPrmdFromResult(result);

      return prmd;

   }

   /**
    * Lecture de toutes les lignes (attention aux performances)
    * 
    * @param maxKeysToRead
    *           nombre maximum d'enregistrements à récupérer
    * @return la liste de tous les PAGM
    */
   public final List<Prmd> findAll(int maxKeysToRead) {
      // On n'utilise pas d'index. On récupère tous les jobs sans distinction,
      // en requêtant directement dans la CF JobRequest
      BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      RangeSlicesQuery<String, String, byte[]> rangeSlicesQuery = HFactory
            .createRangeSlicesQuery(dao.getKeyspace(), StringSerializer.get(),
                  StringSerializer.get(), bytesSerializer);
      rangeSlicesQuery.setColumnFamily(PrmdDao.PRMD_CFNAME);
      rangeSlicesQuery.setRange("", "", false, PrmdDao.MAX_ATTRIBUTS);
      rangeSlicesQuery.setRowCount(maxKeysToRead);
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
      List<Prmd> list = new ArrayList<Prmd>();
      for (ColumnFamilyResult<String, String> row : resultIterator) {
         Prmd prmd = getPrmdFromResult(row);

         if (prmd != null)
            list.add(prmd);
      }
      return list;
   }

   private Prmd getPrmdFromResult(ColumnFamilyResult<String, String> result) {
      Prmd prmd = null;

      if (result != null && result.hasResults()) {
         prmd = new Prmd();
         prmd.setCode(result.getKey());
         prmd.setDescription(result.getString(PrmdDao.PRMD_DESCRIPTION));
         prmd.setLucene(result.getString(PrmdDao.PRMD_LUCENE));

         byte[] bMetadata = result.getByteArray(PrmdDao.PRMD_METADATA);
         prmd.setMetadata(MapSerializer.get().fromBytes(bMetadata));

         prmd.setBean(result.getString(PrmdDao.PRMD_BEAN));
      }

      return prmd;
   }
}

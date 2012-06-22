/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import fr.urssaf.image.sae.droit.dao.PagmDao;
import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.serializer.MapStringSerializer;

/**
 * Classe de support de la classe {@link PagmDao}
 * 
 */
@Component
public class PagmSupport {

   @Autowired
   private PagmDao dao;

   /**
    * Méthode de création d'un ligne
    * 
    * @param pagm
    *           propriétés du PAGM à créer
    * @param clock
    *           horloge de la création
    */
   public final void create(Pagm pagm, long clock) {

      ColumnFamilyUpdater<String, String> updater = dao.getPagmTmpl()
            .createUpdater(pagm.getCode());

      dao.ecritDescription(updater, pagm.getDescription(), clock);
      dao.ecritPagma(updater, pagm.getPagma(), clock);
      dao.ecritPagmp(updater, pagm.getPagmp(), clock);
      dao.ecritParametres(updater, pagm.getParametres(), clock);

      dao.getPagmTmpl().update(updater);

   }

   /**
    * Méthode de suppression d'une ligne
    * 
    * @param code
    *           identifiant du PAGM
    * @param clock
    *           horloge de suppression
    */
   public final void delete(String code, long clock) {

      Mutator<String> mutator = dao.createMutator();

      dao.mutatorSuppressionPagm(mutator, code, clock);

      mutator.execute();
   }

   /**
    * Méthode de lecture d'une ligne
    * 
    * @param code
    *           identifiant du PAGM
    * @return un PAGM correpodant à l'identifiant passé en paramètre
    */
   public final Pagm find(String code) {

      ColumnFamilyResult<String, String> result = dao.getPagmTmpl()
            .queryColumns(code);

      Pagm pagm = getPagmFromResult(result);

      return pagm;

   }

   /**
    * Lecture de toutes les lignes (attention aux performances)
    * 
    * @param maxKeysToRead
    *           nombre maximum d'enregistrements à récupérer
    * @return la liste de tous les PAGM
    */
   public final List<Pagm> findAll(int maxKeysToRead) {
      // On n'utilise pas d'index. On récupère tous les jobs sans distinction,
      // en requêtant directement dans la CF JobRequest
      BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      RangeSlicesQuery<String, String, byte[]> rangeSlicesQuery = HFactory
            .createRangeSlicesQuery(dao.getKeyspace(), StringSerializer.get(),
                  StringSerializer.get(), bytesSerializer);
      rangeSlicesQuery.setColumnFamily(PagmDao.PAGM_CFNAME);
      rangeSlicesQuery.setRange("", "", false, PagmDao.MAX_ATTRIBUTS);
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
      List<Pagm> list = new ArrayList<Pagm>();
      for (ColumnFamilyResult<String, String> row : resultIterator) {
         Pagm pagm = getPagmFromResult(row);

         if (pagm != null)
            list.add(pagm);
      }
      return list;
   }

   private Pagm getPagmFromResult(ColumnFamilyResult<String, String> result) {

      Pagm pagm = null;

      if (result != null && result.hasResults()) {
         pagm = new Pagm();
         pagm.setCode(result.getKey());
         pagm.setDescription(result.getString(PagmDao.PAGM_DESCRIPTION));
         pagm.setPagma(result.getString(PagmDao.PAGM_PAGMA));
         pagm.setPagmp(result.getString(PagmDao.PAGM_PAGMP));

         byte[] bParam = result.getByteArray(PagmDao.PAGM_PARAMETRES);
         MapStringSerializer serializer = MapStringSerializer.get();
         Map<String, String> param = serializer.fromBytes(bParam);
         pagm.setParametres(param);
      }

      return pagm;
   }

}

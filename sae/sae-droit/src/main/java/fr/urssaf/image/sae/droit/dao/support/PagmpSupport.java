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
import fr.urssaf.image.sae.droit.dao.PagmpDao;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;

/**
 * Classe de support de la classe {@link PagmpDao}
 * 
 */
@Component
public class PagmpSupport {

   private final PagmpDao dao;
   
   /**
    * constructeur
    * @param pagmpDao DAO associée au pagmp
    */
   @Autowired
   public PagmpSupport(PagmpDao pagmpDao){
      this.dao = pagmpDao;
   }

   /**
    * Méthode de création d'un ligne
    * 
    * @param pagmp
    *           propriétés du PAGMp à créer
    * @param clock
    *           horloge de la création
    */
   public final void create(Pagmp pagmp, long clock) {

      ColumnFamilyUpdater<String, String> updater = dao.getPagmpTmpl()
            .createUpdater(pagmp.getCode());

      dao.ecritDescription(updater, pagmp.getDescription(), clock);
      dao.ecritPrmd(updater, pagmp.getPrmd(), clock);

      dao.getPagmpTmpl().update(updater);

   }

   /**
    * Méthode de suppression d'une ligne
    * 
    * @param code
    *           identifiant du PAGMp
    * @param clock
    *           horloge de suppression
    */
   public final void delete(String code, long clock) {

      Mutator<String> mutator = dao.createMutator();

      dao.mutatorSuppressionPagmp(mutator, code, clock);

      mutator.execute();
   }

   /**
    * Méthode de lecture d'une ligne
    * 
    * @param code
    *           identifiant du PAGMp
    * @return un PAGMp correpodant à l'identifiant passé en paramètre
    */
   public final Pagmp find(String code) {

      ColumnFamilyResult<String, String> result = dao.getPagmpTmpl()
            .queryColumns(code);

      Pagmp pagmp = getPagmpFromResult(result);

      return pagmp;

   }

   /**
    * Lecture de toutes les lignes (attention aux performances)
    * 
    * @param maxKeysToRead
    *           nombre maximum d'enregistrements à récupérer
    * @return la liste de tous les PAGMp
    */
   public final List<Pagmp> findAll(int maxKeysToRead) {
      // On n'utilise pas d'index. On récupère tous les jobs sans distinction,
      // en requêtant directement dans la CF JobRequest
      BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      RangeSlicesQuery<String, String, byte[]> rangeSlicesQuery = HFactory
            .createRangeSlicesQuery(dao.getKeyspace(), StringSerializer.get(),
                  StringSerializer.get(), bytesSerializer);
      rangeSlicesQuery.setColumnFamily(PagmpDao.PAGMP_CFNAME);
      rangeSlicesQuery.setRange("", "", false, PagmpDao.MAX_ATTRIBUTS);
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
      List<Pagmp> list = new ArrayList<Pagmp>();
      for (ColumnFamilyResult<String, String> row : resultIterator) {
         Pagmp pagmp = getPagmpFromResult(row);

         if (pagmp != null)
            list.add(pagmp);
      }
      return list;
   }

   private Pagmp getPagmpFromResult(ColumnFamilyResult<String, String> result) {

      Pagmp pagmp = null;

      if (result != null && result.hasResults()) {
         pagmp = new Pagmp();
         pagmp.setCode(result.getKey());
         pagmp.setDescription(result.getString(PagmpDao.PAGMP_DESCRIPTION));
         pagmp.setPrmd(result.getString(PagmpDao.PAGMP_PRMD));
      }

      return pagmp;
   }

}

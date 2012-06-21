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
import fr.urssaf.image.sae.droit.dao.PagmaDao;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.serializer.ListStringSerializer;

/**
 * Classe de support de la classe {@link PagmaDao}
 * 
 */
@Component
public class PagmaSupport {

   @Autowired
   private PagmaDao dao;

   /**
    * Méthode de création d'un ligne
    * 
    * @param pagma
    *           propriétés du PAGMa à créer
    * @param clock
    *           horloge de la création
    */
   public final void create(Pagma pagma, long clock) {

      ColumnFamilyUpdater<String, String> updater = dao.getPagmaTmpl()
            .createUpdater(pagma.getCode());

      dao.ecritActionsUnitaires(updater, pagma.getActionUnitaires(), clock);

      dao.getPagmaTmpl().update(updater);

   }

   /**
    * Méthode de suppression d'une ligne
    * 
    * @param code
    *           identifiant du PAGMa
    * @param clock
    *           horloge de suppression
    */
   public final void delete(String code, long clock) {

      Mutator<String> mutator = dao.createMutator();

      dao.mutatorSuppressionPagma(mutator, code, clock);

      mutator.execute();
   }

   /**
    * Méthode de lecture d'une ligne
    * 
    * @param code
    *           identifiant du PAGMa
    * @return un PAGMa correpodant à l'identifiant passé en paramètre
    */
   public final Pagma find(String code) {

      ColumnFamilyResult<String, String> result = dao.getPagmaTmpl()
            .queryColumns(code);

      Pagma pagma = getPagmaFromResult(result);

      return pagma;

   }
   
   /**
    * Lecture de toutes les lignes (attention aux performances)
    * 
    * @param maxKeysToRead
    *           nombre maximum d'enregistrements à récupérer
    * @return la liste de tous les PAGMa
    */
   public final List<Pagma> findAll(int maxKeysToRead) {
   // On n'utilise pas d'index. On récupère tous les jobs sans distinction,
      // en requêtant directement dans la CF JobRequest
      BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      RangeSlicesQuery<String, String, byte[]> rangeSlicesQuery = HFactory
            .createRangeSlicesQuery(dao.getKeyspace(), StringSerializer.get(),
                  StringSerializer.get(), bytesSerializer);
      rangeSlicesQuery.setColumnFamily(PagmaDao.PAGMA_CFNAME);
      rangeSlicesQuery.setRange("", "", false,
            PagmaDao.MAX_ATTRIBUTS);
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
      List<Pagma> list = new ArrayList<Pagma>();
      for (ColumnFamilyResult<String, String> row : resultIterator) {
         Pagma pagma = getPagmaFromResult(row);
         
         if (pagma != null)
            list.add(pagma);
      }
      return list;
   }

   private Pagma getPagmaFromResult(ColumnFamilyResult<String, String> result) {

      Pagma pagma = null;

      if (result != null && result.hasResults()) {
         pagma = new Pagma();
         pagma.setCode(result.getKey());
         ListStringSerializer serializer = new ListStringSerializer();
         List<String> list = serializer.fromBytes(result
               .getByteArray(PagmaDao.PAGMA_AU));
         pagma.setActionUnitaires(list);
      }

      return pagma;
   }
}

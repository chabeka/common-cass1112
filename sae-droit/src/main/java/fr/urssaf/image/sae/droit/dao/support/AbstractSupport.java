/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.support;

import java.util.ArrayList;
import java.util.List;

import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyResultWrapper;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import fr.urssaf.image.commons.cassandra.helper.HectorIterator;
import fr.urssaf.image.commons.cassandra.helper.QueryResultConverter;
import fr.urssaf.image.sae.commons.dao.AbstractDao;

/**
 * Classe de mère de support
 * 
 * @param <BOT>
 *           type back office de l'objet
 * @param <CFT>
 *           Type de l'identifiant de la ligne
 * @param <CT>
 *           Type de l'identifiant de la colonne
 * 
 */
public abstract class AbstractSupport<BOT, CFT, CT> {

   /**
    * @return la dao associé à la classe support
    */
   protected abstract AbstractDao<CFT, CT> getDao();

   /**
    * méthode de création d'une nouvelle ligne
    * 
    * @param object
    *           propriétés de l'objet à créer
    * @param clock
    *           horloge de la création
    */
   public abstract void create(BOT object, long clock);

   /**
    * Méthode de suppression d'une ligne
    * 
    * @param code
    *           identifiant de l'objet
    * @param clock
    *           horloge de la suppression
    */
   public final void delete(CFT code, long clock) {

      // Création du Mutator
      Mutator<CFT> mutator = getDao().createMutator();

      // suppression du JobRequest
      getDao().mutatorSuppressionLigne(mutator, code, clock);

      // Execution de la commande
      mutator.execute();

   }

   /**
    * Lecture d'une ligne
    * 
    * @param code
    *           identifiant de l'objet
    * @return l'objet
    */
   public final BOT find(CFT code) {

      ColumnFamilyResult<CFT, CT> result = getDao().getCfTmpl().queryColumns(
            code);

      BOT object = getObjectFromResult(result);

      return object;
   }

   /**
    * Lecture de toutes les lignes (attention aux performances)
    * 
    * @param maxKeysToRead
    *           nombre maximum d'enregistrements à récupérer
    * @return la liste de toutes les objets
    */
   public final List<BOT> findAll(int maxKeysToRead) {

      // On n'utilise pas d'index. On récupère tous les jobs sans distinction,
      // en requêtant directement dans la CF JobRequest
      BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      RangeSlicesQuery<CFT, CT, byte[]> rangeSlicesQuery = HFactory
            .createRangeSlicesQuery(getDao().getKeyspace(), getDao()
                  .getRowKeySerializer(), getDao().getColumnKeySerializer(),
                  bytesSerializer);

      rangeSlicesQuery.setColumnFamily(getDao().getColumnFamilyName());

      rangeSlicesQuery.setRange(getMin(), getMax(), false,
            AbstractDao.MAX_ATTRIBUTS);
      rangeSlicesQuery.setRowCount(maxKeysToRead);
      QueryResult<OrderedRows<CFT, CT, byte[]>> queryResult = rangeSlicesQuery
            .execute();

      // On convertit le résultat en ColumnFamilyResultWrapper pour faciliter
      // son utilisation
      QueryResultConverter<CFT, CT, byte[]> converter = new QueryResultConverter<CFT, CT, byte[]>();
      ColumnFamilyResultWrapper<CFT, CT> result = converter
            .getColumnFamilyResultWrapper(queryResult, getDao()
                  .getRowKeySerializer(), getDao().getColumnKeySerializer(),
                  bytesSerializer);

      // On itère sur le résultat
      HectorIterator<CFT, CT> resultIterator = new HectorIterator<CFT, CT>(
            result);
      List<BOT> list = new ArrayList<BOT>();
      for (ColumnFamilyResult<CFT, CT> row : resultIterator) {
         BOT actionUnitaire = getObjectFromResult(row);

         if (actionUnitaire != null)
            list.add(actionUnitaire);
      }
      return list;
   }

   /**
    * retourne un objet à partir du résultat
    * 
    * @param row
    *           la ligne en cours
    * @return un objet représentant la ligne
    */
   protected abstract BOT getObjectFromResult(ColumnFamilyResult<CFT, CT> row);

   /**
    * @return le minimum pour les recherches findAll
    */
   protected abstract CT getMin();

   /**
    * @return le maximum pour les recherches findAll
    */
   protected abstract CT getMax();

}

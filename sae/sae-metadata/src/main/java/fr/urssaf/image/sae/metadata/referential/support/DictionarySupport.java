package fr.urssaf.image.sae.metadata.referential.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.helper.HectorIterator;
import fr.urssaf.image.commons.cassandra.helper.QueryResultConverter;
import fr.urssaf.image.sae.commons.dao.AbstractDao;
import fr.urssaf.image.sae.metadata.exceptions.DictionaryNotFoundException;
import fr.urssaf.image.sae.metadata.referential.dao.DictionaryDao;
import fr.urssaf.image.sae.metadata.referential.model.Dictionary;
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

/**
 * classe permettant de réaliser les actions de manipulation des DAO pour la
 * famille de colonne "Dictionary"
 */

@Component
public class DictionarySupport {

   private final DictionaryDao dictionaryDao;

   /**
    * Constructeur de la classe support
    * 
    * @param dictionaryDao
    *           la dao
    */
   @Autowired
   public DictionarySupport(DictionaryDao dictionaryDao) {
      this.dictionaryDao = dictionaryDao;
   }

   /**
    * Ajout d'une entré au dictionnaire, le créé s'il n'existe pas
    * 
    * @param identifiant
    *           identifiant du dictionnaire
    * @param value
    *           valeur de l'entée
    * @param clock
    *           horloge de la colonne
    */
   public final void addElement(String identifiant, String value, long clock) {
      ColumnFamilyUpdater<String, String> updater = dictionaryDao.getCfTmpl()
            .createUpdater(identifiant);
      dictionaryDao.ecritElement(value, updater, clock);
      dictionaryDao.getCfTmpl().update(updater);
   }

   /**
    * Supprime une entré du dictionnaire
    * 
    * @param identifiant
    *           identifiant du dictionnaire
    * @param value
    *           Valeur de l'entrée à supprimer
    * @param clock
    *           Horloge de la colonne
    */
   public final void deleteElement(String identifiant, String value, long clock) {

      Mutator<String> mutator = dictionaryDao.createMutator();
      dictionaryDao.mutatorSuppressionColonne(mutator, identifiant, value,
            clock);
      mutator.execute();
   }

   /**
    * Retourne le dictionnaire avec l'identifiant passé en paramètre
    * 
    * @param identifiant
    *           identifiant du dictionnaire.
    * @return l'objet dictionnaire
    */
   public final Dictionary find(String identifiant) {

      ColumnFamilyResult<String, String> result = dictionaryDao.getCfTmpl()
            .queryColumns(identifiant);

      Dictionary dictionary = null;
      if (result == null || !result.hasResults()) {
         throw new DictionaryNotFoundException(identifiant);
      } else {
         dictionary = new Dictionary(result.getKey(), new ArrayList<String>(
               result.getColumnNames()));
      }

      return dictionary;
   }

   /**
    * Retourne l'ensemble des dictionnaires
    * 
    * @return Liste d'objet dictionnaire
    */
   public final List<Dictionary> findAll() {
      BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      RangeSlicesQuery<String, String, byte[]> rangeSlicesQuery = HFactory
            .createRangeSlicesQuery(dictionaryDao.getKeyspace(),
                  StringSerializer.get(), StringSerializer.get(),
                  bytesSerializer);
      rangeSlicesQuery.setColumnFamily(dictionaryDao.getColumnFamilyName());
      rangeSlicesQuery.setRange("", "", false, AbstractDao.DEFAULT_MAX_COLS);
      rangeSlicesQuery.setRowCount(AbstractDao.DEFAULT_MAX_ROWS);
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
      List<Dictionary> list = new ArrayList<Dictionary>();
      for (ColumnFamilyResult<String, String> row : resultIterator) {
         if (row != null && row.hasResults()) {
            ArrayList<String> valeurs = new ArrayList<String>(row
                  .getColumnNames());
            Dictionary dict = new Dictionary(row.getKey(), valeurs );
            list.add(dict);
         }

      }
      return list;
   }

}

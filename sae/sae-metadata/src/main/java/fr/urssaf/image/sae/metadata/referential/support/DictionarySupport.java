package fr.urssaf.image.sae.metadata.referential.support;

import java.util.ArrayList;
import java.util.List;

import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyResultWrapper;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.helper.HectorIterator;
import fr.urssaf.image.commons.cassandra.helper.QueryResultConverter;
import fr.urssaf.image.sae.metadata.exceptions.DictionaryNotFoundException;
import fr.urssaf.image.sae.metadata.referential.dao.DictionaryDao;
import fr.urssaf.image.sae.metadata.referential.model.Dictionary;

@Component
public class DictionarySupport {
/**
 * classe permettant de réaliser les actions de manipulation des DAO pour la famille de colonne "Dictionary"
 */
   
  private DictionaryDao dictionaryDao;
   
   private int MAX_FIND_RESULT = 2000;
   @Autowired
   public DictionarySupport(DictionaryDao dictionaryDao ){
      this.dictionaryDao = dictionaryDao;
   }
   
   /**
    * Ajout d'une entré au dictionnaire, le créé s'il n'existe pas
    * @param id identifiant du dictionnaire
    * @param value valeur de l'entée
    * @param clock horloge de la colonne
    */
   public void addElement(String id, String value, long clock){
      ColumnFamilyUpdater<String, String> updater = dictionaryDao.getCfTmpl()
      .createUpdater(id);
      dictionaryDao.ecritElement(value, updater, clock);
      updater.update();
   } 
   
   /**
    * Supprime une entré du dictionnaire
    * @param id identifiant du dictionnaire
    * @param value Valeur de l'entrée à supprimer
    * @param clock Horloge de la colonne
    */
   public void deleteElement(String id, String value, long clock){
      
      dictionaryDao.mutatorSuppressionColonne(dictionaryDao.createMutator(), id, value, clock);
   }
   
   
   /**
    * Retourne le dictionnaire avec l'identifiant passé en paramètre
    * @param id identifiant du dictionnaire.
    * @return l'objet dictionnaire
    * @throws DictionaryNotFoundException
    */
   public Dictionary find(String id) throws DictionaryNotFoundException{

      ColumnFamilyResult<String, String> result = dictionaryDao.getCfTmpl()
      .queryColumns(id);

      Dictionary dictionary =null;
      if (result != null && result.hasResults()) {
         dictionary = new Dictionary(result.getKey(), (List<String>) result.getColumnNames());
      }

      return dictionary;
   }
   
   
   /**
    * Retourne l'ensemble des dictionnaires
    * @return Liste d'objet dictionnaire
    */
   public List<Dictionary> findAll(){
      BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      RangeSlicesQuery<String, String, byte[]> rangeSlicesQuery = HFactory
            .createRangeSlicesQuery(dictionaryDao.getKeyspace(), StringSerializer.get(),
                  StringSerializer.get(), bytesSerializer);
      rangeSlicesQuery.setColumnFamily(dictionaryDao.getColumnFamilyName());
      rangeSlicesQuery.setRange("", "", false, MAX_FIND_RESULT);
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
            Dictionary dict = new Dictionary(row.getKey(), (List<String>) row.getColumnNames());
            list.add(dict);
         }

      }
      return list;
   }
   
}

package fr.urssaf.image.sae.extraitdonnees.iterator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.netflix.astyanax.query.AllRowsQuery;
import com.netflix.astyanax.serializers.StringSerializer;

import fr.urssaf.image.sae.extraitdonnees.dao.resultset.CassandraAllRowResultSet;
import fr.urssaf.image.sae.extraitdonnees.exception.CassandraException;
import fr.urssaf.image.sae.extraitdonnees.exception.ErreurTechniqueException;

/**
 * Iterateur bouclant sur le résultat de Cassandra
 * 
 * @param <E>
 *           le type de la clé de la CF
 * 
 */
public final class CassandraIterator<E> implements
      Iterator<Map<String, String>> {

   private final CassandraAllRowResultSet<E, String> resultSet;

   /**
    * Constructeur
    * 
    * @param query
    *           la requête
    */
   public CassandraIterator(AllRowsQuery<E, String> query) {
      resultSet = new CassandraAllRowResultSet<E, String>(query);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean hasNext() {
      return resultSet.hasNext();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Map<String, String> next() {
      resultSet.next();
      Map<String, String> infos = null;

      try {
         List<String> columns = resultSet.getColumnNames();
         infos = new HashMap<String, String>();
         for (String columnKey : columns) {
            infos.put(columnKey, resultSet.getValue(columnKey, StringSerializer
                  .get(), null));
         }

      } catch (CassandraException exception) {
         throw new ErreurTechniqueException(exception);
      }

      return infos;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void remove() {
      throw new ErreurTechniqueException("Fonctionnalité non implémentée");

   }

}

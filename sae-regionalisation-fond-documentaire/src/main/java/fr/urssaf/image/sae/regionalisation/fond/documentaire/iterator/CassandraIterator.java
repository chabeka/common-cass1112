package fr.urssaf.image.sae.regionalisation.fond.documentaire.iterator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.netflix.astyanax.query.AllRowsQuery;
import com.netflix.astyanax.serializers.StringSerializer;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.resultset.CassandraAllRowResultSet;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.exception.CassandraException;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.exception.ErreurTechniqueException;

/**
 * Iterateur bouclant sur le résultat de Cassandra
 * 
 */
public class CassandraIterator<E> implements Iterator<Map<String, String>> {

   private CassandraAllRowResultSet<E, String> resultSet;

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

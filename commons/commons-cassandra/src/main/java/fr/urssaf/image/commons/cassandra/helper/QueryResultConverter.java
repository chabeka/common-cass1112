package fr.urssaf.image.commons.cassandra.helper;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnOrSuperColumn;

import me.prettyprint.cassandra.model.ExecutionResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyResultWrapper;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.query.QueryResult;

/**
 * Classe utilitaire permettant de convertir un QueryResult en ColumnFamilyResultWrapper
 * Utile lorsque l'on veut mixer dans le code les méthodes de "bas niveau" d'hector
 * avec les méthodes de haut niveau à base de template
 * 
 * @param <K>  Type de la clé
 * @param <N>  Type des noms de colonnes
 * @param <V>  Types des valeur de colonnes
 */
public class QueryResultConverter<K, N, V> {

   /**
    * Convertit un QueryResult en ColumnFamilyResultWrapper
    * @param queryResult               : le queryResult à convertir
    * @param keySerializer             : le sérialiser de clé
    * @param columnNameSerializer      : le sérialiseur de nom de colonnes
    * @param valueSerializer           : le sérialiser de valeurs de colonnes
    * @return  le ColumnFamilyResultWrapper
    */
   public final ColumnFamilyResultWrapper<K, N> getColumnFamilyResultWrapper(
         QueryResult<OrderedRows<K, N, V>> queryResult, Serializer<K> keySerializer,
         Serializer<N> columnNameSerializer, Serializer<V> valueSerializer) {

      OrderedRows<K, N, V> orderedRows = queryResult.get();
      Map<ByteBuffer, List<ColumnOrSuperColumn>> map = new HashMap<ByteBuffer, List<ColumnOrSuperColumn>>(orderedRows.getCount());
      for (Row<K, N, V> row : orderedRows) {
         K key = row.getKey();
         ColumnSlice<N, V> columnSlice = row.getColumnSlice();
         List<HColumn<N, V>> cols = columnSlice.getColumns();
         List<ColumnOrSuperColumn> list = new ArrayList<ColumnOrSuperColumn>(cols.size());
         for (HColumn<N, V> hColumn : cols) {
            ColumnOrSuperColumn cosp = new ColumnOrSuperColumn();
            Column c = new Column();
            c.name = columnNameSerializer.toByteBuffer(hColumn.getName());
            c.value = valueSerializer.toByteBuffer(hColumn.getValue());
            c.timestamp = hColumn.getClock();
            c.ttl = hColumn.getTtl();
            cosp.setColumn(c);
            list.add(cosp);
         }
         map.put(keySerializer.toByteBuffer(key), list);
      }
      ExecutionResult<Map<ByteBuffer, List<ColumnOrSuperColumn>>> executionResult = new ExecutionResult<Map<ByteBuffer, List<ColumnOrSuperColumn>>>(
            map, queryResult.getExecutionTimeMicro(), queryResult.getHostUsed());

      return new ColumnFamilyResultWrapper<K, N>(keySerializer, columnNameSerializer, executionResult);

   }
}

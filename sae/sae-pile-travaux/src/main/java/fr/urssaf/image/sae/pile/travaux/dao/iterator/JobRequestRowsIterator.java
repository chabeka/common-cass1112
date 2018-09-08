package fr.urssaf.image.sae.pile.travaux.dao.iterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import org.apache.cassandra.thrift.Column;

import me.prettyprint.cassandra.model.HColumnImpl;
import me.prettyprint.cassandra.model.OrderedRowsImpl;
import me.prettyprint.cassandra.model.QueryResultImpl;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResultWrapper;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import fr.urssaf.image.commons.cassandra.helper.HectorIterator;
import fr.urssaf.image.commons.cassandra.helper.QueryResultConverter;
import fr.urssaf.image.sae.pile.travaux.dao.JobRequestDao;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;

/**
 * Itérateur permettant d'itérer sur rows de la CF JobRequest.
 *
 */
public class JobRequestRowsIterator implements Iterator<JobRequest> {

   private UUID start;

   private int count;

   private RangeSlicesQuery<UUID, String, byte[]> query;

   private boolean isLastIteration;
   
   private QueryResultConverter<UUID, String, byte[]> converter = new QueryResultConverter<UUID, String, byte[]>();
   
   HectorIterator<UUID, String> resultIterator;

   private JobRequestDao jobRequestDao;

   /**
    * Constructeur de l'iterateur.
    * 
    * @param query
    *           RangeSlicesQuery
    * @param count
    *           nombre rows par iteration
    * @param jobRequestDao
    *           objet dao de la JobRequest
    */
   public JobRequestRowsIterator(RangeSlicesQuery<UUID, String, byte[]> query,
         int count, JobRequestDao jobRequestDao) {
      this.start = null;
      this.count = 10000;
      this.query = query;
      this.isLastIteration = false;
      this.jobRequestDao = jobRequestDao;
   }

   /**
    * Methode permettant de recuperer l'iterateur.
    * 
    * @return Iterator
    */
   public Iterator<JobRequest> iterator() {
      return this;
   }

   /**
    * Methode permettant de savoir s'il existe un element suivant.
    * 
    * @return boolean
    */
   @Override
   public boolean hasNext() {
      if (resultIterator == null || !resultIterator.hasNext()) {
         if (isLastIteration)
            return false;

         if (!fetchMore())
            return false;
      }
      return true;
   }

   /**
    * Methode permettant d'avoir l'element suivant.
    * 
    * @return JobRequest
    */
   @Override
   public JobRequest next() {
      return jobRequestDao.createJobRequestFromResult(resultIterator.next());
   }

   /**
    * Methode privee permettant d'avoir plus d'element.
    * 
    * @return boolean
    */
   private boolean fetchMore() {
      try {
         query.setKeys(start, null);
         query.setRowCount(count);
         QueryResult<OrderedRows<UUID, String, byte[]>> queryResult = query
               .execute();
         OrderedRows<UUID, String, byte[]> slice = queryResult.get();
         List<Row<UUID, String, byte[]>> rows = slice.getList();
         int origSize = rows.size();

         if (origSize == 0) {
            return false;
         }
         
         if (origSize >= count)
            start = rows.remove(rows.size()-1).getKey();

         // comme il y a une row en moins, le query result n'est pas le bon, on va en recreer un de toute piece
         QueryResult<OrderedRows<UUID, String, byte[]>> newResult = createNewQueryResult(
               queryResult, rows);
         
         // ensuite, on utilise le query result cree pour le wrapper 
         ColumnFamilyResultWrapper<UUID, String> result = converter
               .getColumnFamilyResultWrapper(newResult, UUIDSerializer.get(),
                     StringSerializer.get(), BytesArraySerializer.get());
         
         // et recuperer un iterator utilisable par le JobRequestDao#createJobRequestFromResult
         resultIterator  = new HectorIterator<UUID, String>(
               result);

         if (origSize < count)
            isLastIteration = true;

         return true;
      } catch (HectorException e) {
         return false;
      }
   }

   /**
    * Methode permettant de creer un nouveau query result a partir d'un ancien
    * @param queryResult query result initial
    * @param rows liste des rows restantes
    * @return QueryResult<OrderedRows<UUID, String, byte[]>>
    */
   private QueryResult<OrderedRows<UUID, String, byte[]>> createNewQueryResult(
         QueryResult<OrderedRows<UUID, String, byte[]>> queryResult,
         List<Row<UUID, String, byte[]>> rows) {
      
      // tout d'abord, il faut creer une map contenant la liste des colonnes pour chaque row
      LinkedHashMap<UUID, List<Column>> map = new LinkedHashMap<UUID, List<Column>>();
      for (Row<UUID, String, byte[]> row : rows) {
         List<Column> cols = new ArrayList<Column>();
         for (HColumn<String, byte[]> colonne : row.getColumnSlice().getColumns()) {
            cols.add(((HColumnImpl<String, byte[]>) colonne).toThrift());
         }
         map.put(row.getKey(), cols);
      }
      
      // ensuite creer l'objet ordered rows
      OrderedRows<UUID, String, byte[]> newSlice = new OrderedRowsImpl<UUID, String, byte[]>(map, StringSerializer.get(), BytesArraySerializer.get());
      
      // et enfin l'objet query result
      QueryResult<OrderedRows<UUID, String, byte[]>> newResult = new QueryResultImpl<OrderedRows<UUID, String, byte[]>>(
            newSlice, queryResult.getExecutionTimeNano(),
            queryResult.getHostUsed(), query);
      return newResult;
   }

   @Override
   public void remove() {
      throw new UnsupportedOperationException();
   }

}

package fr.urssaf.hectotest;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.prettyprint.cassandra.model.CqlRows;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.CompositeSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.Composite;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.HSuperColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.beans.SuperSlice;
import me.prettyprint.hector.api.beans.AbstractComposite.Component;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SuperSliceQuery;

public class Dumper {

   Keyspace keyspace;
   PrintStream sysout;
   public boolean printKeyInHex = false;
   public boolean printKeyInLong = false;
   public boolean printColumnNameInHex = false;
   public boolean printColumnNameInComposite = false;
   public boolean deserializeValue = false;
   /**
    * On n'affiche que les maxValueLenght 1er caractères
    */
   public int maxValueLenght = 200;

   /**
    * Si on affiche le nom de colonne en mode "composite", on peut indiquer dans
    * ce tableau pour chaque élément du composite s'il faut l'afficher en
    * hexadécimal
    */
   public boolean[] compositeDisplayTypeMapper;

   private SimpleDateFormat SIMPLE_DF = new SimpleDateFormat("dd/MM/yyyy");

   private SimpleDateFormat SIMPLE_HF = new SimpleDateFormat("HH:mm:ss");

   public Dumper(Keyspace k, PrintStream p) {
      keyspace = k;
      sysout = p;
   }

   public List<byte[]> getKeys(String CFName, int count) throws Exception {
      return getKeys(CFName, "", count);
   }

   public List<byte[]> getKeys(String CFName, String startKey, int count)
         throws Exception {
      return getKeys(CFName, ConvertHelper.stringToBytes(startKey), count);
   }

   public List<byte[]> getKeys(String CFName, byte[] startKey, int count)
         throws Exception {
      StringSerializer stringSerializer = StringSerializer.get();
      BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      RangeSlicesQuery<byte[], String, byte[]> rangeSlicesQuery = HFactory
            .createRangeSlicesQuery(keyspace, bytesSerializer,
                  stringSerializer, bytesSerializer);

      rangeSlicesQuery.setColumnFamily(CFName);
      // rangeSlicesQuery.setRange("", "", false, count);
      rangeSlicesQuery.setColumnNames("toto");
      // rangeSlicesQuery.setKeys(startKey, "");
      byte[] endBytes = new byte[0];
      rangeSlicesQuery.setKeys(startKey, endBytes);
      rangeSlicesQuery.setRowCount(count);
      rangeSlicesQuery.setReturnKeysOnly();
      QueryResult<OrderedRows<byte[], String, byte[]>> result = rangeSlicesQuery
            .execute();

      OrderedRows<byte[], String, byte[]> orderedRows = result.get();
      // sysout.println("Count " + orderedRows.getCount());
      ArrayList<byte[]> list = new ArrayList<byte[]>(count);
      for (Row<byte[], String, byte[]> row : orderedRows) {
         byte[] key = row.getKey();
         // String t = ConvertHelper.stringToHex(key);
         list.add(key);
         // list.add(key);
         // sysout.println("Key : " + key);
      }
      return list;
   }

   /**
    * Compte le nombre total de clés d'une CF
    * 
    * @param CFName
    * @return
    * @throws Exception
    */
   public int getKeysCount(String CFName) throws Exception {
      StringSerializer stringSerializer = StringSerializer.get();
      BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      RangeSlicesQuery<String, String, byte[]> rangeSlicesQuery = HFactory
            .createRangeSlicesQuery(keyspace, stringSerializer,
                  stringSerializer, bytesSerializer);
      rangeSlicesQuery.setColumnFamily(CFName);
      int blockSize = 1000;
      String startKey = "";
      int total = 1;
      int count;
      do {
         rangeSlicesQuery.setRange("", "", false, 1);
         rangeSlicesQuery.setKeys(startKey, "");
         rangeSlicesQuery.setRowCount(blockSize);
         rangeSlicesQuery.setReturnKeysOnly();
         QueryResult<OrderedRows<String, String, byte[]>> result = rangeSlicesQuery
               .execute();
         OrderedRows<String, String, byte[]> orderedRows = result.get();
         count = orderedRows.getCount();
         // On enlève 1, car sinon à chaque itération, la startKey serait
         // comptée deux fois.
         total += count - 1;
         sysout.print(" " + total);
         // Parcours des rows pour déterminer la dernière clé de l'ensemble
         Row<String, String, byte[]> lastRow = orderedRows.peekLast();
         startKey = lastRow.getKey();

      } while (count == blockSize);
      return total;
   }

   public void dumpCF(String CFName, int count) throws Exception {
      dumpCF(CFName, count, false);
   }

   /**
    * Dump une column family
    * 
    * @param CFName
    * @param count
    * @throws Exception
    */
   public void dumpCF(String CFName, int count, boolean siAuMoinsUneCol)
         throws Exception {
      dumpCF_slice(CFName, new byte[0], new byte[0], count, siAuMoinsUneCol);
   }

   public void dumpCF_slice(String CFName, byte[] sliceStart, byte[] sliceEnd,
         int count) throws Exception {
      dumpCF_slice(CFName, sliceStart, sliceEnd, count, false);
   }

   public void dumpCF_slice(String CFName, byte[] sliceStart, byte[] sliceEnd,
         int count, boolean siAuMoinsUneCol) throws Exception {
      BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      RangeSlicesQuery<byte[], byte[], byte[]> rangeSlicesQuery = HFactory
            .createRangeSlicesQuery(keyspace, bytesSerializer, bytesSerializer,
                  bytesSerializer);
      rangeSlicesQuery.setColumnFamily(CFName);
      // rangeSlicesQuery.setKeys(
      // "SrMd8I1c￿0002dfa8-4085-45f9-9554-d036b105c968", "");
      rangeSlicesQuery.setRange(sliceStart, sliceEnd, false, count);
      rangeSlicesQuery.setRowCount(count);
      QueryResult<OrderedRows<byte[], byte[], byte[]>> result = rangeSlicesQuery
            .execute();
      dumpQueryResult(result, siAuMoinsUneCol);
   }

   public void dumpCF(String CFName, byte[] key) throws Exception {
      dumpCF(CFName, key, false);
   }

   /**
    * Dump les colonnes d'une column family pour une clé donnée
    * 
    * @param CFName
    * @param key
    * @throws Exception
    */
   public void dumpCF(String CFName, byte[] key, boolean siAuMoinsUneCol)
         throws Exception {
      dumpCF_slice(CFName, key, new byte[0], new byte[0], 1000, siAuMoinsUneCol);
   }

   public byte[] getColumnValue(String CFName, byte[] key, byte[] columnName) {

      ColumnQuery<byte[], byte[], byte[]> columnQuery = HFactory
            .createColumnQuery(keyspace, BytesArraySerializer.get(),
                  BytesArraySerializer.get(), BytesArraySerializer.get());
      columnQuery.setColumnFamily(CFName).setKey(key).setName(columnName);
      QueryResult<HColumn<byte[], byte[]>> result = columnQuery.execute();
      if (result.get() == null)
         return null;
      return result.get().getValue();
   }

   /**
    * Dump un ensemble (un slice) de colonnes d'une column family pour une clé
    * donnée
    * 
    * @param CFName
    * @param key
    * @throws Exception
    */
   public void dumpCF_slice(String CFName, byte[] key, byte[] sliceStart,
         byte[] sliceEnd, int count, boolean siAuMoinsUneCol) throws Exception {
      BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      RangeSlicesQuery<byte[], byte[], byte[]> rangeSlicesQuery = HFactory
            .createRangeSlicesQuery(keyspace, bytesSerializer, bytesSerializer,
                  bytesSerializer);
      rangeSlicesQuery.setColumnFamily(CFName);
      rangeSlicesQuery.setKeys(key, key);
      rangeSlicesQuery.setRange(sliceStart, sliceEnd, false, count);
      QueryResult<OrderedRows<byte[], byte[], byte[]>> result = rangeSlicesQuery
            .execute();
      dumpQueryResult(result, siAuMoinsUneCol);
   }

   public void dumpCF(String CFName, String key) throws Exception {
      dumpCF(CFName, key.getBytes(), false);
   }

   public void dumpCF(String CFName, String key, boolean siAuMoinsUneCol)
         throws Exception {
      dumpCF(CFName, key.getBytes(), siAuMoinsUneCol);
   }

   public void dumpCF_StartKey(String CFName, byte[] startKey, int count)
         throws Exception {
      dumpCF_StartKey(CFName, startKey, count, false);
   }

   public void dumpCF_StartKey(String CFName, byte[] startKey, int count,
         boolean siAuMoinsUneCol) throws Exception {
      List<byte[]> keys = getKeys(CFName, startKey, count);
      for (byte[] key : keys) {
         dumpCF(CFName, key, siAuMoinsUneCol);
      }
   }

   public void dumpCqlQueryResult(
         QueryResult<CqlRows<byte[], byte[], byte[]>> result) throws Exception {
      dumpCqlQueryResult(result, false);
   }

   public void dumpCqlQueryResult(
         QueryResult<CqlRows<byte[], byte[], byte[]>> result,
         boolean siAuMoinsUneCol) throws Exception {
      CqlRows<byte[], byte[], byte[]> orderedRows = result.get();
      if (orderedRows != null) {
         sysout.println("Count " + orderedRows.getCount());
         dumpRows(orderedRows, siAuMoinsUneCol);
      } else {
         sysout.println("Pas de résultat trouvé");
      }
   }

   private void dumpQueryResult(
         QueryResult<OrderedRows<byte[], byte[], byte[]>> result,
         boolean siAuMoinsUneCol) throws Exception {
      OrderedRows<byte[], byte[], byte[]> orderedRows = result.get();
      sysout.println("Count " + orderedRows.getCount());
      sysout.println();
      long nbRowsAvecCols = dumpRows(orderedRows, siAuMoinsUneCol);
      sysout.println();
      sysout.println("Count " + orderedRows.getCount());
      sysout.println("Count des lignes avec au moins 1 colonne : "
            + nbRowsAvecCols);
   }

   private long dumpRows(Iterable<Row<byte[], byte[], byte[]>> orderedRows,
         boolean siAuMoinsUneCol) throws Exception {
      int result = 0;
      for (Row<byte[], byte[], byte[]> row : orderedRows) {

         ColumnSlice<byte[], byte[]> columnSlice = row.getColumnSlice();
         List<HColumn<byte[], byte[]>> columns = columnSlice.getColumns();

         if (columns.size() > 0) {
            result++;
         }

         if ((!siAuMoinsUneCol) || (siAuMoinsUneCol && (columns.size() > 0))) {

            if (printKeyInHex) {
               String key = ConvertHelper.getHexString(row.getKey());
               sysout.println("Key (hex) : " + key);
            } else if (printKeyInLong) {
               Long key = LongSerializer.get().fromBytes(row.getKey());
               sysout.println("Key (long) : " + key);
            } else {
               String key = ConvertHelper.getReadableUTF8String(row.getKey());
               sysout.println("Key : " + key);
            }

            dumpColumns(columns);
            
            sysout.println();

         }
         
      }
      return result;
   }

   private void dumpColumns(List<HColumn<byte[], byte[]>> columns)
         throws Exception {
      System.out.println("Nombre de colonnes : " + columns.size());
      for (HColumn<byte[], byte[]> column : columns) {
         String s;
         if (printColumnNameInHex) {
            s = "Name (hex) : " + ConvertHelper.getHexString(column.getName());
         } else if (printColumnNameInComposite) {
            s = "Name (composite) : (";
            Composite comp = new CompositeSerializer().fromBytes(column
                  .getName());
            int i = 0;
            for (Component<?> c : comp.getComponents()) {
               ByteBuffer buffer = (ByteBuffer) c.getValue();
               if (buffer == null)
                  s += "(null)";
               else {
                  byte[] bytes = new byte[buffer.remaining()];
                  buffer.get(bytes);
                  if (compositeDisplayTypeMapper != null
                        && compositeDisplayTypeMapper[i++] == true) {
                     s += "(hex)" + ConvertHelper.getHexString(bytes);
                  } else {
                     s += ConvertHelper.getReadableUTF8String(bytes);
                  }
               }
               s += "   ";
            }
            s += ")";
         } else {
            s = "Name : "
                  + ConvertHelper.getReadableUTF8String(column.getName());
         }
         byte[] value = column.getValue();

         if (deserializeValue) {
            ByteArrayInputStream bis = new ByteArrayInputStream(value);
            ObjectInputStream ois = new ObjectInputStream(bis);
            Object o = ois.readObject();
            s += " - DeseriazedValue : " + o.toString();
         } else {
            if (value.length > maxValueLenght) {
               byte[] dst = new byte[maxValueLenght];
               System.arraycopy(value, 0, dst, 0, maxValueLenght);
               value = dst;
            }
            // int ttl = column.getTtl();
            // s += " - ttl : " + ttl;
            String stringValue = ConvertHelper.getReadableUTF8String(value);
            s += " - StringValue : " + stringValue;
            if (value.length <= 4) {
               int intValue = ConvertHelper.byteArrayToInt(value, 0);
               s += " - IntValue : " + intValue;
            } else if (value.length <= 8) {
               long longValue = ConvertHelper.byteArrayToLong(value);
               s += " - longValue : " + longValue;
               Date date = new java.util.Date(longValue);
               DateFormat dateFormat = new SimpleDateFormat(
                     "dd/MM/yyyy HH:mm:ss");
               s += " - dateValue : '" + dateFormat.format(date) + "'";
            }
            String hexValue = ConvertHelper.getHexString(value);
            s += " - hexValue : " + hexValue;
         }

         // On ajoute le timestamp Cassandra de la colonne
         s += " - clock : " + convertClockToDateString(column.getClock());

         sysout.println(s);
      }
   }

   /**
    * Dump une super column family
    * 
    * @param CFName
    *           nom de la super column family à dumper
    * @param count
    *           nombre de lignes à dumper
    * @throws Exception
    */
   public void dumpSCF(String CFName, int count) throws Exception {
      List<byte[]> keys = getKeys(CFName, count);
      for (byte[] key : keys) {
         dumpSCFRow(CFName, key, count);
      }
   }

   /**
    * Dump une super column family
    * 
    * @param CFName
    *           nom de la super column family à dumper
    * @param startKey
    *           clé de départ
    * @param count
    *           nombre de lignes à dumper
    * @throws Exception
    */
   public void dumpSCF(String CFName, String startKey, int count)
         throws Exception {
      List<byte[]> keys = getKeys(CFName, startKey, count);
      for (byte[] key : keys) {
         dumpSCFRow(CFName, key, count);
      }
   }

   /**
    * Dump une super column family
    * 
    * @param CFName
    *           nom de la super column family à dumper
    * @param startKey
    *           clé de départ
    * @param count
    *           nombre de lignes à dumper
    * @param scNameFilter
    *           seules les super-colonnes dont le nom contient scNameFilter
    *           seront affichées
    * @throws Exception
    */
   public void dumpSCF(String CFName, byte[] startKey, int count,
         String scNameFilter) throws Exception {
      List<byte[]> keys = getKeys(CFName, startKey, count);
      for (byte[] key : keys) {
         dumpSCFRow(CFName, key, count, scNameFilter);
      }
   }

   /**
    * Dump une ligne d'une super column family
    * 
    * @param CFName
    * @param count
    * @throws Exception
    */
   public void dumpSCFRow(String CFName, byte[] key, int count)
         throws Exception {
      dumpSCFRow(CFName, key, count, null);
   }

   /**
    * Dump une ligne d'une super column family
    * 
    * @param CFName
    * @param count
    * @param scNameFilter
    *           : si non null : on affiche que les super-colonne dont le nom
    *           contient scNameFilter
    * @throws Exception
    */
   public void dumpSCFRow(String CFName, byte[] key, int count,
         String scNameFilter) throws Exception {
      StringSerializer stringSerializer = StringSerializer.get();
      BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      SuperSliceQuery<byte[], String, byte[], byte[]> superSliceQuery = HFactory
            .createSuperSliceQuery(keyspace, bytesSerializer, stringSerializer,
                  bytesSerializer, bytesSerializer);
      superSliceQuery.setColumnFamily(CFName);
      superSliceQuery.setRange("", "", false, count);
      superSliceQuery.setKey(key);
      QueryResult<SuperSlice<String, byte[], byte[]>> result = superSliceQuery
            .execute();
      SuperSlice<String, byte[], byte[]> superSlice = result.get();

      List<HSuperColumn<String, byte[], byte[]>> superColumns = superSlice
            .getSuperColumns();
      if (scNameFilter == null) {
         sysout.println("Clé de la super row : " + key);
         sysout.println("Nombre de super colonnes : " + superColumns.size());
      }

      // Parcours des super colonnes
      for (HSuperColumn<String, byte[], byte[]> superColumn : superColumns) {
         String superColumnName = superColumn.getName();
         if (scNameFilter != null) {
            if (!superColumnName.contains(scNameFilter)) {
               break;
            } else {
               sysout.println("Clé de la super row : " + key);
               sysout.println("Nombre de super colonnes : "
                     + superColumns.size());
            }
         }
         sysout.println("SuperName : " + superColumnName);
         List<HColumn<byte[], byte[]>> columns = superColumn.getColumns();

         // Dump des colonnes
         dumpColumns(columns);
      }
      // sysout.println(result.getHostUsed().getHost());
   }

   private String convertClockToDateString(long timestamp) {

      // NB: les timestamp Cassandra sont en microsecondes
      // Il faut les convertir en millisecondes pour la date

      Date laDate = new Date(timestamp / 1000);

      String date = SIMPLE_DF.format(laDate);
      String heure = SIMPLE_HF.format(laDate);

      return date + " " + heure;

   }

}

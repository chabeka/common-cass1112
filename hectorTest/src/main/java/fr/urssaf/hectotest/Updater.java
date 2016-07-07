package fr.urssaf.hectotest;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Date;

import me.prettyprint.cassandra.serializers.BooleanSerializer;
import me.prettyprint.cassandra.serializers.DateSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;

public class Updater {

   Keyspace keyspace;
   PrintStream sysout;

   public Updater(Keyspace k, PrintStream p) {
      keyspace = k;
      sysout = p;
   }

   public void updateColumn(String CFName, String rowName, String columnName,
         Object value) throws Exception {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, CFName, StringSerializer.get(), StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater = cfTmpl
            .createUpdater(rowName);

      Collection<String> columnNames = cfTmpl.queryColumns(rowName)
            .getColumnNames();

      if (columnNames.contains(columnName)) {

         if (value != null && value instanceof String) {
            HColumn<String, String> column = HFactory.createColumn(columnName,
                  (String) value, StringSerializer.get(),
                  StringSerializer.get());
            updater.setColumn(column);
            cfTmpl.update(updater);
         } else if (value != null && value instanceof Long) {
            HColumn<String, Long> column = HFactory.createColumn(columnName,
                  (Long) value, StringSerializer.get(), LongSerializer.get());
            updater.setColumn(column);
            cfTmpl.update(updater);
         } else if (value != null && value instanceof Date) {
            HColumn<String, Date> column = HFactory.createColumn(columnName,
                  (Date) value, StringSerializer.get(), DateSerializer.get());
            updater.setColumn(column);
            cfTmpl.update(updater);
         } else if (value != null && value instanceof Boolean) {
            HColumn<String, Boolean> column = HFactory.createColumn(columnName,
                  (Boolean) value, StringSerializer.get(),
                  BooleanSerializer.get());
            updater.setColumn(column);
            cfTmpl.update(updater);
         } else if (value != null) {
            sysout.println("Type de valeur non prise en charge : "
                  + value.getClass().getName());
         }

      } else {
         sysout.println("Column " + columnName + " inexistante pour la key "
               + rowName + " dans la CF " + CFName);
      }
   }

   public void updateColumn(String CFName, String rowName, String columnName,
         Object value, ObjectSerializer serializer) throws Exception {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, CFName, StringSerializer.get(), StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater = cfTmpl
            .createUpdater(rowName);

      Collection<String> columnNames = cfTmpl.queryColumns(rowName)
            .getColumnNames();

      if (columnNames.contains(columnName)) {

         if (value != null) {
            HColumn<String, Object> column = HFactory.createColumn(columnName,
                  value, StringSerializer.get(),
                  ObjectSerializer.get());
            updater.setColumn(column);
            cfTmpl.update(updater);
            
         }
            
      } else {
         sysout.println("Column " + columnName + " inexistante pour la key "
               + rowName + " dans la CF " + CFName);
      }
   }
}

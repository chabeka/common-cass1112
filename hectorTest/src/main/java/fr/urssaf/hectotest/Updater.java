package fr.urssaf.hectotest;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Date;

import org.junit.Assert;

import me.prettyprint.cassandra.serializers.BooleanSerializer;
import me.prettyprint.cassandra.serializers.DateSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;

public class Updater {

   Keyspace keyspace;
   PrintStream sysout;

   public Updater(final Keyspace k, final PrintStream p) {
      keyspace = k;
      sysout = p;
   }

   public void updateColumn(final String CFName, final String rowName, final String columnName,
                            final Object value) throws Exception {
      final ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<>(
            keyspace, CFName, StringSerializer.get(), StringSerializer.get());

      final ColumnFamilyUpdater<String, String> updater = cfTmpl
            .createUpdater(rowName);

      final Collection<String> columnNames = cfTmpl.queryColumns(rowName)
            .getColumnNames();

      if (columnNames.contains(columnName)) {

         if (value != null && value instanceof String) {
            final HColumn<String, String> column = HFactory.createColumn(columnName,
                                                                         (String) value, StringSerializer.get(),
                                                                         StringSerializer.get());
            updater.setColumn(column);
            cfTmpl.update(updater);
         } else if (value != null && value instanceof Long) {
            final HColumn<String, Long> column = HFactory.createColumn(columnName,
                                                                       (Long) value, StringSerializer.get(), LongSerializer.get());
            updater.setColumn(column);
            cfTmpl.update(updater);
         } else if (value != null && value instanceof Date) {
            final HColumn<String, Date> column = HFactory.createColumn(columnName,
                                                                       (Date) value, StringSerializer.get(), DateSerializer.get());
            updater.setColumn(column);
            cfTmpl.update(updater);
         } else if (value != null && value instanceof Boolean) {
            final HColumn<String, Boolean> column = HFactory.createColumn(columnName,
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

   public void updateColumn(final String CFName, final String rowName, final String columnName,
                            final Object value, final ObjectSerializer serializer) throws Exception {
      final ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<>(
            keyspace, CFName, StringSerializer.get(), StringSerializer.get());

      final ColumnFamilyUpdater<String, String> updater = cfTmpl
            .createUpdater(rowName);

      final Collection<String> columnNames = cfTmpl.queryColumns(rowName)
            .getColumnNames();

      if (columnNames.contains(columnName)) {

         if (value != null) {
            final HColumn<String, Object> column = HFactory.createColumn(columnName,
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

   public void addColumn(final String CFName, final String rowName, final String columnName,
                         final Object value) throws Exception {
      final ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<>(
            keyspace, CFName, StringSerializer.get(), StringSerializer.get());

      final ColumnFamilyUpdater<String, String> updater = cfTmpl
            .createUpdater(rowName);

      if (value != null) {
         final HColumn<String, Object> column = HFactory.createColumn(columnName,
                                                                      value, StringSerializer.get(), ObjectSerializer.get());
         updater.setColumn(column);
         cfTmpl.update(updater);

      }

      Assert.assertTrue(cfTmpl.queryColumns(rowName).getColumnNames()
                        .contains(columnName));
   }

   public void addColumn(final String CFName, final String rowName, final String columnName,
                         final String value)
         throws Exception {
      final ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<>(
                                                                                                         keyspace,
                                                                                                         CFName,
                                                                                                         StringSerializer.get(),
                                                                                                         StringSerializer.get());

      final ColumnFamilyUpdater<String, String> updater = cfTmpl
                                                                .createUpdater(rowName);

      if (value != null) {
         final HColumn<String, String> column = HFactory.createColumn(columnName,
                                                                      value,
                                                                      StringSerializer.get(),
                                                                      StringSerializer.get());
         updater.setColumn(column);
         cfTmpl.update(updater);
      }

      Assert.assertTrue(cfTmpl.queryColumns(rowName)
                              .getColumnNames()
                              .contains(columnName));
   }

   public void deleteRows(final String CFName, final String rowName) {
      final ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<>(
            keyspace, CFName, StringSerializer.get(), StringSerializer.get());
      if (rowName != null) {
         cfTmpl.deleteRow(rowName);

      }

      Assert.assertNull(cfTmpl.queryColumns(rowName).getKey());

   }
}

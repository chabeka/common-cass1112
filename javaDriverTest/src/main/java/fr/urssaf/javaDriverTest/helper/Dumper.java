package fr.urssaf.javaDriverTest.helper;

import java.io.PrintStream;
import java.nio.ByteBuffer;

import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ColumnDefinitions.Definition;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

public class Dumper {
   PrintStream sysout;

   public Dumper(final PrintStream p) {
      sysout = p;
   }

   public void dumpRows(final ResultSet rs) {
      int counter = 0;
      for (final Row row : rs) {
         dumpRow(row);
         sysout.println();
         counter++;
      }
      sysout.println("Nombre de lignes dumpées : " + counter);
   }

   public void dumpRow(final Row row) {
      final ColumnDefinitions colDefinitions = row.getColumnDefinitions();
      for (final Definition colDefinition : colDefinitions) {
         final String colName = colDefinition.getName();
         final DataType colType = colDefinition.getType();
         final String valueAsString = getColAsString(row, colDefinition);
         sysout.println(colName + "(" + colType + ") " + " = " + valueAsString);
      }
   }

   public static String getColAsString(final Row row, final Definition colDefinition) {
      final String colName = colDefinition.getName();
      final Object valueAsObject = row.getObject(colName);
      if (valueAsObject instanceof ByteBuffer) {
         final ByteBuffer valueAsByteBuffer = (ByteBuffer) valueAsObject;
         return new String(valueAsByteBuffer.array());
      }
      return valueAsObject == null ? "NULL" : valueAsObject.toString();
   }
}

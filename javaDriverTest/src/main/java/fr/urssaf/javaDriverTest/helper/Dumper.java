package fr.urssaf.javaDriverTest.helper;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.cql.ColumnDefinition;
import com.datastax.oss.driver.api.core.cql.ColumnDefinitions;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.type.DataType;
import com.fasterxml.jackson.databind.util.ByteBufferBackedInputStream;

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
      for (final ColumnDefinition colDefinition : colDefinitions) {
         final CqlIdentifier colName = colDefinition.getName();
         final DataType colType = colDefinition.getType();
         final String valueAsString = getColAsString(row, colDefinition);
         sysout.println(colName + "(" + colType + ") " + " = " + valueAsString);
      }
   }

   public static String getColAsString(final Row row, final ColumnDefinition colDefinition) {
      final CqlIdentifier colName = colDefinition.getName();
      final Object valueAsObject = row.getObject(colName);
      if (valueAsObject instanceof ByteBuffer) {
         final ByteBuffer valueAsByteBuffer = (ByteBuffer) valueAsObject;
         try {
            final Object object = getBytesAsObject(valueAsByteBuffer);
            return object.getClass().getName() + ":" + object.toString();
         }
         catch (ClassNotFoundException | IOException e) {
            return new String(valueAsByteBuffer.array());
         }
      }
      return valueAsObject == null ? "NULL" : valueAsObject.toString();
   }

   public static Object getBytesAsObject(final ByteBuffer buffer) throws ClassNotFoundException, IOException {
      final InputStream stream = new ByteBufferBackedInputStream(buffer);
      final ObjectInputStream in = new ObjectInputStream(stream);
      return in.readObject();
   }
}

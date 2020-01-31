package fr.urssaf.astyanaxtest.dao;

import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.serializers.StringSerializer;

public class BasesReferenceCF {
   private final static ColumnFamily<String, String> cf = new ColumnFamily<String, String>(
         "BasesReference", StringSerializer.get(), StringSerializer.get());

   public static ColumnFamily<String, String> get() {
      return cf;
   }

}

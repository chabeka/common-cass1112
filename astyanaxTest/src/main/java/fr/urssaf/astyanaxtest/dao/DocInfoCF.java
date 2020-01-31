package fr.urssaf.astyanaxtest.dao;

import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.serializers.StringSerializer;

public class DocInfoCF {
   private final static ColumnFamily<String, String> cf = new ColumnFamily<String, String>(
         "DocInfo", StringSerializer.get(), StringSerializer.get());

   public static ColumnFamily<String, String> get() {
      return cf;
   }

}

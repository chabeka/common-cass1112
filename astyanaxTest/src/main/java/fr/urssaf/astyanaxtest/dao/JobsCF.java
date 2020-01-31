package fr.urssaf.astyanaxtest.dao;

import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.serializers.BytesArraySerializer;
import com.netflix.astyanax.serializers.LongSerializer;
import com.netflix.astyanax.serializers.StringSerializer;

public class JobsCF {
   private final static ColumnFamily<String, String> cf = new ColumnFamily<String, String>(
         "Jobs", StringSerializer.get(), StringSerializer.get());

   public static ColumnFamily<String, String> get() {
      return cf;
   }

}

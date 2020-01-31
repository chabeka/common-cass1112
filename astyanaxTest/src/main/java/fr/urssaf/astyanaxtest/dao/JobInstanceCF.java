package fr.urssaf.astyanaxtest.dao;

import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.serializers.BytesArraySerializer;
import com.netflix.astyanax.serializers.LongSerializer;
import com.netflix.astyanax.serializers.StringSerializer;

public class JobInstanceCF {
   private final static ColumnFamily<byte[], String> cf = new ColumnFamily<byte[], String>(
         "JobInstance", BytesArraySerializer.get(), StringSerializer.get());

   public static ColumnFamily<byte[], String> get() {
      return cf;
   }

}

package fr.urssaf.astyanaxtest.dao.sae;

import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.serializers.LongSerializer;
import com.netflix.astyanax.serializers.StringSerializer;

public class JobInstancesByNameCF {
   private static final ColumnFamily<String, Long> cf = new ColumnFamily<String, Long>(
         "JobInstancesByName",
         StringSerializer.get(),
         LongSerializer.get());

   public static ColumnFamily<String, Long> get() {
      return cf;
   }

}
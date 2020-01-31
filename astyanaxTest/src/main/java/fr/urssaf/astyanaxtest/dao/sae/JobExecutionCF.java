package fr.urssaf.astyanaxtest.dao.sae;

import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.serializers.LongSerializer;
import com.netflix.astyanax.serializers.StringSerializer;

public class JobExecutionCF {
   private static final ColumnFamily<Long, String> cf = new ColumnFamily<Long, String>(
         "JobExecution",
         LongSerializer.get(),
         StringSerializer.get());

   public static ColumnFamily<Long, String> get() {
      return cf;
   }

}

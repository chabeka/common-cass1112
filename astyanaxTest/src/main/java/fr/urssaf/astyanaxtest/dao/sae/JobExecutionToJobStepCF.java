package fr.urssaf.astyanaxtest.dao.sae;

import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.serializers.LongSerializer;

public class JobExecutionToJobStepCF {
   private static final ColumnFamily<Long, Long> cf = new ColumnFamily<Long, Long>(
         "JobExecutionToJobStep",
         LongSerializer.get(),
         LongSerializer.get());

   public static ColumnFamily<Long, Long> get() {
      return cf;
   }

}

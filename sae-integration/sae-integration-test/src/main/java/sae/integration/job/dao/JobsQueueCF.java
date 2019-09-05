package sae.integration.job.dao;

import java.util.UUID;

import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.serializers.StringSerializer;
import com.netflix.astyanax.serializers.UUIDSerializer;

public class JobsQueueCF {
   private static final ColumnFamily<String, UUID> cf = new ColumnFamily<>(
                                                                           "JobsQueue",
                                                                           StringSerializer.get(),
                                                                           UUIDSerializer.get());

   public static ColumnFamily<String, UUID> get() {
      return cf;
   }

}

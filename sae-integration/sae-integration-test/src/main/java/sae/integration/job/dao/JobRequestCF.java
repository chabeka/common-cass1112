package sae.integration.job.dao;

import java.util.UUID;

import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.serializers.StringSerializer;
import com.netflix.astyanax.serializers.UUIDSerializer;

public class JobRequestCF {
   private static final ColumnFamily<UUID, String> cf = new ColumnFamily<>(
                                                                           "JobRequest",
                                                                           UUIDSerializer.get(),
                                                                           StringSerializer.get());

   public static ColumnFamily<UUID, String> get() {
      return cf;
   }

}

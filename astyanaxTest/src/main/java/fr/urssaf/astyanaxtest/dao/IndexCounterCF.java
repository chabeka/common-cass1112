package fr.urssaf.astyanaxtest.dao;

import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.serializers.AnnotatedCompositeSerializer;
import com.netflix.astyanax.serializers.BytesArraySerializer;

public class IndexCounterCF {
	   public final static AnnotatedCompositeSerializer<IndexCounterKey> keySerializer = new AnnotatedCompositeSerializer<IndexCounterKey>(
		         IndexCounterKey.class);

   private final static ColumnFamily<IndexCounterKey, byte[]> cf = new ColumnFamily<IndexCounterKey, byte[]>(
         "IndexCounter", keySerializer, BytesArraySerializer.get());

   public static ColumnFamily<IndexCounterKey,byte[]> get() {
      return cf;
   }

}

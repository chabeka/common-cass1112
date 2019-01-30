package fr.urssaf.image.commons.cassandra.serializer;

import java.nio.ByteBuffer;
import java.util.Date;

import me.prettyprint.cassandra.serializers.AbstractSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;

/**
 * Pareil que DateSerializer, mais supporte les dates "nulles" qui sont
 * traduites en tableau de 0 bytes.
 * 
 * @author Samuel Carrière (basé sur le travail de Jim Ancona)
 * @see me.prettyprint.cassandra.serializers.DateSerializer
 */
public class NullableDateSerializer extends AbstractSerializer<Date> {
   private static final LongSerializer LONG_SERIALIZER = LongSerializer.get();
   private static final NullableDateSerializer INSTANCE = new NullableDateSerializer();

   /**
    * Renvoie un singleton
    * @return singleton
    */
   public static NullableDateSerializer get() {
      return INSTANCE;
   }

   @Override
   public final ByteBuffer toByteBuffer(Date obj) {
      if (obj == null) {
         return ByteBuffer.allocate(0);
      }
      return LONG_SERIALIZER.toByteBuffer(obj.getTime());
   }

   @Override
   public final Date fromByteBuffer(ByteBuffer bytes) {
      if (bytes == null || !bytes.hasRemaining()) {
         return null;
      }
      return new Date(LONG_SERIALIZER.fromByteBuffer(bytes));
   }
}

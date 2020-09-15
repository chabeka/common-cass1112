/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.serializer;

import java.nio.ByteBuffer;
import java.util.Map;

import fr.urssaf.image.commons.cassandra.serializer.XMLSerializer;
import me.prettyprint.cassandra.serializers.AbstractSerializer;

/**
 * Serializer de liste de String
 * 
 */
public final class MapSerializer extends
      AbstractSerializer<Map<String, Object>> {

   private MapSerializer() {
      super();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Map<String, Object> fromByteBuffer(ByteBuffer byteBuffer) {
      @SuppressWarnings("unchecked")
      Map<String, Object> obj = (Map<String, Object>) XMLSerializer.get()
            .fromByteBuffer(byteBuffer);

      return obj;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ByteBuffer toByteBuffer(Map<String, Object> obj) {
      return XMLSerializer.get().toByteBuffer(obj);
   }

   /**
    * @return l'instance de {@link MapSerializer}
    */
   public static MapSerializer get() {
      return MapSerializerHolder.INSTANCE;
   }

   private static final class MapSerializerHolder {
      private MapSerializerHolder() {
      }

      @SuppressWarnings("PMD.AccessorClassGeneration")
      private static final MapSerializer INSTANCE = new MapSerializer();
   }
}

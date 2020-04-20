/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.serializer;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import fr.urssaf.image.commons.cassandra.serializer.XMLSerializer;
import me.prettyprint.cassandra.serializers.AbstractSerializer;

/**
 * 
 * 
 */
public class MapSerializer extends
      AbstractSerializer<Map<String, List<String>>> {

   private static final MapSerializer INSTANCE = new MapSerializer();

   /**
    * {@inheritDoc}
    */
   @Override
   public final Map<String, List<String>> fromByteBuffer(ByteBuffer byteBuffer) {
      @SuppressWarnings("unchecked")
      Map<String, List<String>> map = (Map<String, List<String>>) XMLSerializer
            .get().fromByteBuffer(byteBuffer);

      return map;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final ByteBuffer toByteBuffer(Map<String, List<String>> map) {
      return XMLSerializer.get().toByteBuffer(map);
   }

   /**
    * Retourne une instance de {@link MapSerializer}
    * 
    * @return une instance de {@link MapSerializer}
    */
   public static MapSerializer get() {
      return INSTANCE;
   }

}

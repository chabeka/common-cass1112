/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.serializer;

import java.nio.ByteBuffer;
import java.util.List;

import fr.urssaf.image.commons.cassandra.serializer.XMLSerializer;
import me.prettyprint.cassandra.serializers.AbstractSerializer;

/**
 * 
 * 
 */
public class ListSerializer extends AbstractSerializer<List<String>> {

   private static final ListSerializer INSTANCE = new ListSerializer();

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<String> fromByteBuffer(ByteBuffer byteBuffer) {
      @SuppressWarnings("unchecked")
      List<String> list = (List<String>) XMLSerializer.get().fromByteBuffer(
            byteBuffer);

      return list;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final ByteBuffer toByteBuffer(List<String> map) {
      return XMLSerializer.get().toByteBuffer(map);
   }

   /**
    * Retourne une instance de {@link ListSerializer}
    * 
    * @return une instance de {@link ListSerializer}
    */
   public static ListSerializer get() {
      return INSTANCE;
   }

}

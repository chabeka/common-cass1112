/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.serializer;

import java.nio.ByteBuffer;
import java.util.List;

import me.prettyprint.cassandra.serializers.AbstractSerializer;
import fr.urssaf.image.commons.cassandra.serializer.XMLSerializer;

/**
 * Serializer de liste de String
 * 
 */
public class ListSerializer extends AbstractSerializer<List<String>> {

   private ListSerializer() {
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<String> fromByteBuffer(ByteBuffer byteBuffer) {
      @SuppressWarnings("unchecked")
      List<String> obj = (List<String>) XMLSerializer.get().fromByteBuffer(
            byteBuffer);

      return obj;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ByteBuffer toByteBuffer(List<String> obj) {
      return XMLSerializer.get().toByteBuffer(obj);
   }

   /**
    * @return l'instance de {@link ListSerializer}
    */
   public static ListSerializer get() {
      return ListSerializerHolder.INSTANCE;
   }

   private static class ListSerializerHolder {
      private ListSerializerHolder() {
      }

      private static final ListSerializer INSTANCE = new ListSerializer();
   }
}

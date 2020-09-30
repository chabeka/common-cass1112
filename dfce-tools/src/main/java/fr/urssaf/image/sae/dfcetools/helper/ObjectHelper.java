/**
 *  TODO (AC75007394) Description du fichier
 */
package fr.urssaf.image.sae.dfcetools.helper;

import static com.rainerhahnekamp.sneakythrow.Sneaky.sneak;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;

/**
 * TODO (AC75007394) Description du type
 *
 */
public class ObjectHelper {

   public static Object deserialiseObject(final ByteBuffer byteBuffer) {
      final byte[] bytes = new byte[byteBuffer.remaining()];
      byteBuffer.get(bytes);
      final InputStream stream = new ByteArrayInputStream(bytes);
      final ObjectInputStream ois = sneak(() -> new ObjectInputStream(stream));
      final Object o = sneak(() -> ois.readObject());
      return o;
   }
}

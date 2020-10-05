package fr.urssaf.image.sae.lotinstallmaj.service.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;

import com.fasterxml.jackson.databind.util.ByteBufferBackedInputStream;

import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotGeneralException;

/**
 * Classe de conversion des bytes en Object
 */
public class SerializerUtils {

   public static Object getBytesAsObject(final ByteBuffer buffer) throws MajLotGeneralException {
      final InputStream stream = new ByteBufferBackedInputStream(buffer);
      Object object = null;
      try (ObjectInputStream in = new ObjectInputStream(stream)) {
         object = in.readObject();
      }
      catch (final ClassNotFoundException | IOException e) {
         throw new MajLotGeneralException("Une erreur est survenue lors de la conversion des Bytes en Object", e);
      }

      return object;
   }

}

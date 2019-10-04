package fr.urssaf.image.sae.commons.utils.cql;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Date;

import org.apache.commons.lang.SerializationException;
import org.apache.commons.lang.SerializationUtils;

import me.prettyprint.cassandra.serializers.AbstractSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;

/**
 * Classe de sérialisation/désérialisation des JobParameters
 * Elle utilise un sérialiser json.
 */
public class ObjectCqlSerializer extends AbstractSerializer<Object> {

  private static final ObjectCqlSerializer INSTANCE = new ObjectCqlSerializer();

  /**
   * Renvoie un singleton
   * 
   * @return singleton
   */
  public static ObjectCqlSerializer get() {
    return INSTANCE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ByteBuffer toByteBuffer(final Object obj) {

    final byte[] bytes=SerializationUtils.serialize((Serializable)obj);
    final ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
    return byteBuffer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object fromByteBuffer(final ByteBuffer byteBuffer) {
    Object obj = null;
    try {
      final byte[] bytes = new byte[byteBuffer.capacity()];
      final Object objectDeserialized = SerializationUtils.deserialize(bytes);

      StringSerializer.get().fromByteBuffer(byteBuffer);

      if (objectDeserialized == null) {
        obj = null;
      } else if (objectDeserialized instanceof Date) {
        obj = objectDeserialized;
      } else if (objectDeserialized instanceof Long) {
        obj = objectDeserialized;
      } else if (objectDeserialized instanceof String) {
        obj = objectDeserialized;
      } else if (objectDeserialized instanceof Double) {
        obj = objectDeserialized;
      } else {
        throw new SerializationException(
                                         "Erreur lors de la désérialisation : la classe de la valeur ("
                                             + objectDeserialized.getClass() + ") n'est pas prévue");
      }
    }
    catch (final Exception e) {
      System.out.println("Exception=" + e.getMessage());
    }

    return obj;
  }

}

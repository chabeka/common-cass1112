/**
 *   (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.commons.utils;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.apache.http.protocol.ExecutionContext;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.TypeCodec;
import com.datastax.driver.core.exceptions.InvalidTypeException;
import com.datastax.driver.core.utils.Bytes;
import com.datastax.driver.mapping.Mapper;

import me.prettyprint.cassandra.serializers.AbstractSerializer;
import me.prettyprint.cassandra.serializers.ObjectSerializer;

/**
 * Classe de sérialisation/désérialisation des Object
 * Elle est utilisée par le {@link Mapper} qui se charge de mapper
 * une ligne extraite de la base cassandra pour un CF donnée.
 * <br>
 * Si la classe {@link ExecutionContext} est associée à une CF, chaque ligne de la CF
 * correspondrait à un {@link Object} java de type {@link ExecutionContext}
 * Mapping automatique: 1 instance de {@link ExecutionContext} == 1 ligne de la CF
 * Si la classe associée à la CF contient un champ de type {@link ExecutionContext}
 * Exemple: {@link JobExecution}
 * Lors de la sauvegarde/extraction de données dans la CF, le champs sera serialisé/désérialisé
 * par le {@link Mapper} en se servant des méthodes de cette classe.
 * Dans tous les cas, pour utiliser ce mapping automatique, il faudrait enregistré la classe
 * dans les Codecs du {@link Cluster}
 * Les méthodes de sérialisation/désérialisation de la classe
 * se basent sur la classe {@link AbstractSerializer }
 * Exemple d'utilisation:
 * Cluster().getConfiguration().getCodecRegistry().register(JobParametersCodec.instance);
 */
public class ObjectCodec extends TypeCodec<Object> {

  public static final ObjectCodec instance = new ObjectCodec();

  /**
   * Constructeur
   */
  protected ObjectCodec() {
    super(DataType.blob(), Object.class);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ByteBuffer serialize(final Object value, final ProtocolVersion protocolVersion) throws InvalidTypeException {
    final ObjectSerializer serializer = ObjectSerializer.get();
    final byte[] bytes = serializer.toBytes(value);
    // final byte[] bytes = SerializationUtils.serialize((Serializable) value);
    return value == null ? null : ByteBuffer.wrap(Arrays.copyOf(bytes, bytes.length));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object deserialize(final ByteBuffer bytes, final ProtocolVersion protocolVersion) throws InvalidTypeException {
    final ObjectSerializer serializer = ObjectSerializer.get();
    final byte[] result = bytes.duplicate().array();
    // final byte[] result = new byte[bytes.remaining()];
    final Object obj = serializer.fromBytes(result);
    // final Object obj = SerializationUtils.deserialize(bytes.duplicate().array());
    // final Object obj = SerializationUtils.deserialize(result);

    return obj;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object parse(final String value) throws InvalidTypeException {
    final byte[] bytes = value == null || value.isEmpty() || value.equalsIgnoreCase("NULL")
        ? null : Bytes.fromHexString(value).array();
    final ObjectSerializer serializer = ObjectSerializer.get();

    final Object obj = serializer.fromBytes(bytes);
    return obj;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String format(final Object value) throws InvalidTypeException {
    final ObjectSerializer serializer = ObjectSerializer.get();

    final byte[] bytes = serializer.toBytes(value);
    // final byte[] bytes = SerializationUtils.serialize((Serializable) value);
    if (bytes == null) {
      return "NULL";
    }
    return Bytes.toHexString(bytes);
  }

}

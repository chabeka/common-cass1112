/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.cql.codec;

import java.nio.ByteBuffer;
import java.util.Arrays;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.TypeCodec;
import com.datastax.driver.core.exceptions.InvalidTypeException;
import com.datastax.driver.core.utils.Bytes;

/**
 * Classe venant de la documentation de datastax nous permettant de faire quelques operation de transformation sur les DataType.blob()<br>
 * Each TypeCodec supports a bidirectional mapping between a Java type and a CQL type. A TypeCodec is thus capable of 4 basic operations:<br>
 * <ul>
 * <li>Serialize a Java object into a CQL value</li>
 * <li>Deserialize a CQL value into a Java object</li>
 * <li>Format a Java object into a CQL literal</li>
 * <li>Parse a CQL literal into a Java object</li>
 * </ul>
 * Pour plus d'explication voir sur le site de datastax
 * 
 * @see <a href="https://docs.datastax.com/en/developer/java-driver/3.1/manual/custom_codecs/"> Site datastax</a><br>
 */
public class BytesBlobCodec extends TypeCodec<byte[]> {
  public static final BytesBlobCodec instance = new BytesBlobCodec();

  private BytesBlobCodec() {
    super(DataType.blob(), byte[].class);
  }

  @Override
  public ByteBuffer serialize(final byte[] value, final ProtocolVersion protocolVersion) throws InvalidTypeException {
    return value == null ? null : ByteBuffer.wrap(Arrays.copyOf(value, value.length));
  }

  @Override
  public byte[] deserialize(final ByteBuffer bytes, final ProtocolVersion protocolVersion) throws InvalidTypeException {
    if (bytes == null || bytes.remaining() == 0) {
      return null;
    }

    return bytes.duplicate().array();
  }

  @Override
  public String format(final byte[] value) {
    if (value == null) {
      return "NULL";
    }
    return Bytes.toHexString(value);
  }

  @Override
  public byte[] parse(final String value) {
    return value == null || value.isEmpty() || value.equalsIgnoreCase("NULL")
        ? null : Bytes.fromHexString(value).array();
  }

}

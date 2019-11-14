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

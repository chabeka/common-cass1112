/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.cql.codec;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.TypeCodec;
import com.datastax.driver.core.exceptions.InvalidTypeException;

/**
 * TODO (AC75095028) Description du type
 */
public class JsonCodec<T> extends TypeCodec<T> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  public JsonCodec(final Class<T> javaType) {
    super(DataType.varchar(), javaType);
  }

  @Override
  public ByteBuffer serialize(final T value, final ProtocolVersion protocolVersion) throws InvalidTypeException {
    if (value == null) {
      return null;
    }
    try {
      return ByteBuffer.wrap(objectMapper.writeValueAsBytes(value));
    }
    catch (final IOException e) {
      throw new InvalidTypeException(e.getMessage(), e);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public T deserialize(final ByteBuffer bytes, final ProtocolVersion protocolVersion) throws InvalidTypeException {
    if (bytes == null) {
      return null;
    }
    try {
      final byte[] b = new byte[bytes.remaining()];
      // always duplicate the ByteBuffer instance before consuming it!
      bytes.duplicate().get(b);
      return (T) objectMapper.readValue(b, toJacksonJavaType());
    }
    catch (final IOException e) {
      throw new InvalidTypeException(e.getMessage(), e);
    }
  }

  @Override
  public String format(final T value) throws InvalidTypeException {
    if (value == null) {
      return "NULL";
    }
    String json;
    try {
      json = objectMapper.writeValueAsString(value);
    }
    catch (final IOException e) {
      throw new InvalidTypeException(e.getMessage(), e);
    }
    return '\'' + json.replace("\'", "''") + '\'';
  }

  @Override
  @SuppressWarnings("unchecked")
  public T parse(final String value) throws InvalidTypeException {
    if (value == null || value.isEmpty() || value.equalsIgnoreCase("NULL")) {
      return null;
    }
    if (value.charAt(0) != '\'' || value.charAt(value.length() - 1) != '\'') {
      throw new InvalidTypeException("JSON strings must be enclosed by single quotes");
    }
    final String json = value.substring(1, value.length() - 1).replace("''", "'");
    try {
      return (T) objectMapper.readValue(json, toJacksonJavaType());
    }
    catch (final IOException e) {
      throw new InvalidTypeException(e.getMessage(), e);
    }
  }

  protected JavaType toJacksonJavaType() {
    return TypeFactory.defaultInstance().constructType(getJavaType().getType());
  }

}
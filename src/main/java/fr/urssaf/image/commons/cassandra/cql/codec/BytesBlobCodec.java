/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.cql.codec;

import java.nio.ByteBuffer;
import java.sql.Blob;
import java.util.Arrays;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.TypeCodec;
import com.datastax.driver.core.exceptions.InvalidTypeException;
import com.datastax.driver.core.utils.Bytes;

/**
 *	Cette classe permet de faire le mapping bidirectionnel (sérialisation/désérialisation)
 *	entre un objet java de type {@link Blob} et un objet CQL de type Blob.
 *	La classe herite de {@link TypeCodec} qui prend en charge 4 types d'opération
 *	<ul>
 *		<li>La serialisation</li>
 *		<li>La déserialisation</li>
 *		<li>La Formatage</li>
 *		<li>Le parsing</li>
 *	</ul>
 *
 *	Voir la documentation des codes personalisé. <br>
 *	 <a>https://translate.google.fr/translate?hl=fr&sl=auto&tl=fr&u=https%3A%2F%2Fdocs.datastax.
     	com%2Fen%2Fdeveloper%2Fjava-driver%2F3.1%2Fmanual%2Fcustom_codecs%2F</a>
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

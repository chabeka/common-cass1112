/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.cql.codec;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.Blob;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.TypeCodec;
import com.datastax.driver.core.exceptions.InvalidTypeException;
import com.datastax.driver.mapping.Mapper;

/**
 *	Cette classe permet de faire le mapping bidirectionnel
 *	entre un objet java et un objet CQL de type <b>VARCHAR</b>.
 *	La classe herite de {@link TypeCodec} qui prend en charge 4 types d'opération<br>
 *	<ul>
 *		<li>La serialisation</li>
 *		<li>La déserialisation</li>
 *		<li>La Formatage</li>
 *		<li>Le parsing</li>
 *	</ul><br>
 *	Exemple tiré de la documentation datastax:
 *  Prenons le scénario suivant: un utilisateur possède des documents JSON stockés dans une colonne varchar
 *  dans la base cassandra et souhaite que le pilote mappe automatiquement cette colonne sur un objet Java 
 *  à l'aide de la bibliothèque Jackson , au lieu de renvoyer la chaîne JSON brute. 
 *  Lorsque la classe est enregisté dans les configurations du {@link Cluster}, le pilote ({@link Mapper}) se chargera de faire 
 *  la conversion automatique de la colonne extraite sur l'objet java souhaité.<br><br>

 *  <b>Enregistrement du codec dans le config du cluster :</b><br>
 *   <code>ccf.getCluster().getConfiguration().getCodecRegistry().register(new JsonCodec<BatchStatus>(BatchStatus.class));</code> <br>
 *    
 *	<b>Voir la documentation des codec sur datastax personalisé.</b> <br>
 *	 <a>https://translate.google.fr/translate?hl=fr&sl=auto&tl=fr&u=https%3A%2F%2Fdocs.datastax.
     	com%2Fen%2Fdeveloper%2Fjava-driver%2F3.1%2Fmanual%2Fcustom_codecs%2F</a>
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
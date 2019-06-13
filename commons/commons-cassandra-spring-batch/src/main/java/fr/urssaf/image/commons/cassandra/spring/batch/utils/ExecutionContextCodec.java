/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.utils;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.item.ExecutionContext;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.TypeCodec;
import com.datastax.driver.core.exceptions.InvalidTypeException;
import com.datastax.driver.core.utils.Bytes;
import com.datastax.driver.mapping.Mapper;

import fr.urssaf.image.commons.cassandra.spring.batch.serializer.ExecutionContextSerializer;

/**
 * Classe de sérialisation/désérialisation des JobParameters
 * Elle est utilisée par le {@link Mapper} qui se charge de mapper
 * une ligne extraite de la base cassandra pour un CF donnée.
 * <br>
 * Si la classe {@link ExecutionContext} est associée à une CF, chaque ligne de la CF
 * correspondrait à un {@link Object} java de type {@link ExecutionContext}
 * Mapping automatique: 1 instance de {@link ExecutionContext} == 1 ligne de la CF
 * Si la classe associée à la CF contient un champ de type {@link ExecutionContext}
 * Exemple: {@link JobExecution}
 * 
 * Lors de la sauvegarde/extraction de données dans la CF, le champs sera serialisé/désérialisé
 * par le {@link Mapper} en se servant des méthodes de cette classe.
 * 
 * Dans tous les cas, pour utiliser ce mapping automatique, il faudrait enregistré la classe
 * dans les Codecs du {@link Cluster}
 * 
 * Les méthodes de sérialisation/désérialisation de la classe
 * se basent sur la classe {@link ExecutionContextSerializer}
 * 
 * Exemple d'utilisation:
 *    Cluster().getConfiguration().getCodecRegistry().register(JobParametersCodec.instance);
 */
public class ExecutionContextCodec extends TypeCodec<ExecutionContext> {

  public static final ExecutionContextCodec instance = new ExecutionContextCodec();

  /**
   * Constructeur
   */
  protected ExecutionContextCodec() {
    super(DataType.blob(), ExecutionContext.class);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ByteBuffer serialize(final ExecutionContext value, final ProtocolVersion protocolVersion) throws InvalidTypeException {
    final ExecutionContextSerializer serializer = ExecutionContextSerializer.get();
    final byte[] bytes = serializer.toBytes(value);
    return value == null ? null : ByteBuffer.wrap(Arrays.copyOf(bytes, bytes.length));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ExecutionContext deserialize(final ByteBuffer bytes, final ProtocolVersion protocolVersion) throws InvalidTypeException {
    final ExecutionContextSerializer serializer = ExecutionContextSerializer.get();
    final ExecutionContext executionContext = serializer.fromBytes(bytes.duplicate().array());
    return executionContext;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ExecutionContext parse(final String value) throws InvalidTypeException {
    final byte[] bytes = value == null || value.isEmpty() || value.equalsIgnoreCase("NULL")
        ? null : Bytes.fromHexString(value).array();
    final ExecutionContextSerializer serializer = ExecutionContextSerializer.get();
    final ExecutionContext executionContext = serializer.fromBytes(bytes);
    return executionContext;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String format(final ExecutionContext value) throws InvalidTypeException {
    final ExecutionContextSerializer serializer = ExecutionContextSerializer.get();
    final byte[] bytes = serializer.toBytes(value);
    if (bytes == null) {
      return "NULL";
    }
    return Bytes.toHexString(bytes);
  }

}

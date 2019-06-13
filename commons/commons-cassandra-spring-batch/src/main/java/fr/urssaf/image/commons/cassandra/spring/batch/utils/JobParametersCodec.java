/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.utils;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.TypeCodec;
import com.datastax.driver.core.exceptions.InvalidTypeException;
import com.datastax.driver.core.utils.Bytes;
import com.datastax.driver.mapping.Mapper;

import fr.urssaf.image.commons.cassandra.spring.batch.serializer.JobParametersSerializer;

/**
 * Classe de sérialisation/désérialisation des JobParameters
 * Elle est utilisée par le {@link Mapper} qui se charge de mapper
 * une ligne extraite de la base cassandra pour un CF donnée.
 * <br>
 * Si la classe {@link JobParameters} est associée à une CF, chaque ligne de la CF
 * correspondrait à un {@link Object} java de type {@link JobParameters}
 * <br>
 * Mapping automatique: 1 instance de {@link JobParameters} == 1 ligne de la CF
 * <br>
 * Si la classe associée à la CF contient un champ de type {@link JobParameters}
 * Exemple: {@link JobInstance}
 * <br>
 * Lors de la sauvegarde/extraction de données dans la CF, le champs sera serialisé/désérialisé
 * par le {@link Mapper} en se servant des méthodes de cette classe.
 * <br>
 * Dans tous les cas, pour utiliser ce mapping automatique, il faudrait enregistré la classe
 * dans les Codecs du {@link Cluster}
 * <br>
 * Les méthodes de sérialisation/désérialisation de la classe
 * se baseent sur la classe {@link JobParametersSerializer}
 * <br>
 * Exemple d'utilisation:
 * <blockquote>Cluster().getConfiguration().getCodecRegistry().register(JobParametersCodec.instance);<blockquote>
 */
public class JobParametersCodec extends TypeCodec<JobParameters> {

  public static final JobParametersCodec instance = new JobParametersCodec();

  /**
   * Constructeur
   */
  protected JobParametersCodec() {
    super(DataType.blob(), JobParameters.class);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ByteBuffer serialize(final JobParameters value, final ProtocolVersion protocolVersion) throws InvalidTypeException {
    final JobParametersSerializer serializer = JobParametersSerializer.get();
    final byte[] bytes = serializer.toBytes(value);
    return value == null ? null : ByteBuffer.wrap(Arrays.copyOf(bytes, bytes.length));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JobParameters deserialize(final ByteBuffer bytes, final ProtocolVersion protocolVersion) throws InvalidTypeException {
    final JobParametersSerializer serializer = JobParametersSerializer.get();
    final JobParameters jobParameters = serializer.fromBytes(bytes.duplicate().array());
    return jobParameters;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JobParameters parse(final String value) throws InvalidTypeException {
    final byte[] bytes = value == null || value.isEmpty() || value.equalsIgnoreCase("NULL") ? null : Bytes.fromHexString(value).array();
    final JobParametersSerializer serializer = JobParametersSerializer.get();
    final JobParameters jobParameters = serializer.fromBytes(bytes);
    return jobParameters;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String format(final JobParameters value) throws InvalidTypeException {
    final JobParametersSerializer serializer = JobParametersSerializer.get();
    final byte[] bytes = serializer.toBytes(value);
    if (bytes == null) {
      return "NULL";
    }
    return Bytes.toHexString(bytes);
  }

}

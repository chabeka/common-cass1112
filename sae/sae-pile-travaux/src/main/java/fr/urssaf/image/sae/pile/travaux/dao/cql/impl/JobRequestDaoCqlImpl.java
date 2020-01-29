package fr.urssaf.image.sae.pile.travaux.dao.cql.impl;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.CodecRegistry;
import com.datastax.driver.core.TypeCodec;
import com.datastax.driver.core.exceptions.CodecNotFoundException;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;

import fr.urssaf.image.commons.cassandra.cql.codec.BytesBlobCodec;
import fr.urssaf.image.commons.cassandra.cql.codec.JsonCodec;
import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobRequestDaoCql;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobRequestCql;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;

/**
 * DAO de la colonne famille <code>JobRequest</code>
 */
@Repository
public class JobRequestDaoCqlImpl extends GenericDAOImpl<JobRequestCql, UUID> implements IJobRequestDaoCql {

   /**
   * @param ccf
   */
  @Autowired
  public JobRequestDaoCqlImpl(final CassandraCQLClientFactory ccf) {
    super(ccf);
  }

  /**
   * TODO (AC75095028) Description du champ
   */
   private static final String JOBKEY2 = "jobkey";

   /**
    * Cette methode est appelé après l'instanciation de la classe par spring.
    * Grace à l'annotation {@link PostConstruct} on est sur que les dependances
    * son bien injectés ({@link CassandraClientFactory}) et cela nous permet d'enregistrer tous les <b>codec</b> nécessaires
    * aux opérations sur la table (CF) de ce DAO
    */
   @PostConstruct
   public void setRegister() {
	  if(ccf != null) {
      final CodecRegistry registry = ccf.getCluster().getConfiguration().getCodecRegistry();
      registerCodecIfNotFound(registry, new JsonCodec<>(VIContenuExtrait.class));
      registerCodecIfNotFound(registry, BytesBlobCodec.instance);

      // ccf.getCluster().getConfiguration().getCodecRegistry().register(new JsonCodec<>(VIContenuExtrait.class));
      //// ccf.getCluster().getConfiguration().getCodecRegistry().register(new EnumNameCodec<JobState>(JobState.class));
      // ccf.getCluster().getConfiguration().getCodecRegistry().register(BytesBlobCodec.instance);

	  }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Optional<JobRequestCql> getJobRequestIdByJobKey(final byte[] jobKey) {
      final Select query = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
      query.where(eq(JOBKEY2, jobKey));
      return Optional.ofNullable(getMapper().map(getSession().execute(query)).one());
   }

  private void registerCodecIfNotFound(final CodecRegistry registry, final TypeCodec<?> codec) {
    try {
      registry.codecFor(codec.getCqlType(), codec.getJavaType());
    }
    catch (final CodecNotFoundException e) {
      registry.register(codec);
    }
  }
}

package fr.urssaf.image.sae.pile.travaux.dao.cql.impl;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.extras.codecs.enums.EnumNameCodec;

import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.sae.commons.context.BytesBlobCodec;
import fr.urssaf.image.sae.commons.context.JsonCodec;
import fr.urssaf.image.sae.commons.dao.impl.GenericDAOImpl;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobRequestDaoCql;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;

/**
 * DAO de la colonne famille <code>JobRequest</code>
 */
@Repository
public class JobRequestDaoCqlImpl extends GenericDAOImpl<JobRequest, UUID> implements IJobRequestDaoCql {

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
    ccf.getCluster().getConfiguration().getCodecRegistry().register(new JsonCodec<VIContenuExtrait>(VIContenuExtrait.class));
    ccf.getCluster().getConfiguration().getCodecRegistry().register(new EnumNameCodec<JobState>(JobState.class));
    ccf.getCluster().getConfiguration().getCodecRegistry().register(BytesBlobCodec.instance);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<JobRequest> getJobRequestIdByJobKey(final byte[] jobKey) {
    final Select query = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
    query.where(eq(JOBKEY2, jobKey));
    return Optional.ofNullable(getMapper().map(getSession().execute(query)).one());
  }

}

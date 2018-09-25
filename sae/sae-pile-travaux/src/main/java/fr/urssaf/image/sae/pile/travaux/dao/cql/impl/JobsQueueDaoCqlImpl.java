package fr.urssaf.image.sae.pile.travaux.dao.cql.impl;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.mapping.Mapper.Option;

import fr.urssaf.image.sae.commons.dao.impl.GenericDAOImpl;
import fr.urssaf.image.sae.commons.utils.ColumnUtil;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobsQueueDaoCql;
import fr.urssaf.image.sae.pile.travaux.model.JobQueueCql;

/**
 * DAO de la colonne famille <code>JobsQueue</code>
 */
@Repository
public class JobsQueueDaoCqlImpl extends GenericDAOImpl<JobQueueCql, UUID> implements IJobsQueueDaoCql {

  /**
   * TODO (AC75095028) Description du champ
   */
  private static final String JOBSITUATION = "jobsituation";

  private static final String JOBS_WAITING_KEY = "jobsWaiting";

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteByIdAndIndexColumn(final UUID id, final String colSituation, final long clock) {
    Assert.notNull(id, " id est requis");
    Assert.notNull(id, " colSituation est requis");
    Assert.notNull(clock, " le clock est requis");
    final Delete delete = QueryBuilder.delete().from(ccf.getKeyspace(), getTypeArgumentsName());
    final Field keyField = ColumnUtil.getSimplePartionKeyField(daoType);
    Assert.notNull(keyField, "La clé de l'entité à supprimer ne peut être null");

    final String keyName = keyField.getName();
    delete.where(eq(keyName, id));
    delete.where(eq(JOBSITUATION, colSituation));
    getMapper().setDefaultDeleteOptions(Option.timestamp(clock));
    getMapper().map(getSession().execute(delete));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterator<JobQueueCql> getUnreservedJobRequest() {
    final Select query = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
    query.where(eq(JOBSITUATION, JOBS_WAITING_KEY));
    return getMapper().map(getSession().execute(query)).iterator();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterator<JobQueueCql> getNonTerminatedSimpleJobs(final String hostname) {
    final Select query = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
    query.where(eq(JOBSITUATION, hostname));
    return getMapper().map(getSession().execute(query)).iterator();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<JobQueueCql> getNonTerminatedJobs(final String key) {
    return null;
  }
}

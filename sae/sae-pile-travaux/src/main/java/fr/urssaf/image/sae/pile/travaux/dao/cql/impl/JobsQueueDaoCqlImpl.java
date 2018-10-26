package fr.urssaf.image.sae.pile.travaux.dao.cql.impl;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;

import fr.urssaf.image.sae.commons.dao.impl.GenericDAOImpl;
import fr.urssaf.image.sae.commons.utils.ColumnUtil;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobsQueueDaoCql;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobQueueCql;

/**
 * DAO de la colonne famille <code>JobsQueue</code>
 */
@Repository
public class JobsQueueDaoCqlImpl extends GenericDAOImpl<JobQueueCql, String> implements IJobsQueueDaoCql {

   /**
    * TODO (AC75095028) Description du champ
    */
   private static final String JOB_ID = "idjob";

   private static final String JOBS_WAITING_KEY = "jobsWaiting";

   @Override
   public Optional<JobQueueCql> findByIndexedColumn(final UUID idjob) {
      Assert.notNull(idjob, " key est requis");
      final Select select = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
      select.where(eq(JOB_ID, idjob));
      final ResultSet result = getSession().execute(select);
      return Optional.ofNullable(getMapper().map(result).one());
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Optional<JobQueueCql> findByIdAndIndexColumn(final UUID id, final String key) {
      Assert.notNull(id, " id est requis");
      Assert.notNull(key, " colSituation est requis");
      final Select select = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
      final Field keyField = ColumnUtil.getSimplePartionKeyField(daoType);
      Assert.notNull(keyField, "La clé de l'entité à supprimer ne peut être null");

      final String keyName = keyField.getName();
      select.where(eq(keyName, key));
      select.where(eq(JOB_ID, id));
      final ResultSet result = getSession().execute(select);
      return Optional.ofNullable(getMapper().map(result).one());
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void deleteByIdAndIndexColumn(final UUID id, final String key, final long clock) {
      Assert.notNull(id, " id est requis");
      Assert.notNull(key, " key est requis");
      Assert.notNull(clock, " le clock est requis");
      final Delete delete = QueryBuilder.delete().from(ccf.getKeyspace(), getTypeArgumentsName());
      final Field keyField = ColumnUtil.getSimplePartionKeyField(daoType);
      Assert.notNull(keyField, "La clé de l'entité à supprimer ne peut être null");

      final String keyName = keyField.getName();
      delete.where(eq(keyName, key));
      delete.where(eq(JOB_ID, id));
      // getMapper().setDefaultDeleteOptions(Option.timestamp(clock));
      getMapper().map(getSession().execute(delete));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Iterator<JobQueueCql> getUnreservedJobRequest() {
      final Select query = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());

      final Field keyField = ColumnUtil.getSimplePartionKeyField(daoType);
      Assert.notNull(keyField, "La clé de l'entité à supprimer ne peut être null");
      final String keyName = keyField.getName();

      query.where(eq(keyName, JOBS_WAITING_KEY));
      return getMapper().map(getSession().execute(query)).iterator();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Iterator<JobQueueCql> getNonTerminatedSimpleJobs(final String hostname) {
      final Select query = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());

      final Field keyField = ColumnUtil.getSimplePartionKeyField(daoType);
      Assert.notNull(keyField, "La clé de l'entité à supprimer ne peut être null");
      final String keyName = keyField.getName();

      query.where(eq(keyName, hostname));
      return getMapper().map(getSession().execute(query)).iterator();

   }

   /**
    * {@inheritDoc}
    */
   /*
    * @Override
    * public List<JobQueueCql> getNonTerminatedJobs(final String key) {
    * return null;
    * }
    */
}
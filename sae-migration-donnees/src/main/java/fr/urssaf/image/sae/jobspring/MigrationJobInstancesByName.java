/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.jobspring;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Row;

import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstancesByNameCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobInstancesByNameDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.utils.Constante;
import fr.urssaf.image.sae.IMigration;
import fr.urssaf.image.sae.jobspring.model.GenericJobSpring;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;

@Component
public class MigrationJobInstancesByName extends MigrationJob implements IMigration {

   private static final Logger LOGGER = LoggerFactory.getLogger(MigrationJobInstancesByName.class);

   @Autowired
   IJobInstancesByNameDaoCql jobInstByNamedao;

   protected static final String JOBINSTANCES_BY_NAME_CFNAME = "JobInstancesByName";

   /**
    * {@inheritDoc}
    */
   @Override
   public void migrationFromThriftToCql() {

      final Iterator<GenericJobSpring> it = genericdao.findAllByCFName(Constante.JOBINSTANCES_BY_NAME_CFNAME, ccfthrift.getKeyspace().getKeyspaceName());
      while (it.hasNext()) {
         final Row row = (Row) it.next();
         final String key = StringSerializer.get().fromByteBuffer(row.getBytes("key"));
         if (!"_unreserved".equals(key)) {
            final Long id = row.getLong("column1");
            final String value = StringSerializer.get().fromByteBuffer(row.getBytes("value"));
            final JobInstancesByNameCql jobcql = new JobInstancesByNameCql();
            jobcql.setJobInstanceId(id);
            jobcql.setJobName(key);
            jobInstByNamedao.save(jobcql);
         } else {

         }
      }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void migrationFromCqlTothrift() {
      final Iterator<JobInstancesByNameCql> it = jobInstByNamedao.findAllWithMapper();
      final ColumnFamilyTemplate<String, Long> jobInstancesByNameTemplate = new ThriftColumnFamilyTemplate<String, Long>(
                                                                                                                         ccfthrift.getKeyspace(),
                                                                                                                         JOBINSTANCES_BY_NAME_CFNAME,
                                                                                                                         StringSerializer.get(),
                                                                                                                         LongSerializer.get());
      while (it.hasNext()) {
         final JobInstancesByNameCql job = it.next();
         final ColumnFamilyUpdater<String, Long> updater2 = jobInstancesByNameTemplate.createUpdater(job.getJobName());
         updater2.setByteArray(job.getJobInstanceId(), new byte[0]);
         jobInstancesByNameTemplate.update(updater2);
      }

   }

}

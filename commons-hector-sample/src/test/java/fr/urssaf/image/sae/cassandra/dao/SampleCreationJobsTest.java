package fr.urssaf.image.sae.cassandra.dao;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import junit.framework.Assert;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.IndexedSlicesPredicate;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import me.prettyprint.hector.api.beans.HColumn;

import org.apache.cassandra.thrift.IndexOperator;
import org.cassandraunit.AbstractCassandraUnit4TestCase;
import org.cassandraunit.dataset.DataSet;
import org.cassandraunit.dataset.xml.ClassPathXmlDataSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.helper.IndexedSlicesPredicateHelper;
import fr.urssaf.image.sae.cassandra.dao.exception.CassandraEx;
import fr.urssaf.image.sae.cassandra.dao.model.JobModel;

/**
 * Plusieurs exemple d'utilisation de l'api Hector sans passé par les Templetes
 * pour persister les données ainsi que la suppression des données dans
 * Cassandra. Utilisation des tables avec des indexes secondaires.
 * 
 * La base utilisé est une base embarqué.
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-hector.xml" })
public class SampleCreationJobsTest extends AbstractCassandraUnit4TestCase {
   Logger LOG = LoggerFactory.getLogger(SampleCreationJobsTest.class);

   @Autowired
   @Qualifier("jobDAO")
   JobDAO jobDAO;

   @Test
   public void checkStartCassandraServer() {
      assertThat(getCluster(), notNullValue());
   }

   @Test
   public void saveAndLoadJob() throws CassandraEx {
      jobDAO.setClefColumnFamilyName("Jobs_sae");
      jobDAO.setColumnFamilyName("saeJobs");
      jobDAO.setKeyspace(getKeyspace());
      JobModel paramJobModel = new JobModel();
      paramJobModel.setIdJob(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
      paramJobModel.setJobParam("-Xms500m -Xmx2500m");
      paramJobModel.setJobType("CEM");
      paramJobModel.setJobState("en attente");
      paramJobModel.setDateReceiptRequests(new Date());
      jobDAO.saveOrUpdate(paramJobModel);
      Assert.assertNotNull(jobDAO.load(paramJobModel.getIdJob()).getJobParam());

   }

   @Test
   public void loadJobById() throws CassandraEx {
      jobDAO.setClefColumnFamilyName("Jobs_sae");
      jobDAO.setColumnFamilyName("saeJobs");
      jobDAO.setKeyspace(getKeyspace());
      JobModel paramJobModel = new JobModel();
      paramJobModel.setIdJob(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
      paramJobModel.setJobParam("-Xms500m -Xmx2500m");
      paramJobModel.setJobType("CEM");
      paramJobModel.setJobState("en attente");
      paramJobModel.setDateReceiptRequests(new Date());
      jobDAO.saveOrUpdate(paramJobModel);

      ColumnFamilyTemplate<String, UUID> template = new ThriftColumnFamilyTemplate<String, UUID>(
            getKeyspace(), "saeJobs", StringSerializer.get(), UUIDSerializer
                  .get());

      HColumn<UUID, String> value = template.querySingleColumn("Jobs_sae",
            paramJobModel.getIdJob(), StringSerializer.get());

      assertThat(value, notNullValue());

   }

   @Test
   public void loadJobsByListId() throws CassandraEx {
      List<UUID> idJobs = new ArrayList<UUID>();
      jobDAO.setColumnFamilyName("saeJobs");
      jobDAO.setClefColumnFamilyName("saeJobsWithRow");
      jobDAO.setKeyspace(getKeyspace());
      JobModel paramJobModel = new JobModel();
      paramJobModel.setIdJob(UUID
            .fromString("e3408630-5191-11e1-ad8a-005056c00008"));
      paramJobModel.setJobParam("-Xms500m -Xmx2500m");
      paramJobModel.setJobType("CEM");
      paramJobModel.setJobState("en attente");
      paramJobModel.setDateReceiptRequests(new Date());
      jobDAO.saveOrUpdate(paramJobModel);

      paramJobModel.setIdJob(UUID
            .fromString("e3408630-5191-11e1-ad8a-005056c00010"));
      jobDAO.saveOrUpdate(paramJobModel);

      ColumnFamilyTemplate<UUID, String> template = new ThriftColumnFamilyTemplate<UUID, String>(
            getKeyspace(), "saeJobsWithRow", UUIDSerializer.get(),
            StringSerializer.get());

      idJobs.add(UUID.fromString("e3408630-5191-11e1-ad8a-005056c00008"));
      idJobs.add(UUID.fromString("e3408630-5191-11e1-ad8a-005056c00010"));
      ColumnFamilyResult<UUID, String> value = template.queryColumns(idJobs);

      assertThat(value, notNullValue());

   }

   @Test
   public void loadJobsWhitIndex() throws CassandraEx {
      jobDAO.setColumnFamilyName("saeJobsWithIndex");
      jobDAO.setClefColumnFamilyName("columnWithIndex");
      jobDAO.setKeyspace(getKeyspace());
      ThriftColumnFamilyTemplate<UUID, String> template = new ThriftColumnFamilyTemplate<UUID, String>(
            getKeyspace(), "saeJobsWithIndex", UUIDSerializer.get(),
            StringSerializer.get());

      IndexedSlicesPredicate<UUID, String, String> predicate = new IndexedSlicesPredicate<UUID, String, String>(
            UUIDSerializer.get(), StringSerializer.get(), StringSerializer
                  .get());
      predicate.addExpression("columnWithIndex", IndexOperator.EQ,
            "columnWithIndex");
      IndexedSlicesPredicateHelper.setEmptyStartKey(predicate);
      predicate.count(500);
      ColumnFamilyResult<UUID, String> result = template
            .queryColumns(predicate);

      while (true) {
         if (result.hasResults()) {
            LOG.debug("param " + result.getString("param"));
            assertThat(result.getString("param"), notNullValue());
         }
         if (result.hasNext())
            result = result.next();
         else
            break;
      }

   }

   @Override
   public DataSet getDataSet() {
      return new ClassPathXmlDataSet("xml/datasetJobWithIndex.xml");
   }
}

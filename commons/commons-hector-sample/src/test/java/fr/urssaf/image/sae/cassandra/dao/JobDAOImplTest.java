package fr.urssaf.image.sae.cassandra.dao;

import java.util.Date;
import java.util.UUID;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.apache.commons.lang.time.DateUtils;
import org.cassandraunit.AbstractCassandraUnit4TestCase;
import org.cassandraunit.dataset.DataSet;
import org.cassandraunit.dataset.xml.ClassPathXmlDataSet;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.cassandra.dao.exception.CassandraEx;
import fr.urssaf.image.sae.cassandra.dao.exception.ZookeeperEx;
import fr.urssaf.image.sae.cassandra.dao.model.JobModel;

/**
 * Plusieurs exemple d'utilisation de Templete pour persister les données ainsi
 * que la suppression des données dans Cassandra.
 * La base utilisé est une base embarqué.
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-hector.xml" })
public class JobDAOImplTest extends AbstractCassandraUnit4TestCase {
   @Autowired
   @Qualifier("jobDAO")
   JobDAO jobDAO;

   @Test
   public void delete() throws CassandraEx {
      UUID uuid1 = UUID.fromString("27c1dfc0-42b0-11e1-9aa5-005056c00008");
      jobDAO.delete(uuid1);

   }

   @Test
   public void save() throws CassandraEx {
      jobDAO.setKeyspace(getKeyspace());
      JobModel paramJobModel = new JobModel();
      paramJobModel.setIdJob(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
      paramJobModel.setJobParam("-Xms500m -Xmx2500m");
      paramJobModel.setJobType("CEM");
      paramJobModel.setJobState("en attente");
      paramJobModel.setDateReceiptRequests(new Date());
      jobDAO.saveOrUpdate(paramJobModel);

   }

   @Test
   public void load() throws CassandraEx, ZookeeperEx {
      jobDAO.setKeyspace(getKeyspace());
      JobModel paramJobModel = new JobModel();
      paramJobModel.setIdJob(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
      paramJobModel.setJobParam("-Xms500m -Xmx2500m");
      paramJobModel.setJobType("CEM");
      paramJobModel.setJobState("en attente");
      paramJobModel.setDateReceiptRequests(new Date());
      jobDAO.saveOrUpdate(paramJobModel);

      JobModel jobModel = jobDAO.load(paramJobModel.getIdJob());
      Assert.assertNotNull(jobModel);
   }

   @Test
   public void loadListOfJobs() throws CassandraEx, InterruptedException {
      jobDAO.setKeyspace(getKeyspace());
      JobModel paramJobModel = new JobModel();
      paramJobModel.setIdJob(TimeUUIDUtils.getTimeUUID(new Date().getTime()));
      paramJobModel.setJobParam("-Xms500m -Xmx2500m");
      paramJobModel.setJobType("CEM");
      paramJobModel.setJobState("en attente");
      paramJobModel.setDateReceiptRequests(new Date());

      jobDAO.saveOrUpdate(paramJobModel);
      paramJobModel = new JobModel();
      paramJobModel.setIdJob(TimeUUIDUtils.getTimeUUID(DateUtils.addDays(
            new Date(), -1).getTime()));
      paramJobModel.setJobParam("-Xms500m -Xmx2500m");
      paramJobModel.setJobType("CEM2");
      paramJobModel.setJobState("en attente");
      paramJobModel.setDateReceiptRequests(new Date());
      jobDAO.saveOrUpdate(paramJobModel);
      
      Date date = DateUtils.addDays(new Date(), -10);
      Assert.assertNotNull(jobDAO.loadJobs(date));
   }

   @Override
   public DataSet getDataSet() {
      return new ClassPathXmlDataSet("xml/sampleJobDataset.xml");
   }
}

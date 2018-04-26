package fr.urssaf.image.sae.cassandra.dao;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;

import junit.framework.Assert;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.cassandraunit.AbstractCassandraUnit4TestCase;
import org.cassandraunit.dataset.DataSet;
import org.cassandraunit.dataset.xml.ClassPathXmlDataSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.cassandra.dao.exception.CassandraEx;
import fr.urssaf.image.sae.cassandra.dao.model.JobModel;
/**
 * Exemple d'insertion d'un job dans Cassandra.
 * La base utilisé est une base embarqué.
 * 
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-hector.xml" })
public class CassandraStartAndLoadRuleTest extends
      AbstractCassandraUnit4TestCase {

   @Autowired
   @Qualifier("jobDAO")
   JobDAO jobDAO;

   @Test
   public void save() throws CassandraEx {
      jobDAO.setColumnFamilyName("SaeJobs");
      //Initialisation du KeySpace.
      jobDAO.setKeyspace(getKeyspace());
      JobModel paramJobModel = new JobModel();
      paramJobModel.setIdJob(TimeUUIDUtils.getUniqueTimeUUIDinMillis());
      paramJobModel.setJobParam("-Xms500m -Xmx2500m");
      paramJobModel.setJobType("CEM");
      paramJobModel.setJobState("en attente");
      paramJobModel.setDateReceiptRequests(new Date());
      jobDAO.saveOrUpdate(paramJobModel);
      Assert.assertNotNull(paramJobModel.getIdJob());
      Assert.assertNotNull(jobDAO.load(paramJobModel.getIdJob()).getJobParam());

   }

   @Override
   public DataSet getDataSet() {
      return new ClassPathXmlDataSet("xml/sampleJobDataset.xml");
   }
}

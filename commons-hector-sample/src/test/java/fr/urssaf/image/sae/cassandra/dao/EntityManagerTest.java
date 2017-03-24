package fr.urssaf.image.sae.cassandra.dao;

import java.util.UUID;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import me.prettyprint.hom.EntityManagerImpl;

import org.cassandraunit.AbstractCassandraUnit4TestCase;
import org.cassandraunit.dataset.DataSet;
import org.cassandraunit.dataset.xml.ClassPathXmlDataSet;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.cassandra.dao.exception.CassandraEx;
import fr.urssaf.image.sae.cassandra.dao.model.SaeJobPojo;

/**
 * Exemple d'utilisation d'EntityManager pour persister les données dans
 * Cassandra. La base utilisé est une base embarqué.
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-hector.xml" })
@SuppressWarnings("all")
public class EntityManagerTest extends AbstractCassandraUnit4TestCase {
   Logger LOG = LoggerFactory.getLogger(EntityManagerTest.class);

   @Test
   public void saveAndLoadEmbadedDocument() throws CassandraEx {

      EntityManagerImpl em = new EntityManagerImpl(getKeyspace(),
            "fr.urssaf.image.sae.cassandra.dao.model");
      JSONObject values = new JSONObject();
      values.put("param1", "value");
      SaeJobPojo document = new SaeJobPojo();

      document.setClef("captureEnMasse");
      UUID jobId2 = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      document.setClefColonne(jobId2);
      document.setValues(values.toJSONString());
      em.persist(document);

      SaeJobPojo job3 = em.find(SaeJobPojo.class, "captureEnMasse");

      values.put("param1", "value2");
      document.setValues(values.toJSONString());
      em.persist(document);
      job3 = em.find(SaeJobPojo.class, "captureEnMasse");
      LOG.debug("apres test EntityManager " + job3.getValues());

   }

   @Override
   public DataSet getDataSet() {
      return new ClassPathXmlDataSet("xml/jobEntityDataset.xml");
   }
}

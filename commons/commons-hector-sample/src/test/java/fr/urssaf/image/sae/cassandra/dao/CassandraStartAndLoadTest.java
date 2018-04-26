package fr.urssaf.image.sae.cassandra.dao;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.cassandraunit.AbstractCassandraUnit4TestCase;
import org.cassandraunit.dataset.DataSet;
import org.cassandraunit.dataset.xml.ClassPathXmlDataSet;
import org.junit.Test;

/**
 * Test de vérification si le serveur est démarrer ainsi que le cluster. La base
 * utilisé est une base embarqué.
 * 
 */

public class CassandraStartAndLoadTest extends AbstractCassandraUnit4TestCase {

   @Override
   public DataSet getDataSet() {
      return new ClassPathXmlDataSet("xml/sampleJobDataset.xml");
   }

   @Test
   public void serverIsUp() throws Exception {
      assertThat(getKeyspace(), notNullValue());
   }

   @Test
   public void clusterIsUp() throws Exception {
      assertThat(getCluster(), notNullValue());
   }

}

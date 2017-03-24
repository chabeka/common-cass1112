package fr.urssaf.image.sae.cassandra.dao;

import java.util.UUID;

import me.prettyprint.cassandra.model.IndexedSlicesQuery;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;

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

import fr.urssaf.image.sae.cassandra.dao.exception.CassandraEx;

/**
 * Exemple simple d'utilisation des indexes secondaires.
 * 
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-hector.xml" })
public class SampleJobWithIndexTest extends AbstractCassandraUnit4TestCase {
   Logger LOOGER = LoggerFactory.getLogger(SampleJobWithIndexTest.class);

   @Autowired
   @Qualifier("jobDAO")
   JobDAO jobDAO;

   @Test
   public void loadJobsWithoutTemplete() throws CassandraEx {
      IndexedSlicesQuery<UUID, String, String> query = HFactory
            .createIndexedSlicesQuery(getKeyspace(), UUIDSerializer.get(),
                  StringSerializer.get(), StringSerializer.get());
      query.addEqualsExpression("columnWithIndex", "columnWithIndex");
      query.setColumnFamily("saeJobsWithIndex");
      query.setRange("", "", false, 100); // On prend toutes les colonnes
      query.setRowCount(500);
      QueryResult<OrderedRows<UUID, String, String>> result = query.execute();
      OrderedRows<UUID, String, String> values = result.get();
      values.getList();
      for (Row<UUID, String, String> row : values) {
         LOOGER.debug("param " + row.getColumnSlice().getColumnByName("param"));
      }
   }

   @Override
   public DataSet getDataSet() {
      return new ClassPathXmlDataSet("xml/datasetJobWithIndex.xml");
   }
}

package fr.urssaf.image.commons.cassandra.spring.batch.springcontext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.spring.batch.daothrift.CassandraJobExecutionDaoThrift;

/**
 * Test la création de la DAO JobExecution par spring
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-cassandra-main2.xml"})
public class SpringContextTest2 {

  @Autowired
  private CassandraJobExecutionDaoThrift jobExecutionDao;

  @Autowired
  private CassandraServerBean cassandraServer;

  @Before
  public final void init() throws Exception {
    // Après chaque test, on reset les données de cassandra
    cassandraServer.resetData();
  }

  @Test
  public final void springContextTest() {
    final int count = jobExecutionDao.countJobExecutions();
    Assert.assertEquals(0, count);
  }
}

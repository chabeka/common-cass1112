/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.SequencesCql;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-cassandra-local.xml"})
public class SequencesDaoCqlTest {

  @Autowired
  private ISequencesDaoCql sequencesDaoCql;

  @Autowired
  private CassandraServerBean server;

  String[] listCode = {"jobExecutionId", "jobInstanceId", "stepExecutionId"};

  Long[] listValues = {new Long(1090), new Long(1090), new Long(9102)};

  @After
  public void after() throws Exception {
    server.resetData();
  }


  /**
   * Migration des données droitreferentielFormatcql vers DroitRnd
   */
  @Test
  public void migrationFromCqlTothrift() {

    final SequencesCql sequencesCql = new SequencesCql();
    sequencesCql.setJobIdName(listCode[0]);
    sequencesCql.setValue(listValues[0]);
    sequencesDaoCql.saveWithMapper(sequencesCql);

    final SequencesCql sequence = sequencesDaoCql.findWithMapperById("jobExecutionId").get();
    final String id = sequence.getJobIdName();
    final Long timestampColumn = sequencesDaoCql.getColunmClock(id);
    Assert.assertNotNull("Le timestamp de la colonne ne peut être null ", timestampColumn);

  }

}

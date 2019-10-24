/**
 *   (AC75095351)
 */
package fr.urssaf.image.sae.jobspring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.SequencesCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.ISequencesDaoCql;
import fr.urssaf.image.sae.spring.batch.MigrationSequences;
import fr.urssaf.image.sae.testutils.CompareUtils;

/**
 * (AC75095351) Classe de test migration des referentielFormat
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-migration-test.xml"})
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)

public class MigrationSequencesTest {


  @Autowired
  private ISequencesDaoCql sequencesDaoCql;

  @Autowired
  MigrationSequences migrationSequences;

  @Autowired
  private CassandraServerBean server;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationSequencesTest.class);

  private static final String SEQUENCE_KEY = "sequences";



  String[] listCode = {"jobExecutionId", "jobInstanceId", "stepExecutionId"};

  Long[] listValues = {new Long(1090), new Long(1090), new Long(9102)};

  @After
  public void after() throws Exception {
    server.resetData();
  }

  /**
   * Migration des données Rnd vers referentielFormatcql
   */
  @Test
  public void migrationFromThriftToCql() {
    try {

      populateTableThrift();
      /*
       * final List<TypeDocument> listThrift = supportThrift.findAll();
       * migrationRnd.migrationFromThriftToCql();
       * final List<TypeDocument> listCql = supportCql.findAll();
       */
      /*
       * Assert.assertEquals(listThrift.size(), listCode.length);
       * Assert.assertEquals(listThrift.size(), listCql.size());
       * Assert.assertTrue(CompareUtils.compareListsGeneric(listThrift, listCql));
       */
    }
    catch (final Exception ex) {
      LOGGER.debug("exception=" + ex);
      Assert.assertTrue(false);
    }
  }

  private void populateTableThrift() {


    for (final String element : listCode) {
      //supportThrift.ajouterRnd(createTypeDocument(i), new Date().getTime());
    }
  }

  /**
   * Migration des données droitreferentielFormatcql vers DroitRnd
   */
  @Test
  public void migrationFromCqlTothrift() {

    populateTableCql();
    migrationSequences.migrationFromCqlTothrift();
    final List<SequencesCql> listThrift = migrationSequences.findAllThrift(SEQUENCE_KEY);

    final Iterator<SequencesCql> it=sequencesDaoCql.findAllWithMapper();
    final List<SequencesCql> listCql=new ArrayList<>();
    it.forEachRemaining(listCql::add);
    Assert.assertTrue(!listThrift.isEmpty());
    Assert.assertTrue(!listCql.isEmpty());
    Assert.assertEquals(listThrift.size(), listCql.size());
    Assert.assertTrue(CompareUtils.compareListsGeneric(listThrift, listCql));
  }

  /**
   * On crée les enregistrements dans la table droitreferentielFormatcql
   */
  private void populateTableCql() {
    int i = 0;
    for (final String element : listCode) {
      final SequencesCql sequencesCql = new SequencesCql();
      sequencesCql.setJobIdName(listCode[i]);
      sequencesCql.setValue(listValues[i]);
      sequencesDaoCql.saveWithMapper(sequencesCql);
      i++;
    }
  }
}

/**
 *   (AC75095351)
 */
package fr.urssaf.image.sae.jobspring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.ListCompareAlgorithm;
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
import fr.urssaf.image.sae.utils.CompareUtils;


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
   * Migration des données sequencescql vers Sequences
   */
  @Test
  public void migrationFromThriftToCql() {
    try {

      populateTableThrift();
      migrationSequences.migrationFromThriftToCql();
        final List<SequencesCql> listThrift = migrationSequences.findAllThrift(SEQUENCE_KEY);
        migrationSequences.migrationFromThriftToCql();
        final List<SequencesCql> listCql = new ArrayList<>();
        final Iterator<SequencesCql> it = sequencesDaoCql.findAllWithMapper();
        while (it.hasNext()) {
          final SequencesCql sequenceCql = it.next();
          listCql.add(sequenceCql);
        }
        Assert.assertEquals(listThrift.size(), listCode.length);
        Assert.assertEquals(listThrift.size(), listCql.size());
        Assert.assertTrue(CompareUtils.compareListsGeneric(listThrift, listCql));
       
    }
    catch (final Exception ex) {
      LOGGER.debug("exception=" + ex);
      Assert.assertTrue(false);
    }
  }

  private void populateTableThrift() {

    for (int i=0;i<listCode.length;i++) {
    	migrationSequences.addSequence(listCode[i], listValues[i]);
    }
  }

  /**
   * Migration des données sequencescql vers Sequences
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
  
  @Test
  public void diffAddTest() {

    populateTableThrift();
    migrationSequences.migrationFromThriftToCql();
  

    final SequencesCql sequenceCql = new SequencesCql();
    sequenceCql.setJobIdName("JOBADD");
    sequenceCql.setValue(new Long(2000));
    sequencesDaoCql.saveWithMapper(sequenceCql);
    final Iterator<SequencesCql> it=sequencesDaoCql.findAllWithMapper();
    final List<SequencesCql> listCql=new ArrayList<>();
    it.forEachRemaining(listCql::add);
    final Javers javers = JaversBuilder
        .javers()
        .withListCompareAlgorithm(ListCompareAlgorithm.SIMPLE)
        .build();
    final Diff diff = migrationSequences.compareSequences(javers);
    Assert.assertTrue(diff.hasChanges());
    final String changes = diff.getChanges().get(0).toString();
    Assert.assertTrue(changes.equals("NewObject{ new object: fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.SequencesCql/JOBADD }"));

  }

  @Test
  public void diffDescTest() {

    populateTableThrift();
    migrationSequences.migrationFromThriftToCql();
    final Iterator<SequencesCql> it=sequencesDaoCql.findAllWithMapper();
    final List<SequencesCql> listCql=new ArrayList<>();
    it.forEachRemaining(listCql::add);
    final Optional<SequencesCql> sequencesCqlOpt=sequencesDaoCql.findWithMapperById("jobExecutionId");
    if (sequencesCqlOpt.isPresent()) {
      final SequencesCql sequencesCql=new SequencesCql();
    	sequencesCql.setJobIdName(sequencesCqlOpt.get().getJobIdName());
    	sequencesCql.setValue(Long.valueOf("5000"));
    	sequencesDaoCql.saveWithMapper(sequencesCql);
    }
    final Javers javers = JaversBuilder
        .javers()
        .withListCompareAlgorithm(ListCompareAlgorithm.SIMPLE)
        .build();
    final Diff diff = migrationSequences.compareSequences(javers);
    
    Assert.assertTrue(diff.hasChanges());
    final String changes = diff.getChanges().get(0).toString();
    Assert.assertTrue(changes.equals("ValueChange{ 'value' value changed from '1090' to '5000' }"));

  }
  
}

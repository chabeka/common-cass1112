/**
 *   (AC75095351) Description du fichier
 */
package fr.urssaf.image.sae.spring.batch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.ListCompareAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.SequencesCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.ISequencesDaoCql;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;

/**
 * (AC75095351) Classe de migration séquences
 */
@Component
public class MigrationSequences {

  @Autowired
  private ISequencesDaoCql sequencesDaoCql;

  @Autowired
  private CassandraClientFactory ccf;


  @Autowired
  private JobClockSupport jobClockSupport;

  private static final String SEQUENCE_CF = "Sequences";

  private static final String SEQUENCE_KEY = "sequences";

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationSequences.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */

  public void migrationFromThriftToCql() {

    MigrationSequences.LOGGER.info(" MigrationSequences - migrationFromThriftToCql- start ");

    final List<SequencesCql> listThrift = findAllThrift(MigrationSequences.SEQUENCE_KEY);
    // Enregistrement des données Thrift dans la table Cql
    sequencesDaoCql.saveAll(listThrift);

    MigrationSequences.LOGGER.info(" MigrationSequences - migrationFromThriftToCql- end ");
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  public void migrationFromCqlTothrift() {

    MigrationSequences.LOGGER.info(" MigrationSequences - migrationFromCqlTothrift- start ");
    // Recherche des valeurs Cql
    final Iterator<SequencesCql> it = sequencesDaoCql.findAllWithMapper();

    final ColumnFamilyTemplate<String, String> template = new ThriftColumnFamilyTemplate<>(ccf.getKeyspace(),
        MigrationSequences.SEQUENCE_CF,
        StringSerializer.get(),
        StringSerializer.get());
    // On s'assure que le nouveau timestamp est supérieur à l'ancien

    final ColumnFamilyUpdater<String, String> updater = template
        .createUpdater(MigrationSequences.SEQUENCE_KEY);
    while (it.hasNext()) {
      final SequencesCql sequencesCql=it.next();
      updater.setClock(jobClockSupport.currentCLock());
      updater.setLong(sequencesCql.getJobIdName(), sequencesCql.getValue());
      template.update(updater);
    }
    MigrationSequences.LOGGER.info(" MigrationSequences - migrationFromCqlTothrift- end ");
  }

  public void addSequence(final String jobIdName, final long value) {
    final ColumnFamilyTemplate<String, String> template = new ThriftColumnFamilyTemplate<>(ccf.getKeyspace(),
        MigrationSequences.SEQUENCE_CF,
        StringSerializer.get(),
        StringSerializer.get());
    // On s'assure que le nouveau timestamp est supérieur à l'ancien

    final ColumnFamilyUpdater<String, String> updater = template
        .createUpdater(MigrationSequences.SEQUENCE_KEY);
    updater.setClock(jobClockSupport.currentCLock());
    updater.setLong(jobIdName, value);
    template.update(updater);
  }

  /**
   * Retourne tous les enregistrements Thrift sous forme d'entités Cql pour pouvoir comparer facilement les listes avant et après migration
   * 
   * @return List<SequencesCql>
   */
  public List<SequencesCql> findAllThrift(final String key) {
    final List<SequencesCql> listSequencesThrift = new ArrayList<>();
    final ColumnFamilyTemplate<String, String> template = new ThriftColumnFamilyTemplate<>(ccf.getKeyspace(),
        MigrationSequences.SEQUENCE_CF,
        StringSerializer.get(),
        StringSerializer.get());
    final ColumnFamilyResult<String, String> result = template.queryColumns(key);
    final Iterator<String> it = result.getColumnNames().iterator();
    while (it.hasNext()) {
      final SequencesCql sequencesCql=new SequencesCql();
      final String name=it.next();
      sequencesCql.setJobIdName(name);
      sequencesCql.setValue(result.getLong(name));
      listSequencesThrift.add(sequencesCql);
    }
    return listSequencesThrift;
  }

  public Diff compareSequences() {
    // liste d'objet cql venant de la base thrift après transformation
    final List<SequencesCql> sequencesThrift = findAllThrift(MigrationSequences.SEQUENCE_KEY);
    // liste venant de la base cql
    final List<SequencesCql> sequencesCql = new ArrayList<>();
    final Iterator<SequencesCql> it = sequencesDaoCql.findAllWithMapper();
    while (it.hasNext()) {
      final SequencesCql sequenceCql = it.next();
      sequencesCql.add(sequenceCql);
    }
    MigrationSequences.LOGGER.info(" MigrationSequences - migrationFromThriftToCql- nbThrift={} ", sequencesThrift.size());
    MigrationSequences.LOGGER.info(" MigrationSequences - migrationFromThriftToCql- nbCql={} ", sequencesCql.size());
    Collections.sort(sequencesThrift);
    Collections.sort(sequencesCql);
    final Javers javers = JaversBuilder
        .javers()
        .withListCompareAlgorithm(ListCompareAlgorithm.SIMPLE)
        .build();
    final Diff diff = javers.compareCollections(sequencesThrift, sequencesCql, SequencesCql.class);
    return diff;

  }
}

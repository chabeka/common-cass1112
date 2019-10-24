/**
 *   (AC75095351) Description du fichier
 */
package fr.urssaf.image.sae.spring.batch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
 * (AC75095351) Description du type
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

    LOGGER.info(" MigrationSequences - migrationFromThriftToCql- start ");

    final List<SequencesCql> listThrift = findAllThrift(SEQUENCE_KEY);
    // Enregistrement des données Thrift dans la table Cql
    sequencesDaoCql.saveAll(listThrift);

    LOGGER.info(" MigrationSequences - migrationFromThriftToCql- end ");
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  public void migrationFromCqlTothrift() {

    LOGGER.info(" MigrationSequences - migrationFromCqlTothrift- start ");
    // Recherche des valeurs Cql
    final Iterator<SequencesCql> it = sequencesDaoCql.findAllWithMapper();

    final ColumnFamilyTemplate<String, String> template = new ThriftColumnFamilyTemplate<>(ccf.getKeyspace(),
        SEQUENCE_CF,
        StringSerializer.get(),
        StringSerializer.get());
    // On s'assure que le nouveau timestamp est supérieur à l'ancien

    final ColumnFamilyUpdater<String, String> updater = template
        .createUpdater(SEQUENCE_KEY);
    while (it.hasNext()) {

      final SequencesCql sequencesCql=it.next();
      updater.setClock(jobClockSupport.currentCLock());
      updater.setLong(sequencesCql.getJobIdName(), sequencesCql.getValue());
      template.update(updater);
    }
    LOGGER.info(" MigrationSequences - migrationFromCqlTothrift- end ");
  }

  /**
   * Retourne tous les enregistrements Thrift sous forme d'entités Cql pour pouvoir comparer facilement les listes avant et après migration
   * 
   * @return List<SequencesCql>
   */
  public List<SequencesCql> findAllThrift(final String key) {
    final List<SequencesCql> listSequencesThrift = new ArrayList<>();
    final ColumnFamilyTemplate<String, String> template = new ThriftColumnFamilyTemplate<>(ccf.getKeyspace(),
        SEQUENCE_CF,
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
}

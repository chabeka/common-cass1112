package fr.urssaf.image.sae.pile.travaux.dao;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

/**
 * DAO de la colonne famille <code>JobHistory</code>
 * 
 * 
 */
@Repository
public class JobHistoryDao {

  public static final String JOBHISTORY_CFNAME = "JobHistory";

  private static final int MAX_JOB_ATTIBUTS = 100;

  private static final int TTL = 2592000; // 2592000 secondes, soit 30 jours

  private final ColumnFamilyTemplate<UUID, UUID> jobHistoryTmpl;

  private final Keyspace keyspace;

  /**
   * 
   * @param keyspace
   *           Keyspace utilisé par la pile des travaux
   */
  @Autowired
  public JobHistoryDao(final Keyspace keyspace) {

    this.keyspace = keyspace;

    // Propriété de clé:
    // - Type de la valeur : UUID
    // - Serializer de la valeur : UUIDSerializer

    jobHistoryTmpl = new ThriftColumnFamilyTemplate<>(keyspace,
        JOBHISTORY_CFNAME, UUIDSerializer.get(), UUIDSerializer.get());

    jobHistoryTmpl.setCount(MAX_JOB_ATTIBUTS);

  }

  /**
   * 
   * @return CassandraTemplate de <code>JobHistory</code>
   */
  public final ColumnFamilyTemplate<UUID, UUID> getJobHistoryTmpl() {

    return jobHistoryTmpl;
  }

  /**
   * 
   * @return Mutator de <code>JobHistory</code>
   */
  public final Mutator<UUID> createMutator() {

    final Mutator<UUID> mutator = HFactory.createMutator(keyspace, UUIDSerializer
                                                         .get());

    return mutator;

  }


  private void addColumn(final ColumnFamilyUpdater<UUID, UUID> updater,
                         final UUID colName, final String value, final Serializer<UUID> nameSerializer,
                         final Serializer<String> valueSerializer, final long clock) {

    final HColumn<UUID, String> column = HFactory.createColumn(colName, value,
                                                               nameSerializer, valueSerializer);

    column.setClock(clock);
    updater.setColumn(column);

  }

  /**
   * Ajoute d'une nouvelle colonne.
   * 
   * @param updater
   *           Updater de <code>JobHistory</code>
   * @param timestampTrace
   *           clé de la colonne
   * @param messageTrace
   *           valeur de la colonne
   * @param clock
   *           horloge de la colonne
   */
  public final void ecritColonneTrace(final ColumnFamilyUpdater<UUID, UUID> updater,
                                      final UUID timestampTrace, final String messageTrace, final long clock) {

    addColumn(updater, timestampTrace, messageTrace, UUIDSerializer.get(),
              StringSerializer.get(), clock);

  }

  /**
   * Suppression d'un JobHistory
   * 
   * @param mutator
   *           Mutator de <code>JobHistory</code>
   * @param idJob
   *           nom de la ligne
   * @param clock
   *           horloge de la suppression
   */
  public final void mutatorSuppressionJobHistory(final Mutator<UUID> mutator,
                                                 final UUID idJob, final long clock) {

    mutator.addDeletion(idJob, JOBHISTORY_CFNAME, clock);

  }

}

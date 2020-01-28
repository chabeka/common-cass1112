package fr.urssaf.image.sae.pile.travaux.dao;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.pile.travaux.dao.serializer.JobQueueSerializer;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;
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
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;

/**
 * DAO de la colonne famille <code>JobsQueue</code>
 * 
 * 
 */
@Repository
public class JobsQueueDao {

  public static final String JOBSQUEUE_CFNAME = "JobsQueue";

  private static final int MAX_NON_TERMINATED_JOBS = 500;

  private static final int TTL = 2592000; // 2592000 secondes, soit 30 jours

  private final ColumnFamilyTemplate<String, UUID> jobsQueueTmpl;

  private static final int MAX_ROWS = 5000;

  private final Keyspace keyspace;

  /**
   * 
   * @param keyspace
   *           Keyspace utilisé par la pile des travaux
   */
  @Autowired
  public JobsQueueDao(final Keyspace keyspace) {

    this.keyspace = keyspace;

    // Propriété de clé:
    // - Type de la valeur : String
    // - Serializer de la valeur : StringSerializer

    jobsQueueTmpl = new ThriftColumnFamilyTemplate<>(keyspace,
        JOBSQUEUE_CFNAME, StringSerializer.get(), UUIDSerializer.get());

    jobsQueueTmpl.setCount(MAX_NON_TERMINATED_JOBS);

  }

  /**
   * 
   * @return CassandraTemplate de <code>JobsQueue</code>
   */
  public final ColumnFamilyTemplate<String, UUID> getJobsQueueTmpl() {

    return jobsQueueTmpl;
  }

  /**
   * 
   * @return SliceQuery de <code>JobsQueue</code>
   */
  public final SliceQuery<String, UUID, String> createSliceQuery() {

    final SliceQuery<String, UUID, String> sliceQuery = HFactory.createSliceQuery(
                                                                                  keyspace, StringSerializer.get(), UUIDSerializer.get(),
                                                                                  StringSerializer.get());
    sliceQuery.setColumnFamily(JOBSQUEUE_CFNAME);

    return sliceQuery;
  }

  /**
   * 
   * @return Mutator de <code>JobsQueue</code>
   */
  public final Mutator<String> createMutator() {

    final Mutator<String> mutator = HFactory.createMutator(keyspace,
                                                           StringSerializer.get());

    return mutator;

  }

  @SuppressWarnings("unchecked")
  private void addColumn(final ColumnFamilyUpdater<String, UUID> updater,
                         final Object colName, final Object value, final Serializer nameSerializer,
                         final Serializer valueSerializer, final long clock) {

    final HColumn<UUID, Object> column = HFactory.createColumn(colName, value,
                                                               nameSerializer, valueSerializer);

    column.setClock(clock);
    updater.setColumn(column);

  }

  /**
   * Ajout d'une colonne.
   * 
   * @param updater
   *           Updater de <code>JobsQueue</code>
   * @param idJob
   *           clé de la colonne
   * @param jobQueue
   *           valeur de la colonne
   * @param clock
   *           horloge de la colonne
   */
  public final void ecritColonneJobQueue(
                                         final ColumnFamilyUpdater<String, UUID> updater, final UUID idJob,
                                         final JobQueue jobQueue, final long clock) {

    addColumn(updater, idJob, jobQueue, UUIDSerializer.get(),
              JobQueueSerializer.get(), clock);

  }

  /**
   * Ajoute une nouvelle ligne
   * 
   * @param mutator
   *           Mutator de <code>JobsQueue</code>
   * @param hostnameOuJobsWaiting
   *           clé de la ligne
   * @param jobQueue
   *           valeur de la colonne
   * @param clock
   *           horloge de la colonne
   */
  public final void mutatorAjouterInsertionJobQueue(final Mutator<String> mutator,
                                                    final String hostnameOuJobsWaiting, final JobQueue jobQueue, final long clock) {

    final HColumn<UUID, JobQueue> col = HFactory.createColumn(jobQueue.getIdJob(),
                                                              jobQueue, UUIDSerializer.get(), JobQueueSerializer.get());

    col.setTtl(TTL);
    col.setClock(clock);

    mutator.addInsertion(hostnameOuJobsWaiting, JOBSQUEUE_CFNAME, col);

  }

  /**
   * Suppression d'une colonne dans une file
   * 
   * @param mutator
   *           Mutator de <code>JobsQueue</code>
   * @param hostnameOuJobsWaiting
   *           clé de la ligne
   * @param idJob
   *           nom de la colonne
   * @param clock
   *           horloge de la suppression
   */
  public final void mutatorAjouterSuppressionJobQueue(final Mutator<String> mutator,
                                                      final String hostnameOuJobsWaiting, final UUID idJob, final long clock) {

    mutator.addDeletion(hostnameOuJobsWaiting, JOBSQUEUE_CFNAME, idJob,
                        UUIDSerializer.get(), clock);

  }

  /**
   * 
   * @return RangeSlicesQuery de <code>JobsQueue</code>
   */
  public RangeSlicesQuery<String, UUID, String> createRangeSlicesQuery() {
    final RangeSlicesQuery<String, UUID, String> query = HFactory
        .createRangeSlicesQuery(keyspace, StringSerializer.get(),
                                UUIDSerializer.get(), StringSerializer.get());

    query.setColumnFamily(JOBSQUEUE_CFNAME);
    query.setRowCount(MAX_ROWS);

    return query;
  }
}

package fr.urssaf.image.sae.pile.travaux.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.cassandra.thrift.IndexOperator;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.urssaf.image.commons.cassandra.helper.HectorIterator;
import fr.urssaf.image.commons.cassandra.helper.IndexedSlicesPredicateHelper;
import fr.urssaf.image.commons.cassandra.helper.QueryResultConverter;
import fr.urssaf.image.sae.pile.travaux.dao.JobRequestDao;
import fr.urssaf.image.sae.pile.travaux.dao.iterator.JobQueueIterator;
import fr.urssaf.image.sae.pile.travaux.dao.iterator.JobRequestRowsIterator;
import fr.urssaf.image.sae.pile.travaux.dao.serializer.JobQueueSerializer;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.model.JobHistory;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.service.JobLectureService;
import fr.urssaf.image.sae.pile.travaux.support.JobHistorySupport;
import fr.urssaf.image.sae.pile.travaux.support.JobRequestSupport;
import fr.urssaf.image.sae.pile.travaux.support.JobsQueueSupport;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyResultWrapper;
import me.prettyprint.cassandra.service.template.IndexedSlicesPredicate;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;

/**
 * Implémentation du service {@link JobLectureService}
 */
@Service
public class JobLectureServiceImpl implements JobLectureService {

  private static final int MAX_ALL_JOBS = 200;

  private final JobRequestSupport jobRequestSupport;

  private final JobsQueueSupport jobsQueueSupport;

  private final JobHistorySupport jobHistorySupport;

  private static final int MAX_JOB_ATTIBUTS = 100;

  /**
   * Valeur de la clé pour les jobs en attente de réservation
   */
  private static final String JOBS_WAITING_KEY = "jobsWaiting";

  /**
   * @param jobRequestDao
   *          DAO de {@link JobRequest}
   * @param jobsQueueDao
   *          DAO de {@link JobQueue}
   * @param jobHistoryDao
   *          DAO de {@link JobHistory}
   */
  @Autowired
  public JobLectureServiceImpl(final JobRequestSupport jobRequestSupport,
                               final JobsQueueSupport jobsQueueSupport, final JobHistorySupport jobHistorySupport) {

    this.jobRequestSupport = jobRequestSupport;
    this.jobsQueueSupport = jobsQueueSupport;
    this.jobHistorySupport = jobHistorySupport;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final JobRequest getJobRequest(final UUID jobRequestUUID) {

    // Requête dans Cassandra
    // final ColumnFamilyResult<UUID, String> result = this.jobRequestSupport.getJobRequestTmpl().queryColumns(jobRequestUUID);
    final ColumnFamilyResult<UUID, String> result = this.jobRequestSupport.getJobRequestTmpl().queryColumns(jobRequestUUID);

    // Conversion en objet JobRequest
    final JobRequest jobRequest = jobRequestSupport.createJobRequestFromResult(result);

    return jobRequest;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final JobRequest getJobRequestNotNull(final UUID uuidJob) throws JobInexistantException {
    final JobRequest jobRequest = this.getJobRequest(uuidJob);
    if (jobRequest == null) {
      throw new JobInexistantException(uuidJob);
    }
    return jobRequest;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final Iterator<JobQueue> getUnreservedJobRequestIterator() {

    final SliceQuery<String, UUID, String> sliceQuery = jobsQueueSupport.createSliceQuery();
    sliceQuery.setKey(JOBS_WAITING_KEY);

    return new JobQueueIterator(sliceQuery);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final List<JobQueue> getNonTerminatedSimpleJobs(final String hostname) {

    final ColumnFamilyResult<String, UUID> result = jobsQueueSupport.getJobsQueueTmpl()
                                                                    .queryColumns(hostname);
    final Collection<UUID> colNames = result.getColumnNames();
    final List<JobQueue> list = new ArrayList<JobQueue>(colNames.size());
    final JobQueueSerializer serializer = JobQueueSerializer.get();
    for (final UUID uuid : colNames) {
      final JobQueue jobQueue = serializer.fromBytes(result.getByteArray(uuid));
      list.add(jobQueue);
    }
    return list;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final List<JobRequest> getNonTerminatedJobs(final String key) {

    final List<JobQueue> jobQueues = this.getNonTerminatedSimpleJobs(key);

    final List<JobRequest> jobRequests = new ArrayList<JobRequest>();

    for (final JobQueue jobQueue : jobQueues) {
      final JobRequest jobRequest = this.getJobRequest(jobQueue.getIdJob());
      jobRequests.add(jobRequest);
    }

    return jobRequests;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final List<JobHistory> getJobHistory(final UUID idJob) {

    final ColumnFamilyResult<UUID, UUID> result = jobHistorySupport.getJobHistoryTmpl()
                                                                   .queryColumns(idJob);

    final Collection<UUID> colNames = result.getColumnNames();
    final List<JobHistory> histories = new ArrayList<JobHistory>(colNames.size());
    final StringSerializer serializer = StringSerializer.get();
    for (final UUID timeUUID : colNames) {

      final JobHistory jobHistory = new JobHistory();

      jobHistory.setTrace(serializer
                                    .fromBytes(result.getByteArray(timeUUID)));
      jobHistory.setDate(new Date(TimeUUIDUtils.getTimeFromUUID(timeUUID)));

      histories.add(jobHistory);

    }
    return histories;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final List<JobRequest> getAllJobs(final Keyspace keyspace) {
    return getAllJobs(keyspace, MAX_ALL_JOBS);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final List<JobRequest> getAllJobs(final Keyspace keyspace, final int maxKeysToRead) {

    // On n'utilise pas d'index. On récupère tous les jobs sans distinction,
    // en requêtant directement dans la CF JobRequest
    final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
    final RangeSlicesQuery<UUID, String, byte[]> rangeSlicesQuery = HFactory
                                                                            .createRangeSlicesQuery(keyspace,
                                                                                                    UUIDSerializer.get(),
                                                                                                    StringSerializer.get(),
                                                                                                    bytesSerializer);
    rangeSlicesQuery.setColumnFamily(JobRequestDao.JOBREQUEST_CFNAME);
    rangeSlicesQuery.setRange("", "", false, MAX_JOB_ATTIBUTS);
    rangeSlicesQuery.setRowCount(maxKeysToRead);
    final QueryResult<OrderedRows<UUID, String, byte[]>> queryResult = rangeSlicesQuery
                                                                                       .execute();

    // On convertit le résultat en ColumnFamilyResultWrapper pour faciliter
    // son utilisation
    final QueryResultConverter<UUID, String, byte[]> converter = new QueryResultConverter<UUID, String, byte[]>();
    final ColumnFamilyResultWrapper<UUID, String> result = converter
                                                                    .getColumnFamilyResultWrapper(queryResult,
                                                                                                  UUIDSerializer.get(),
                                                                                                  StringSerializer.get(),
                                                                                                  bytesSerializer);

    // On itère sur le résultat
    final HectorIterator<UUID, String> resultIterator = new HectorIterator<UUID, String>(
                                                                                         result);
    final List<JobRequest> list = new ArrayList<JobRequest>();
    for (final ColumnFamilyResult<UUID, String> row : resultIterator) {
      final JobRequest jobRequest = jobRequestSupport.createJobRequestFromResult(row);
      // On peut obtenir un jobRequest null dans le cas d'un jobRequest
      // effacé
      if (jobRequest != null) {
        list.add(jobRequest);
      }
    }
    return list;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<JobRequest> getJobsToDelete(final Keyspace keyspace, final Date dateMax) {

    final List<JobRequest> list = new ArrayList<JobRequest>();
    // On n'utilise pas d'index. On récupère tous les jobs sans distinction,
    // en requêtant directement dans la CF JobRequest
    final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
    final RangeSlicesQuery<UUID, String, byte[]> rangeSlicesQuery = HFactory
                                                                            .createRangeSlicesQuery(keyspace,
                                                                                                    UUIDSerializer.get(),
                                                                                                    StringSerializer.get(),
                                                                                                    bytesSerializer);
    rangeSlicesQuery.setColumnFamily(JobRequestDao.JOBREQUEST_CFNAME);
    rangeSlicesQuery.setRange("", "", false, MAX_JOB_ATTIBUTS);

    final JobRequestRowsIterator iterator = new JobRequestRowsIterator(rangeSlicesQuery, 1000, jobRequestSupport);
    while (iterator.hasNext()) {
      final JobRequest jobRequest = iterator.next();
      // On peut obtenir un jobRequest null dans le cas d'un jobRequest
      // effacé
      if (jobRequest != null && (jobRequest.getCreationDate().before(dateMax)
          || DateUtils.isSameDay(jobRequest.getCreationDate(), dateMax))) {
        list.add(jobRequest);
      }
    }

    return list;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isJobResettable(final JobRequest job) {
    final String state = job.getState().toString();
    if (state.equals("RESERVED") || state.equals("STARTING")) {
      return true;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isJobRemovable(final JobRequest job) {
    final String state = job.getState().toString();
    if ("CREATED".equals(state) || "STARTING".equals(state)
        || "RESERVED".equals(state)) {
      return true;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID getJobRequestIdByJobKey(final byte[] jobKey) {
    Assert.notNull(jobKey, "Job key must not be null.");
    final IndexedSlicesPredicate<UUID, String, byte[]> predicate = new IndexedSlicesPredicate<UUID, String, byte[]>(
                                                                                                                    UUIDSerializer.get(),
                                                                                                                    StringSerializer.get(),
                                                                                                                    BytesArraySerializer.get());
    predicate.addExpression(JobRequestDao.JR_JOB_KEY_COLUMN,
                            IndexOperator.EQ,
                            jobKey);

    // Il est obligatoire de préciser une "start_key". (à ne pas confondre
    // avec Starsky !)
    // Ce "start_key" ne correspond pas à la clé de JobInstance, mais à la clé
    // de JobInstance.jobKey_idx
    // Il faut donc l'exprimer en bytes, mais l'API d'hector veut l'exprimer
    // en long, ce qui n'est pas bon.
    // Pour contourner le problème on passe par
    // IndexedSlicesPredicateHelper.setEmptyStartKey
    IndexedSlicesPredicateHelper.setEmptyStartKey(predicate);
    // predicate.startKey(new byte[0]);
    predicate.count(1);
    final ColumnFamilyResult<UUID, String> result = this.jobRequestSupport.getJobRequestTmpl().queryColumns(predicate);

    if (!result.hasResults()) {
      return null;
    }

    return result.getKey();
  }

}

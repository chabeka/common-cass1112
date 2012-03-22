package fr.urssaf.image.sae.pile.travaux.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.util.Assert;

import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.SliceQuery;

import fr.urssaf.image.commons.cassandra.helper.HectorIterator;
import fr.urssaf.image.commons.cassandra.serializer.NullableDateSerializer;
import fr.urssaf.image.sae.pile.travaux.dao.JobQueueDao;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.model.SimpleJobRequest;

/**
 * Implémentation du Service DAO {@link JobQueueDao}
 * 
 */
@SuppressWarnings({"PMD.LongVariable" })
public class JobQueueDaoImpl implements JobQueueDao {

   private final Keyspace keyspace;

   private static final String JOBREQUEST_CFNAME = "JobRequest";
   private static final String JOBSQUEUE_CFNAME = "JobsQueue";

   private final ColumnFamilyTemplate<UUID, String> jobRequestTmpl;
   private final ThriftColumnFamilyTemplate<String, UUID> jobsQueueTmpl;

   // Colonnes de JobRequest
   private static final String JR_TYPE_COLUMN = "type";
   private static final String JR_PARAMETERS_COLUMN = "parameters";
   private static final String JR_STATE_COLUMN = "state";
   private static final String JR_RESERVED_BY_COLUMN = "reservedBy";
   private static final String JR_CREATION_DATE_COLUMN = "creationDate";
   private static final String JR_RESERVATION_DATE_COLUMN = "reservationDate";
   private static final String JR_STARTING_DATE_COLUMN = "startingDate";
   private static final String JR_ENDING_DATE_COLUMN = "endingDate";
   private static final String JR_MESSAGE = "message";

   // Clés constantes
   private static final String JOBS_WAITING_KEY = "jobsWaiting";
   // Autres constantes
   private static final int MAX_NON_TERMINATED_JOBS = 500;

   // Durée de vie des colonnes
   private static final int TTL = 2592000;      // 2592000 secondes, soit 30 jours

   /**
    * Constructeur de la DAO
    * 
    * @param keyspace
    *           Keyspace cassandra à utiliser
    */
   public JobQueueDaoImpl(Keyspace keyspace) {
      this.keyspace = keyspace;

      jobRequestTmpl = new ThriftColumnFamilyTemplate<UUID, String>(keyspace,
            JOBREQUEST_CFNAME, UUIDSerializer.get(), StringSerializer.get());
      jobsQueueTmpl = new ThriftColumnFamilyTemplate<String, UUID>(keyspace,
            JOBSQUEUE_CFNAME, StringSerializer.get(), UUIDSerializer.get());
      jobsQueueTmpl.setCount(MAX_NON_TERMINATED_JOBS);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final JobRequest getJobRequest(UUID jobRequestUUID) {
      ColumnFamilyResult<UUID, String> result = jobRequestTmpl
            .queryColumns(jobRequestUUID);
      return getJobRequestFromResult(result);
   }

   /**
    * Crée un objet JobRequest à partir de données lues de cassandra.
    * 
    * @param result
    *           Données de cassandra
    * @return le jobRequest
    */
   private JobRequest getJobRequestFromResult(
         ColumnFamilyResult<UUID, String> result) {
      if (result == null || !result.hasResults()) {
         return null;
      }
      Serializer<Date> dSlz = NullableDateSerializer.get();
      JobRequest jobRequest = new JobRequest();
      jobRequest.setIdJob(result.getKey());
      jobRequest.setType(result.getString(JR_TYPE_COLUMN));
      jobRequest.setParameters(result.getString(JR_PARAMETERS_COLUMN));
      String state = result.getString(JR_STATE_COLUMN);
      jobRequest.setState(JobState.valueOf(state));
      jobRequest.setReservedBy(result.getString(JR_RESERVED_BY_COLUMN));
      Date creationDate = dSlz.fromBytes(result
            .getByteArray(JR_CREATION_DATE_COLUMN));
      jobRequest.setCreationDate(creationDate);
      Date reservationDate = dSlz.fromBytes(result
            .getByteArray(JR_RESERVATION_DATE_COLUMN));
      jobRequest.setReservationDate(reservationDate);
      Date startingDate = dSlz.fromBytes(result
            .getByteArray(JR_STARTING_DATE_COLUMN));
      jobRequest.setStartingDate(startingDate);
      Date endingDate = dSlz.fromBytes(result
            .getByteArray(JR_ENDING_DATE_COLUMN));
      jobRequest.setEndingDate(endingDate);
      jobRequest.setMessage(result.getString(JR_MESSAGE));
      return jobRequest;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<JobRequest> getNonTerminatedJobs(String hostname) {
      // Récupération des id, par ordre croissant
      ColumnFamilyResult<String, UUID> result = jobsQueueTmpl
            .queryColumns(hostname);
      Collection<UUID> jobRequestIds = result.getColumnNames();
      return getJobRequestsFromIds(jobRequestIds);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<SimpleJobRequest> getNonTerminatedSimpleJobs(String hostname) {
      ColumnFamilyResult<String, UUID> result = jobsQueueTmpl.queryColumns(hostname);
      Collection<UUID> colNames = result.getColumnNames();
      List<SimpleJobRequest> list = new ArrayList<SimpleJobRequest>(colNames.size());
      SimpleJobRequestSerializer slz = SimpleJobRequestSerializer.get();
      for (UUID uuid : colNames) {
         SimpleJobRequest simpleJobRequest = slz.fromBytes(result.getByteArray(uuid));
         list.add(simpleJobRequest);
      }
      return list;
   }

   /**
    * Récupère une liste de JobRequest à partir d'une liste d'id On fait en
    * sorte de renvoyer les jobRequest dans le même ordre que la liste des id
    * 
    * @param jobRequestIds
    *           Liste des id des jobRequest
    * @return Liste des jobRequest
    */
   private List<JobRequest> getJobRequestsFromIds(Collection<UUID> jobRequestIds) {
      ColumnFamilyResult<UUID, String> result = jobRequestTmpl
            .queryColumns(jobRequestIds);
      Map<UUID, JobRequest> map = new HashMap<UUID, JobRequest>(jobRequestIds
            .size());
      HectorIterator<UUID, String> resultIterator = new HectorIterator<UUID, String>(
            result);
      for (ColumnFamilyResult<UUID, String> row : resultIterator) {
         JobRequest jobRequest = getJobRequestFromResult(row);
         map.put(row.getKey(), jobRequest);
      }

      // On renvoie les jobRequest dans l'ordre des jobRequestIds
      List<JobRequest> list = new ArrayList<JobRequest>(jobRequestIds.size());
      for (UUID jobRequestId : jobRequestIds) {
         if (map.containsKey(jobRequestId)) {
            list.add(map.get(jobRequestId));
         }
      }
      return list;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Iterator<SimpleJobRequest> getUnreservedJobRequestIterator() {
      SliceQuery<String, UUID, String> sliceQuery = HFactory.createSliceQuery(
            keyspace, StringSerializer.get(), UUIDSerializer.get(),
            StringSerializer.get());
      sliceQuery.setKey(JOBS_WAITING_KEY);
      sliceQuery.setColumnFamily(JOBSQUEUE_CFNAME);
      return new JobRequestIterator(sliceQuery);
   }

   @SuppressWarnings("unchecked")
   private void addColumn(ColumnFamilyUpdater<UUID, String> updater, String colName, Object value, Serializer slz) {
      HColumn<String, Object> column = HFactory.createColumn(colName, value, StringSerializer.get(), slz);
      column.setTtl(TTL);
      updater.setColumn(column);
   }
   
   /**
    * {@inheritDoc} Attention : la modification de reservedBy n'est pas
    * supportée
    */
   @Override
   public final void saveJobRequest(JobRequest jobRequest) {
      // On vérifie que le jobRequest est suffisamment complet
      validateJobRequest(jobRequest);

      NullableDateSerializer dSlz = NullableDateSerializer.get();
      StringSerializer sSlz = StringSerializer.get();
      BytesArraySerializer bSlz = BytesArraySerializer.get();
      // On enregistre la demande dans JobRequest
      ColumnFamilyUpdater<UUID, String> updater = jobRequestTmpl
            .createUpdater(jobRequest.getIdJob());
      addColumn(updater, JR_TYPE_COLUMN, jobRequest.getType(), sSlz);
      addColumn(updater, JR_PARAMETERS_COLUMN, jobRequest.getParameters(), sSlz);
      addColumn(updater, JR_STATE_COLUMN, jobRequest.getState().name(), sSlz);
      if (jobRequest.getReservedBy() != null) {
         addColumn(updater, JR_RESERVED_BY_COLUMN, jobRequest.getReservedBy(), sSlz);
      }
      addColumn(updater, JR_CREATION_DATE_COLUMN, dSlz.toBytes(jobRequest.getCreationDate()), bSlz);
      addColumn(updater, JR_RESERVATION_DATE_COLUMN, dSlz.toBytes(jobRequest.getReservationDate()), bSlz);
      addColumn(updater, JR_STARTING_DATE_COLUMN, dSlz.toBytes(jobRequest.getStartingDate()), bSlz);
      addColumn(updater, JR_ENDING_DATE_COLUMN, dSlz.toBytes(jobRequest.getEndingDate()), bSlz);
      if (jobRequest.getMessage() != null) {
         addColumn(updater, JR_MESSAGE, jobRequest.getMessage(), sSlz);
      }
      jobRequestTmpl.update(updater);

      Mutator<String> mutator = HFactory.createMutator(keyspace,
            StringSerializer.get());
      // On indexe la demande dans JobsQueue pour indiquer si le job est
      // "en cours" ou non
      String hostname = jobRequest.getReservedBy();
      if (hostname != null && !hostname.isEmpty()) {
         JobState state = jobRequest.getState();
         if (state == JobState.RESERVED || state == JobState.STARTING) {
            // Le job est "en cours"
            HColumn<UUID, SimpleJobRequest> col = HFactory.createColumn(
                  jobRequest.getIdJob(), jobRequest.getSimpleJob(),
                  UUIDSerializer.get(), SimpleJobRequestSerializer.get());
            col.setTtl(TTL);
            mutator.addInsertion(hostname, JOBSQUEUE_CFNAME, col);
         } else {
            // Le job est n'est pas "en cours"
            mutator.addDeletion(hostname, JOBSQUEUE_CFNAME, jobRequest
                  .getIdJob(), UUIDSerializer.get());
         }
      }
      // On indexe la demande dans JobsQueue pour indiquer si le job est réservé
      // ou non
      if (hostname == null || hostname.isEmpty()) {
         // Le job est n'est pas réservé
         HColumn<UUID, SimpleJobRequest> col = HFactory.createColumn(jobRequest
               .getIdJob(), jobRequest.getSimpleJob(), UUIDSerializer.get(),
               SimpleJobRequestSerializer.get());
         col.setTtl(TTL);
         mutator.addInsertion(JOBS_WAITING_KEY, JOBSQUEUE_CFNAME, col);
      //} else {
         // NOPDM : TODO : virer la règle EmptyIfStmt
         // On ne fait rien ici : on dé-indexe seulement dans la méthode de réservation
      }
      mutator.execute();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void reserveJobRequest(JobRequest jobRequest, String hostname, Date reservationDate) {
      // On vérifie que le jobRequest est suffisamment complet
      validateJobRequest(jobRequest);
      UUID jobId = jobRequest.getIdJob();
      StringSerializer sSlz = StringSerializer.get();
      NullableDateSerializer dSlz = NullableDateSerializer.get();
      BytesArraySerializer bSlz = BytesArraySerializer.get();
      
      // On enregistre la demande dans JobRequest
      ColumnFamilyUpdater<UUID, String> updater = jobRequestTmpl.createUpdater(jobId);
      addColumn(updater, JR_STATE_COLUMN, JobState.RESERVED.name(), sSlz);
      addColumn(updater, JR_RESERVED_BY_COLUMN, hostname, sSlz);
      addColumn(updater, JR_RESERVATION_DATE_COLUMN, dSlz.toBytes(reservationDate), bSlz);
      jobRequestTmpl.update(updater);

      Mutator<String> mutator = HFactory.createMutator(keyspace,
            StringSerializer.get());
      // On indexe la demande dans JobsQueue pour indiquer que le job est "en cours" sur le serveur
      HColumn<UUID, SimpleJobRequest> col = HFactory.createColumn(
            jobId, jobRequest.getSimpleJob(),
            UUIDSerializer.get(), SimpleJobRequestSerializer.get());
      col.setTtl(TTL);
      mutator.addInsertion(hostname, JOBSQUEUE_CFNAME, col);
      // On indexe la demande dans JobsQueue pour indiquer que le job est réservé
      mutator.addDeletion(JOBS_WAITING_KEY, JOBSQUEUE_CFNAME, jobId, UUIDSerializer.get());
      mutator.execute();
   }
   
   /**
    * Valide qu'un objet jobRequest est suffisamment complet pour être persisté
    * @param jobRequest    Le jobRequest à valider
    */
   private void validateJobRequest(JobRequest jobRequest) {
      Assert.notNull(jobRequest, "jobRequest should not be null");
      Assert.notNull(jobRequest.getIdJob(), "Id should not be null");
      Assert.notNull(jobRequest.getType(), "Type should not be null");
      Assert.notNull(jobRequest.getParameters(), "Parameters should not be null");
      Assert.notNull(jobRequest.getState(), "State should not be null");
   }

   /**
    * {@inheritDoc} Attention : la modification de reservedBy n'est pas
    * supportée
    */
   @Override
   public final void updateJobRequest(JobRequest jobRequest) {
      saveJobRequest(jobRequest);
   }

   @Override
   public final void deleteJobRequest(JobRequest jobRequest) {
      // Suppression dans JobRequest
      Mutator<UUID> mutator = HFactory.createMutator(keyspace, UUIDSerializer
            .get());
      mutator.addDeletion(jobRequest.getIdJob(), JOBREQUEST_CFNAME);
      mutator.execute();

      // Suppression dans JobsQueue
      Mutator<String> mutator2 = HFactory.createMutator(keyspace,
            StringSerializer.get());
      mutator2.addDeletion(JOBS_WAITING_KEY, JOBSQUEUE_CFNAME, jobRequest
            .getIdJob(), UUIDSerializer.get());
      String hostname = jobRequest.getReservedBy();
      if (hostname != null && !hostname.isEmpty()) {
         mutator2.addDeletion(hostname, JOBSQUEUE_CFNAME,
               jobRequest.getIdJob(), UUIDSerializer.get());
      }
      mutator2.execute();

   }

}

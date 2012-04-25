package fr.urssaf.image.commons.exemples.cassandra.piletravaux.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.mutation.Mutator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.curator.framework.CuratorFramework;

import fr.urssaf.image.commons.exemples.cassandra.piletravaux.dao.JobHistoryDao;
import fr.urssaf.image.commons.exemples.cassandra.piletravaux.dao.JobQueuesDao;
import fr.urssaf.image.commons.exemples.cassandra.piletravaux.dao.JobRequestDao;
import fr.urssaf.image.commons.exemples.cassandra.piletravaux.modele.JobQueue;
import fr.urssaf.image.commons.exemples.cassandra.piletravaux.modele.JobRequest;
import fr.urssaf.image.commons.exemples.cassandra.piletravaux.modele.JobToCreate;
import fr.urssaf.image.commons.zookeeper.ZookeeperMutex;

public class PileTravauxEcritureService {

   
   private static final Logger LOGGER = LoggerFactory.getLogger(PileTravauxEcritureService.class);
   
   /**
    * Valeur de la clé pour les jobs en attente de réservation
    */
   private static final String JOBS_WAITING_KEY = "jobsWaiting";
   
   
   /**
    * Temps maximum de décalage d'horloge qu'il nous parait acceptable, en micro-secondes 
    */
   private static final int MAX_TIME_SYNCHRO_ERROR = 10 * 1000 * 1000;
   
   /**
    * Temps maximum de décalage d'horloge, en micro-secondes. Au delà, on logue une warning. 
    */
   private static final int MAX_TIME_SYNCHRO_WARN = 2 * 1000 * 1000;
   
   
   private static final long ONE_THOUSAND = 1000L;
   
   
   private Keyspace keyspace;
   
   private CuratorFramework curatorClient;
   
   private JobRequestDao jobRequestDao;
   
   private JobQueuesDao jobQueuesDao;
   
   private JobHistoryDao jobHistoryDao;
   
   private ColumnFamilyTemplate<String, String> jobQueuesTmpl;
   
   private ColumnFamilyTemplate<UUID, String> jobRequestTmpl;
   
   private ColumnFamilyTemplate<UUID, String> jobHistoryTmpl;
   
   
   
   
   /**
    * Time-out du lock, en secondes
    */
   private static final int LOCK_TIME_OUT = 20;
   
   
   public PileTravauxEcritureService(
         Keyspace keyspace,
         CuratorFramework curatorClient,
         JobRequestDao jobRequestDao,
         JobQueuesDao jobQueuesDao,
         JobHistoryDao jobHistoryDao) {
      
      this.keyspace = keyspace;
      this.curatorClient = curatorClient;
      
      this.jobRequestDao = jobRequestDao;
      this.jobQueuesDao = jobQueuesDao;
      this.jobHistoryDao = jobHistoryDao;
      
      jobQueuesTmpl = jobQueuesDao.createCFTemplate(keyspace);
      jobRequestTmpl = jobRequestDao.createCFTemplate(keyspace);
      jobHistoryTmpl = jobHistoryDao.createCFTemplate(keyspace);
      
   }
   
   
   
   private long getCurrentClock() {
      return keyspace.createClock();
   }
   
   
   private long getClockAvecGestionDecalage(long clockColonneExistante) {
      
      // On s'assure que le nouveau timestamp est supérieur à l'ancien
      long newClock = getCurrentClock();      
      if (newClock <= clockColonneExistante) {
         // On s'assure que le décalage n'est pas trop important
         // Les clocks sont exprimés en micro-secondes
         if ((clockColonneExistante - newClock) > MAX_TIME_SYNCHRO_ERROR) {
            // TODO : Mettre une belle exception pas runtime
            throw new RuntimeException(
                  "Problème détecté lors de l'analyse du décalage des timestamp."
                  + " Vérifier la sychronisation des horloges des serveurs."
                  + " Ancien timestamp :" + clockColonneExistante + " - Nouveau timestamp : " + newClock);
         }
         if ((clockColonneExistante - newClock) > MAX_TIME_SYNCHRO_WARN) {
            LOGGER.warn("Attention, les horloges des serveurs semblent désynchronisées. Le décalage est au moins de " 
                  + (clockColonneExistante - newClock) / ONE_THOUSAND + " ms");
         }
         // Sinon, on positionne le nouveau timestamp juste au dessus de l'ancien
         newClock = clockColonneExistante + 1;
      }
      
      // Renvoie du résultat
      return newClock;
      
      
   }
   
   
   private void logClock(long clock) {
      if (LOGGER.isDebugEnabled()) {
         Date dateClock = new Date(clock / 1000); // /1000 car clock est un micro-sec et new Date() attend des ms 
         String pattern = "dd/MM/yyyy HH:mm:ss.SSS";
         DateFormat dateFormat = new SimpleDateFormat(pattern);
         String dateFormatee = dateFormat.format(dateClock);
         LOGGER.debug(dateFormatee);
      }
   }
   
   
   public void ajouterNouveauJob(JobToCreate jobToCreate) {
      
      // Timestamp de l'opération
      // Pas besoin de gérer le décalage ici : on ne fait que la création
      long clock = getCurrentClock();
      logClock(clock);
      
      // Ecriture dans la CF "JobRequest"
      ajouterJobDansJobRequest(jobToCreate,clock);
      
      // Ecriture dans la CF "JobQueues"
      ajouterJobDansJobQueuesEnWaiting(
            jobToCreate.getIdJob(),
            jobToCreate.getType(),
            jobToCreate.getParameters(),
            clock);
      
      // Ecriture dans la CF "JobHistory"
      String messageTrace = "CREATION DU JOB";
      UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      ajouterTrace(jobToCreate.getIdJob(), timestampTrace, messageTrace, clock);
      
   }
   
   
   private void ajouterJobDansJobRequest(
         JobToCreate jobToCreate, long clock) {
      
      
      // Valeur définie "en dur" par la méthode
      String state = "CREATED";
      
      // On utilise un ColumnFamilyUpdater, et on renseigne
      // la valeur de la clé dans la construction de l'updater
      ColumnFamilyUpdater<UUID, String> updaterJobRequest = 
          jobRequestTmpl.createUpdater(jobToCreate.getIdJob());
      
      // Ecriture des colonnes
      jobRequestDao.ecritColonneType(updaterJobRequest, jobToCreate.getType(), clock);
      jobRequestDao.ecritColonneParameters(updaterJobRequest, jobToCreate.getParameters(), clock);
      jobRequestDao.ecritColonneState(updaterJobRequest, state, clock);
      jobRequestDao.ecritColonneCreationDate(updaterJobRequest, jobToCreate.getCreationDate(), clock);
      jobRequestDao.ecritColonneSaeHost(updaterJobRequest, jobToCreate.getSaeHost(), clock);
      jobRequestDao.ecritColonneClientHost(updaterJobRequest, jobToCreate.getClientHost(), clock);
      jobRequestDao.ecritColonneDocCount(updaterJobRequest, jobToCreate.getDocCount(), clock);
      
      // Ecrit en base
      jobRequestTmpl.update(updaterJobRequest);
      
   }
   
   
   
   private void ajouterJobDansJobQueuesEnWaiting(
         UUID idJob,
         String type,
         String parameters,
         long clock) {
      
      // On utilise un ColumnFamilyUpdater, et on renseigne
      // la valeur de la clé dans la construction de l'updater
      ColumnFamilyUpdater<String, String> updaterJobQueues = 
         jobQueuesTmpl.createUpdater(JOBS_WAITING_KEY);
      
      // Ecriture des colonnes
      JobQueue jobQueue = new JobQueue();
      jobQueue.setIdJob(idJob);
      jobQueue.setType(type);
      jobQueue.setParameters(parameters);
      jobQueuesDao.ecritColonneJobQueue(updaterJobQueues, idJob, jobQueue, clock);
      
      // Ecrit en base
      jobQueuesTmpl.update(updaterJobQueues);
      
   }
   
   
   private void ajouterTrace(
         UUID idJob,
         UUID timestampTrace,
         String messageTrace,
         long clock) {
      
      // On utilise un ColumnFamilyUpdater, et on renseigne
      // la valeur de la clé dans la construction de l'updater
      ColumnFamilyUpdater<UUID, String> updaterJobHistory = 
         jobHistoryTmpl.createUpdater(idJob);
      
      // Ecriture des colonnes
      jobHistoryDao.ecritColonneTrace(updaterJobHistory, timestampTrace, messageTrace, clock);
      
      // Ecrit en base
      jobHistoryTmpl.update(updaterJobHistory);
      
   }
   
   
   public void reserverUnJob(
         UUID idJob,
         String reservedBy,
         Date reservationDate) {
      
      // TODO: Vérifier que le job existe
      // TODO: Vérifier que le job n'est pas déjà RESERVED
      // TODO: Vérifier que le job est à l'état CREATED
      
      // Lecture du job
      ColumnFamilyResult<UUID, String> result = jobRequestTmpl.queryColumns(idJob);
      
      // Récupération du timestamp courant de la colonne "state"
      long clockColonneExistante = jobRequestDao.getClockColonneState(result);
      
      // Lecture des propriétés du job dont on a besoin
      JobRequest jobRequest = jobRequestDao.createJobRequestFromResult(result);
      String type = jobRequest.getType();
      String parameters = jobRequest.getParameters(); 
      
      // Timestamp de l'opération
      // Il faut vérifier le décalage de temps
      long clock = getClockAvecGestionDecalage(clockColonneExistante);
      logClock(clock);
      
      // Ecriture dans la CF "JobRequest"
      reserverJobDansJobRequest(idJob,reservedBy,reservationDate,clock);
      
      // Ecriture dans la CF "JobQueues"
      reserverJobDansJobQueues(idJob, reservedBy, type, parameters, clock);
      
      // Ecriture dans la CF "JobHistory"
      String messageTrace = "RESERVATION DU JOB";
      UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      ajouterTrace(idJob, timestampTrace, messageTrace, clock);
      
   }
   
   
   
   private void reserverJobDansJobRequest(
         UUID idJob,
         String reservedBy,
         Date reservationDate,
         long clock) {
      
      
      // Il faut mettre un lock sur la table JobRequest pour éviter
      // qu'un autre process écrive en même temps
      ZookeeperMutex mutex = new ZookeeperMutex(curatorClient, "/JobRequest/"
            + idJob);
      try {
         
         // Sauce au mutex
         if (!mutex.acquire(LOCK_TIME_OUT, TimeUnit.SECONDS)) {
            // TODO: Repasser à LockTimeoutException
            throw new RuntimeException(
                  "Erreur lors de la tentative d'acquisition du lock pour le jobRequest "
                        + idJob + " : on n'a pas obtenu le lock au bout de "
                        + LOCK_TIME_OUT + " secondes.");
         }
         
         // Valeur définie "en dur" par la méthode
         String state = "RESERVED";
         
         // On utilise un ColumnFamilyUpdater, et on renseigne
         // la valeur de la clé dans la construction de l'updater
         ColumnFamilyUpdater<UUID, String> updaterJobRequest = 
             jobRequestTmpl.createUpdater(idJob);
         
         // Ecriture des colonnes
         jobRequestDao.ecritColonneState(updaterJobRequest, state, clock);
         jobRequestDao.ecritColonneReservedBy(updaterJobRequest, reservedBy, clock);
         jobRequestDao.ecritColonneReservationDate(updaterJobRequest, reservationDate, clock);
         
         // Ecrit en base
         jobRequestTmpl.update(updaterJobRequest);
         
      } finally {
         mutex.release();
      }
      
   }
   
   
   
   private void reserverJobDansJobQueues(
         UUID idJob, 
         String reservedBy, 
         String type, 
         String parameters, 
         long clock) {
      
      // Dans la CF JobQueues, on "switch" le job entre :
      //  - la clé "jobsWaiting" (suppression)
      //  - la clé "valeur de reservedBy" (création)
      
      // Pour cela, on utilise un Mutator pour réaliser en "batch" les
      // deux opérations
      
      // Création du Mutator
      Mutator<String> mutator = jobQueuesDao.createMutator(keyspace);
      
      // Opération 1: Ajout du job pour le serveur qui l'a réservé
      JobQueue jobQueue = new JobQueue();
      jobQueue.setIdJob(idJob);
      jobQueue.setType(type);
      jobQueue.setParameters(parameters);
      jobQueuesDao.mutatorAjouterInsertionJobQueue(
            mutator, reservedBy, jobQueue, clock);
      
      // Opération 2: Suppression du job de la liste des jobs non réservé
      jobQueuesDao.mutatorAjouterSuppressionJobQueue(mutator, JOBS_WAITING_KEY, idJob, clock);
      
      // Exécution des 2 opérations
      mutator.execute();
      
   }
   
   
   
   public void passerJobAetatEnCours(
         UUID idJob,
         Date startingDate) {
      
      // TODO: Vérifier que le job existe
      // TODO: Vérifier que le job est à l'état RESERVED
      
      
      // Lecture du job
      ColumnFamilyResult<UUID, String> result = jobRequestTmpl.queryColumns(idJob);
      
      // Récupération du timestamp courant de la colonne "state"
      long clockColonneExistante = jobRequestDao.getClockColonneState(result);
      
      // Timestamp de l'opération
      // Il faut vérifier le décalage de temps
      long clock = getClockAvecGestionDecalage(clockColonneExistante);
      logClock(clock);
      
      // Ecriture dans la CF "JobRequest"
      passerEtatEnCoursJobRequest(idJob,startingDate,clock);
      
      // Ecriture dans la CF "JobQueues"
      // rien à écrire
      
      // Ecriture dans la CF "JobHistory"
      String messageTrace = "DEMARRAGE DU JOB";
      UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      ajouterTrace(idJob, timestampTrace, messageTrace, clock);
      
      
   }
   
   
   private void passerEtatEnCoursJobRequest(
         UUID idJob,
         Date startingDate,
         long clock) {
      
      // Valeur définie "en dur" par la méthode
      String state = "STARTING";
      
      // On utilise un ColumnFamilyUpdater, et on renseigne
      // la valeur de la clé dans la construction de l'updater
      ColumnFamilyUpdater<UUID, String> updaterJobRequest = 
          jobRequestTmpl.createUpdater(idJob);
      
      // Ecriture des colonnes
      jobRequestDao.ecritColonneState(updaterJobRequest, state, clock);
      jobRequestDao.ecritColonneStartingDate(updaterJobRequest, startingDate, clock);
      
      // Ecrit en base
      jobRequestTmpl.update(updaterJobRequest);
      
   }
   
   
   
   public void passerJobAetatTermine(
         UUID idJob,
         Date endingDate,
         boolean success,
         String message) {
      
      // TODO: Vérifier que le job existe
      // TODO: Vérifier que le job est à l'état STARTING
      
      
      // Lecture du job
      ColumnFamilyResult<UUID, String> result = jobRequestTmpl.queryColumns(idJob);
      
      // Récupération du timestamp courant de la colonne "state"
      long clockColonneExistante = jobRequestDao.getClockColonneState(result);
      
      // Lecture des propriétés du job dont on a besoin
      JobRequest jobRequest = jobRequestDao.createJobRequestFromResult(result);
      String reservedBy = jobRequest.getReservedBy();
      
      // Timestamp de l'opération
      // Il faut vérifier le décalage de temps
      long clock = getClockAvecGestionDecalage(clockColonneExistante);
      logClock(clock);
      
      // Ecriture dans la CF "JobRequest"
      passerEtatTermineJobRequest(idJob,endingDate,success,message,clock);
      
      // Ecriture dans la CF "JobQueues"
      supprimerJobDeJobsQueues(idJob, reservedBy, clock);
      
      // Ecriture dans la CF "JobHistory"
      String messageTrace = "FIN DU JOB";
      UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      ajouterTrace(idJob, timestampTrace, messageTrace, clock);
      
   }
   
   
   private void passerEtatTermineJobRequest(
         UUID idJob,
         Date endingDate,
         boolean success,
         String message,
         long clock) {
    
      // Valeur définie "en dur" par la méthode
      String state;
      if (success) {
         state = "SUCCESS";
      } else {
         state = "FAILURE";
      }
      
      // On utilise un ColumnFamilyUpdater, et on renseigne
      // la valeur de la clé dans la construction de l'updater
      ColumnFamilyUpdater<UUID, String> updaterJobRequest = 
          jobRequestTmpl.createUpdater(idJob);
      
      // Ecriture des colonnes
      jobRequestDao.ecritColonneState(updaterJobRequest, state, clock);
      jobRequestDao.ecritColonneEndingDate(updaterJobRequest, endingDate, clock);
      jobRequestDao.ecritColonneMessage(updaterJobRequest, message, clock);
      
      // Ecrit en base
      jobRequestTmpl.update(updaterJobRequest);
      
   }
   
   
   private void supprimerJobDeJobsQueues(
         UUID idJob,
         String reservedBy,
         long clock) {
      
      // On ne peut pas passer par un ColumnFamilyTemplate
      // Car les méthodes deleteColumn() du template ne permettent
      // pas de spécifier le type du nom de la colonne, et prenne le serializer par défaut
      // Or, dans notre cas, le type du nom de la colonne est UUID
      
//      // Suppression du job de la clé correspondant au serveur ayant réservé
//      // le job
//      jobQueuesTmpl.deleteColumn(reservedBy, idJob);
      
      
      // Création du Mutator
      Mutator<String> mutator = jobQueuesDao.createMutator(keyspace);
      
      // Opération unique : on supprime le job
      jobQueuesDao.mutatorAjouterSuppressionJobQueue(mutator, reservedBy, idJob, clock);
      
      // Exécution de l'opération
      mutator.execute();
      
      
   }
   
   
   public void renseignerPidJob(
         UUID idJob,
         Integer pid) {
      
      // TODO: Vérifier que le job existe
      
      
      // Lecture du job
      ColumnFamilyResult<UUID, String> result = jobRequestTmpl.queryColumns(idJob);
      
      // Récupération du timestamp courant de la colonne "pid", si elle est présente
      boolean gererDecalage;
      long clockColonneExistante;
      if (jobRequestDao.existeColonnePid(result)) {
         gererDecalage = true;
         clockColonneExistante = jobRequestDao.getClockColonnePid(result);
      } else {
         gererDecalage = true;
         clockColonneExistante = -1;
      }
      
      // Timestamp de l'opération
      // Il faut éventuellement vérifier le décalage de temps
      long clock;
      if (gererDecalage) {
         clock = getClockAvecGestionDecalage(clockColonneExistante); 
         }
      else {
         clock = getCurrentClock();
      }
      logClock(clock);
      
      // Ecriture dans la CF "JobRequest"
      renseignerPidDansJobRequest(idJob,pid,clock);
      
      // Ecriture dans la CF "JobQueues"
      // rien à écrire
      
      // Ecriture dans la CF "JobHistory"
      String messageTrace = "PID RENSEIGNE";
      UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      ajouterTrace(idJob, timestampTrace, messageTrace, clock);
      
   }
   
   
   private void renseignerPidDansJobRequest(
         UUID idJob,
         Integer pid,
         long clock) {
      
      // On utilise un ColumnFamilyUpdater, et on renseigne
      // la valeur de la clé dans la construction de l'updater
      ColumnFamilyUpdater<UUID, String> updaterJobRequest = 
          jobRequestTmpl.createUpdater(idJob);
      
      // Ecriture des colonnes
      jobRequestDao.ecritColonnePid(updaterJobRequest, pid, clock);
      
      // Ecrit en base
      jobRequestTmpl.update(updaterJobRequest);
      
   }
   
   
   public void ajouterTrace(
         UUID idJob,
         String messageTrace) {
      
      // Timestamp de l'opération
      // Pas besoin de gérer le décalage ici : on ne fait que la création
      long clock = getCurrentClock();
      logClock(clock);
      
      // Ecriture dans la CF "JobRequest"
      // rien à écrire
      
      // Ecriture dans la CF "JobQueues"
      // rien à écrire
      
      // Ecriture dans la CF "JobHistory"
      UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      ajouterTrace(idJob, timestampTrace, messageTrace, clock);
      
   }
   
   
}

package fr.urssaf.image.sae.pile.travaux.dao;

import java.util.Date;
import java.util.UUID;

import me.prettyprint.cassandra.serializers.BooleanSerializer;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.IntegerSerializer;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.serializer.NullableDateSerializer;
import fr.urssaf.image.sae.pile.travaux.dao.serializer.VISerializer;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;

/**
 * DAO de la colonne famille <code>JobRequest</code>
 * 
 * 
 */
@Repository
public class JobRequestDao {

   private static final String JOBREQUEST_CFNAME = "JobRequest";

   /**
    * Colonne {@value #JR_TYPE_COLUMN}
    */
   public static final String JR_TYPE_COLUMN = "type";

   /**
    * Colonne {@value #JR_PARAMETERS_COLUMN}
    */
   public static final String JR_PARAMETERS_COLUMN = "parameters";

   /**
    * Colonne {@value #JR_STATE_COLUMN}
    */
   public static final String JR_STATE_COLUMN = "state";

   /**
    * Colonne {@value #JR_RESERVED_BY_COLUMN}
    */
   public static final String JR_RESERVED_BY_COLUMN = "reservedBy";

   /**
    * Colonne {@value #JR_CREATION_DATE_COLUMN}
    */
   public static final String JR_CREATION_DATE_COLUMN = "creationDate";

   /**
    * Colonne {@value #JR_TYPE_COLUMN}
    */
   public static final String JR_RESERVATION_DATE_COLUMN = "reservationDate";

   /**
    * Colonne {@value #JR_STARTING_DATE_COLUMN}
    */
   public static final String JR_STARTING_DATE_COLUMN = "startingDate";

   /**
    * Colonne {@value #JR_ENDING_DATE_COLUMN}
    */
   public static final String JR_ENDING_DATE_COLUMN = "endingDate";

   /**
    * Colonne {@value #JR_MESSAGE}
    */
   public static final String JR_MESSAGE = "message";

   /**
    * Colonne {@value #JR_SAE_HOST}
    */
   public static final String JR_SAE_HOST = "saeHost";

   /**
    * Colonne {@value #JR_CLIENT_HOST}
    */
   public static final String JR_CLIENT_HOST = "clientHost";

   /**
    * Colonne {@value #JR_DOC_COUNT}
    */
   public static final String JR_DOC_COUNT = "docCount";

   /**
    * Colonne {@value #JR_PID}
    */
   public static final String JR_PID = "pid";

   /**
    * Colonne {@value #JR_TO_CHECK_FLAG}
    */
   public static final String JR_TO_CHECK_FLAG = "toCheckFlag";

   /**
    * Colonne {@value #JR_TO_CHECK_FLAG_RAISON}
    */
   public static final String JR_TO_CHECK_FLAG_RAISON = "toCheckFlagRaison";

   /**
    * Colonne {@value #JR_VI}
    */
   public static final String JR_VI = "vi";

   private static final int MAX_JOB_ATTIBUTS = 100;

   private static final int TTL = 2592000; // 2592000 secondes, soit 30 jours

   private final ColumnFamilyTemplate<UUID, String> jobRequestTmpl;

   private final Keyspace keyspace;

   /**
    * 
    * @param keyspace
    *           Keyspace utilisé par la pile des travaux
    */
   @Autowired
   public JobRequestDao(Keyspace keyspace) {

      this.keyspace = keyspace;

      // Propriété de clé:
      // - Type de la valeur : UUID
      // - Serializer de la valeur : UUIDSerializer

      jobRequestTmpl = new ThriftColumnFamilyTemplate<UUID, String>(keyspace,
            JOBREQUEST_CFNAME, UUIDSerializer.get(), StringSerializer.get());

      jobRequestTmpl.setCount(MAX_JOB_ATTIBUTS);

   }

   @SuppressWarnings("unchecked")
   private void addColumn(ColumnFamilyUpdater<UUID, String> updater,
         String colName, Object value, Serializer nameSerializer,
         Serializer valueSerializer, long clock) {

      HColumn<String, Object> column = HFactory.createColumn(colName, value,
            nameSerializer, valueSerializer);

      column.setTtl(TTL);
      column.setClock(clock);
      updater.setColumn(column);

   }

   /**
    * 
    * @return CassandraTemplate de <code>JobRequest</code>
    */
   public final ColumnFamilyTemplate<UUID, String> getJobRequestTmpl() {

      return this.jobRequestTmpl;
   }

   /**
    * 
    * @return Mutator de <code>JobRequest</code>
    */
   public final Mutator<UUID> createMutator() {

      Mutator<UUID> mutator = HFactory.createMutator(keyspace, UUIDSerializer
            .get());

      return mutator;

   }

   /**
    * Ajoute une colonne {@value #JR_TYPE_COLUMN}
    * 
    * @param updater
    *           Updater de <code>JobRequest</code>
    * @param valeur
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritColonneType(
         ColumnFamilyUpdater<UUID, String> updater, String valeur, long clock) {

      addColumn(updater, JR_TYPE_COLUMN, valeur, StringSerializer.get(),
            StringSerializer.get(), clock);

   }

   /**
    * Ajoute une colonne {@value #JR_PARAMETERS_COLUMN}
    * 
    * @param updater
    *           Updater de <code>JobRequest</code>
    * @param valeur
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritColonneParameters(
         ColumnFamilyUpdater<UUID, String> updater, String valeur, long clock) {

      addColumn(updater, JR_PARAMETERS_COLUMN, valeur, StringSerializer.get(),
            StringSerializer.get(), clock);

   }

   /**
    * Ajoute une colonne {@value #JR_STATE_COLUMN}
    * 
    * @param updater
    *           Updater de <code>JobRequest</code>
    * @param state
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritColonneState(
         ColumnFamilyUpdater<UUID, String> updater, String state, long clock) {

      addColumn(updater, JR_STATE_COLUMN, state, StringSerializer.get(),
            StringSerializer.get(), clock);

   }

   /**
    * Ajoute une colonne {@value #JR_RESERVED_BY_COLUMN}
    * 
    * @param updater
    *           Updater de <code>JobRequest</code>
    * @param reservedBy
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritColonneReservedBy(
         ColumnFamilyUpdater<UUID, String> updater, String reservedBy,
         long clock) {

      addColumn(updater, JR_RESERVED_BY_COLUMN, reservedBy, StringSerializer
            .get(), StringSerializer.get(), clock);

   }

   /**
    * Ajoute une colonne {@value #JR_CREATION_DATE_COLUMN}
    * 
    * @param updater
    *           Updater de <code>JobRequest</code>
    * @param creationDate
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritColonneCreationDate(
         ColumnFamilyUpdater<UUID, String> updater, Date creationDate,
         long clock) {

      Serializer<Date> dSlz = NullableDateSerializer.get();
      BytesArraySerializer bSlz = BytesArraySerializer.get();

      addColumn(updater, JR_CREATION_DATE_COLUMN, dSlz.toBytes(creationDate),
            StringSerializer.get(), bSlz, clock);

   }

   /**
    * Ajoute une colonne {@value #JR_RESERVATION_DATE_COLUMN}
    * 
    * @param updater
    *           Updater de <code>JobRequest</code>
    * @param reservationDate
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritColonneReservationDate(
         ColumnFamilyUpdater<UUID, String> updater, Date reservationDate,
         long clock) {

      Serializer<Date> dSlz = NullableDateSerializer.get();
      BytesArraySerializer bSlz = BytesArraySerializer.get();

      addColumn(updater, JR_RESERVATION_DATE_COLUMN, dSlz
            .toBytes(reservationDate), StringSerializer.get(), bSlz, clock);

   }

   /**
    * Ajoute une colonne {@value #JR_STARTING_DATE_COLUMN}
    * 
    * @param updater
    *           Updater de <code>JobRequest</code>
    * @param startingDate
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritColonneStartingDate(
         ColumnFamilyUpdater<UUID, String> updater, Date startingDate,
         long clock) {

      Serializer<Date> dSlz = NullableDateSerializer.get();
      BytesArraySerializer bSlz = BytesArraySerializer.get();

      addColumn(updater, JR_STARTING_DATE_COLUMN, dSlz.toBytes(startingDate),
            StringSerializer.get(), bSlz, clock);

   }

   /**
    * Ajoute une colonne {@value #JR_ENDING_DATE_COLUMN}
    * 
    * @param updater
    *           Updater de <code>JobRequest</code>
    * @param endingDate
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritColonneEndingDate(
         ColumnFamilyUpdater<UUID, String> updater, Date endingDate, long clock) {

      Serializer<Date> dSlz = NullableDateSerializer.get();
      BytesArraySerializer bSlz = BytesArraySerializer.get();

      addColumn(updater, JR_ENDING_DATE_COLUMN, dSlz.toBytes(endingDate),
            StringSerializer.get(), bSlz, clock);

   }

   /**
    * Ajoute une colonne {@value #JR_MESSAGE}
    * 
    * @param updater
    *           Updater de <code>JobRequest</code>
    * @param message
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritColonneMessage(
         ColumnFamilyUpdater<UUID, String> updater, String message, long clock) {

      addColumn(updater, JR_MESSAGE, message, StringSerializer.get(),
            StringSerializer.get(), clock);

   }

   /**
    * Ajoute une colonne {@value #JR_SAE_HOST}
    * 
    * @param updater
    *           Updater de <code>JobRequest</code>
    * @param saeHost
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritColonneSaeHost(
         ColumnFamilyUpdater<UUID, String> updater, String saeHost, long clock) {

      addColumn(updater, JR_SAE_HOST, saeHost, StringSerializer.get(),
            StringSerializer.get(), clock);

   }

   /**
    * Ajoute une colonne {@value #JR_CLIENT_HOST}
    * 
    * @param updater
    *           Updater de <code>JobRequest</code>
    * @param clientHost
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritColonneClientHost(
         ColumnFamilyUpdater<UUID, String> updater, String clientHost,
         long clock) {

      addColumn(updater, JR_CLIENT_HOST, clientHost, StringSerializer.get(),
            StringSerializer.get(), clock);

   }

   /**
    * Ajoute une colonne {@value #JR_DOC_COUNT}
    * 
    * @param updater
    *           Updater de <code>JobRequest</code>
    * @param docCount
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritColonneDocCount(
         ColumnFamilyUpdater<UUID, String> updater, Integer docCount, long clock) {

      addColumn(updater, JR_DOC_COUNT, docCount, StringSerializer.get(),
            IntegerSerializer.get(), clock);

   }

   /**
    * Ajoute une colonne {@value #JR_PID}
    * 
    * @param updater
    *           Updater de <code>JobRequest</code>
    * @param pid
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritColonnePid(ColumnFamilyUpdater<UUID, String> updater,
         Integer pid, long clock) {

      addColumn(updater, JR_PID, pid, StringSerializer.get(), IntegerSerializer
            .get(), clock);

   }

   /**
    * Ajoute une colonne {@value #JR_TO_CHECK_FLAG}
    * 
    * @param updater
    *           Updater de <code>JobRequest</code>
    * @param toCheckFlag
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritColonneToCheckFlag(
         ColumnFamilyUpdater<UUID, String> updater, Boolean toCheckFlag,
         long clock) {

      addColumn(updater, JR_TO_CHECK_FLAG, toCheckFlag, StringSerializer.get(),
            BooleanSerializer.get(), clock);

   }

   /**
    * Ajoute une colonne {@value #JR_TO_CHECK_FLAG_RAISON}
    * 
    * @param updater
    *           Updater de <code>JobRequest</code>
    * @param toCheckFlagRaison
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritColonneToCheckFlagRaison(
         ColumnFamilyUpdater<UUID, String> updater, String toCheckFlagRaison,
         long clock) {

      addColumn(updater, JR_TO_CHECK_FLAG_RAISON, toCheckFlagRaison,
            StringSerializer.get(), StringSerializer.get(), clock);

   }

   /**
    * Ajoute une colonne {@value #JR_VI}
    * 
    * @param updater
    *           Updater de <code>JobRequest</code>
    * @param valeur
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritColonneVi(ColumnFamilyUpdater<UUID, String> updater,
         VIContenuExtrait valeur, long clock) {

      addColumn(updater, JR_VI, valeur, StringSerializer.get(),
            VISerializer.get(), clock);

   }

   /**
    * Suppression d'un JobRequest
    * 
    * @param mutator
    *           Mutator de <code>JobRequest</code>
    * @param idJob
    *           nom de la ligne
    * @param clock
    *           horloge de la suppression
    */
   public final void mutatorSuppressionJobRequest(Mutator<UUID> mutator,
         UUID idJob, long clock) {

      mutator.addDeletion(idJob, JOBREQUEST_CFNAME, clock);

   }

   /**
    * 
    * @param result
    *           données d'un job
    * 
    * @return instance de {@link JobRequest}
    */
   public final JobRequest createJobRequestFromResult(
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

      if (result.getByteArray(JR_RESERVATION_DATE_COLUMN) != null) {
         Date reservationDate = dSlz.fromBytes(result
               .getByteArray(JR_RESERVATION_DATE_COLUMN));
         jobRequest.setReservationDate(reservationDate);
      }

      if (result.getByteArray(JR_STARTING_DATE_COLUMN) != null) {
         Date startingDate = dSlz.fromBytes(result
               .getByteArray(JR_STARTING_DATE_COLUMN));
         jobRequest.setStartingDate(startingDate);
      }

      if (result.getByteArray(JR_ENDING_DATE_COLUMN) != null) {
         Date endingDate = dSlz.fromBytes(result
               .getByteArray(JR_ENDING_DATE_COLUMN));
         jobRequest.setEndingDate(endingDate);
      }

      jobRequest.setMessage(result.getString(JR_MESSAGE));

      jobRequest.setSaeHost(result.getString(JR_SAE_HOST));

      jobRequest.setClientHost(result.getString(JR_CLIENT_HOST));

      jobRequest.setPid(result.getInteger(JR_PID));

      jobRequest.setDocCount(result.getInteger(JR_DOC_COUNT));

      jobRequest.setToCheckFlag(result.getBoolean(JR_TO_CHECK_FLAG));

      jobRequest
            .setToCheckFlagRaison(result.getString(JR_TO_CHECK_FLAG_RAISON));

      if (result.getByteArray(JR_VI) != null) {
         VIContenuExtrait contenuExtrait = VISerializer.get().fromBytes(
               result.getByteArray(JR_VI));
         jobRequest.setVi(contenuExtrait);
      }

      return jobRequest;
   }

}

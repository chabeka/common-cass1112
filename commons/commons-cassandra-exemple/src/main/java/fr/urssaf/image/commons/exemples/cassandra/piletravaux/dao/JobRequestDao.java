package fr.urssaf.image.commons.exemples.cassandra.piletravaux.dao;

import java.util.Date;
import java.util.UUID;

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
import fr.urssaf.image.commons.cassandra.serializer.NullableDateSerializer;
import fr.urssaf.image.commons.exemples.cassandra.piletravaux.modele.JobRequest;
import fr.urssaf.image.commons.exemples.cassandra.piletravaux.modele.JobState;

public class JobRequestDao {

   private static final String JOBREQUEST_CFNAME = "JobRequest";
   
   private static final String JR_TYPE_COLUMN = "type";
   private static final String JR_PARAMETERS_COLUMN = "parameters";
   private static final String JR_STATE_COLUMN = "state";
   private static final String JR_RESERVED_BY_COLUMN = "reservedBy";
   private static final String JR_CREATION_DATE_COLUMN = "creationDate";
   private static final String JR_RESERVATION_DATE_COLUMN = "reservationDate";
   private static final String JR_STARTING_DATE_COLUMN = "startingDate";
   private static final String JR_ENDING_DATE_COLUMN = "endingDate";
   private static final String JR_MESSAGE = "message";
   private static final String JR_SAE_HOST = "saeHost";
   private static final String JR_CLIENT_HOST = "clientHost";
   private static final String JR_DOC_COUNT = "docCount";
   private static final String JR_PID = "pid";
   
   private static final int MAX_JOB_ATTIBUTS = 100;
   
   private static final int TTL = 2592000; // 2592000 secondes, soit 30 jours
   
   
   
   public ColumnFamilyTemplate<UUID, String> createCFTemplate(Keyspace keyspace) {
      
      // Propriété de clé:
      //  - Type de la valeur : UUID
      //  - Serializer de la valeur : UUIDSerializer
      
      ColumnFamilyTemplate<UUID, String> jobRequestTmpl = new ThriftColumnFamilyTemplate<UUID, String>(
            keyspace,
            JOBREQUEST_CFNAME,
            UUIDSerializer.get(),
            StringSerializer.get());
      
      jobRequestTmpl.setCount(MAX_JOB_ATTIBUTS);
      
      return jobRequestTmpl; 
      
   }
   
   
   
   @SuppressWarnings("unchecked")
   private void addColumn(
         ColumnFamilyUpdater<UUID, String> updater,
         String colName, 
         Object value, 
         Serializer nameSerializer,
         Serializer valueSerializer,
         long clock) {
      
      HColumn<String, Object> column = HFactory.createColumn(
            colName, 
            value,
            nameSerializer, 
            valueSerializer);
      
      column.setTtl(TTL);
      column.setClock(clock);
      updater.setColumn(column);
      
   }
   
   
   public void ecritColonneType(
         ColumnFamilyUpdater<UUID, String> updater,
         String valeur, 
         long clock) {
      
      addColumn(updater, JR_TYPE_COLUMN, valeur, StringSerializer.get(), StringSerializer.get(), clock);
      
   }
   
   
   public void ecritColonneParameters(
         ColumnFamilyUpdater<UUID, String> updater,
         String valeur, 
         long clock) {
      
      addColumn(updater, JR_PARAMETERS_COLUMN, valeur, StringSerializer.get(), StringSerializer.get(), clock);
      
   }
   
   
   public void ecritColonneState(
         ColumnFamilyUpdater<UUID, String> updater,
         String state, 
         long clock) {
      
      addColumn(updater, JR_STATE_COLUMN, state, StringSerializer.get(), StringSerializer.get(), clock);
      
   }
   
   
   public void ecritColonneReservedBy(
         ColumnFamilyUpdater<UUID, String> updater,
         String reservedBy, 
         long clock) {
      
      addColumn(updater, JR_RESERVED_BY_COLUMN, reservedBy, StringSerializer.get(), StringSerializer.get(), clock);
      
   }
   
   
   public void ecritColonneCreationDate(
         ColumnFamilyUpdater<UUID, String> updater,
         Date creationDate, 
         long clock) {
      
      Serializer<Date> dSlz = NullableDateSerializer.get();
      BytesArraySerializer bSlz = BytesArraySerializer.get();
      
      addColumn(updater, JR_CREATION_DATE_COLUMN, dSlz.toBytes(creationDate), StringSerializer.get(), bSlz, clock);
      
   }
   
   
   public void ecritColonneReservationDate(
         ColumnFamilyUpdater<UUID, String> updater,
         Date reservationDate, 
         long clock) {
      
      Serializer<Date> dSlz = NullableDateSerializer.get();
      BytesArraySerializer bSlz = BytesArraySerializer.get();
      
      addColumn(updater, JR_RESERVATION_DATE_COLUMN, dSlz.toBytes(reservationDate), StringSerializer.get(), bSlz, clock);
      
   }
   
   
   public void ecritColonneStartingDate(
         ColumnFamilyUpdater<UUID, String> updater,
         Date startingDate, 
         long clock) {
      
      Serializer<Date> dSlz = NullableDateSerializer.get();
      BytesArraySerializer bSlz = BytesArraySerializer.get();
      
      addColumn(updater, JR_STARTING_DATE_COLUMN, dSlz.toBytes(startingDate), StringSerializer.get(), bSlz, clock);
      
   }
   
   
   public void ecritColonneEndingDate(
         ColumnFamilyUpdater<UUID, String> updater,
         Date endingDate, 
         long clock) {
      
      Serializer<Date> dSlz = NullableDateSerializer.get();
      BytesArraySerializer bSlz = BytesArraySerializer.get();
      
      addColumn(updater, JR_ENDING_DATE_COLUMN, dSlz.toBytes(endingDate), StringSerializer.get(), bSlz, clock);
      
   }
   
   
   public void ecritColonneMessage(
         ColumnFamilyUpdater<UUID, String> updater,
         String message, 
         long clock) {
      
      addColumn(updater, JR_MESSAGE, message, StringSerializer.get(), StringSerializer.get(), clock);
      
   }
   
   
   public void ecritColonneSaeHost(
         ColumnFamilyUpdater<UUID, String> updater,
         String saeHost, 
         long clock) {
      
      addColumn(updater, JR_SAE_HOST, saeHost, StringSerializer.get(), StringSerializer.get(), clock);
      
   }
   
   
   public void ecritColonneClientHost(
         ColumnFamilyUpdater<UUID, String> updater,
         String clientHost, 
         long clock) {
      
      addColumn(updater, JR_CLIENT_HOST, clientHost, StringSerializer.get(), StringSerializer.get(), clock);
      
   }
   
   
   public void ecritColonneDocCount(
         ColumnFamilyUpdater<UUID, String> updater,
         Integer docCount, 
         long clock) {
      
      addColumn(updater, JR_DOC_COUNT, docCount, StringSerializer.get(), IntegerSerializer.get(), clock);
      
   }
   
   
   public void ecritColonnePid(
         ColumnFamilyUpdater<UUID, String> updater,
         Integer pid, 
         long clock) {
      
      addColumn(updater, JR_PID, pid, StringSerializer.get(), IntegerSerializer.get(), clock);
      
   }
   
   
   public JobRequest createJobRequestFromResult(
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
      
      if (result.getByteArray(JR_RESERVATION_DATE_COLUMN)!=null) {
         Date reservationDate = dSlz.fromBytes(result
               .getByteArray(JR_RESERVATION_DATE_COLUMN));
         jobRequest.setReservationDate(reservationDate);
      }
      
      if (result.getByteArray(JR_STARTING_DATE_COLUMN)!=null) {
         Date startingDate = dSlz.fromBytes(result
               .getByteArray(JR_STARTING_DATE_COLUMN));
         jobRequest.setStartingDate(startingDate);
      }
      
      if (result.getByteArray(JR_ENDING_DATE_COLUMN)!=null) {
         Date endingDate = dSlz.fromBytes(result
               .getByteArray(JR_ENDING_DATE_COLUMN));
         jobRequest.setEndingDate(endingDate);
      }
      
      jobRequest.setMessage(result.getString(JR_MESSAGE));
      
      jobRequest.setSaeHost(result.getString(JR_SAE_HOST));
      
      jobRequest.setClientHost(result.getString(JR_CLIENT_HOST));
      
      jobRequest.setPid(result.getInteger(JR_PID));
      
      jobRequest.setDocCount(result.getInteger(JR_DOC_COUNT));

      return jobRequest;
   }
   
   
   public long getClockColonneState(ColumnFamilyResult<UUID, String> result) {
      
      return result.getColumn(JR_STATE_COLUMN).getClock();
      
   }
   
   
   public long getClockColonnePid(ColumnFamilyResult<UUID, String> result) {
      
      // Attention : result.getColumn(JR_PID) peut être null
      // Appeler avant existeColonnePid
      return result.getColumn(JR_PID).getClock();
      
      
   }
   
   
   public boolean existeColonnePid(ColumnFamilyResult<UUID, String> result) {
      return result.getColumn(JR_PID)!=null;
   }
   
   
   
}

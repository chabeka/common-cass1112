package fr.urssaf.image.sae.cassandra.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.cassandra.service.spring.HectorTemplateImpl;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.SliceQuery;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.eaio.uuid.UUIDGen;

import fr.urssaf.image.sae.cassandra.dao.JobDAO;
import fr.urssaf.image.sae.cassandra.dao.exception.CassandraEx;
import fr.urssaf.image.sae.cassandra.dao.model.JobModel;
import fr.urssaf.image.sae.cassandra.dao.utils.Utils;

/**
 * Exemple de DAO pour manipuler les templates d'Hector.
 */
public class JobDAOImpl extends HectorTemplateImpl implements JobDAO {

   private static final String PARAMETRES_DU_TRAITEMENT = "parametresDuTraitement";
   private static final String DATE_DEMANDE = "dateDemande";
   private static final String SERVEUR = "serveur";
   private static final String ETAT = "etat";
   private static final String TYPE = "type";
   private static final String ID_JOB = "idJob";
   private String columnFamilyName;
   private String clefColumnFamilyName;
   private Keyspace keyspace;

   public Keyspace getEmbadedKeyspace() {
      return keyspace == null ? getKeyspace() : keyspace;
   }

   public void setEmbadedKeyspace(Keyspace keyspace) {
      this.keyspace = keyspace;
   }

   private ColumnFamilyTemplate<String, UUID> template;

   public JobDAOImpl() throws CassandraEx {
      template = new ThriftColumnFamilyTemplate<String, UUID>(getKeyspace(),
            columnFamilyName, StringSerializer.get(), UUIDSerializer.get());
   }

   public void setColumnFamilyName(String columnFamilyName) {
      this.columnFamilyName = columnFamilyName;
   }

   @Override
   public void delete(UUID primaryKey) throws CassandraEx {
      template.deleteColumn(getClefColumnFamilyName(), primaryKey);

   }

   @SuppressWarnings("unchecked")
   @Override
   public void saveOrUpdate(JobModel paramJobModel) throws CassandraEx {
      template = new ThriftColumnFamilyTemplate<String, UUID>(getKeyspace(),
            columnFamilyName, StringSerializer.get(), UUIDSerializer.get());
      ColumnFamilyUpdater<String, UUID> updater = template
            .createUpdater(getClefColumnFamilyName());
      JSONObject values = new JSONObject();
      values.put(ID_JOB, paramJobModel.getIdJob().toString());
      values.put(TYPE, paramJobModel.getJobType());
      values.put(ETAT, paramJobModel.getJobState());
      values.put(SERVEUR, paramJobModel.getServerName());
      values.put(DATE_DEMANDE, Utils.formatDateToString(paramJobModel
            .getDateReceiptRequests()));
      values.put(PARAMETRES_DU_TRAITEMENT, paramJobModel.getJobParam());

      updater.setString(paramJobModel.getIdJob(), values.toJSONString());
      template.update(updater);
   }

   @Override
   public JobModel load(UUID primaryKey) throws CassandraEx {
      if (primaryKey == null) {
         throw new CassandraEx(
               "Problème de configuration du modéle de données.");
      }
      template = new ThriftColumnFamilyTemplate<String, UUID>(getKeyspace(),
            columnFamilyName, StringSerializer.get(), UUIDSerializer.get());
      HColumn<UUID, String> value = template.querySingleColumn(
            getClefColumnFamilyName(), primaryKey, StringSerializer.get());
      // String json = value.getValue();
      JSONObject jobValues;
      JobModel jobModel = new JobModel();
      try {
         jobValues = (JSONObject) new JSONParser().parse(value.getValue()
               .toString());

         jobModel.setIdJob(UUID.fromString(jobValues.get(ID_JOB).toString()));
         jobModel.setJobState(jobValues.get(ETAT).toString());
         jobModel.setDateReceiptRequests(Utils.formatStringToDate(jobValues
               .get(DATE_DEMANDE).toString()));
         jobModel.setJobType((String) jobValues.get(TYPE));
         jobModel.setJobParam((String) jobValues.get(PARAMETRES_DU_TRAITEMENT));
         jobModel.setServerName((String) jobValues.get(SERVEUR));
      } catch (ParseException e) {
         throw new CassandraEx("Problème de parsing." + " Erreur :"
               + e.getMessage());
      }

      return jobModel;
   }

   /**
    * @return the clefClumnFamilyName
    */
   public String getClefColumnFamilyName() {
      return clefColumnFamilyName;
   }

   /**
    * @param clefColumnFamilyName
    *           the clefClumnFamilyName to set
    */
   public void setClefColumnFamilyName(String clefColumnFamilyName) {
      this.clefColumnFamilyName = clefColumnFamilyName;
   }

   /**
    * @return the columnFamilyName
    */
   public String getColumnFamilyName() {
      return columnFamilyName;
   }

   @Override
   public List<JobModel> loadJobs(Date startDate) throws CassandraEx {
      if (startDate == null) {
         throw new CassandraEx("Date début est null.");
      }
      UUID startUUID = TimeUUIDUtils.getTimeUUID(startDate.getTime());
      UUID finishId = new UUID(UUIDGen.createTime(System.currentTimeMillis()),
            UUIDGen.getClockSeqAndNode());
      SliceQuery<String, UUID, String> sliceQuery = HFactory.createSliceQuery(
            getKeyspace(), StringSerializer.get(), UUIDSerializer.get(),
            StringSerializer.get()).setKey(getClefColumnFamilyName())
            .setColumnFamily(columnFamilyName);
      ColumnSliceIterator<String, UUID, String> iterator = new ColumnSliceIterator<String, UUID, String>(
            sliceQuery, startUUID, finishId, false);
      JobModel jobModel = null;
      String valuesSubsit = null;
      List<JobModel> listeJobs = null;
      while (iterator.hasNext()) {
         jobModel = new JobModel();
         listeJobs = new ArrayList<JobModel>();
         HColumn values = iterator.next();
         try {
            JSONObject jobValues = (JSONObject) new JSONParser().parse(values
                  .getValue().toString());
            jobModel
                  .setIdJob(UUID.fromString(jobValues.get(ID_JOB).toString()));
            jobModel.setJobState(jobValues.get(ETAT).toString());
            jobModel.setDateReceiptRequests(Utils.formatStringToDate(jobValues
                  .get(DATE_DEMANDE).toString()));
            jobModel.setJobType((String) jobValues.get(TYPE));
            jobModel.setJobParam((String) jobValues
                  .get(PARAMETRES_DU_TRAITEMENT));
            jobModel.setServerName((String) jobValues.get(SERVEUR));
            listeJobs.add(jobModel);
         } catch (ParseException e) {
            throw new CassandraEx("Problème de parsing de la chaîne ."
                  + valuesSubsit + " Erreur :" + e.getMessage());
         }
      }

      return listeJobs;
   }

   public void setTemplate(ColumnFamilyTemplate<String, UUID> template) {
      this.template = template;
   }

   public ColumnFamilyTemplate<String, UUID> getTemplate() {
      return template;
   }

}

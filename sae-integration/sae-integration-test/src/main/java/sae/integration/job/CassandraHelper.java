package sae.integration.job;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.AuthenticationCredentials;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.connectionpool.impl.SimpleAuthenticationCredentials;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ConsistencyLevel;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;

import sae.integration.environment.Environment;
import sae.integration.job.dao.JobRequestCF;
import sae.integration.job.dao.JobsQueueCF;

/**
 * Permet d'écrire dans le keyspace SAE de la base cassandra
 * Utilisé pour insérer des jobs dans la file d'attente des travaux.
 */
public class CassandraHelper {

   private final Keyspace saeKeyspace;

   private final Environment environment;

   private static final Logger LOGGER = LoggerFactory.getLogger(CassandraHelper.class);

   private static final String VI_FOR_JOB_OLD = "{\"codeAppli\":\"CS_SATURNE\",\"idUtilisateur\":\"NON_RENSEIGNE\",\"saeDroits\":{\"archivage_masse\":[{\"prmd\":{\"code\":\"PRMD_SATURNE_COTISANT\",\"description\":\"SATURNE - Tous documents cotisants \",\"lucene\":\"DomaineCotisant:true\",\"metadata\":{\"DomaineCotisant\":[\"true\"]},\"bean\":\"\"},\"values\":{}}],\"modification_masse\":[{\"prmd\":{\"code\":\"PRMD_SATURNE_COTISANT\",\"description\":\"SATURNE - Tous documents cotisants \",\"lucene\":\"DomaineCotisant:true\",\"metadata\":{\"DomaineCotisant\":[\"true\"]},\"bean\":\"\"},\"values\":{}}],\"reprise_masse\":[{\"prmd\":{\"code\":\"PRMD_SATURNE_COTISANT\",\"description\":\"SATURNE - Tous documents cotisants \",\"lucene\":\"DomaineCotisant:true\",\"metadata\":{\"DomaineCotisant\":[\"true\"]},\"bean\":\"\"},\"values\":{}}],\"transfert_masse\":[{\"prmd\":{\"code\":\"PRMD_SATURNE_COTISANT\",\"description\":\"SATURNE - Tous documents cotisants \",\"lucene\":\"DomaineCotisant:true\",\"metadata\":{\"DomaineCotisant\":[\"true\"]},\"bean\":\"\"},\"values\":{}}]},\"pagms\":[\"PAGM_SATURNE\"],\"listControlProfil\":[]}";

   private static final String VI_FOR_JOB = "{\"codeAppli\":\"CS_DEV_TOUTES_ACTIONS\",\"idUtilisateur\":\"NON_RENSEIGNE\",\"saeDroits\":{\"restore_masse\":[{\"prmd\":{\"code\":\"PRMD_PERMIT_ALL\",\"description\":\"PRMD avec toutes permissions\",\"lucene\":\"\",\"metadata\":{},\"bean\":\"permitAll\"},\"values\":{}}],\"archivage_masse\":[{\"prmd\":{\"code\":\"PRMD_PERMIT_ALL\",\"description\":\"PRMD avec toutes permissions\",\"lucene\":\"\",\"metadata\":{},\"bean\":\"permitAll\"},\"values\":{}}],\"copie\":[{\"prmd\":{\"code\":\"PRMD_PERMIT_ALL\",\"description\":\"PRMD avec toutes permissions\",\"lucene\":\"\",\"metadata\":{},\"bean\":\"permitAll\"},\"values\":{}}],\"ajout_note\":[{\"prmd\":{\"code\":\"PRMD_PERMIT_ALL\",\"description\":\"PRMD avec toutes permissions\",\"lucene\":\"\",\"metadata\":{},\"bean\":\"permitAll\"},\"values\":{}}],\"modification_masse\":[{\"prmd\":{\"code\":\"PRMD_PERMIT_ALL\",\"description\":\"PRMD avec toutes permissions\",\"lucene\":\"\",\"metadata\":{},\"bean\":\"permitAll\"},\"values\":{}}],\"deblocage\":[{\"prmd\":{\"code\":\"PRMD_PERMIT_ALL\",\"description\":\"PRMD avec toutes permissions\",\"lucene\":\"\",\"metadata\":{},\"bean\":\"permitAll\"},\"values\":{}}],\"modification\":[{\"prmd\":{\"code\":\"PRMD_PERMIT_ALL\",\"description\":\"PRMD avec toutes permissions\",\"lucene\":\"\",\"metadata\":{},\"bean\":\"permitAll\"},\"values\":{}}],\"recherche\":[{\"prmd\":{\"code\":\"PRMD_PERMIT_ALL\",\"description\":\"PRMD avec toutes permissions\",\"lucene\":\"\",\"metadata\":{},\"bean\":\"permitAll\"},\"values\":{}}],\"suppression_masse\":[{\"prmd\":{\"code\":\"PRMD_PERMIT_ALL\",\"description\":\"PRMD avec toutes permissions\",\"lucene\":\"\",\"metadata\":{},\"bean\":\"permitAll\"},\"values\":{}}],\"reprise_masse\":[{\"prmd\":{\"code\":\"PRMD_PERMIT_ALL\",\"description\":\"PRMD avec toutes permissions\",\"lucene\":\"\",\"metadata\":{},\"bean\":\"permitAll\"},\"values\":{}}],\"transfert\":[{\"prmd\":{\"code\":\"PRMD_PERMIT_ALL\",\"description\":\"PRMD avec toutes permissions\",\"lucene\":\"\",\"metadata\":{},\"bean\":\"permitAll\"},\"values\":{}}],\"transfert_masse\":[{\"prmd\":{\"code\":\"PRMD_PERMIT_ALL\",\"description\":\"PRMD avec toutes permissions\",\"lucene\":\"\",\"metadata\":{},\"bean\":\"permitAll\"},\"values\":{}}],\"archivage_unitaire\":[{\"prmd\":{\"code\":\"PRMD_PERMIT_ALL\",\"description\":\"PRMD avec toutes permissions\",\"lucene\":\"\",\"metadata\":{},\"bean\":\"permitAll\"},\"values\":{}}],\"suppression\":[{\"prmd\":{\"code\":\"PRMD_PERMIT_ALL\",\"description\":\"PRMD avec toutes permissions\",\"lucene\":\"\",\"metadata\":{},\"bean\":\"permitAll\"},\"values\":{}}],\"consultation\":[{\"prmd\":{\"code\":\"PRMD_PERMIT_ALL\",\"description\":\"PRMD avec toutes permissions\",\"lucene\":\"\",\"metadata\":{},\"bean\":\"permitAll\"},\"values\":{}}],\"ajout_doc_attache\":[{\"prmd\":{\"code\":\"PRMD_PERMIT_ALL\",\"description\":\"PRMD avec toutes permissions\",\"lucene\":\"\",\"metadata\":{},\"bean\":\"permitAll\"},\"values\":{}}],\"recherche_iterateur\":[{\"prmd\":{\"code\":\"PRMD_PERMIT_ALL\",\"description\":\"PRMD avec toutes permissions\",\"lucene\":\"\",\"metadata\":{},\"bean\":\"permitAll\"},\"values\":{}}]},\"pagms\":[\"PAGM_TOUTES_ACTIONS\"],\"listControlProfil\":[]}";

   private static final String CODE_TRAITEMENT = "UR666";

   public CassandraHelper(final Environment environment) {
      this.environment = environment;
      final String servers = environment.getCassandraServers();
      final AuthenticationCredentials credentials = new SimpleAuthenticationCredentials(
            "root",
            "regina4932");

      final AstyanaxContext<Keyspace> context = new AstyanaxContext.Builder()
            .forCluster("SAE")
            .forKeyspace("SAE")
            .withAstyanaxConfiguration(
                  new AstyanaxConfigurationImpl()
                  .setDiscoveryType(NodeDiscoveryType.NONE)
                  .setDefaultReadConsistencyLevel(
                        ConsistencyLevel.CL_QUORUM)
                  .setDefaultWriteConsistencyLevel(
                        ConsistencyLevel.CL_QUORUM))
            .withConnectionPoolConfiguration(
                  new ConnectionPoolConfigurationImpl("MyConnectionPool")
                  .setPort(9160)
                  .setMaxConnsPerHost(1)
                  .setSeeds(servers)
                  .setAuthenticationCredentials(credentials))
            .withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
            .buildKeyspace(ThriftFamilyFactory.getInstance());

      context.start();
      saeKeyspace = context.getClient();
   }

   /**
    * On écrit un job d'archivage de masse, d'id "jobId" dans cassandra, en indiquant qu'il est réservé par le serveur "appliServer"
    * 
    * @param jobId
    *           id du job
    * @param sommaireURL
    *           url du sommaire
    * @param sommaireHash
    *           hash (sha1) du sommaire
    * @throws Exception
    */
   public void writeArchivageJob(final UUID jobId, final String sommaireURL, final String sommaireHash) throws Exception {
      final String appliServer = environment.getAppliServer();
      String json = "{\"idJob\":\"ID_JOB\",\"type\":\"capture_masse\",\"parameters\":null,\"jobParameters\":{\"ecdeUrl\":\"SOMMAIRE_URL\"}}";
      json = json.replace("ID_JOB", jobId.toString());
      json = json.replace("SOMMAIRE_URL", sommaireURL);
      final String jobType = "capture_masse";
      writeJob(jobId, sommaireURL, sommaireHash, appliServer, json, jobType);
   }

   /**
    * On écrit un job de transfert de masse, d'id "jobId" dans cassandra, en indiquant qu'il est réservé par le serveur "appliServer"
    * 
    * @param jobId
    *           id du job
    * @param sommaireURL
    *           url du sommaire
    * @param sommaireHash
    *           hash (sha1) du sommaire
    * @throws Exception
    */
   public void writeTransfertJob(final UUID jobId, final String sommaireURL, final String sommaireHash) throws Exception {
      final String appliServer = environment.getAppliServer();
      String json = "{\"idJob\":\"ID_JOB\",\"type\":\"transfert_masse\",\"parameters\":null,\"jobParameters\":{\"typeHash\": \"SHA-1\",\"ecdeUrl\":\"SOMMAIRE_URL\",\"hash\": \"SOMMAIRE_HASH\"}}";
      json = json.replace("ID_JOB", jobId.toString());
      json = json.replace("SOMMAIRE_URL", sommaireURL);
      json = json.replace("SOMMAIRE_HASH", sommaireHash);
      final String jobType = "transfert_masse";
      writeJob(jobId, sommaireURL, sommaireHash, appliServer, json, jobType);
   }

   /**
    * On écrit un job de modification de masse, d'id "jobId" dans cassandra, en indiquant qu'il est réservé par le serveur "appliServer"
    * 
    * @param jobId
    *           id du job
    * @param sommaireURL
    *           url du sommaire
    * @param sommaireHash
    *           hash (sha1) du sommaire
    * @throws Exception
    */
   public void writeModificationJob(final UUID jobId, final String sommaireURL, final String sommaireHash) throws Exception {
      final String appliServer = environment.getAppliServer();
      String json = "{\"idJob\":\"ID_JOB\",\"type\":\"modification_masse\",\"parameters\":null,\"jobParameters\":{\"typeHash\": \"SHA-1\",\"ecdeUrl\":\"SOMMAIRE_URL\",\"codeTraitement\":\"CODE_TRAITEMENT\",\"hash\": \"SOMMAIRE_HASH\"}}";
      json = json.replace("ID_JOB", jobId.toString());
      json = json.replace("SOMMAIRE_URL", sommaireURL);
      json = json.replace("CODE_TRAITEMENT", CODE_TRAITEMENT);
      json = json.replace("SOMMAIRE_HASH", sommaireHash);
      final String jobType = "modification_masse";
      writeJob(jobId, sommaireURL, sommaireHash, appliServer, json, jobType);
   }

   /**
    * On écrit un job de suppression de masse, d'id "jobId" dans cassandra, en indiquant qu'il est réservé par le serveur "appliServer"
    * 
    * @param jobId
    *           id du job
    * @param sommaireURL
    *           url du sommaire
    * @param sommaireHash
    *           hash (sha1) du sommaire
    * @throws Exception
    */
   public void writeSuppressionJob(final UUID jobId, final String deletionRequest) throws Exception {
      final String appliServer = environment.getAppliServer();
      String json = "{\"idJob\":\"ID_JOB\",\"type\":\"suppression_masse\",\"parameters\":null,\"jobParameters\":{\"requeteSuppression\":\"DELETION_REQUEST\"}}";
      json = json.replace("ID_JOB", jobId.toString());
      json = json.replace("DELETION_REQUEST", JSONValue.escape(deletionRequest));
      final String jobType = "suppression_masse";
      String jobParametersAsXML = "<?xml version='1.0' encoding='UTF-8'?><map><entry><string>requeteSuppression</string><string>DELETION_REQUEST</string></entry></map>";
      jobParametersAsXML = jobParametersAsXML.replace("DELETION_REQUEST", StringEscapeUtils.escapeXml(deletionRequest));
      writeJob(jobId, jobParametersAsXML, appliServer, json, jobType);
   }

   private void writeJob(final UUID jobId, final String sommaireURL, final String sommaireHash, final String appliServer, final String json,
         final String jobType)
               throws ConnectionException {
      String jobParametersAsXML = "<?xml version='1.0' encoding='UTF-8'?><map><entry><string>typeHash</string><string>SHA-1</string></entry><entry><string>ecdeUrl</string><string>SOMMAIRE_URL</string></entry>ADDITIONAL_ENTRY<entry><string>hash</string><string>SOMMAIRE_HASH</string></entry></map>";
      jobParametersAsXML = jobParametersAsXML.replace("SOMMAIRE_URL", sommaireURL);
      jobParametersAsXML = jobParametersAsXML.replace("SOMMAIRE_HASH", sommaireHash);
      String additionalEntry = "";
      if ("modification_masse".equals(jobType)) {
         additionalEntry = "<entry><string>codeTraitement</string><string>" + CODE_TRAITEMENT + "</string></entry>";
      }
      jobParametersAsXML = jobParametersAsXML.replace("ADDITIONAL_ENTRY", additionalEntry);

      writeJob(jobId, jobParametersAsXML, appliServer, json, jobType);
   }

   private void writeJob(final UUID jobId, final String jobParametersAsXML, final String appliServer, final String json,
         final String jobType)
               throws ConnectionException {

      final MutationBatch batch = saeKeyspace.prepareMutationBatch();
      final long currentDate = System.currentTimeMillis() / 1000L;
      batch.withRow(JobRequestCF.get(), jobId)
      .putColumn("clientHost", "1.1.1.1")
      .putColumn("creationDate", currentDate)
      .putColumn("jobParameters", jobParametersAsXML)
      .putColumn("reservedBy", appliServer)
      .putColumn("reservationDate", currentDate)
      .putColumn("saeHost", appliServer)
      .putColumn("state", "RESERVED")
      .putColumn("type", jobType)
      .putColumn("vi", VI_FOR_JOB);
      batch.withRow(JobsQueueCF.get(), appliServer)
      .putColumn(jobId, json);

      final OperationResult<Void> result = batch.execute();
      LOGGER.debug("Insertion du job {} dans cassandra en {} ms", jobId, result.getLatency(TimeUnit.MILLISECONDS));
   }

   /**
    * Utilisé pour re-réserver un job, par exemple dans le cas où sont statut est passé en FAILED
    * 
    * @param jobId
    *           id du job
    */
   public void reserveJobAgain(final UUID jobId) throws Exception {

      final Column<String> row = saeKeyspace.prepareQuery(JobRequestCF.get())
            .getKey(jobId)
            .getColumn("state")
            .execute()
            .getResult();
      final String state = row.getStringValue();
      LOGGER.info("Etat actuel du job {} : {}", jobId, state);

      if (!StringUtils.isEmpty(state)) {
         final MutationBatch batch = saeKeyspace.prepareMutationBatch();
         batch.withRow(JobRequestCF.get(), jobId)
         .putColumn("state", "RESERVED");
         final OperationResult<Void> result = batch.execute();
         LOGGER.info("Réservation du job {} dans cassandra en {} ms", jobId, result.getLatency(TimeUnit.MILLISECONDS));
      }
   }

}

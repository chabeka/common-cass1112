package sae.integration.job;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.oss.driver.api.core.uuid.Uuids;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.SSHException;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import sae.integration.environment.Environment;
import sae.integration.util.ResourceUtils;
import sae.integration.util.SSHHelper;
import sae.integration.util.StringHelper;

/**
 * Classe permettant la gestion d'un traitement de masse (job)
 */
public class JobManager implements Closeable {

   private static final String CREATE_JOB_DIR_SCRIPT = "create_job_dir.sh";

   private final Environment environment;

   private final SSHClient sshClient;

   private final CassandraHelper cassandraHelper;

   private final UUID jobId;

   private static final Logger LOGGER = LoggerFactory.getLogger(JobManager.class);

   private String ecdePath;

   public JobManager(final Environment environment) throws Exception {
      this.environment = environment;
      jobId = Uuids.timeBased();
      sshClient = new SSHClient();
      sshClient.addHostKeyVerifier(new PromiscuousVerifier());
      sshClient.connect(environment.getAppliServer());
      sshClient.authPassword("root", "hwicnir");
      cassandraHelper = new CassandraHelper(environment);
   }

   public UUID getJobId() {
      return jobId;
   }

   public void launchArchivageMasse(final String sommaire, final List<String> docPaths, final List<String> docTargetNames) throws Exception {
      // Poser le sommaire et les documents sur l'ecde
      sendSommaireAndFiles(sommaire, docPaths, docTargetNames);
      // Créer le job dans cassandra
      final String sommaireHash = DigestUtils.sha1Hex(sommaire);
      cassandraHelper.writeArchivageJob(jobId, getEcdeUrl() + "/sommaire.xml", sommaireHash);
      // Appel de l'exécutable du job
      launchRemoteJob();
   }

   public void launchTransfertMasse(final String sommaire) throws Exception {
      // Poser le sommaire et les documents sur l'ecde
      sendSommaire(sommaire);
      // Créer le job dans cassandra
      final String sommaireHash = DigestUtils.sha1Hex(sommaire);
      cassandraHelper.writeTransfertJob(jobId, getEcdeUrl() + "/sommaire.xml", sommaireHash);
      // Appel de l'exécutable du job
      launchRemoteJob();
   }

   private void launchRemoteJob() throws Exception {
      final String jobCommandPattern = "java -jar -Xms500m -Xmx2500m -DLOGS_UUID={} -Dlogback.configurationFile=/hawai/data/ged/sae-services-executable/logback-sae-services-executable.xml -Dfile.encoding=UTF-8 /hawai/data/ged/sae-services-executable/sae-services-executable.jar traitementMasse {} /hawai/data/ged/sae-config.properties";
      final String command = StringHelper.format(jobCommandPattern, jobId, jobId);
      SSHHelper.execute(sshClient, command);
   }

   public String getJobLog() throws Exception {
      final String command = StringHelper.format("cat /hawai/logs/ged/sae_services_executable.{}-debug.log", jobId);
      return SSHHelper.execute(sshClient, command);
   }

   public String getResultatsXML() throws Exception {
      final String resultatPath = getEcdePath() + "/resultats.xml";
      final String command = StringHelper.format("cat {}", resultatPath);
      return SSHHelper.execute(sshClient, command);
   }

   /**
    * @param sommaire
    * @param docPaths
    * @throws SSHException
    */
   private void sendSommaireAndFiles(final String sommaire, final List<String> docPaths, final List<String> docTargetNames) throws Exception {
      sendSommaire(sommaire);
      final String jobPath = getEcdePath();
      final String docDir = jobPath + "/documents";
      SSHHelper.execute(sshClient, "mkdir " + docDir);

      for (int i = 0; i < docPaths.size(); i++) {
         final String docPath = docPaths.get(i);
         final String targetPath = docDir + "/" + docTargetNames.get(i);
         SSHHelper.writeFileFromResource(sshClient, this, targetPath, docPath);
      }
   }

   private void sendSommaire(final String sommaire) throws Exception {
      final String jobPath = getEcdePath();
      LOGGER.info("Chemin du job : {}", jobPath);
      SSHHelper.writeTextFile(sshClient, jobPath + "/sommaire.xml", sommaire);
   }

   public String getEcdePath() throws Exception {
      if (ecdePath != null) {
         return ecdePath;
      }
      sendHelperScripts();
      final String path = SSHHelper.execute(sshClient, "/tmp/" + CREATE_JOB_DIR_SCRIPT + " " + environment.getEcdeMountPoint());
      ecdePath = path.trim();
      return ecdePath;
   }

   private String getEcdeUrl() throws Exception {
      return getEcdePath().replace(environment.getEcdeMountPoint(), "ecde://" + environment.getEcdeName());
   }

   /**
    * @throws Exception
    * @throws SSHException
    * @throws
    */
   private void sendHelperScripts() throws Exception {
      final String remoteScript = "/tmp/" + CREATE_JOB_DIR_SCRIPT;
      final String scriptAsString = ResourceUtils.loadResourceAsString(this, "job/" + CREATE_JOB_DIR_SCRIPT);
      final String md5 = DigestUtils.md5Hex(scriptAsString);
      final String command = StringHelper.format("test -f {} && md5sum {} || echo fileNotFound", remoteScript, remoteScript);
      final String out = SSHHelper.execute(sshClient, command);
      if (!out.contains(md5)) {
         LOGGER.info("Envoi du script {} sur le serveur {}", remoteScript, environment.getAppliServer());
         SSHHelper.writeTextFile(sshClient, remoteScript, scriptAsString);
         SSHHelper.execute(sshClient, "chmod +x " + remoteScript);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void close() throws IOException {
      if (sshClient != null) {
         sshClient.close();
      }
   }
}

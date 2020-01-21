package sae.integration.job;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.oss.driver.api.core.uuid.Uuids;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.SSHException;
import sae.integration.environment.Environment;
import sae.integration.util.ResourceUtils;
import sae.integration.util.SSHHelper;
import sae.integration.util.StringHelper;

/**
 * Classe permettant la gestion d'un traitement de masse (job), soit :
 * - dépôt des fichiers sommaires.xml et documents sur l'ecde
 * - déclaration d'un traitement de masse dans cassandra, et réservation du traitement
 * - suivant le mode : lancement du traitement de masse
 * Le traitement de masse peut être soit :
 * - exécuté à distance, par lancement du jar en ssh
 * - exécuté à distance en mode debug, par lancement du jar en ssh (pour faire du remote debug)
 * - exécuté manuellement localement en mode de debug (pour faire du debug local)
 */
public class JobManager implements Closeable {

   private static final String CREATE_JOB_DIR_SCRIPT = "create_job_dir.sh";

   private final Environment environment;

   private final SSHClient sshClient;

   private boolean withRemoteDebug = false;

   private boolean withLocalDebug = false;

   private final CassandraHelper cassandraHelper;

   private final UUID jobId;

   private static final Logger LOGGER = LoggerFactory.getLogger(JobManager.class);

   private String ecdePath;

   public JobManager(final Environment environment) throws Exception {
      this.environment = environment;
      jobId = Uuids.timeBased();
      withLocalDebug = environment.getEnvCode().equals("LOCAL_BATCH");
      if (withLocalDebug) {
         sshClient = null;
      }
      else {
         sshClient = SSHHelper.getSSHClient(environment);
      }
      cassandraHelper = new CassandraHelper(environment);
   }

   /**
    * Permet d'activer le lancement du jar en mode remote debug, pour faire du debug distant avec Eclipse
    * 
    * @param withRemoteDebug
    *           Vrai pour activer le remote debug
    */
   public void setRemoteDebug(final boolean withRemoteDebug) {
      this.withRemoteDebug = withRemoteDebug;
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

   public void launchModificationMasse(final String sommaire) throws Exception {
      // Poser le sommaire et les documents sur l'ecde
      sendSommaire(sommaire);
      // Créer le job dans cassandra
      final String sommaireHash = DigestUtils.sha1Hex(sommaire);
      cassandraHelper.writeModificationJob(jobId, getEcdeUrl() + "/sommaire.xml", sommaireHash);
      // Appel de l'exécutable du job
      launchRemoteJob();
   }

   private void launchRemoteJob() throws Exception {
      if (withLocalDebug) {
         LOGGER.info("Il faut lancer manuellement le process sae-services-executable en mode debug local");
         waitEndOfJob();
         return;
      }

      if (withRemoteDebug) {
         LOGGER.info("Process sae-services-executable lancé en mode remote debug. Il faut lancer un session de debug sur {}:8000",
               environment.getAppliServer());
      }
      final String debugOptions = withRemoteDebug ? "-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000" : "";
      final String jobCommandPattern = "java " + debugOptions
            + " -jar -Xms500m -Xmx2500m -DLOGS_UUID={} -Dlogback.configurationFile=/hawai/data/ged/sae-services-executable/logback-sae-services-executable.xml -Dfile.encoding=UTF-8 /hawai/data/ged/sae-services-executable/sae-services-executable.jar traitementMasse {} /hawai/data/ged/sae-config.properties";
      final String command = StringHelper.format(jobCommandPattern, jobId, jobId);
      SSHHelper.execute(sshClient, command);
   }

   /**
    * Lorsqu'on fait du debug local : attend la fin du traitement en attendant l'apparition du fichier fin_traitement.flag
    * 
    */
   private void waitEndOfJob() throws Exception {
      final String flagPath = getEcdePath() + "/fin_traitement.flag";
      while(true) {
         if (new File(flagPath).exists()) {
            return;
         }
         Thread.sleep(1000);
      }
   }

   public String getJobLog() throws Exception {
      if (withLocalDebug) {
         final String logPath = StringHelper.format("c:/hawai/logs/ged/sae_services_executable.{}-debug.log", jobId);
         return new String(Files.readAllBytes(Paths.get(logPath)));
      } else {
         final String command = StringHelper.format("cat /hawai/logs/ged/sae_services_executable.{}-debug.log", jobId);
         return SSHHelper.execute(sshClient, command);
      }
   }

   public String getResultatsXML() throws Exception {
      final String resultatPath = getEcdePath() + "/resultats.xml";
      if (withLocalDebug) {
         return new String(Files.readAllBytes(Paths.get(resultatPath)));
      } else {
         final String command = StringHelper.format("cat {}", resultatPath);
         return SSHHelper.execute(sshClient, command);
      }
   }

   /**
    * @param sommaire
    * @param docPaths
    * @throws SSHException
    */
   private void sendSommaireAndFiles(final String sommaire, final List<String> docPaths, final List<String> docTargetNames) throws Exception {
      sendSommaire(sommaire);
      createDocDirInECDE();

      for (int i = 0; i < docPaths.size(); i++) {
         sendDocumentInECDE(docPaths.get(i), docTargetNames.get(i));
      }
   }

   public void createDocDirInECDE() throws Exception {
      final String jobPath = getEcdePath();
      final String docDir = jobPath + "/documents";
      if (withLocalDebug) {
         new File(docDir).mkdirs();
      } else {
         SSHHelper.execute(sshClient, "mkdir " + docDir);
      }
   }

   /**
    * Envoie un document sur l'ecde
    * 
    * @param docResourcePath
    * @param docTargetName
    * @throws Exception
    * @return le chemin du document sur l'ecde
    */
   public String sendDocumentInECDE(final String docResourcePath, final String docTargetName) throws Exception {
      final String jobPath = getEcdePath();
      final String docDir = jobPath + "/documents";
      final String targetPath = docDir + "/" + docTargetName;
      if (withLocalDebug) {
         ResourceUtils.copyResourceToFile(this, docResourcePath, targetPath);
      } else {
         SSHHelper.writeFileFromResource(sshClient, this, targetPath, docResourcePath);
      }
      return getEcdeUrl() + "/documents/" + docTargetName;
   }

   private void sendSommaire(final String sommaire) throws Exception {
      final String jobPath = getEcdePath();
      LOGGER.info("Chemin du job : {}", jobPath);
      final String filePath = jobPath + "/sommaire.xml";
      if (withLocalDebug) {
         Files.write(Paths.get(filePath), sommaire.getBytes());
      } else {
         SSHHelper.writeTextFile(sshClient, filePath, sommaire);
      }
   }

   public String getEcdePath() throws Exception {
      if (ecdePath != null) {
         return ecdePath;
      }
      if (withLocalDebug) {
         ecdePath = getLocalEcdePath();
         return ecdePath;
      } else {
         sendHelperScripts();
         final String path = SSHHelper.execute(sshClient, "/tmp/" + CREATE_JOB_DIR_SCRIPT + " " + environment.getEcdeMountPoint());
         ecdePath = path.trim();
         return ecdePath;
      }
   }

   /**
    * Crée et renvoie un nouveau répertoire sur l'ecde local
    */
   private String getLocalEcdePath() {
      final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
      final String dir = environment.getEcdeMountPoint() + "/sae-integration-test/" + LocalDateTime.now().format(formatter);
      new File(dir).mkdirs();
      int counter = 1;
      while (true) {
         final String subDirAsString = dir + "/" + counter;
         final File subDir = new File(subDirAsString);
         if (!subDir.exists()) {
            subDir.mkdir();
            return subDirAsString;
         }
         counter++;
      }
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

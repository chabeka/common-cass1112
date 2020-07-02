package fr.urssaf.image.parser_opencsv.application.job;

import java.io.File;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.urssaf.image.parser_opencsv.application.JobLauncher;
import fr.urssaf.image.parser_opencsv.application.component.BndMigrationComponent;
import fr.urssaf.image.parser_opencsv.application.constantes.FileConst;
import fr.urssaf.image.parser_opencsv.application.exception.BNDScriptRuntimeException;
import fr.urssaf.image.parser_opencsv.application.model.Statistic;
import fr.urssaf.image.parser_opencsv.application.model.entity.JobEntity;
import fr.urssaf.image.parser_opencsv.application.reader.ResultatsXMLReader;
import fr.urssaf.image.parser_opencsv.application.service.ICaptureMasseService;
import fr.urssaf.image.parser_opencsv.application.service.IJobService;
import fr.urssaf.image.parser_opencsv.application.service.impl.AsynchronousService;
import fr.urssaf.image.parser_opencsv.utils.FileUtils;

/**
 * Composant de lancement du Script de migration
 */
@Component
@Scope("prototype")
public class Worker implements Runnable {

   private static final Logger LOGGER = LoggerFactory.getLogger(Worker.class);

   @Autowired
   private BndMigrationComponent bndComponent;

   @Autowired
   private ICaptureMasseService captureService;

   @Autowired
   private IJobService jobService;

   @Autowired
   private AsynchronousService executorService;

   ResultatsXMLReader resultatsXMLReader;

   private String csvFileName;

   private String jobUUID;

   public Worker() {
      super();
      resultatsXMLReader = new ResultatsXMLReader();
   }

   public Worker(final String csvFileName) {
      this();
      this.csvFileName = csvFileName;
   }

   public Worker(final String jobUUID, final String csvFileName) {
      this(csvFileName);
      this.jobUUID = jobUUID;
   }

   private void launch() {
      LOGGER.info("Generation du sommaire");
      JobEntity jobEntity = null;
      try {
         jobEntity = new JobEntity(jobUUID, csvFileName);
         jobEntity = jobService.saveJob(jobEntity);
         bndComponent.generateSommaireFromCSV(csvFileName, jobEntity);
         jobService.saveJob(jobEntity);
         LOGGER.info("Le job {}  a été persisté en base", jobEntity);

         final String hash = FileUtils.getHash(jobEntity.getTargetPath() + FileConst.SOMMAIRE_FILE_NAME);
         final String uuid = captureService.lancerCaptureMasseAvecHash(jobEntity.getEcdeUrl(), hash);
         jobEntity.setIdTraitementMasse(uuid);
         jobService.saveJob(jobEntity);
         LOGGER.info("Le Traitement uuid : {} et JobId : {}", uuid, jobEntity);
      }
      catch (XMLStreamException | IOException e) {
         final String message = "Une erreur est survenue lors du parsing du fichier CSV";
         LOGGER.error(message);
         throw new RuntimeException(message, e);
      }
      finally {
         LOGGER.info("Fermeture de tous les flux de fichier ouvert!");
         bndComponent.closeStreamWriter();
      }
      // Attendre la fin du Job
      try {
         waitEndingJob(jobEntity);
      }
      catch (final InterruptedException e) {
         Thread.currentThread().interrupt();
      }
   }

   /**
    * Suppression des fichiers temporaire
    * 
    * @param info
    */
   public void cleanDirectories(final JobEntity info) {
      // Suppression du sommaire dans le repertoire source une fois envoyé sur l'ECDE
      final File sommaire = new File(info.getSourcePath() + FileConst.SOMMAIRE_FILE_NAME);
      if (sommaire.delete()) {
         LOGGER.info("Suppression du sommaire du dossier source {}", info.getSourcePath());
      } else {
         LOGGER.error("Erreur de suppression du sommaire du dossier source");
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void run() {
      launch();
   }

   /**
    * @return the csvFileName
    */
   public String getCsvFileName() {
      return csvFileName;
   }

   public void setCsvFileName(final String csvFileName) {
      this.csvFileName = csvFileName;
   }

   /**
    * Attendre la fin du job une fois lancé par l'ordonnanceur
    * 
    * @param jobEntity
    * @throws InterruptedException
    * @throws XMLStreamException
    * @throws IOException
    */
   private void waitEndingJob(final JobEntity jobEntity) throws InterruptedException {

      final String targetPath = jobEntity.getTargetPath();
      final String flagPath = targetPath + "fin_traitement.flag";
      LOGGER.info("Attente de la fin du traitement [{}]...",
                  jobEntity.getJobUUid()
            );
      while (!new File(flagPath).exists()) {
         Thread.sleep(1000);
         LOGGER.info("Attente de la fin du process {} thread N°{}",
                     jobEntity.getIdTraitementMasse(),
                     JobLauncher.nombreTraitementsTerminee.get());
      }

      // Mise à jour du nombre de documents insérés en GED
      Statistic statistic;
      try {
         statistic = resultatsXMLReader.getResultats(targetPath);
         LOGGER.info("intialCount : {} | integrated : {} | nonIntegrated : {}",
                     statistic.getInitialDocumentsCount(),
                     statistic.getAddedDocumentsCount(),
                     statistic.getNonAddedDocumentsCount());
         jobEntity.setNombreDocumentsInGED(statistic.getAddedDocumentsCount());
         jobService.saveJob(jobEntity);
      }
      catch (IOException | XMLStreamException e) {
         final String message = "Une erreur est survenue lors de lecture du fichier Resultats.xml";
         LOGGER.error("");
         throw new BNDScriptRuntimeException(message, e);
      }
      finally {
         try {
            resultatsXMLReader.closeResultatStream();
         }
         catch (final XMLStreamException e) {
            final String message = "Une erreur est survenue lors de la fermeture du flux du fichier Resultats.xml";
            LOGGER.error("");
            throw new BNDScriptRuntimeException(message, e);
         }
      }

      // Arrêt du Pool de Thread et fermeture du Context Spring
      LOGGER.info("Stats : {}", statistic);
      executorService.stopPool();

      LOGGER.info("Fin du traitement [{}] ", jobEntity.getJobUUid());
   }
}

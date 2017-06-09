package fr.urssaf.image.sae.trace.executable.support;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.commons.service.ParametersService;
import fr.urssaf.image.sae.trace.executable.exception.TraceExecutableException;
import fr.urssaf.image.sae.trace.executable.utils.SaeFileUtils;
import fr.urssaf.image.sae.trace.model.JournalisationType;
import fr.urssaf.image.sae.trace.service.JournalisationService;

/**
 * Classe permettant de réaliser des opérations de journalisation
 * 
 */
@Component
public class JournalisationSupport {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(JournalisationSupport.class);

   @Autowired
   private JournalisationService journalisationService;

   @Autowired
   private CaptureSupport captureSupport;

   @Autowired
   private ParametersService paramService;

   /**
    * Journalisation des traces d'un journal à une date donnée
    * 
    * @param typeJournalisation
    *           type de journal
    * @param date
    *           date à laquelle réaliser la journalisation
    * @throws TraceExecutableException
    *            exception levée lors du traitement de journalisation
    */
   public final void journaliser(JournalisationType typeJournalisation,
         Date date) throws TraceExecutableException {
      String trcPrefix = "journaliserDate()";

      String tempPath = System.getProperty("java.io.tmpdir");
      String path = journalisationService.exporterTraces(typeJournalisation,
            tempPath, date);
      File xmlFile = new File(path);

      String zipPath = SaeFileUtils.generateGZip(path);
      boolean isFileDeleted = FileUtils.deleteQuietly(xmlFile);

      if (!isFileDeleted) {
         LOGGER.warn("{} - le fichier {} n'a pas pu être supprimé",
               new Object[] { trcPrefix, path });
      }

      File zipFile = new File(zipPath);
      String hash = SaeFileUtils.calculateSha1(zipFile);
      UUID uuid = captureSupport.capture(zipPath, date);
      LOGGER.info("{} - Journal du {} archivé. UUID : {}. SHA-1 : {}",
            new Object[] {
                  trcPrefix,
                  new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH)
                        .format(date), uuid, hash });
      isFileDeleted = FileUtils.deleteQuietly(zipFile);

      if (!isFileDeleted) {
         LOGGER.warn("{} - le fichier {} n'a pas pu être supprimé",
               new Object[] { trcPrefix, zipPath });
      }

      updateParameters(typeJournalisation, uuid, hash, date);
   }

   private void updateParameters(JournalisationType typeJournalisation,
         UUID uuid, String hash, Date date) throws TraceExecutableException {

      if (JournalisationType.JOURNALISATION_EVT.equals(typeJournalisation)) {

         paramService.setJournalisationEvtIdJournPrec(uuid.toString());
         paramService.setJournalisationEvtHashJournPrec(hash);
         paramService.setJournalisationEvtDate(date);

      } else {
         throw new TraceExecutableException(
               "Type de journalisation non supportée");
      }

   }

}
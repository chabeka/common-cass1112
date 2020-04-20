package fr.urssaf.image.sae.trace.service.support;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.trace.model.PurgeType;

/**
 * Support pour l'écriture des traces applicatives
 */
@Component
public final class LoggerSupport {

   private final DateFormat dateFormatJournee = new SimpleDateFormat(
         "yyyyMMdd", Locale.FRANCE);

   /**
    * Trace de purge : Début
    * 
    * @param logger
    *           le logger
    * @param prefixe
    *           le préfixe de la trace (nom de la méthode)
    * @param typePurge
    *           le type de purge
    */
   public void logPurgeDebut(Logger logger, String prefixe, PurgeType typePurge) {
      logger.info("{} - Purge {} - Début", new Object[] { prefixe, typePurge });
   }

   /**
    * Trace de purge : Fin
    * 
    * @param logger
    *           le logger
    * @param prefixe
    *           le préfixe de la trace (nom de la méthode)
    * @param typePurge
    *           le type de purge
    */
   public void logPurgeFin(Logger logger, String prefixe, PurgeType typePurge) {
      logger.info("{} - Purge {} - Fin", new Object[] { prefixe, typePurge });
   }

   /**
    * Trace de purge : plage de journées à purger
    * 
    * @param logger
    *           le logger
    * @param prefixe
    *           le préfixe de la trace (nom de la méthode)
    * @param typePurge
    *           le type de purge
    * @param dateDebut
    *           la date de début de purge
    * @param dateFin
    *           la date de fin de purge
    */
   public void logPurgeJournees(Logger logger, String prefixe,
         PurgeType typePurge, Date dateDebut, Date dateFin) {
      String journeeDebut = dateFormatJournee.format(dateDebut);
      String journeeFin = dateFormatJournee.format(dateFin);
      logger.info("{} - Purge {} - Journées à purger {} -> {}", new Object[] {
            prefixe, typePurge, journeeDebut, journeeFin });
   }

   /**
    * Trace de purge : positionnement du flag "en cours" à true ou false
    * 
    * @param logger
    *           le logger
    * @param prefixe
    *           le préfixe de la trace (nom de la méthode)
    * @param typePurge
    *           le type de purge
    * @param flag
    *           la valeur du flag
    */
   public void logPurgeFlag(Logger logger, String prefixe, PurgeType typePurge,
         Boolean flag) {
      logger.info("{} - Purge {} - Positionnement du flag \"en cours\" à {}",
            new Object[] { prefixe, typePurge, flag });
   }

   /**
    * Trace de purge : début de purge d'une journée
    * 
    * @param logger
    *           le logger
    * @param prefixe
    *           le préfixe de la trace (nom de la méthode)
    * @param typePurge
    *           le type de purge
    * @param journee
    *           la journée
    */
   public void logPurgeJourneeDebut(Logger logger, String prefixe,
         PurgeType typePurge, String journee) {
      logger.info("{} - Purge {} - Traitement de la journée {} - Début",
            new Object[] { prefixe, typePurge, journee });
   }

   /**
    * Trace de purge : fin de purge d'une journée
    * 
    * @param logger
    *           le logger
    * @param prefixe
    *           le préfixe de la trace (nom de la méthode)
    * @param typePurge
    *           le type de purge
    * @param journee
    *           la journée
    * @param nbTracesPurgees
    *           le nombre de traces purgées
    */
   public void logPurgeJourneeFin(Logger logger, String prefixe,
         PurgeType typePurge, String journee, long nbTracesPurgees) {
      logger
            .info(
                  "{} - Purge {} - Traitement de la journée {} - Fin ({} traces purgées)",
                  new Object[] { prefixe, typePurge, journee, nbTracesPurgees });
   }

}

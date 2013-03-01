/**
 * 
 */
package fr.urssaf.image.sae.trace.executable.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.trace.dao.support.ServiceProviderSupport;
import fr.urssaf.image.sae.trace.executable.exception.TraceExecutableException;
import fr.urssaf.image.sae.trace.executable.exception.TraceExecutableRuntimeException;
import fr.urssaf.image.sae.trace.executable.service.TraitementService;
import fr.urssaf.image.sae.trace.executable.support.AuthentificationSupport;
import fr.urssaf.image.sae.trace.executable.support.JournalisationSupport;
import fr.urssaf.image.sae.trace.model.JournalisationType;
import fr.urssaf.image.sae.trace.model.PurgeType;
import fr.urssaf.image.sae.trace.service.JournalisationService;
import fr.urssaf.image.sae.trace.service.PurgeService;
import fr.urssaf.image.sae.trace.service.StatusService;

/**
 * Classe d'implémentation du support {@link TraitementService}. Cette classe
 * est un singleton et peut être accessible par le mécanisme d'injection IOC
 * avec l'annotation @Autowired
 * 
 */
@Service
public class TraitementServiceImpl implements TraitementService {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(TraitementServiceImpl.class);

   @Autowired
   private PurgeService purgeService;

   @Autowired
   private ServiceProviderSupport providerSupport;

   @Autowired
   private JournalisationService journalisationService;

   @Autowired
   private StatusService statusService;

   @Autowired
   private JournalisationSupport journalisationSupport;

   @Autowired
   private AuthentificationSupport authentificationSupport;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void purger(PurgeType purgeType) {

      String trcPrefix = "purge()";

      try {
         providerSupport.connect();

         LOGGER.info("{} - début du traitement de la purge pour le type {}",
               new Object[] { trcPrefix, purgeType.toString() });
         purgeService.purgerRegistre(purgeType);
         LOGGER.info("{} - fin du traitement de la purge pour le type {}",
               new Object[] { trcPrefix, purgeType.toString() });

      } finally {
         providerSupport.disconnect();
      }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void journaliser(JournalisationType typeJournalisation) {

      authentificationSupport.authentifier();

      String trcPrefix = "journaliser()";

      boolean isRunning = statusService
            .isJournalisationRunning(typeJournalisation);

      if (isRunning) {
         throw new TraceExecutableRuntimeException(StringUtils.replace(
               "la journalisation {0} est déjà en cours", "{0}",
               typeJournalisation.toString()));
      }

      LOGGER.info(
            "{} - début du traitement de la journalisation pour le type {}",
            new Object[] { trcPrefix, typeJournalisation.toString() });

      statusService
            .updateJournalisationStatus(typeJournalisation, Boolean.TRUE);
      List<Date> dates = journalisationService
            .recupererDates(typeJournalisation);

      try {
         for (Date date : dates) {
            LOGGER
                  .info(
                        "{} - début du traitement de la journalisation pour le type {} et à la date du {}",
                        new Object[] { trcPrefix,
                              typeJournalisation.toString(),
                              DateFormatUtils.ISO_DATE_FORMAT.format(date) });

            journalisationSupport.journaliser(typeJournalisation, date);

            LOGGER
                  .info(
                        "{} - fin du traitement de la journalisation pour le type {} et à la date du {}",
                        new Object[] { trcPrefix,
                              typeJournalisation.toString(),
                              DateFormatUtils.ISO_DATE_FORMAT.format(date) });
         }
      } catch (TraceExecutableException exception) {
         LOGGER.error("{} - Erreur lors de la journalisation {}", new Object[] {
               trcPrefix, typeJournalisation }, exception);
      } finally {
         statusService.updateJournalisationStatus(typeJournalisation,
               Boolean.FALSE);
      }

      LOGGER.info(
            "{} - fin du traitement de la journalisation pour le type {}",
            new Object[] { trcPrefix, typeJournalisation.toString() });

   }
}

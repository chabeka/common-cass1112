package fr.urssaf.image.sae.trace.service.impl;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.commons.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.commons.service.ParametersService;
import fr.urssaf.image.sae.trace.exception.TraceRuntimeException;
import fr.urssaf.image.sae.trace.model.PurgeType;
import fr.urssaf.image.sae.trace.service.JournalEvtService;
import fr.urssaf.image.sae.trace.service.PurgeService;
import fr.urssaf.image.sae.trace.service.RegExploitationService;
import fr.urssaf.image.sae.trace.service.RegSecuriteService;
import fr.urssaf.image.sae.trace.service.RegService;
import fr.urssaf.image.sae.trace.service.RegTechniqueService;
import fr.urssaf.image.sae.trace.service.StatusService;
import fr.urssaf.image.sae.trace.service.support.LoggerSupport;
import fr.urssaf.image.sae.trace.utils.DateRegUtils;

/**
 * Classe d'implémentation de l'interface {@link PurgeService}. Cette classe est
 * un singleton et peut être accessible via le mécanisme d'injection IOC avec
 * l'annotation @Autowired
 * 
 */
@Service
public class PurgeServiceImpl implements PurgeService {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(PurgeServiceImpl.class);

   private final SimpleDateFormat dateJourneeFormat = new SimpleDateFormat(
         "yyyy-MM-dd", Locale.FRENCH);

   @Autowired
   private ParametersService paramService;

   @Autowired
   private RegExploitationService exploitService;

   @Autowired
   private RegTechniqueService techService;

   @Autowired
   private RegSecuriteService secuService;

   @Autowired
   private JournalEvtService evtService;

   @Autowired
   private StatusService statusService;

   @Autowired
   private LoggerSupport loggerSupport;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void purgerRegistre(PurgeType typePurge) {

      String trcPrefix = "purgerRegistre()";

      LOGGER.debug("{} - début", trcPrefix);
      loggerSupport.logPurgeDebut(LOGGER, trcPrefix, typePurge);

      List<PurgeType> authorized = Arrays.asList(PurgeType.PURGE_EXPLOITATION,
            PurgeType.PURGE_SECURITE, PurgeType.PURGE_TECHNIQUE);

      if (!authorized.contains(typePurge)) {
         throw new TraceRuntimeException("la purge ne concerne pas un registre");
      }

      Boolean isRunning = statusService.isPurgeRunning(typePurge);
      if (Boolean.TRUE.equals(isRunning)) {
         String registre = getLibelleFromPurgeType(typePurge);
         throw new TraceRuntimeException(StringUtils.replace(
               "La purge des registres {0} est déjà en cours", "{0}", registre));
      }

      LOGGER.debug("{} - mise à jour du flag de traitement à TRUE", trcPrefix);
      loggerSupport.logPurgeFlag(LOGGER, trcPrefix, typePurge, Boolean.TRUE);
      statusService.updatePurgeStatus(typePurge, Boolean.TRUE);
      try {

         Date minDate = getDateFromPurgeType(typePurge);
         Integer retentionDuration = getDureeFomPurgeType(typePurge);
         Date maxDate = DateUtils.addDays(new Date(), -retentionDuration);
         loggerSupport.logPurgeJournees(LOGGER, trcPrefix, typePurge, minDate,
               maxDate);

         if (minDate.before(maxDate)) {

            @SuppressWarnings("unchecked")
            RegService servicePurge;
            if (PurgeType.PURGE_EXPLOITATION.equals(typePurge)) {
               servicePurge = exploitService;

            } else if (PurgeType.PURGE_SECURITE.equals(typePurge)) {
               servicePurge = secuService;

            } else {
               servicePurge = techService;
            }

            purge(servicePurge, minDate, maxDate, typePurge);

            maxDate = DateUtils.truncate(maxDate, Calendar.DATE);
            setDateFromPurgeType(typePurge, maxDate);

         }

      } finally {
         LOGGER.debug("{} - mise à jour du flag de traitement à FALSE",
               trcPrefix);
         loggerSupport
               .logPurgeFlag(LOGGER, trcPrefix, typePurge, Boolean.FALSE);
         statusService.updatePurgeStatus(typePurge, Boolean.FALSE);
      }

      loggerSupport.logPurgeFin(LOGGER, trcPrefix, typePurge);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void purgerJournal(PurgeType typePurge) {

      String trcPrefix = "purgerJournal()";
      List<PurgeType> authorized = Arrays.asList(PurgeType.PURGE_EVT);

      if (!authorized.contains(typePurge)) {
         throw new TraceRuntimeException("la purge ne concerne pas un journal");
      }

      Boolean isRunning = statusService.isPurgeRunning(typePurge);
      if (Boolean.TRUE.equals(isRunning)) {
         String libelle = getLibelleFromPurgeType(typePurge);
         throw new TraceRuntimeException(StringUtils.replace(
               "La purge du journal {0} est déjà en cours", "{0}", libelle));
      }

      LOGGER.debug("{} - mise à jour du flag de traitement à TRUE", trcPrefix);
      loggerSupport.logPurgeFlag(LOGGER, trcPrefix, typePurge, Boolean.TRUE);
      statusService.updatePurgeStatus(typePurge, Boolean.TRUE);
      try {

         Date dateDerniereJourneePurgee = getDateFromPurgeType(typePurge);
         Date premiereJourneeApurger = DateUtils.addDays(
               dateDerniereJourneePurgee, 1);
         LOGGER.debug("{} - Première journée à purger : {}", trcPrefix,
               dateJourneeFormat.format(premiereJourneeApurger));

         Integer retentionDuration = getDureeFomPurgeType(typePurge);
         LOGGER.debug("{} - Durée de rétention des traces : {}", trcPrefix,
               retentionDuration);

         Date maxDate = DateUtils.addDays(new Date(), -retentionDuration);

         LOGGER
               .debug(
                     "{} - Dernière journée à purger calculée uniquement avec la durée de rétention : {}",
                     trcPrefix, dateJourneeFormat.format(maxDate));

         Date lastDateJournalisation = getDateJournalisationFromPurgeType(typePurge);
         LOGGER.debug("{} - Dernière journée qui a été journalisée : {}",
               trcPrefix, dateJourneeFormat.format(lastDateJournalisation));

         if (lastDateJournalisation.before(maxDate)) {
            maxDate = lastDateJournalisation;
         }

         LOGGER
               .debug(
                     "{} - Dernière journée à purger calculée avec la durée de rétention et la dernière date de journalisation : {}",
                     trcPrefix, dateJourneeFormat.format(maxDate));

         if (premiereJourneeApurger.before(maxDate)) {

            loggerSupport.logPurgeJournees(LOGGER, trcPrefix, typePurge,
                  premiereJourneeApurger, maxDate);

            purge(evtService, premiereJourneeApurger, maxDate, typePurge);

         } else {

            LOGGER.debug("{} - Rien à purger", trcPrefix);

         }
      } finally {
         LOGGER.debug("{} - mise à jour du flag de traitement à FALSE",
               trcPrefix);
         loggerSupport
               .logPurgeFlag(LOGGER, trcPrefix, typePurge, Boolean.FALSE);
         statusService.updatePurgeStatus(typePurge, Boolean.FALSE);
      }

   }

   @SuppressWarnings("unchecked")
   private void purge(RegService servicePurge, Date minDate, Date maxDate,
         PurgeType typePurge) {

      String prefix = "purge()";
      LOGGER.debug("{} - debut de purge pour le type {}", new Object[] {
            prefix, typePurge.toString() });

      Date date = DateUtils.truncate(minDate, Calendar.DATE);
      Date endDate = DateUtils.truncate(maxDate, Calendar.DATE);

      do {

         loggerSupport.logPurgeJourneeDebut(LOGGER, prefix, typePurge,
               DateRegUtils.getJournee(date));

         servicePurge.purge(date);

         setDateFromPurgeType(typePurge, date);

         date = DateUtils.addDays(date, 1);

      } while (date.compareTo(endDate) <= 0);

      LOGGER.debug("{} - fin de la purge pour le type {}", new Object[] {
            prefix, typePurge.toString() });

   }

   private void setDateFromPurgeType(PurgeType typePurge, Date date) {

      if (PurgeType.PURGE_EXPLOITATION.equals(typePurge)) {
         paramService.setPurgeExploitDate(date);

      } else if (PurgeType.PURGE_SECURITE.equals(typePurge)) {
         paramService.setPurgeSecuDate(date);

      } else if (PurgeType.PURGE_TECHNIQUE.equals(typePurge)) {
         paramService.setPurgeTechDate(date);

      } else if (PurgeType.PURGE_EVT.equals(typePurge)) {
         paramService.setPurgeEvtDate(date);

      } else {
         throw new IllegalArgumentException("typePurge");
      }

   }

   private Date getDateFromPurgeType(PurgeType typePurge) {

      try {
         if (PurgeType.PURGE_EXPLOITATION.equals(typePurge)) {
            return paramService.getPurgeExploitDate();

         } else if (PurgeType.PURGE_SECURITE.equals(typePurge)) {
            return paramService.getPurgeSecuDate();

         } else if (PurgeType.PURGE_TECHNIQUE.equals(typePurge)) {
            return paramService.getPurgeTechDate();

         } else if (PurgeType.PURGE_EVT.equals(typePurge)) {
            return paramService.getPurgeEvtDate();

         } else {
            throw new IllegalArgumentException("typePurge");
         }
      } catch (ParameterNotFoundException e) {
         throw new TraceRuntimeException(e);
      }

   }

   private Integer getDureeFomPurgeType(PurgeType typePurge) {

      try {
         if (PurgeType.PURGE_EXPLOITATION.equals(typePurge)) {
            return paramService.getPurgeExploitDuree();

         } else if (PurgeType.PURGE_SECURITE.equals(typePurge)) {
            return paramService.getPurgeSecuDuree();

         } else if (PurgeType.PURGE_TECHNIQUE.equals(typePurge)) {
            return paramService.getPurgeTechDuree();

         } else if (PurgeType.PURGE_EVT.equals(typePurge)) {
            return paramService.getPurgeEvtDuree();

         } else {
            throw new IllegalArgumentException("typePurge");
         }
      } catch (ParameterNotFoundException e) {
         throw new TraceRuntimeException(e);
      }

   }

   private String getLibelleFromPurgeType(PurgeType purgeType) {
      String registre;
      if (PurgeType.PURGE_EXPLOITATION.equals(purgeType)) {
         registre = "d'exploitation";

      } else if (PurgeType.PURGE_SECURITE.equals(purgeType)) {
         registre = "de sécurité";

      } else if (PurgeType.PURGE_TECHNIQUE.equals(purgeType)) {
         registre = "techniques";

      } else {
         registre = "des événements";
      }

      return registre;
   }

   private Date getDateJournalisationFromPurgeType(PurgeType typePurge) {

      if (PurgeType.PURGE_EVT.equals(typePurge)) {

         try {
            Date date = paramService.getJournalisationEvtDate();

            return date;

         } catch (ParameterNotFoundException e) {
            throw new TraceRuntimeException(e);
         }

      } else {
         throw new TraceRuntimeException(
               "Type de journalisation non supporté pour cette purge");
      }

   }

}

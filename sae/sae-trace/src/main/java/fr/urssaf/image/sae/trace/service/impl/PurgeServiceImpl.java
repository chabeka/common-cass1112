/**
 * 
 */
package fr.urssaf.image.sae.trace.service.impl;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.trace.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.trace.exception.TraceRuntimeException;
import fr.urssaf.image.sae.trace.model.Parameter;
import fr.urssaf.image.sae.trace.model.ParameterType;
import fr.urssaf.image.sae.trace.model.PurgeType;
import fr.urssaf.image.sae.trace.service.JournalEvtService;
import fr.urssaf.image.sae.trace.service.ParametersService;
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

      String trcPrefix = "purgerRegistre";

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

      Date minDate = getDateFromPurgeType(typePurge);
      Integer retentionDuration = getDureeFomPurgeType(typePurge);
      Date maxDate = DateUtils.addDays(new Date(), -retentionDuration);
      loggerSupport.logPurgeJournees(LOGGER, trcPrefix, typePurge, minDate,
            maxDate);

      try {
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
         updateDate(typePurge, maxDate);

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
   public void purgerJournal(PurgeType typePurge) {

      String trcPrefix = "purgerRegistre";
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

      Date dateMin = getDateFromPurgeType(typePurge);
      Integer retentionDuration = getDureeFomPurgeType(typePurge);
      Date maxDate = DateUtils.addDays(new Date(), -retentionDuration);
      Date lastDateJournalisation = getDateJournalisationFromPurgeType(typePurge);

      if (lastDateJournalisation.before(maxDate)) {
         maxDate = lastDateJournalisation;
      }

      if (dateMin.before(maxDate)) {
         loggerSupport.logPurgeJournees(LOGGER, trcPrefix, typePurge, dateMin,
               maxDate);
         purge(evtService, dateMin, maxDate, typePurge);
      }

      LOGGER.debug("{} - mise à jour du flag de traitement à TRUE", trcPrefix);
      loggerSupport.logPurgeFlag(LOGGER, trcPrefix, typePurge, Boolean.FALSE);
      statusService.updatePurgeStatus(typePurge, Boolean.FALSE);

   }

   @SuppressWarnings("unchecked")
   private void purge(RegService servicePurge, Date minDate, Date maxDate,
         PurgeType typePurge) {

      String prefix = "purge()";
      LOGGER.debug("{} - debut de purge pour le type {}", new Object[] {
            prefix, typePurge.toString() });

      Date date = DateUtils.truncate(minDate, Calendar.DATE);
      Date endDate = DateUtils.truncate(maxDate, Calendar.DATE);
      Parameter parameter;

      do {

         loggerSupport.logPurgeJourneeDebut(LOGGER, prefix,
               PurgeType.PURGE_SECURITE, DateRegUtils.getJournee(date));
         servicePurge.purge(date);

         parameter = new Parameter(
               getDateParameterTypeFromPurgeType(typePurge), date);
         paramService.saveParameter(parameter);

         date = DateUtils.addDays(date, 1);

      } while (date.compareTo(endDate) <= 0);

      LOGGER.debug("{} - fin de la purge pour le type {}", new Object[] {
            prefix, typePurge.toString() });

   }

   private Date getDateFromPurgeType(PurgeType purgeType) {

      ParameterType type = getDateParameterTypeFromPurgeType(purgeType);

      Date date;
      try {
         Parameter param = paramService.loadParameter(type);
         date = (Date) param.getValue();

      } catch (ParameterNotFoundException exception) {

         // On commence au 01/01/2013
         Calendar calendar = Calendar.getInstance();
         calendar.set(Calendar.YEAR, 2013);
         calendar.set(Calendar.MONTH, 0); // les numéros de mois commencent à 0
         calendar.set(Calendar.DAY_OF_MONTH, 1);
         calendar.set(Calendar.HOUR_OF_DAY, 0);
         calendar.set(Calendar.MINUTE, 0);
         calendar.set(Calendar.SECOND, 0);
         calendar.set(Calendar.MILLISECOND, 0);
         date = calendar.getTime();

      }

      return date;
   }

   private ParameterType getDateParameterTypeFromPurgeType(PurgeType purgeType) {
      ParameterType type;
      if (PurgeType.PURGE_EXPLOITATION.equals(purgeType)) {
         type = ParameterType.PURGE_EXPLOIT_DATE;

      } else if (PurgeType.PURGE_SECURITE.equals(purgeType)) {
         type = ParameterType.PURGE_SECU_DATE;

      } else if (PurgeType.PURGE_TECHNIQUE.equals(purgeType)) {
         type = ParameterType.PURGE_TECH_DATE;

      } else {
         type = ParameterType.PURGE_EVT_DATE;
      }

      return type;
   }

   private Integer getDureeFomPurgeType(PurgeType purgeType) {
      ParameterType type;
      if (PurgeType.PURGE_EXPLOITATION.equals(purgeType)) {
         type = ParameterType.PURGE_EXPLOIT_DUREE;

      } else if (PurgeType.PURGE_SECURITE.equals(purgeType)) {
         type = ParameterType.PURGE_SECU_DUREE;

      } else if (PurgeType.PURGE_TECHNIQUE.equals(purgeType)) {
         type = ParameterType.PURGE_TECH_DUREE;

      } else {
         type = ParameterType.PURGE_EVT_DUREE;
      }

      Integer duree;
      try {
         Parameter param = paramService.loadParameter(type);
         duree = (Integer) param.getValue();

      } catch (ParameterNotFoundException exception) {
         throw new TraceRuntimeException(exception);
      }

      return duree;
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

   private void updateDate(PurgeType typePurge, Date maxDate) {
      ParameterType type = getDateParameterTypeFromPurgeType(typePurge);
      Parameter parameter = new Parameter(type, maxDate);
      paramService.saveParameter(parameter);
   }

   private Date getDateJournalisationFromPurgeType(PurgeType typePurge) {

      Date date;
      try {
         Parameter param = paramService
               .loadParameter(ParameterType.JOURNALISATION_EVT_DATE);
         date = (Date) param.getValue();

      } catch (ParameterNotFoundException exception) {
         throw new TraceRuntimeException(exception);
      }

      return date;
   }

}

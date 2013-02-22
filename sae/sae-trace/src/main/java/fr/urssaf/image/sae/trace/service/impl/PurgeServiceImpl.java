/**
 * 
 */
package fr.urssaf.image.sae.trace.service.impl;

import java.util.Calendar;
import java.util.Date;

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
import fr.urssaf.image.sae.trace.service.ParametersService;
import fr.urssaf.image.sae.trace.service.PurgeService;
import fr.urssaf.image.sae.trace.service.RegExploitationService;
import fr.urssaf.image.sae.trace.service.RegSecuriteService;
import fr.urssaf.image.sae.trace.service.RegTechniqueService;
import fr.urssaf.image.sae.trace.service.support.LoggerSupport;

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
   private LoggerSupport loggerSupport;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void purgerRegistre(PurgeType typePurge) {

      String trcPrefix = "purgerRegistre";

      LOGGER.debug("{} - début", trcPrefix);
      loggerSupport.logPurgeDebut(LOGGER, trcPrefix, typePurge);

      Boolean isRunning = getPurgeIsRunningFromPurgeType(typePurge);
      if (Boolean.TRUE.equals(isRunning)) {
         String registre = getRegistreFromPurgeType(typePurge);
         throw new TraceRuntimeException(StringUtils.replace(
               "La purge des registres {0} est déjà en cours", "{0}", registre));
      }

      LOGGER.debug("{} - mise à jour du flag de traitement à TRUE", trcPrefix);
      loggerSupport.logPurgeFlag(LOGGER, trcPrefix, typePurge, Boolean.TRUE);
      updateIsRunning(typePurge, Boolean.TRUE);

      Date minDate = getDateFromPurgeType(typePurge);
      Integer retentionDuration = getDureeFomPurgeType(typePurge);
      Date maxDate = DateUtils.addDays(new Date(), -retentionDuration);
      loggerSupport.logPurgeJournees(LOGGER, trcPrefix, typePurge, minDate,
            maxDate);

      try {
         if (PurgeType.PURGE_EXPLOITATION.equals(typePurge)) {
            exploitService.purge(minDate, maxDate);

         } else if (PurgeType.PURGE_SECURITE.equals(typePurge)) {
            secuService.purge(minDate, maxDate);

         } else {
            techService.purge(minDate, maxDate);
         }

         maxDate = DateUtils.truncate(maxDate, Calendar.DATE);
         updateDate(typePurge, maxDate);

      } finally {
         LOGGER.debug("{} - mise à jour du flag de traitement à FALSE",
               trcPrefix);
         loggerSupport
               .logPurgeFlag(LOGGER, trcPrefix, typePurge, Boolean.FALSE);
         updateIsRunning(typePurge, Boolean.FALSE);
      }

      loggerSupport.logPurgeFin(LOGGER, trcPrefix, typePurge);

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

      } else {
         type = ParameterType.PURGE_TECH_DATE;
      }

      return type;
   }

   private Integer getDureeFomPurgeType(PurgeType purgeType) {
      ParameterType type;
      if (PurgeType.PURGE_EXPLOITATION.equals(purgeType)) {
         type = ParameterType.PURGE_EXPLOIT_DUREE;

      } else if (PurgeType.PURGE_SECURITE.equals(purgeType)) {
         type = ParameterType.PURGE_SECU_DUREE;

      } else {
         type = ParameterType.PURGE_TECH_DUREE;
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

   private Boolean getPurgeIsRunningFromPurgeType(PurgeType purgeType) {

      ParameterType type = getIsRunningParameterFromPurgeType(purgeType);

      Boolean isRunning;
      try {
         Parameter param = paramService.loadParameter(type);
         isRunning = (Boolean) param.getValue();

      } catch (ParameterNotFoundException exception) {
         isRunning = Boolean.FALSE;
      }

      return isRunning;
   }

   private ParameterType getIsRunningParameterFromPurgeType(PurgeType purgeType) {
      ParameterType type;
      if (PurgeType.PURGE_EXPLOITATION.equals(purgeType)) {
         type = ParameterType.PURGE_EXPLOIT_IS_RUNNING;

      } else if (PurgeType.PURGE_SECURITE.equals(purgeType)) {
         type = ParameterType.PURGE_SECU_IS_RUNNING;

      } else {
         type = ParameterType.PURGE_TECH_IS_RUNNING;
      }

      return type;
   }

   private String getRegistreFromPurgeType(PurgeType purgeType) {
      String registre;
      if (PurgeType.PURGE_EXPLOITATION.equals(purgeType)) {
         registre = "d'exploitation";

      } else if (PurgeType.PURGE_SECURITE.equals(purgeType)) {
         registre = "de sécurité";

      } else {
         registre = "techniques";
      }

      return registre;
   }

   private void updateDate(PurgeType typePurge, Date maxDate) {
      ParameterType type = getDateParameterTypeFromPurgeType(typePurge);
      Parameter parameter = new Parameter(type, maxDate);
      paramService.saveParameter(parameter);
   }

   private void updateIsRunning(PurgeType purgeType, Boolean isRunning) {
      ParameterType type = getIsRunningParameterFromPurgeType(purgeType);
      Parameter parameter = new Parameter(type, isRunning);
      paramService.saveParameter(parameter);
   }

}

/**
 * 
 */
package fr.urssaf.image.sae.trace.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.trace.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.trace.exception.TraceRuntimeException;
import fr.urssaf.image.sae.trace.model.JournalisationType;
import fr.urssaf.image.sae.trace.model.Parameter;
import fr.urssaf.image.sae.trace.model.ParameterType;
import fr.urssaf.image.sae.trace.model.PurgeType;
import fr.urssaf.image.sae.trace.service.ParametersService;
import fr.urssaf.image.sae.trace.service.StatusService;

/**
 * Classe d'implémentation de l'interface {@link StatusService}. Cette classe
 * est un singleton et peut être accessible via le mécanisme d'injection IOC
 * avec l'annotation @Autowired
 * 
 */
@Service
public class StatusServiceImpl implements StatusService {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(StatusServiceImpl.class);

   @Autowired
   private ParametersService paramService;

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean isJournalisationRunning(
         JournalisationType typeJournalisation) {

      boolean isRunning = false;
      ParameterType parameterType = null;

      if (JournalisationType.JOURNALISATION_EVT.equals(typeJournalisation)) {
         parameterType = ParameterType.JOURNALISATION_EVT_IS_RUNNING;
      }

      try {
         Parameter parameter = paramService.loadParameter(parameterType);

         if (parameter != null && parameter.getValue() instanceof Boolean) {
            isRunning = (Boolean) parameter.getValue();
         }

      } catch (ParameterNotFoundException exception) {
         LOGGER.info("{} - Le paramètre n'existe pas");
      }

      return isRunning;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean isPurgeRunning(PurgeType typePurge) {

      boolean isRunning = false;
      ParameterType parameterType = getParameterIsRunningPurge(typePurge);

      try {
         Parameter parameter = paramService.loadParameter(parameterType);

         if (parameter != null && parameter.getValue() instanceof Boolean) {

            isRunning = (Boolean) parameter.getValue();
         }

      } catch (ParameterNotFoundException exception) {
         LOGGER.info("{} - Le paramètre n'existe pas");
      }

      return isRunning;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void updateJournalisationStatus(
         JournalisationType typeJournalisation, Boolean value) {

      ParameterType parameterType = getParameterIsRunningJournalisation(typeJournalisation);

      Parameter parameter = new Parameter(parameterType, value);
      paramService.saveParameter(parameter);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void updatePurgeStatus(PurgeType typePurge, Boolean value) {
      ParameterType parameterType = getParameterIsRunningPurge(typePurge);

      Parameter parameter = new Parameter(parameterType, value);
      paramService.saveParameter(parameter);

   }

   private ParameterType getParameterIsRunningPurge(PurgeType typePurge) {
      ParameterType parameterType = null;

      if (PurgeType.PURGE_EVT.equals(typePurge)) {
         parameterType = ParameterType.PURGE_EVT_IS_RUNNING;

      } else if (PurgeType.PURGE_EXPLOITATION.equals(typePurge)) {
         parameterType = ParameterType.PURGE_EXPLOIT_IS_RUNNING;

      } else if (PurgeType.PURGE_SECURITE.equals(typePurge)) {
         parameterType = ParameterType.PURGE_SECU_IS_RUNNING;

      } else {
         parameterType = ParameterType.PURGE_TECH_IS_RUNNING;
      }

      return parameterType;
   }

   private ParameterType getParameterIsRunningJournalisation(
         JournalisationType typeJournalisation) {

      ParameterType type;
      if (JournalisationType.JOURNALISATION_EVT.equals(typeJournalisation)) {
         type = ParameterType.JOURNALISATION_EVT_IS_RUNNING;

      } else {
         throw new TraceRuntimeException("Type de journalisation non supporté");
      }
      
      return type;
   }
}

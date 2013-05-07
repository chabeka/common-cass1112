package fr.urssaf.image.sae.trace.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.commons.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.commons.service.ParametersService;
import fr.urssaf.image.sae.trace.exception.TraceRuntimeException;
import fr.urssaf.image.sae.trace.model.JournalisationType;
import fr.urssaf.image.sae.trace.model.PurgeType;
import fr.urssaf.image.sae.trace.service.StatusService;

/**
 * Classe d'implémentation de l'interface {@link StatusService}. Cette classe
 * est un singleton et peut être accessible via le mécanisme d'injection IOC
 * avec l'annotation @Autowired
 * 
 */
@Service
public class StatusServiceImpl implements StatusService {

   @Autowired
   private ParametersService paramService;

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean isJournalisationRunning(
         JournalisationType typeJournalisation) {

      if (JournalisationType.JOURNALISATION_EVT.equals(typeJournalisation)) {
         try {
            return paramService.isJournalisationEvtIsRunning();
         } catch (ParameterNotFoundException e) {
            throw new TraceRuntimeException(e);
         }
      } else {
         throw new IllegalArgumentException("typeJournalisation");
      }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean isPurgeRunning(PurgeType typePurge) {

      try {

         if (PurgeType.PURGE_EVT.equals(typePurge)) {
            return paramService.isPurgeEvtIsRunning();

         } else if (PurgeType.PURGE_EXPLOITATION.equals(typePurge)) {
            return paramService.isPurgeExploitIsRunning();

         } else if (PurgeType.PURGE_SECURITE.equals(typePurge)) {
            return paramService.isPurgeSecuIsRunning();

         } else if (PurgeType.PURGE_TECHNIQUE.equals(typePurge)) {
            return paramService.isPurgeTechIsRunning();

         } else {
            throw new IllegalArgumentException("typePurge");

         }

      } catch (ParameterNotFoundException e) {
         return false;
      }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void updateJournalisationStatus(
         JournalisationType typeJournalisation, Boolean value) {

      if (JournalisationType.JOURNALISATION_EVT.equals(typeJournalisation)) {
         paramService.setJournalisationEvtIsRunning(value);
      } else {
         throw new IllegalArgumentException("typeJournalisation");
      }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void updatePurgeStatus(PurgeType typePurge, Boolean value) {

      if (PurgeType.PURGE_EVT.equals(typePurge)) {
         paramService.setPurgeEvtIsRunning(value);

      } else if (PurgeType.PURGE_EXPLOITATION.equals(typePurge)) {
         paramService.setPurgeExploitIsRunning(value);

      } else if (PurgeType.PURGE_SECURITE.equals(typePurge)) {
         paramService.setPurgeSecuIsRunning(value);

      } else if (PurgeType.PURGE_TECHNIQUE.equals(typePurge)) {
         paramService.setPurgeTechIsRunning(value);

      } else {
         throw new IllegalArgumentException("typePurge");

      }

   }

}

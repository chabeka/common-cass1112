/**
 * 
 */
package fr.urssaf.image.sae.trace.service.validation;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.trace.model.JournalisationType;
import fr.urssaf.image.sae.trace.model.PurgeType;

/**
 * Classe de validation des implémentations des méthodes de l'interface
 * RegService
 * 
 */
@Aspect
public class StatusServiceValidation {

   private static final String ARG_0 = "{0}";

   private static final String TYPE_DE_PURGE = "type de purge";

   private static final String MESSAGE_ERREUR = "l'argument {0} est obligatoire";

   private static final String CLASS_NAME = "fr.urssaf.image.sae.trace.service.StatusService.";

   private static final String PURGE_RUNNING_METHOD = "execution(boolean "
         + CLASS_NAME + "isPurgeRunning(*))" + " && args(type)";
   private static final String JOURNALISATION_RUNNING_METHOD = "execution(boolean "
         + CLASS_NAME + "isJournalisationRunning(*))" + " && args(type)";

   private static final String UPDATE_PURGE_METHOD = "execution(void "
         + CLASS_NAME + "updatePurgeStatus(*,*))" + " && args(type,value)";
   private static final String UPDATE_JOURNALISATION_METHOD = "execution(void "
         + CLASS_NAME + "updateJournalisationStatus(*,*))"
         + " && args(type,value)";

   /**
    * Réalise la validation de la méthode isPurgeRunning de l'interface
    * StatusService
    * 
    * @param type
    *           type de la purge
    */
   @Before(PURGE_RUNNING_METHOD)
   public final void purgeIsRunning(PurgeType type) {

      if (type == null) {
         throw new IllegalArgumentException(StringUtils.replace(MESSAGE_ERREUR,
               ARG_0, TYPE_DE_PURGE));
      }
   }

   /**
    * Réalise la validation de la méthode isJournalisationRunning de l'interface
    * StatusService
    * 
    * @param type
    *           type de la journalisation
    */
   @Before(JOURNALISATION_RUNNING_METHOD)
   public final void journalisationIsRunning(JournalisationType type) {

      if (type == null) {
         throw new IllegalArgumentException(StringUtils.replace(MESSAGE_ERREUR,
               ARG_0, "type de journalisation"));
      }
   }

   /**
    * Réalise la validation de la méthode updatePurgeStatus de l'interface
    * StatusService
    * 
    * @param type
    *           type de la purge
    * @param value
    *           valeur
    */
   @Before(UPDATE_PURGE_METHOD)
   public final void updatePurge(PurgeType type, Boolean value) {

      if (type == null) {
         throw new IllegalArgumentException(StringUtils.replace(MESSAGE_ERREUR,
               ARG_0, TYPE_DE_PURGE));
      }

      if (value == null) {
         throw new IllegalArgumentException(StringUtils.replace(MESSAGE_ERREUR,
               ARG_0, "valeur"));
      }
   }

   /**
    * Réalise la validation de la méthode updateJournalisationStatus de
    * l'interface StatusService
    * 
    * @param type
    *           type de la journalisation
    * @param value
    *           valeur
    */
   @Before(UPDATE_JOURNALISATION_METHOD)
   public final void updateJournalisation(JournalisationType type, Boolean value) {

      if (type == null) {
         throw new IllegalArgumentException(StringUtils.replace(MESSAGE_ERREUR,
               ARG_0, "type de journalisation"));
      }

      if (value == null) {
         throw new IllegalArgumentException(StringUtils.replace(MESSAGE_ERREUR,
               ARG_0, "valeur"));
      }
   }
}

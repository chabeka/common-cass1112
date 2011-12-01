package fr.urssaf.image.sae.storage.dfce.services.support.validation;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.storage.dfce.utils.TimeUtils;

/**
 * Classe de validation des arguments en entrée des implémentations du service
 * {@link fr.urssaf.image.sae.storage.dfce.services.support.InterruptionTraitementSupport}
 * .<br>
 * La validation est basée sur la programmation aspect
 * 
 * 
 */
@Aspect
public class InterruptionTraitementSupportValidation {

   private static final String CLASS = "fr.urssaf.image.sae.storage.dfce.services.support.InterruptionTraitementSupport";

   private static final String METHOD = "execution(void " + CLASS
         + ".interruption(*,*,*))" + "&& args(start,delay,tentatives)";

   private static final String ARG_EMPTY = "L''argument ''{0}'' doit être renseigné.";

   private static final String ARG_TIME = "L''argument ''{0}''=''{1}'' doit être au format HH:mm:ss.";

   private static final String ARG_POSITIF = "L''argument ''{0}'' doit être au moins égal à 1.";

   /**
    * Validation des méthodes de
    * {@link fr.urssaf.image.sae.storage.dfce.services.support.InterruptionTraitementSupport#interruption(String, long, int)}
    * <br>
    * 
    * @param start
    *           doit être renseigné au format HH:mm:ss
    * @param delay
    *           doit être supérieure à 0
    * @param tentatives
    *           doit être supérieure à 0
    */
   @Before(METHOD)
   public final void interruption(String start, long delay, int tentatives) {

      if (StringUtils.isBlank(start)) {

         throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
               "start"));
      }

      if (!TimeUtils.isValidate(start)) {

         throw new IllegalArgumentException(MessageFormat.format(ARG_TIME,
               "start",start));
      }

      if (delay < 1) {

         throw new IllegalArgumentException(MessageFormat.format(ARG_POSITIF,
               "delay"));
      }

      if (tentatives < 1) {

         throw new IllegalArgumentException(MessageFormat.format(ARG_POSITIF,
               "tentatives"));
      }

   }
}

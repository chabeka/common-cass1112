package fr.urssaf.image.sae.storage.dfce.services.support.validation;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.joda.time.DateTime;

import fr.urssaf.image.sae.storage.dfce.services.support.model.InterruptionTraitementConfig;
import fr.urssaf.image.sae.storage.dfce.utils.LocalTimeUtils;
import fr.urssaf.image.sae.storage.model.jmx.JmxIndicator;

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

   private static final String METHOD_INTERRUPTION = "execution(void "
         + CLASS
         + ".interruption(*,*,*)) && args(currentDate,interruptionConfig,jmxIndicator)";

   private static final String METHOD_HAS_INTERRUPTED = "execution(boolean "
         + CLASS
         + ".hasInterrupted(*,*)) && args(currentDate,interruptionConfig)";

   private static final String ARG_EMPTY = "L''argument ''{0}'' doit être renseigné.";

   private static final String ARG_TIME = "L''argument ''{0}''=''{1}'' doit être au format HH:mm:ss.";

   private static final String ARG_POSITIF = "L''argument ''{0}'' doit être au moins égal à 1.";

   private void validate(InterruptionTraitementConfig interruptionConfig) {

      if (interruptionConfig == null) {

         throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
               "interruptionConfig"));
      }

      if (StringUtils.isBlank(interruptionConfig.getStart())) {

         throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
               "startTime"));
      }

      if (!LocalTimeUtils.isValidate(interruptionConfig.getStart())) {

         throw new IllegalArgumentException(MessageFormat.format(ARG_TIME,
               "startTime", interruptionConfig.getStart()));
      }

      if (interruptionConfig.getDelay() < 1) {

         throw new IllegalArgumentException(MessageFormat.format(ARG_POSITIF,
               "delay"));
      }

      if (interruptionConfig.getTentatives() < 1) {

         throw new IllegalArgumentException(MessageFormat.format(ARG_POSITIF,
               "tentatives"));
      }

   }

   private void validate(DateTime currentDate) {

      if (currentDate == null) {

         throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
               "currentDate"));
      }
   }

   /**
    * Validation des méthodes de
    * {@link fr.urssaf.image.sae.storage.dfce.services.support.InterruptionTraitementSupport#interruption(DateTime,InterruptionTraitementConfig, JmxIndicator)}
    * <br>
    * 
    * @param currentDate
    *           doit être renseigné
    * @param interruptionConfig
    *           doit être renseigné
    *           <ul>
    *           <li>{@link InterruptionTraitementConfig#getStart()} doit être
    *           renseigné au format HH:mm:ss</li>
    *           <li>{@link InterruptionTraitementConfig#getDelay()} doit être
    *           supérieure à 0</li>
    *           <li>{@link InterruptionTraitementConfig#getTentatives()} doit
    *           être supérieure à 0</li>
    *           </ul>
    * @param jmxIndicator
    *           doit être renseigné
    */
   @Before(METHOD_INTERRUPTION)
   public final void interruption(DateTime currentDate,
         InterruptionTraitementConfig interruptionConfig,
         JmxIndicator jmxIndicator) {

      this.validate(currentDate);

      this.validate(interruptionConfig);

      if (jmxIndicator == null) {

         throw new IllegalArgumentException(MessageFormat.format(ARG_EMPTY,
               "jmxIndicator"));
      }

   }

   /**
    * Validation des méthodes de
    * {@link fr.urssaf.image.sae.storage.dfce.services.support.InterruptionTraitementSupport#hasInterrupted(DateTime, InterruptionTraitementConfig)}
    * <br>
    * 
    * @param currentDate
    *           doit être renseigné
    * @param interruptionConfig
    *           doit être renseigné
    *           <ul>
    *           <li>{@link InterruptionTraitementConfig#getStart()} doit être
    *           renseigné au format HH:mm:ss</li>
    *           <li>{@link InterruptionTraitementConfig#getDelay()} doit être
    *           supérieure à 0</li>
    *           <li>{@link InterruptionTraitementConfig#getTentatives()} doit
    *           être supérieure à 0</li>
    *           </ul>
    */
   @Before(METHOD_HAS_INTERRUPTED)
   public final void hasInterrupted(DateTime currentDate,
         InterruptionTraitementConfig interruptionConfig) {

      this.validate(currentDate);

      this.validate(interruptionConfig);
   }

}

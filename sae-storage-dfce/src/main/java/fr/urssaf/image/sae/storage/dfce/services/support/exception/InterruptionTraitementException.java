package fr.urssaf.image.sae.storage.dfce.services.support.exception;

import java.text.MessageFormat;

/**
 * Exception levée lorsque la reprise d'un traitement après une interruption a
 * échoué
 * 
 * 
 */
public class InterruptionTraitementException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   private static final String EXCEPTION_MESSAGE = "Après une déconnexion DFCE programmée à {0} il est impossible de reprendre le traitement après {1} secondes et {2} tentatives.";

   private final String startTime;

   private final int delay;

   private final int tentatives;

   /**
    * 
    * @param startTime
    *           Heure de programmation du début de l'interruption
    * @param delay
    *           Durée de l'interruption
    * @param tentatives
    *           Nombre de tentatives de reprise du traitement
    * @param cause
    *           cause de l'exception
    */
   public InterruptionTraitementException(String startTime, int delay,
         int tentatives, Throwable cause) {
      super(cause);

      this.startTime = startTime;
      this.delay = delay;
      this.tentatives = tentatives;

   }

   /**
    * 
    * {@inheritDoc} <br>
    * <br>
    * Le message est formaté sur le modèle {@value #EXCEPTION_MESSAGE}
    * <ul>
    * <li>{0} : <code>startTime</code></li>
    * <li>{1} : <code>delay</code></li>
    * <li>{2} : <code>tentatives</code></li>
    * </ul>
    * 
    * 
    */
   @Override
   public final String getMessage() {

      String message = MessageFormat.format(EXCEPTION_MESSAGE, startTime,
            delay, tentatives);

      return message;
   }

}

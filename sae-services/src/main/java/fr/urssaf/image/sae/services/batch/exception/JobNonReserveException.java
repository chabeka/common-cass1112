package fr.urssaf.image.sae.services.batch.exception;

import java.text.MessageFormat;
import java.util.UUID;

/**
 * Erreur levée lorsqu'on essaie de d'exécuter un job sans l'avoir préalablement
 * réservé
 * 
 * 
 */
public class JobNonReserveException extends Exception {

   private static final long serialVersionUID = 1L;

   private static final String EXCEPTION_MESSAGE = "Impossible d''exécuter le traitement n°{0} car il n''a pas été réservé.";

   private final UUID jobId;

   /**
    * 
    * @param jobId
    *           identifiant du jobRequest qui n'a pas été réservé
    */
   public JobNonReserveException(UUID jobId) {
      super();
      this.jobId = jobId;
   }

   /**
    * 
    * @return identifiant du jobRequest qui n'a pas été réservé
    */
   public final UUID getJobId() {
      return this.jobId;
   }

   /**
    * 
    * {@inheritDoc} <br>
    * <br>
    * Le message est formaté sur le modèle {@value #EXCEPTION_MESSAGE}
    * <ul>
    * <li>{0} : <code>identifiant du jobRequest qui n'a pas été réservé</code></li>
    * </ul>
    * 
    * 
    */
   @Override
   public final String getMessage() {

      String message = MessageFormat.format(EXCEPTION_MESSAGE, jobId);

      return message;
   }

}

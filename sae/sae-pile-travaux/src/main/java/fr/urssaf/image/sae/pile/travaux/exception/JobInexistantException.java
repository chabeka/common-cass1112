package fr.urssaf.image.sae.pile.travaux.exception;

import java.text.MessageFormat;
import java.util.UUID;

/**
 * Erreur levée lorsqu'on essaie de lancer ou de réserver un jobRequest qui
 * n'existe pas
 * 
 * 
 */
public class JobInexistantException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * Format du message de l'exception
    */
   protected static final String EXCEPTION_MESSAGE = "Impossible de lancer, de modifier ou de réserver le traitement n°{0} car il n''existe pas.";

   private final UUID jobRequestId;

   /**
    * 
    * @param jobRequestId
    *           identifiant du jobRequest qui n'existe pas
    */
   public JobInexistantException(UUID jobRequestId) {
      super();
      this.jobRequestId = jobRequestId;
   }

   /**
    * 
    * @return identifiant du job qui n'existe pas
    */
   public final UUID getInstanceId() {
      return this.jobRequestId;
   }

   /**
    * 
    * {@inheritDoc} <br>
    * <br>
    * Le message est formaté sur le modèle {@value #EXCEPTION_MESSAGE}
    * <ul>
    * <li>{0} : <code>identifiant du job qui n'existe pas</code></li>
    * </ul>
    * 
    * 
    */
   @Override
   public final String getMessage() {

      String message = MessageFormat.format(EXCEPTION_MESSAGE, jobRequestId);

      return message;
   }

}

package fr.urssaf.image.sae.pile.travaux.exception;

import java.text.MessageFormat;
import java.util.UUID;

/**
 * Erreur levée lorsqu'on essaie de lancer ou de réserver un jobRequest qui
 * existe déjà dans la pile des travaux.
 * 
 * 
 */
public class JobRequestAlreadyExistsException extends Exception {

   /**
    * SUID
    */
   private static final long serialVersionUID = 3038972663556810749L;

   /**
    * Format du message de l'exception
    */
   protected static final String EXCEPTION_MESSAGE = "Impossible de créer le traitement demandé car un traitement identique (n°{0}) existe déjà dans la pile des travaux.";

   /**
    * Identifiant du job
    */
   private final UUID jobRequestId;

   /**
    * 
    * @param jobRequestId
    *           identifiant du jobRequest qui n'existe pas
    */
   public JobRequestAlreadyExistsException(UUID jobRequestId) {
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

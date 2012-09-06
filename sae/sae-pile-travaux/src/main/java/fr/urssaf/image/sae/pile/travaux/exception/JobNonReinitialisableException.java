package fr.urssaf.image.sae.pile.travaux.exception;

import java.text.MessageFormat;
import java.util.UUID;

/**
 * Erreur levée lorsqu’une demande de réinitialisation de job est réalisée sur
 * un job dont l’état ne le permet pas
 * 
 * 
 */
public class JobNonReinitialisableException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * Format du message de l'exception
    */
   protected static final String EXCEPTION_MESSAGE = "L état du job n°{0} ne permet sa réinitialisation.";

   private final UUID jobRequestId;

   /**
    * 
    * @param jobRequestId
    *           identifiant du job déjà réservé
    * @param server
    *           nom du serveur qui a réservé le job
    */
   public JobNonReinitialisableException(UUID jobRequestId) {

      super();

      this.jobRequestId = jobRequestId;
   }

   /**
    * 
    * @return identifiant du job
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
    * <li>{0} : <code>identifiant du job déjà réservé</code></li>
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

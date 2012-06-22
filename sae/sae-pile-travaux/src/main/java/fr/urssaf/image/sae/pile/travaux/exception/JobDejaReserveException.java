package fr.urssaf.image.sae.pile.travaux.exception;

import java.text.MessageFormat;
import java.util.UUID;

/**
 * Erreur levée lorsqu'on essaie de réserver un traitement qui l'est déjà.
 * 
 * 
 */
public class JobDejaReserveException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * Format du message de l'exception
    */
   protected static final String EXCEPTION_MESSAGE = "Le traitement n°{0} est déjà réservé par le serveur ''{1}''.";

   private final UUID jobRequestId;

   private final String server;

   /**
    * 
    * @param jobRequestId
    *           identifiant du job déjà réservé
    * @param server
    *           nom du serveur qui a réservé le job
    */
   public JobDejaReserveException(UUID jobRequestId, String server) {

      super();

      this.jobRequestId = jobRequestId;
      this.server = server;
   }

   /**
    * 
    * @return identifiant du job déjà réservé
    */
   public final UUID getInstanceId() {
      return this.jobRequestId;
   }

   /**
    * 
    * @return nom du serveur qui a réservé le job
    */
   public final String getServer() {
      return this.server;
   }

   /**
    * 
    * {@inheritDoc} <br>
    * <br>
    * Le message est formaté sur le modèle {@value #EXCEPTION_MESSAGE}
    * <ul>
    * <li>{0} : <code>identifiant du job déjà réservé</code></li>
    * <li>{1} : <code>serveur qui a réservé le job</code></li>
    * </ul>
    * 
    * 
    */
   @Override
   public final String getMessage() {

      String message = MessageFormat.format(EXCEPTION_MESSAGE, jobRequestId,
            server);

      return message;
   }

}

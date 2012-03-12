package fr.urssaf.image.sae.ordonnanceur.exception;

import java.text.MessageFormat;

/**
 * Erreur levée lorsqu'on essaie de réserver un traitement qui l'est déjà.
 * 
 * 
 */
public class JobDejaReserveException extends Exception {

   private static final long serialVersionUID = 1L;

   private static final String EXCEPTION_MESSAGE = "Le traitement n°{0} est déjà réservé par le serveur ''{1}''.";

   private final long instanceId;

   private final String server;

   /**
    * 
    * @param instanceId
    *           identifiant du job déjà réservé
    * @param server
    *           nom du serveur qui a réservé le job
    */
   public JobDejaReserveException(long instanceId, String server) {

      super();

      this.instanceId = instanceId;
      this.server = server;
   }

   /**
    * 
    * @return identifiant du job déjà réservé
    */
   public final long getInstanceId() {
      return this.instanceId;
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

      String message = MessageFormat.format(EXCEPTION_MESSAGE, instanceId,
            server);

      return message;
   }

}

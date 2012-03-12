package fr.urssaf.image.sae.services.batch.exception;

import java.text.MessageFormat;

/**
 * Erreur levée lorsqu'on essaie de lancer un job qui n'existe pas
 * 
 * 
 */
public class JobInexistantException extends Exception {

   private static final long serialVersionUID = 1L;

   private static final String EXCEPTION_MESSAGE = "Impossible de lancer le traitement n°{0} car il n''existe pas.";

   private final long instanceId;

   /**
    * 
    * @param instanceId
    *           identifiant du job qui n'existe pas
    */
   public JobInexistantException(long instanceId) {
      super();
      this.instanceId = instanceId;
   }

   /**
    * 
    * @return identifiant du job qui n'existe pas
    */
   public final long getInstanceId() {
      return this.instanceId;
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

      String message = MessageFormat.format(EXCEPTION_MESSAGE, instanceId);

      return message;
   }

}

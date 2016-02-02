package fr.urssaf.image.sae.services.batch.exception;

import java.text.MessageFormat;

import fr.urssaf.image.sae.pile.travaux.model.JobRequest;

/**
 * Erreur levée lorsqu'on essaie de d'exécuter un job inattendu
 * 
 * 
 */
public class JobTypeInexistantException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   /**
    * Le format du message de l'exception
    */
   protected static final String EXCEPTION_MESSAGE = 
      "Traitement n° {0} - Le type de traitement ''{1}'' est inconnu.";

   private final JobRequest job;

   /**
    * 
    * @param job
    *           traitement avec un traitement inconnu
    * 
    * 
    */
   public JobTypeInexistantException(JobRequest job) {
      this.job = job;
   }

   /**
    * 
    * @return traitement avec un type inconnu
    */
   public final JobRequest getJob() {
      return this.job;
   }

   /**
    * 
    * {@inheritDoc} <br>
    * <br>
    * Le message est formaté sur le modèle {@value #EXCEPTION_MESSAGE}
    * <ul>
    * <li>{0} :
    * <code>identifiant du jobRequest avec un traitement inattendu</code></li>
    * <li>{1} : 
    * <code>type de traitement obtenu</code></li>
    * </ul>
    * 
    * 
    */
   @Override
   public final String getMessage() {

      String message = MessageFormat.format(
            EXCEPTION_MESSAGE, job.getIdJob(), job.getType());

      return message;
   }

}

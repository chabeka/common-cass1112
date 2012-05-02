package fr.urssaf.image.sae.services.batch.exception;

import java.text.MessageFormat;

import fr.urssaf.image.sae.pile.travaux.model.JobRequest;

/**
 * Erreur levée lorsqu'on essaie de d'exécuter un job inattendu
 * 
 * 
 */
public class JobInattenduException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   private static final String EXCEPTION_MESSAGE = "Le traitement n°{0} est inattendu. On attend un traitement de type ''{1}'' et le type est ''{2}''.";

   private final JobRequest job;

   private final String expectedType;

   /**
    * 
    * @param job
    *           traitement avec un traitement inattendu
    * @param expectedType
    *           type de traitement attendu
    * 
    * 
    */
   public JobInattenduException(JobRequest job, String expectedType) {
      super();
      this.job = job;
      this.expectedType = expectedType;
   }

   /**
    * 
    * @return traitement avec un traitement inattendu
    */
   public final JobRequest getJob() {
      return this.job;
   }

   /**
    * 
    * @return type de traitement attendu
    */
   public final String getExpectedType() {
      return this.expectedType;
   }

   /**
    * 
    * {@inheritDoc} <br>
    * <br>
    * Le message est formaté sur le modèle {@value #EXCEPTION_MESSAGE}
    * <ul>
    * <li>{0} :
    * <code>identifiant du jobRequest avec un traitement inattendu</code></li>
    * <li>{1} : <code>type de traitement attendu</code></li>
    * <li>{2} : <code>type de traitement obtenu</code></li>
    * </ul>
    * 
    * 
    */
   @Override
   public final String getMessage() {

      String message = MessageFormat.format(EXCEPTION_MESSAGE, job.getIdJob(),
            expectedType, job.getType());

      return message;
   }

}

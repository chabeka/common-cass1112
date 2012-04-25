package fr.urssaf.image.sae.ordonnanceur.exception;

import fr.urssaf.image.sae.pile.travaux.model.JobQueue;

/**
 * Exception levée lorsque que la réservation ou le lancement d'un traitement
 * pose problème
 * 
 * 
 */
public class JobRuntimeException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   private final JobQueue job;

   /**
    * @param job
    *           traitement de la pile des travaux causant une exception
    * @param cause
    *           exception levée par le traitement de la pile des travaux
    */
   public JobRuntimeException(JobQueue job, Throwable cause) {
      super(cause);
      this.job = job;
   }

   /**
    * 
    * @return traitement de la pile des travaux causant une exception
    */
   public final JobQueue getJob() {
      return this.job;
   }

}

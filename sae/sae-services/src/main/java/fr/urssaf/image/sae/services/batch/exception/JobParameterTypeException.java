package fr.urssaf.image.sae.services.batch.exception;

import java.text.MessageFormat;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;

/**
 * Erreur levée lorsque que les paramètres du job dans la pile des travaux ne
 * sont pas dans le type ou le format attendu lorsqu'on tente de désérialiser la
 * propriété {@link JobRequest#getParameters()}
 * 
 * 
 */
public class JobParameterTypeException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   /**
    * Le format du message de l'exception
    */
   protected static final String EXCEPTION_MESSAGE = "Le traitement n°{0} a des paramètres inattendu : ''{1}''.";

   private final JobRequest job;

   /**
    * 
    * @param job
    *           traitement avec des paramètres inattendu
    * @param cause
    *           exception levée
    * 
    * 
    */
   public JobParameterTypeException(JobRequest job, Throwable cause) {
      super(cause);
      this.job = job;

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
    * {@inheritDoc} <br>
    * <br>
    * Le message est formaté sur le modèle {@value #EXCEPTION_MESSAGE}
    * <ul>
    * <li>{0} :
    * <code>identifiant du jobRequest avec des paramètres inattendu</code></li>
    * <li>{1} : <code>paramètres inattendus</code></li>
    * </ul>
    * 
    * 
    */
   @Override
   public final String getMessage() {

      String message = StringUtils.EMPTY;
      if(StringUtils.isNotBlank(job.getParameters())){
         message = MessageFormat.format(EXCEPTION_MESSAGE, job.getIdJob(),
               job.getParameters());
      }else{
         if(MapUtils.isNotEmpty(job.getJobParameters())){
            message = MessageFormat.format(EXCEPTION_MESSAGE, job.getIdJob(),
                  job.getJobParameters().get(Constantes.ECDE_URL));
         }
      }
      
      return message;
   }

}

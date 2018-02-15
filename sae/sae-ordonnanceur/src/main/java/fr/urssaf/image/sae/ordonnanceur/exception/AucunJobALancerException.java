package fr.urssaf.image.sae.ordonnanceur.exception;

/**
 * Erreur levée lorsqu'on détermine quel job est à lancer, et qu'aucun n'est
 * trouvé.
 * 
 * 
 */
public class AucunJobALancerException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * Message de l'exception
    */
   protected static final String EXCEPTION_MESSAGE = "Il n'y a aucun traitement à lancer";

   /**
    * 
    * {@inheritDoc} <br>
    * <br>
    * Le message est formaté sur le modèle {@value #EXCEPTION_MESSAGE}
    * 
    * 
    */
   @Override
   public final String getMessage() {

      String message = EXCEPTION_MESSAGE;

      return message;
   }

}

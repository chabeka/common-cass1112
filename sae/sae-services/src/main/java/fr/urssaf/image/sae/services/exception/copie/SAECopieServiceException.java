package fr.urssaf.image.sae.services.exception.copie;

import fr.urssaf.image.sae.services.exception.SAEServiceException;

public class SAECopieServiceException extends SAEServiceException {

   private static final long serialVersionUID = 1L;

   /**
    * Le message de l'exception
    */
   protected static final String MSG_EXCEPTION = "Une exception a eu lieu dans la copie";

   /**
    * le message de l'exception est {@value #MSG_EXCEPTION}
    * 
    * @param cause
    *           cause de l'exception
    */
   public SAECopieServiceException(Throwable cause) {
      super(MSG_EXCEPTION, cause);
   }

   public SAECopieServiceException(String cause) {
      super(cause);
   }

}

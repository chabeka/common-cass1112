package fr.urssaf.image.sae.services.exception.capture;

import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;

/**
 * Encapsulation des exceptions levées non spécifiées <br>
 * L'exception hérite de {@link RuntimeException}
 * */
public class SAECaptureServiceRuntimeException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   /**
    * le message de l'exception est {@value #MSG_EXCEPTION}
    * 
    * @param cause
    *           cause de l'exception
    */
   public SAECaptureServiceRuntimeException(Throwable cause) {
      super(ResourceMessagesUtils
            .loadMessage("erreur.technique.capture.unitaire"), cause);
   }

}

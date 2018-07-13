package fr.urssaf.image.sae.anais.framework.service.exception;

/**
 * Exception d'encapsulation pour les exceptions ANAIS<br> 
 * @see {@link AnaisExceptionServerCommunication}
 * @see {@link AnaisExceptionAuthFailure}
 * @see {@link AnaisExceptionPwdExpired}
 * @see {@link AnaisExceptionPwdExpiring}
 * @see {@link AnaisExceptionAuthAccountLocked}
 * @see {@link AnaisExceptionAuthMultiUid}
 * @see {@link AnaisExceptionFailure}
 * @see {@link AnaisExceptionNoObject}
 */
public class SaeAnaisApiException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   /**
    * 
    * @param exception
    *           exception du Framework ANAIS
    */
   public SaeAnaisApiException(Exception exception) {
      super(exception);
   }

}

package fr.urssaf.image.sae.ecde.exception;

/**
 * Encapsulation des exceptions levées non spécifiées
 * <br>
 * L'exception hérite de {@link RuntimeException}
 * */
public class EcdeRuntimeException extends RuntimeException {

   private static final long serialVersionUID = 1L;
   
   /**
    * implémenation d'un message spécifique
    * 
    * @param throwable
    *           cause de l'exception levée
    */
   public EcdeRuntimeException(Throwable throwable) {
      super(throwable);
   }
   /**
    * implémenation d'un message spécifique
    * 
    * @param message
    *           message de l'exception levée
    * @param throwable
    *           cause de l'exception levée
    */
   public EcdeRuntimeException(String message, Throwable throwable) {
      super(message, throwable);
   }
   
   
   /**
    * Constructeur
    *  
    * @param message le message de l'exception
    */
   public EcdeRuntimeException(String message) {
      super(message);
   }

}

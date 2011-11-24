package fr.urssaf.image.sae.ecde.exception;

/**
 * Exception levée lorsqu'un sommaire a sa propriété batchMode<br>
 * différente de TOUT ou RIEN.
 * 
 * Exemple : Partiel -> Genere une exception
 * 
 * @see EcdeGeneralException
 * 
 * */
public class EcdeInvalidBatchModeException extends EcdeGeneralException {

   private static final long serialVersionUID = 1L;

   /**
    * Constructor
    * 
    *
    * @param message cause
    */
   public EcdeInvalidBatchModeException(String message) {
      super(message);
   }
   
   /**
    * Constructor
    * 
    *
    * @param message erreur
    * @param except cause
    */
   public EcdeInvalidBatchModeException(String message, Throwable except) {
      super(message, except);
   }
   
}

package fr.urssaf.image.commons.cassandra.exception;


/**
 * Exception levée lors d'un problème de configuration de la connection cassandra
 * 
 * 
 */
public class ModeGestionAPIUnkownException extends RuntimeException {

   private static final long serialVersionUID = 1L;


   /**
    * 
    * @param cause : Exception mère
    */
   public ModeGestionAPIUnkownException(Throwable cause) {
      super(cause);
   }

   /**
    * 
    * @param message : message a renvoyer
    */
   public ModeGestionAPIUnkownException(String message) {
      super(message);
   }
}

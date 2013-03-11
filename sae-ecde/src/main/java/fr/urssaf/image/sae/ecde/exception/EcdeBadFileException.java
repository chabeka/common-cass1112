package fr.urssaf.image.sae.ecde.exception;

/**
 * 
 * Exception levée lorsqu'un chemin de fichier d'un ECDE n'appartient à aucun ECDE connu
 * 
 * @see EcdeGeneralException
 * */

public class EcdeBadFileException extends EcdeGeneralException {

   private static final long serialVersionUID = 1L;
   
   /**
    * Constructor
    * 
    * @param message cause
    */
   public EcdeBadFileException(String message) {
      super(message);
   }
   
   /**
    * Constructor
    * 
    * @param message message
    * @param throwable cause
    */
   public EcdeBadFileException(String message, Throwable throwable) {
      super(message, throwable);
   }

}
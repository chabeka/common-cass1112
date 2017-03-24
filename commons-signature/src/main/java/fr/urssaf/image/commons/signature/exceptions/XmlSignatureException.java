package fr.urssaf.image.commons.signature.exceptions;

/**
 * Exception de signature XML<br>
 * <br>
 * Permet d'englober la foultitude d'exceptions qui peuvent être levées lors
 * d'une signature XML.
 *
 */
public class XmlSignatureException extends Exception {

   
   private static final long serialVersionUID = 2228588763306873059L;

   /**
    * Constructeur
    * 
    * @param cause exception cause
    */
   public XmlSignatureException(Throwable cause) {
      super(cause);
   }
   
   
}

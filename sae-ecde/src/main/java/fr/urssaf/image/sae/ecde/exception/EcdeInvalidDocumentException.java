package fr.urssaf.image.sae.ecde.exception;

/**
 * Exception levée lorsqu'un document du sommaire.xml n'est pas valide.
 * A savoir par exemple lorsqu'il est impossible d'ouvrir le fichier de l'objet numérique.
 * 
 *
 */
public class EcdeInvalidDocumentException extends EcdeGeneralException {
   
   private static final long serialVersionUID = 1L;

   /**
    * Constructor
    * 
    *
    * @param message cause
    */
   public EcdeInvalidDocumentException(String message) {
      super(message);
   }
   
   /**
    * Constructor
    * 
    *
    * @param message erreur
    * @param except cause
    */
   public EcdeInvalidDocumentException(String message, Throwable except) {
      super(message, except);
   }
}

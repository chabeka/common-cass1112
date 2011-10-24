package fr.urssaf.image.sae.ecde.exception;
/**
 * Classe exception appellée par la stratégie SommaireUnserializerStrategy
 * <br> 
 * Le sommaire n'a pas pu être désérialisé : erreur de structure, fichier corrompu,
 * <br>fichier introuvable, etc
 *
 */
public class EcdeBadSummaryException extends EcdeGeneralException {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   
   /**
    * Constructor
    * 
    * @param message cause
    */
   public EcdeBadSummaryException(String message) {
      super(message);
   }
   /**
    * Constructor
    * 
    * @param message message
    * @param throwable cause
    */
   public EcdeBadSummaryException(String message, Throwable throwable) {
      super(message, throwable);
   }

}

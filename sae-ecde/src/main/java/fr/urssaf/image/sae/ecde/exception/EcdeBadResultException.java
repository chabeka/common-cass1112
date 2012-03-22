package fr.urssaf.image.sae.ecde.exception;
/**
 * Classe exception appellée par la stratégie ResultatSerializerStrategy
 * <br> 
 * Le résultat n'a pas pu être sérialisé ou persisté.
 *
 */
public class EcdeBadResultException extends EcdeGeneralException {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   
   /**
    * Constructor
    * 
    * @param message cause
    */
   public EcdeBadResultException(String message) {
      super(message);
   }
   
   /**
    * Constructor
    * 
    * @param message erreur
    * @param except cause
    */
   public EcdeBadResultException(String message, Throwable except) {
      super(message, except);
   }

}


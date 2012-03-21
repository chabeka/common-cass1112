package fr.urssaf.image.commons.cassandra.spring.batch.idgenerator;

/**
 * Exception envoyée lorsque la tentative de création d'un ID a échouée
 *
 */
public class IdGeneratorException extends RuntimeException {

   /**
    * @see java.lang.RuntimeException
    * @param message    le message
    */
   public IdGeneratorException(String message) {
      super(message);
   }

   /**
    * @see java.lang.RuntimeException
    * @param message    le message
    * @param inner      l'inner exception
    */
   public IdGeneratorException(String message, Exception inner) {
      super(message, inner);
   }

   private static final long serialVersionUID = -6170112160731829564L;
   
}

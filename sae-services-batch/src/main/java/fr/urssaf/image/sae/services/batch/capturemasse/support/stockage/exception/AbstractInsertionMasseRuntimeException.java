/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.exception;

/**
 * Classe
 * 
 */
public abstract class AbstractInsertionMasseRuntimeException extends
      RuntimeException {

   private static final long serialVersionUID = 1L;

   private final int index;
   private final Exception cause;

   /**
    * constructeur
    * 
    * @param index
    *           index de l'erreur
    * @param cause
    *           erreur source
    */
   public AbstractInsertionMasseRuntimeException(int index, Exception cause) {
      super(cause);
      this.index = index;
      this.cause = cause;
   }

   /**
    * @return index dans le sommaire où l'insertion a échoué
    */
   public final int getIndex() {
      return index;
   }

   /**
    * @return cause de l'échec
    */
   @Override
   public final Exception getCause() {
      return cause;
   }

}

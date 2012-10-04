/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.exception;

/**
 * Exception levée lorsque le format d'une ligne est erroné
 * 
 */
public class LineFormatException extends RuntimeException {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   private final int indexLigne;

   /**
    * @param indexLigne
    *           index de la ligne en erreur
    */
   public LineFormatException(int indexLigne) {
      super();
      this.indexLigne = indexLigne;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String getMessage() {
      return "Erreur de format à la ligne " + indexLigne + "\n"
            + super.getMessage();
   }

}

/**
 * 
 */
package fr.urssaf.image.sae.metadata.exceptions;

import java.util.List;

/**
 * Le code long n'a pas été trouvé dans le référentiel des métadonnées
 * 
 */
public class LongCodeNotFoundException extends Exception {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   private List<String> listCode;

    
   /**
    * constructeur
    * 
    * @param message
    *           message d'erreur
    * @param listCode
    *           la liste des codes qui n'existent pas
    */
   public LongCodeNotFoundException(String message, List<String> listCode) {
      super(message);
      this.listCode = listCode;
   }

   /**
    * @return the listCode
    */
   public final List<String> getListCode() {
      return listCode;
   }

}

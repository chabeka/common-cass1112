package fr.urssaf.image.sae.metadata.exceptions;

import java.text.MessageFormat;

/**
 *Exception levée lorsque le dictionnaire des donnée n'existe pas.
 */
@SuppressWarnings("PMD")
public class DictionaryNotFoundException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur
    * 
    * @param dictName
    *           nom du dictionnaire
    */
   public DictionaryNotFoundException(String dictName) {
      super(MessageFormat.format("Le dictionnaire {0} n''a pas été trouvé",
            dictName));
   }

   /**
    * Constructeur
    * 
    * @param e
    *           exception levée
    */
   public DictionaryNotFoundException(Exception e) {
      super(e);
   }
}

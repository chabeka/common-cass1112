package fr.urssaf.image.sae.metadata.exceptions;

import java.text.MessageFormat;


/**
 *Exception levée lorsque le dictionnaire des donnée n'existe pas. 
 */
@SuppressWarnings("PMD")
public class DictionaryNotFoundException extends Exception {
   /**
    * Constructeur
    * 
    * @param dictName nom du dictionnaire
    */
   public DictionaryNotFoundException(String message){
      super(MessageFormat.format("Le dictionnaire {0} n''a pas été trouvé",
            message));
   }
   /**
    * Constructeur
    * @param e exception levée
    */
   public DictionaryNotFoundException(Exception e){
      super(e);
   }
}

package fr.urssaf.image.sae.metadata.exceptions;

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
      super(message);
   }
   /**
    * Constructeur
    * @param e exception levée
    */
   public DictionaryNotFoundException(Exception e){
      super(e);
   }
}

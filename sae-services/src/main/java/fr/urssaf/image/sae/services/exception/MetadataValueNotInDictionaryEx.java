package fr.urssaf.image.sae.services.exception;

/**
 * Exception levée lorsque la valeur de la métadonnée n'est pas en conformité avec les valeurs du dictionnaire de données.
 * 
 *
 */
public class MetadataValueNotInDictionaryEx extends Exception{
   
   /**
    * Constructeur
    * @param message message d'origine
    */
   public MetadataValueNotInDictionaryEx(String message){
      super(message);
   }
   
   /**
    * Constructeur
    * @param exception exception d'origine
    */
   public MetadataValueNotInDictionaryEx(Exception exception){
      super(exception);
   }
}

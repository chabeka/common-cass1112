package fr.urssaf.image.sae.services.exception;

/**
 * Exception levée lorsque la valeur de la métadonnée n'est pas en conformité avec les valeurs du dictionnaire de données.
 * 
 *
 */
public class ArchiveInexistanteEx extends Exception{
   
   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   /**
    * Constructeur
    * @param message message d'origine
    */
   public ArchiveInexistanteEx(String message){
      super(message);
   }
   
   /**
    * Constructeur
    * @param exception exception d'origine
    */
   public ArchiveInexistanteEx(Exception exception){
      super(exception);
   }
}

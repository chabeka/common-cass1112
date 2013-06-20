package fr.urssaf.image.sae.storage.dfce.exception;

public class MetadonneeInexistante extends Exception {
   private static final long serialVersionUID = 1L;


   /**
    * Constructeur
    * 
    * @param message
    *           message d'erreur
    */
   public MetadonneeInexistante(String message) {
      super(message);
   }
}

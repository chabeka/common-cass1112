package fr.urssaf.image.sae.storage.dfce.exception;

/**
 * Exception levée lorsqu'une métadonnée n'est pas trouvée dans DFCE alors qu'on
 * l'y attendait
 */
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

/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.exception;

/**
 * Erreur soulevée lors d'un problème d'accès à CASSANDRA
 * 
 */
public class DfceException extends Exception {

   private static final long serialVersionUID = -8421792929190463141L;

   /**
    * Constructeur
    * 
    * @param exception
    *           erreur mère
    */
   public DfceException(Exception exception) {
      super(exception);
   }

}

package fr.urssaf.image.sae.webservices.security.igc.exception;

/**
 * Exception levée lorsqu'une erreur se produit lors du chargement des
 * certificats des AC racine et des CRL
 * 
 * 
 */
public class LoadCertifsAndCrlException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * Implémentation du message et de la cause de l'exception
    * 
    * @param message
    *           message de l'exception
    * @param cause
    *           origine de l'exception
    */
   public LoadCertifsAndCrlException(String message, Throwable cause) {
      super(message, cause);
   }

}

package fr.urssaf.image.commons.dfce.exception;


/**
 * Exception levée lors de la configuration de DFCE
 * 
 * 
 */
public class DFCEConfigurationRuntimeException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   /**
    * 
    * @param cause
    *           exception par le chargement du fichier de configuration
    */
   public DFCEConfigurationRuntimeException(Throwable cause) {
      super(cause);

   }

}

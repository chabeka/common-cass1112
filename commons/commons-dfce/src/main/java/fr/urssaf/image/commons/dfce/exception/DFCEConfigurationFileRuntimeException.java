package fr.urssaf.image.commons.dfce.exception;

import java.io.File;
import java.text.MessageFormat;

/**
 * Exception levée lors du chargement du fichier de configuration DFCE
 * 
 * 
 */
public class DFCEConfigurationFileRuntimeException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   private static final String EXCEPTION_MESSAGE = "Il est impossible de charger la configuration ''{0}''.";

   private final File configuration;

   /**
    * 
    * @param configuration
    *           chemin complet de configuration
    * @param cause
    *           exception par le chargement du fichier de configuration
    */
   public DFCEConfigurationFileRuntimeException(File configuration,
         Throwable cause) {
      super(cause);
      this.configuration = configuration;
   }

   /**
    * 
    * {@inheritDoc} <br>
    * <br>
    * Le message est formaté sur le modèle {@value #EXCEPTION_MESSAGE}
    * <ul>
    * <li>{0} : <code>fichier de configuration lève l'exception lors du chargement</code></li>
    * </ul>
    * 
    */
   @Override
   public final String getMessage() {

      String message = MessageFormat.format(EXCEPTION_MESSAGE, configuration);

      return message;
   }

}

package fr.urssaf.image.commons.dfce.exception;

import java.text.MessageFormat;

/**
 * Exception levée lorsqu'un paramètre est introuvable ou que aucune valeur
 * n'est renseignée dans la configuration
 * 
 * 
 */
public class DFCEConfigurationParameterNotFoundRuntimeException extends
      RuntimeException {

   private static final long serialVersionUID = 1L;

   /**
    * Le format du message de l'exception
    */
   protected static final String EXCEPTION_MESSAGE = "Le paramètre ''{0}'' doit être obligatoirement renseigné.";

   private final String parameter;

   /**
    * 
    * @param parameter
    *           paramètre non trouvé dans la configuration
    */
   public DFCEConfigurationParameterNotFoundRuntimeException(String parameter) {
      super();
      this.parameter = parameter;

   }

   /**
    * 
    * {@inheritDoc} <br>
    * <br>
    * Le message est formaté sur le modèle {@value #EXCEPTION_MESSAGE}
    * <ul>
    * <li>{0} : <code>paramètre introuvable dans la configuration</code></li>
    * </ul>
    * 
    */
   @Override
   public final String getMessage() {

      String message = MessageFormat.format(EXCEPTION_MESSAGE, parameter);

      return message;
   }
}

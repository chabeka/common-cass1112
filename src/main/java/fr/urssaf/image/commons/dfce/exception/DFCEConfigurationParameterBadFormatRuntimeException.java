package fr.urssaf.image.commons.dfce.exception;

import java.text.MessageFormat;

/**
 * Exception levée lorsqu'un paramètre n'est pas au bon format dans la
 * configuration
 * 
 * 
 */
public class DFCEConfigurationParameterBadFormatRuntimeException extends
      RuntimeException {

   private static final long serialVersionUID = 1L;

   private static final String EXCEPTION_MESSAGE = "Le paramètre ''{0}'' n''est pas au bon format.";

   private final String parameter;

   /**
    * 
    * @param parameter
    *           paramètre avec un mauvais format dans la configuration
    */
   public DFCEConfigurationParameterBadFormatRuntimeException(String parameter) {
      super();
      this.parameter = parameter;

   }

   /**
    * 
    * {@inheritDoc} <br>
    * <br>
    * Le message est formaté sur le modèle {@value #EXCEPTION_MESSAGE}
    * <ul>
    * <li>{0} :
    * <code>paramètre avec un mauvais format dans la configuration</code></li>
    * </ul>
    * 
    */
   @Override
   public final String getMessage() {

      String message = MessageFormat.format(EXCEPTION_MESSAGE, parameter);

      return message;
   }
}

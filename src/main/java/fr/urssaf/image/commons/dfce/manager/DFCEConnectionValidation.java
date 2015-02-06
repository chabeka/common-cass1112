package fr.urssaf.image.commons.dfce.manager;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import fr.urssaf.image.commons.dfce.exception.DFCEConfigurationParameterBadFormatRuntimeException;
import fr.urssaf.image.commons.dfce.exception.DFCEConfigurationParameterNotFoundRuntimeException;

/**
 * Cette classe permet la validation des paramètres du fichier de configuration
 * DFCE.
 * <ul>
 * <li>
 * {@value fr.urssaf.image.commons.dfce.manager.DFCEConnectionParameter#DFCE_CONFIG}
 * : doit être renseigné</li>
 * <li>
 * {@value fr.urssaf.image.commons.dfce.manager.DFCEConnectionParameter#DFCE_PASSWORD}
 * : doit être renseigné</li>
 * <li>
 * {@value fr.urssaf.image.commons.dfce.manager.DFCEConnectionParameter#DFCE_HOSTNAME}
 * : doit être renseigné</li>
 * <li>
 * {@value fr.urssaf.image.commons.dfce.manager.DFCEConnectionParameter#DFCE_HOSTPORT}
 * : doit être renseigné et être un entier</li>
 * <li>
 * {@value fr.urssaf.image.commons.dfce.manager.DFCEConnectionParameter#DFCE_CONTEXTROOT}
 * : doit être renseigné</li>
 * <li>
 * {@value fr.urssaf.image.commons.dfce.manager.DFCEConnectionParameter#DFCE_SECURE}
 * : doit être renseigné</li>
 * </ul>
 * 
 * 
 */
public final class DFCEConnectionValidation {

   private DFCEConnectionValidation() {

   }

   /**
    * Validation des paramètres de la connexion DFCE
    * 
    * @param dfceProperties
    *           configuration DFCE
    */
   public static void validate(Properties dfceProperties) {

      // DFCE_LOGIN
      String loginValue = dfceProperties
            .getProperty(DFCEConnectionParameter.DFCE_LOGIN);
      validateRequired(loginValue, DFCEConnectionParameter.DFCE_LOGIN);

      // DFCE_PASSWORD
      String passwordValue = dfceProperties
            .getProperty(DFCEConnectionParameter.DFCE_PASSWORD);
      validateRequired(passwordValue, DFCEConnectionParameter.DFCE_PASSWORD);

      // HOSTNAME
      String hostName = dfceProperties
            .getProperty(DFCEConnectionParameter.DFCE_HOSTNAME);
      validateRequired(hostName, DFCEConnectionParameter.DFCE_HOSTNAME);

      // HOSTPORT
      String hostPort = dfceProperties
            .getProperty(DFCEConnectionParameter.DFCE_HOSTPORT);
      validateRequired(hostPort, DFCEConnectionParameter.DFCE_HOSTPORT);
      validateInteger(hostPort, DFCEConnectionParameter.DFCE_HOSTPORT);

      // CONTEXTROOT
      String contextRoot = dfceProperties
            .getProperty(DFCEConnectionParameter.DFCE_CONTEXTROOT);
      validateRequired(contextRoot, DFCEConnectionParameter.DFCE_CONTEXTROOT);

      // SECURE
      String secure = dfceProperties
            .getProperty(DFCEConnectionParameter.DFCE_SECURE);
      validateRequired(secure, DFCEConnectionParameter.DFCE_SECURE);

   }

   private static void validateRequired(String value, String parameter) {

      if (StringUtils.isBlank(value)) {
         throw new DFCEConfigurationParameterNotFoundRuntimeException(parameter);
      }

   }

   private static void validateInteger(String value, String parameter) {

      if (!NumberUtils.isDigits(value)) {
         throw new DFCEConfigurationParameterBadFormatRuntimeException(
               parameter);
      }

   }
}

package fr.urssaf.image.commons.dfce.factory;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import fr.urssaf.image.commons.dfce.exception.DFCEConfigurationFileRuntimeException;
import fr.urssaf.image.commons.dfce.exception.DFCEConfigurationParameterRuntimeException;
import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.commons.dfce.util.PropertiesUtils;

/**
 * Classe d'instanciation d'objet de type {@link DFCEConnection}
 * 
 * 
 */
public final class DFCEConnectionFactory {

   private DFCEConnectionFactory() {

   }

   /**
    * Paramètre indiquant le paramètre de configuration du chemin complet du
    * fichier de configuration de DFCE
    */
   public static final String DFCE_CONFIG = "sae.dfce.cheminFichierConfig";

   /**
    * Paramètre indiquant le paramètre de configuration du login de connexion à
    * DFCE
    */
   public static final String DFCE_LOGIN = "db.login";

   /**
    * Paramètre indiquant le paramètre de configuration du mot de passe de
    * connexion à DFCE
    */
   public static final String DFCE_PASSWORD = "db.password";

   /**
    * Paramètre indiquant le paramètre de configuration l'URL de connexion à
    * DFCE
    */
   public static final String DFCE_SERVER_URL = "db.serverUrl";

   /**
    * 
    * @param saeConfiguration
    *           chemin complet du fichier de configuration générale
    * @return configuration DFCE
    */
   public static DFCEConnection createDFCEConnectionBySAEConfiguration(
         File saeConfiguration) {

      Validate.notNull(saeConfiguration, "'saeConfiguration' is required");

      Properties saeProperties;
      try {
         saeProperties = PropertiesUtils.load(saeConfiguration);
      } catch (IOException e) {
         throw new DFCEConfigurationFileRuntimeException(saeConfiguration, e);
      }

      DFCEConnection dfceConnection = createDFCEConnectionBySAEConfiguration(saeProperties);

      return dfceConnection;
   }

   /**
    * 
    * @param saeProperties
    *           propriétés de la configuration générale
    * @return configuration DFCE
    */
   public static DFCEConnection createDFCEConnectionBySAEConfiguration(
         Properties saeProperties) {

      // récupération de la valeur du chemin complet du fichier de configuration
      String dfceConfigResource = saeProperties.getProperty(DFCE_CONFIG);

      if (StringUtils.isBlank(dfceConfigResource)) {
         throw new DFCEConfigurationParameterRuntimeException(DFCE_CONFIG);
      }

      DFCEConnection dfceConnection = createDFCEConnectionByDFCEConfiguration(new File(
            dfceConfigResource));

      return dfceConnection;
   }

   /**
    * 
    * @param dfceConfiguration
    *           chemin complet du fichier de configuration de DFCE
    * @return configuration DFCE
    */
   public static DFCEConnection createDFCEConnectionByDFCEConfiguration(
         File dfceConfiguration) {

      Validate.notNull(dfceConfiguration, "'dfceConfiguration' is required");

      Properties dfceProperties;
      try {
         dfceProperties = PropertiesUtils.load(dfceConfiguration);
      } catch (IOException e) {
         throw new DFCEConfigurationFileRuntimeException(dfceConfiguration, e);
      }

      DFCEConnection dfceConnection = createDFCEConnectionByDFCEConfiguration(dfceProperties);

      return dfceConnection;

   }

   /**
    * 
    * @param dfceProperties
    *           propriétés de la configuration de DFCE
    * @return configuration DFCE
    */
   public static DFCEConnection createDFCEConnectionByDFCEConfiguration(
         Properties dfceProperties) {

      DFCEConnection dfceConnection = new DFCEConnection();

      // récupération de la valeur du login
      String loginValue = dfceProperties.getProperty(DFCE_LOGIN);

      if (StringUtils.isBlank(loginValue)) {
         throw new DFCEConfigurationParameterRuntimeException(DFCE_LOGIN);
      }

      dfceConnection.setLogin(loginValue);

      // récupération de la valeur du password
      String passwordValue = dfceProperties.getProperty(DFCE_PASSWORD);

      if (StringUtils.isBlank(passwordValue)) {
         throw new DFCEConfigurationParameterRuntimeException(DFCE_PASSWORD);
      }

      dfceConnection.setPassword(passwordValue);

      // récupération de la valeur de l'URL
      String serverUrlValue = dfceProperties.getProperty(DFCE_SERVER_URL);

      if (StringUtils.isBlank(serverUrlValue)) {
         throw new DFCEConfigurationParameterRuntimeException(DFCE_SERVER_URL);
      }

      dfceConnection.setServerUrl(serverUrlValue);

      return dfceConnection;

   }

}

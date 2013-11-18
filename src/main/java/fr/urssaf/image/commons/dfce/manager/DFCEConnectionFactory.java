package fr.urssaf.image.commons.dfce.manager;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.core.io.AbstractResource;

import fr.urssaf.image.commons.dfce.exception.DFCEConfigurationFileRuntimeException;
import fr.urssaf.image.commons.dfce.exception.DFCEConfigurationParameterNotFoundRuntimeException;
import fr.urssaf.image.commons.dfce.exception.DFCEConfigurationRuntimeException;
import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.commons.dfce.util.PropertiesUtils;
import fr.urssaf.image.commons.dfce.util.UrlUtils;

/**
 * Classe d'instanciation d'objet de type {@link DFCEConnection}
 * 
 * 
 */
public final class DFCEConnectionFactory {

   private DFCEConnectionFactory() {

   }

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
      String dfceConfigResource = saeProperties
            .getProperty(DFCEConnectionParameter.DFCE_CONFIG);

      if (StringUtils.isBlank(dfceConfigResource)) {
         throw new DFCEConfigurationParameterNotFoundRuntimeException(
               DFCEConnectionParameter.DFCE_CONFIG);
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

      // validation de la configuration
      DFCEConnectionValidation.validate(dfceProperties);

      DFCEConnection dfceConnection = new DFCEConnection();

      // récupération de la valeur du login
      String loginValue = dfceProperties
            .getProperty(DFCEConnectionParameter.DFCE_LOGIN);
      dfceConnection.setLogin(loginValue);

      // récupération de la valeur du password
      String passwordValue = dfceProperties
            .getProperty(DFCEConnectionParameter.DFCE_PASSWORD);
      dfceConnection.setPassword(passwordValue);

      // récupération de la valeur de l'URL

      // HOSTNAME
      String hostName = dfceProperties
            .getProperty(DFCEConnectionParameter.DFCE_HOSTNAME);

      // HOSTPORT
      int hostPort = Integer.parseInt(dfceProperties
            .getProperty(DFCEConnectionParameter.DFCE_HOSTPORT));

      // CONTEXTROOT
      String contextRoot = dfceProperties
            .getProperty(DFCEConnectionParameter.DFCE_CONTEXTROOT);

      // SECURE
      boolean secure = Boolean.parseBoolean(dfceProperties
            .getProperty(DFCEConnectionParameter.DFCE_SECURE));

      URL serverUrl;
      try {
         serverUrl = UrlUtils
               .createURL(hostName, hostPort, contextRoot, secure);
      } catch (MalformedURLException e) {
         throw new DFCEConfigurationRuntimeException(e);
      }

      dfceConnection.setServerUrl(serverUrl);

      return dfceConnection;

   }

   /**
    * Instancie un objet DFCEConnection en allant chercher les paramètres de
    * configuration à DFCE dans un objet de type Resource qui lui-même a été
    * chargé à partir d'un fichier properties.
    * 
    * @param dfceConfigurationResource
    *           l'objet Resource contenant le fichier de properties DFCE
    * @return configuration DFCE
    */
   public static DFCEConnection createDFCEConnectionByDFCEConfigurationResource(
         AbstractResource dfceConfigurationResource) {

      Validate.notNull(dfceConfigurationResource,
            "'dfceConfigurationResource' is required");

      Properties dfceProperties;
      try {
         dfceProperties = PropertiesUtils.load(dfceConfigurationResource
               .getInputStream());
      } catch (IOException e) {
         throw new DFCEConfigurationRuntimeException(e);
      }

      DFCEConnection dfceConnection = createDFCEConnectionByDFCEConfiguration(dfceProperties);

      return dfceConnection;

   }

}

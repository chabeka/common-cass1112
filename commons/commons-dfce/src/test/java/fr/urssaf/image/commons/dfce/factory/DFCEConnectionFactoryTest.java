package fr.urssaf.image.commons.dfce.factory;

import java.io.File;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import fr.urssaf.image.commons.dfce.exception.DFCEConfigurationFileRuntimeException;
import fr.urssaf.image.commons.dfce.exception.DFCEConfigurationParameterRuntimeException;
import fr.urssaf.image.commons.dfce.model.DFCEConnection;

@SuppressWarnings("PMD.MethodNamingConventions")
public class DFCEConnectionFactoryTest {

   private static final String LOGIN_VALUE = "_ADMIN";

   private static final String PASSWORD_VALUE = "DOCUBASE";

   private static final String URL_VALUE = "http://cer69-ds4int:8080/dfce-webapp/toolkit/";

   private static final String EXPECTED_MESSAGE = "le message de l'exception est inattendu";

   @Test
   public void createDFCEConnectionBySAEConfiguration_success() {

      File dfceConfiguration = new File(
            "src\\test\\resources\\config\\sae-config-test.properties");

      DFCEConnection dfceConnection = DFCEConnectionFactory
            .createDFCEConnectionBySAEConfiguration(dfceConfiguration);

      Assert.assertEquals("la valeur du login est inattendue", LOGIN_VALUE,
            dfceConnection.getLogin());

      Assert.assertEquals("la valeur du password est inattendue",
            PASSWORD_VALUE, dfceConnection.getPassword());

      Assert.assertEquals("la valeur de l'url est inattendue", URL_VALUE,
            dfceConnection.getServerUrl());
   }

   @Test
   public void createDFCEConnectionBySAEConfiguration_failure() {

      File saeConfiguration = new File(
            "src\\test\\resources\\config\\notfound.properties");

      try {
         DFCEConnectionFactory
               .createDFCEConnectionBySAEConfiguration(saeConfiguration);

         Assert
               .fail("Une exception DFCEConfigurationFileRuntimeException doit être levée");

      } catch (DFCEConfigurationFileRuntimeException e) {

         Assert.assertEquals(EXPECTED_MESSAGE,
               "Il est impossible de charger la configuration '"
                     + saeConfiguration + "'.", e.getMessage());
      }

   }

   @Test
   public void createDFCEConnectionBySAEConfiguration_failure_dfceconfig() {

      Properties saeProperties = new Properties();
      saeProperties.setProperty(DFCEConnectionFactory.DFCE_CONFIG, " ");

      try {
         DFCEConnectionFactory
               .createDFCEConnectionBySAEConfiguration(saeProperties);

         Assert
               .fail("Une exception DFCEConfigurationFileRuntimeException doit être levée");

      } catch (DFCEConfigurationParameterRuntimeException e) {

         Assert.assertEquals(EXPECTED_MESSAGE, "Le paramètre '"
               + DFCEConnectionFactory.DFCE_CONFIG
               + "' doit être obligatoirement renseigné.", e.getMessage());
      }

   }

   @Test
   public void createDFCEConnectionByDFCEConfiguration_success() {

      File dfceConfiguration = new File(
            "src\\test\\resources\\config\\dfce-config-test.properties");

      DFCEConnection dfceConnection = DFCEConnectionFactory
            .createDFCEConnectionByDFCEConfiguration(dfceConfiguration);

      Assert.assertEquals("la valeur du login est inattendue", LOGIN_VALUE,
            dfceConnection.getLogin());

      Assert.assertEquals("la valeur du password est inattendue",
            PASSWORD_VALUE, dfceConnection.getPassword());

      Assert.assertEquals("la valeur de l'url est inattendue", URL_VALUE,
            dfceConnection.getServerUrl());
   }

   @Test
   public void createDFCEConnectionByDFCEConfiguration_failure_fileNotFound() {

      File dfceConfiguration = new File(
            "src\\test\\resources\\config\\notfound.properties");

      try {
         DFCEConnectionFactory
               .createDFCEConnectionByDFCEConfiguration(dfceConfiguration);

         Assert
               .fail("Une exception DFCEConfigurationFileRuntimeException doit être levée");

      } catch (DFCEConfigurationFileRuntimeException e) {

         Assert.assertEquals(EXPECTED_MESSAGE,
               "Il est impossible de charger la configuration '"
                     + dfceConfiguration + "'.", e.getMessage());
      }

   }

   @Test
   public void createDFCEConnectionByDFCEConfiguration_failure_login() {

      Properties dfceProperties = new Properties();
      dfceProperties.setProperty(DFCEConnectionFactory.DFCE_LOGIN, " ");
      dfceProperties.setProperty(DFCEConnectionFactory.DFCE_PASSWORD,
            PASSWORD_VALUE);
      dfceProperties.setProperty(DFCEConnectionFactory.DFCE_SERVER_URL,
            URL_VALUE);

      assertParameter(dfceProperties, DFCEConnectionFactory.DFCE_LOGIN);

   }

   @Test
   public void createDFCEConnectionByDFCEConfiguration_failure_password() {

      Properties dfceProperties = new Properties();
      dfceProperties.setProperty(DFCEConnectionFactory.DFCE_LOGIN, LOGIN_VALUE);
      dfceProperties.setProperty(DFCEConnectionFactory.DFCE_PASSWORD, " ");
      dfceProperties.setProperty(DFCEConnectionFactory.DFCE_SERVER_URL,
            URL_VALUE);

      assertParameter(dfceProperties, DFCEConnectionFactory.DFCE_PASSWORD);

   }

   @Test
   public void createDFCEConnectionByDFCEConfiguration_failure_url() {

      Properties dfceProperties = new Properties();
      dfceProperties.setProperty(DFCEConnectionFactory.DFCE_LOGIN, LOGIN_VALUE);
      dfceProperties.setProperty(DFCEConnectionFactory.DFCE_PASSWORD,
            PASSWORD_VALUE);
      dfceProperties.setProperty(DFCEConnectionFactory.DFCE_SERVER_URL, " ");

      assertParameter(dfceProperties, DFCEConnectionFactory.DFCE_SERVER_URL);

   }

   private void assertParameter(Properties dfceProperties, String property) {

      try {
         DFCEConnectionFactory
               .createDFCEConnectionByDFCEConfiguration(dfceProperties);

         Assert
               .fail("Une exception DFCEConfigurationParameterRuntimeException doit être levée");

      } catch (DFCEConfigurationParameterRuntimeException e) {

         Assert.assertEquals(EXPECTED_MESSAGE, "Le paramètre '" + property
               + "' doit être obligatoirement renseigné.", e.getMessage());
      }
   }
}

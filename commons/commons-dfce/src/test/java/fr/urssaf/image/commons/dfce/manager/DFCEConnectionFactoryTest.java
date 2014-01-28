package fr.urssaf.image.commons.dfce.manager;

import java.io.File;
import java.util.Properties;

import org.apache.commons.lang.ObjectUtils;
import org.junit.Assert;
import org.junit.Test;

import fr.urssaf.image.commons.dfce.exception.DFCEConfigurationFileRuntimeException;
import fr.urssaf.image.commons.dfce.exception.DFCEConfigurationParameterBadFormatRuntimeException;
import fr.urssaf.image.commons.dfce.exception.DFCEConfigurationParameterNotFoundRuntimeException;
import fr.urssaf.image.commons.dfce.model.DFCEConnection;

@SuppressWarnings("PMD.MethodNamingConventions")
public class DFCEConnectionFactoryTest {

   private static final String LOGIN_VALUE = "_ADMIN";

   private static final String PASSWORD_VALUE = "DOCUBASE";

   private static final String URL_VALUE = "http://cer69-ds4int:8080/dfce-webapp/toolkit/";

   private static final String URL_SECURE_VALUE = "https://cer69-ds4int:8080/dfce-webapp/toolkit/";

   private static final String EXPECTED_MESSAGE = "le message de l'exception est inattendu";

   @Test
   public void createDFCEConnectionBySAEConfiguration_success() {

      File dfceConfiguration = new File(
            "src/test/resources/config/sae-config-test.properties");

      DFCEConnection dfceConnection = DFCEConnectionFactory
            .createDFCEConnectionBySAEConfiguration(dfceConfiguration);

      Assert.assertEquals("la valeur du login est inattendue", LOGIN_VALUE,
            dfceConnection.getLogin());

      Assert.assertEquals("la valeur du password est inattendue",
            PASSWORD_VALUE, dfceConnection.getPassword());

      Assert.assertEquals("la valeur de l'url est inattendue", URL_VALUE,
            ObjectUtils.toString(dfceConnection.getServerUrl()));

      Assert.assertEquals("la valeur du timeout est inattendue", 30000,
            dfceConnection.getTimeout());
   }

   @Test
   public void createDFCEConnectionBySAEConfiguration_failure() {

      File saeConfiguration = new File(
            "src/test/resources/config/notfound.properties");

      try {
         DFCEConnectionFactory
               .createDFCEConnectionBySAEConfiguration(saeConfiguration);

         Assert.fail("Une exception DFCEConfigurationFileRuntimeException doit être levée");

      } catch (DFCEConfigurationFileRuntimeException e) {

         Assert.assertEquals(EXPECTED_MESSAGE,
               "Il est impossible de charger la configuration '"
                     + saeConfiguration + "'.", e.getMessage());
      }

   }

   @Test
   public void createDFCEConnectionBySAEConfiguration_failure_dfceconfig() {

      Properties saeProperties = new Properties();
      saeProperties.setProperty(DFCEConnectionParameter.DFCE_CONFIG, " ");

      try {
         DFCEConnectionFactory
               .createDFCEConnectionBySAEConfiguration(saeProperties);

         Assert.fail("Une exception DFCEConfigurationFileRuntimeException doit être levée");

      } catch (DFCEConfigurationParameterNotFoundRuntimeException e) {

         Assert.assertEquals(EXPECTED_MESSAGE, "Le paramètre '"
               + DFCEConnectionParameter.DFCE_CONFIG
               + "' doit être obligatoirement renseigné.", e.getMessage());
      }

   }

   @Test
   public void createDFCEConnectionByDFCEConfiguration_success() {

      File dfceConfiguration = new File(
            "src/test/resources/config/dfce-config-test.properties");

      DFCEConnection dfceConnection = DFCEConnectionFactory
            .createDFCEConnectionByDFCEConfiguration(dfceConfiguration);

      Assert.assertEquals("la valeur du login est inattendue", LOGIN_VALUE,
            dfceConnection.getLogin());

      Assert.assertEquals("la valeur du password est inattendue",
            PASSWORD_VALUE, dfceConnection.getPassword());

      Assert.assertEquals("la valeur de l'url est inattendue", URL_VALUE,
            ObjectUtils.toString(dfceConnection.getServerUrl()));

      Assert.assertEquals("la valeur du timeout est inattendue", 30000,
            dfceConnection.getTimeout());
   }

   @Test
   public void createDFCEConnectionByDFCEConfiguration_success_secure() {

      File dfceConfiguration = new File(
            "src/test/resources/config/dfce-config-secure-test.properties");

      DFCEConnection dfceConnection = DFCEConnectionFactory
            .createDFCEConnectionByDFCEConfiguration(dfceConfiguration);

      Assert.assertEquals("la valeur du login est inattendue", LOGIN_VALUE,
            dfceConnection.getLogin());

      Assert.assertEquals("la valeur du password est inattendue",
            PASSWORD_VALUE, dfceConnection.getPassword());

      Assert.assertEquals("la valeur de l'url est inattendue",
            URL_SECURE_VALUE,
            ObjectUtils.toString(dfceConnection.getServerUrl()));

      Assert.assertEquals("la valeur du timeout est inattendue", 30000,
            dfceConnection.getTimeout());
   }

   @Test(expected = DFCEConfigurationParameterBadFormatRuntimeException.class)
   public void createDFCEConnectionByDFCEConfiguration_failure() {

      File dfceConfiguration = new File(
            "src/test/resources/config/dfce-config-failure-test.properties");

      DFCEConnectionFactory
            .createDFCEConnectionByDFCEConfiguration(dfceConfiguration);

   }

   @Test
   public void createDFCEConnectionByDFCEConfiguration_failure_fileNotFound() {

      File dfceConfiguration = new File(
            "src/test/resources/config/notfound.properties");

      try {
         DFCEConnectionFactory
               .createDFCEConnectionByDFCEConfiguration(dfceConfiguration);

         Assert.fail("Une exception DFCEConfigurationFileRuntimeException doit être levée");

      } catch (DFCEConfigurationFileRuntimeException e) {

         Assert.assertEquals(EXPECTED_MESSAGE,
               "Il est impossible de charger la configuration '"
                     + dfceConfiguration + "'.", e.getMessage());
      }

   }

}

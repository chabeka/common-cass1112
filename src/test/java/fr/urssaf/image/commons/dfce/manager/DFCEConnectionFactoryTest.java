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
   private static final String URL_VALUE_TRANSFERT = "http://cer69-saeint3:8080/dfce-webapp/toolkit/";

   private static final String URL_SECURE_VALUE = "https://cer69-ds4int:8080/dfce-webapp/toolkit/";

   private static final String EXPECTED_MESSAGE = "le message de l'exception est inattendu";

   private static final String HOST_NAME = "cer69-ds4int";
   private static final String HOST_NAME_TRANSFERT = "cer69-saeint3";

   private static final int HOST_PORT = 8080;

   private static final String CONTEXT_ROOT = "/dfce-webapp/toolkit/";

   private static final boolean SECURE = false;

   private static final int TIMEOUT = 30000;

   private static final String URL_TOOLKIT = "http://cer69-ds4int.cer69.recouv:8080/dfce-webapp/toolkit/";
   private static final String URL_TOOLKIT_TRANSFERT = "http://cer69-saeint3.cer69.recouv:8080/dfce-webapp/toolkit/";

   private static final boolean CHECK_HASH = true;

   private static final String DIGEST_ALGO = "SHA-1";

   private static final String BASE_NAME = "SAE-TEST";
   private static final String BASE_NAME_TRANSFERT = "SAE-INT";
   
   private final static String CHEMIN_SAE_CONFIG = "src/test/resources/config/sae-config-test.properties";
   private final static String CHEMIN_DFCE_CONFIG = "src/test/resources/config/dfce-config-test.properties";
   
   
   private void assert_createDFCEConnectionTestConfiguration(DFCEConnection dfceConnection, Boolean transfert){
      
      if(!transfert){
         Assert.assertEquals("la valeur du hostname est inattendue", HOST_NAME,
               dfceConnection.getHostName());
         
         Assert.assertEquals("la valeur de l'url est inattendue", URL_VALUE,
               ObjectUtils.toString(dfceConnection.getServerUrl()));
         
         Assert.assertEquals("la valeur de l'url toolkit est inattendue",
               URL_TOOLKIT, dfceConnection.getUrlToolkit());
         
         Assert.assertEquals("la valeur de baseName est inattendue", BASE_NAME,
               dfceConnection.getBaseName());
      } else {
         Assert.assertEquals("la valeur du hostname est inattendue", HOST_NAME_TRANSFERT,
               dfceConnection.getHostName());
         
         Assert.assertEquals("la valeur de l'url est inattendue", URL_VALUE_TRANSFERT,
               ObjectUtils.toString(dfceConnection.getServerUrl()));
         
         Assert.assertEquals("la valeur de l'url toolkit est inattendue",
               URL_TOOLKIT_TRANSFERT, dfceConnection.getUrlToolkit());
         
         Assert.assertEquals("la valeur de baseName est inattendue", BASE_NAME_TRANSFERT,
               dfceConnection.getBaseName());
      }
      
      Assert.assertEquals("la valeur du login est inattendue", LOGIN_VALUE,
            dfceConnection.getLogin());
      
      Assert.assertEquals("la valeur du password est inattendue",
            PASSWORD_VALUE, dfceConnection.getPassword());
      
      Assert.assertEquals("la valeur du timeout est inattendue", TIMEOUT,
            dfceConnection.getTimeout());
      
      Assert.assertEquals("la valeur du hostport est inattendue", HOST_PORT,
            dfceConnection.getHostPort());
      
      Assert.assertEquals("la valeur du contextroot est inattendue",
            CONTEXT_ROOT, dfceConnection.getContextRoot());
      
      Assert.assertEquals("la valeur de checkHash est inattendue", CHECK_HASH,
            dfceConnection.isCheckHash());
      
      Assert.assertEquals("la valeur de digestAlgo est inattendue",
            DIGEST_ALGO, dfceConnection.getDigestAlgo());
   }
   

   @Test
   public void createDFCEConnectionBySAEConfiguration_success() {
      
      File saeConfiguration = new File(CHEMIN_SAE_CONFIG);

      //-- Instancie une connection DFCE au serveur GNS 
      DFCEConnection dfceConnection = DFCEConnectionFactory
            .createDFCEConnectionBySAEConfiguration(saeConfiguration);
      
      assert_createDFCEConnectionTestConfiguration(dfceConnection, false);
   }
   

   @Test
   public void createDFCEConnectionBySAEConfiguration_failure() {
      File saeConfiguration = new File("src/test/resources/config/notfound.properties");
      try {
         DFCEConnectionFactory.createDFCEConnectionBySAEConfiguration(saeConfiguration);
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

      File dfceConfiguration = new File(CHEMIN_DFCE_CONFIG);

      DFCEConnection dfceConnection = DFCEConnectionFactory
            .createDFCEConnectionByDFCEConfiguration(dfceConfiguration);

      assert_createDFCEConnectionTestConfiguration(dfceConnection, false);
   }
   
   @Test
   public void createDFCEConnectionTransfertBySAEConfiguration_success() {
      
      File dfceConfiguration = new File(CHEMIN_SAE_CONFIG);
      
      DFCEConnection dfceConnection = DFCEConnectionFactory
         .createDFCEConnectionTransfertBySAEConfiguration(dfceConfiguration);
      
      assert_createDFCEConnectionTestConfiguration(dfceConnection, true);
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
            URL_SECURE_VALUE, ObjectUtils.toString(dfceConnection
                  .getServerUrl()));

      Assert.assertEquals("la valeur du timeout est inattendue", TIMEOUT,
            dfceConnection.getTimeout());

      Assert.assertEquals("la valeur du hostname est inattendue", HOST_NAME,
            dfceConnection.getHostName());

      Assert.assertEquals("la valeur du hostport est inattendue", HOST_PORT,
            dfceConnection.getHostPort());

      Assert.assertEquals("la valeur du contextroot est inattendue",
            CONTEXT_ROOT, dfceConnection.getContextRoot());

      Assert.assertEquals("la valeur secure est inattendue", SECURE,
            dfceConnection.getSecure());

      Assert.assertEquals("la valeur de l'url toolkit est inattendue",
            URL_TOOLKIT, dfceConnection.getUrlToolkit());

      Assert.assertEquals("la valeur de checkHash est inattendue", CHECK_HASH,
            dfceConnection.isCheckHash());

      Assert.assertEquals("la valeur de digestAlgo est inattendue",
            DIGEST_ALGO, dfceConnection.getDigestAlgo());

      Assert.assertEquals("la valeur de baseName est inattendue", BASE_NAME,
            dfceConnection.getBaseName());
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

      File dfceConfiguration = new File("src/test/resources/config/notfound.properties");
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
}

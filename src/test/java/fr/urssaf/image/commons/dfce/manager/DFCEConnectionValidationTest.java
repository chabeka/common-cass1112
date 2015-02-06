package fr.urssaf.image.commons.dfce.manager;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import fr.urssaf.image.commons.dfce.exception.DFCEConfigurationParameterBadFormatRuntimeException;
import fr.urssaf.image.commons.dfce.exception.DFCEConfigurationParameterNotFoundRuntimeException;

@SuppressWarnings("PMD.MethodNamingConventions")
public class DFCEConnectionValidationTest {

   private static final String LOGIN_VALUE = "_ADMIN";

   private static final String PASSWORD_VALUE = "DOCUBASE";

   private static final String EXPECTED_MESSAGE = "le message de l'exception est inattendu";

   private static final String HOSTNAME_VALUE = "cer69-ds4int";

   private static final String HOSTPORT_VALUE = "8080";

   private static final String CONTEXTROOT_VALUE = "dfce-webapp/toolkit";

   private static final String SECURE_VALUE = "true";

   @Test
   public void validate_failure_empty_login() {

      Properties dfceProperties = createDfceProperties(
            DFCEConnectionParameter.DFCE_LOGIN, " ");

      assertRequired(dfceProperties, DFCEConnectionParameter.DFCE_LOGIN);

   }

   @Test
   public void validate_failure_empty_password() {

      Properties dfceProperties = createDfceProperties(
            DFCEConnectionParameter.DFCE_PASSWORD, " ");

      assertRequired(dfceProperties, DFCEConnectionParameter.DFCE_PASSWORD);

   }

   @Test
   public void validate_failure_empty_port() {

      Properties dfceProperties = createDfceProperties(
            DFCEConnectionParameter.DFCE_HOSTPORT, " ");

      assertRequired(dfceProperties, DFCEConnectionParameter.DFCE_HOSTPORT);

   }

   @Test
   public void validate_failure_badformat_port() {

      Properties dfceProperties = createDfceProperties(
            DFCEConnectionParameter.DFCE_HOSTPORT, "toto");

      assertBadFormat(dfceProperties, DFCEConnectionParameter.DFCE_HOSTPORT);

   }

   @Test
   public void validate_failure_empty_hostname() {

      Properties dfceProperties = createDfceProperties(
            DFCEConnectionParameter.DFCE_HOSTNAME, " ");

      assertRequired(dfceProperties, DFCEConnectionParameter.DFCE_HOSTNAME);

   }

   @Test
   public void validate_failure_empty_contextroot() {

      Properties dfceProperties = createDfceProperties(
            DFCEConnectionParameter.DFCE_CONTEXTROOT, " ");

      assertRequired(dfceProperties, DFCEConnectionParameter.DFCE_CONTEXTROOT);

   }

   @Test
   public void validate_failure_empty_secure() {

      Properties dfceProperties = createDfceProperties(
            DFCEConnectionParameter.DFCE_SECURE, " ");

      assertRequired(dfceProperties, DFCEConnectionParameter.DFCE_SECURE);

   }

   private Properties createDfceProperties(String key, String value) {

      Properties dfceProperties = new Properties();
      dfceProperties.setProperty(DFCEConnectionParameter.DFCE_LOGIN,
            LOGIN_VALUE);
      dfceProperties.setProperty(DFCEConnectionParameter.DFCE_PASSWORD,
            PASSWORD_VALUE);
      dfceProperties.setProperty(DFCEConnectionParameter.DFCE_CONTEXTROOT,
            CONTEXTROOT_VALUE);
      dfceProperties.setProperty(DFCEConnectionParameter.DFCE_HOSTNAME,
            HOSTNAME_VALUE);
      dfceProperties.setProperty(DFCEConnectionParameter.DFCE_HOSTPORT,
            HOSTPORT_VALUE);
      dfceProperties.setProperty(DFCEConnectionParameter.DFCE_SECURE,
            SECURE_VALUE);

      dfceProperties.setProperty(key, value);

      return dfceProperties;
   }

   private void assertRequired(Properties dfceProperties, String property) {

      try {
         DFCEConnectionValidation.validate(dfceProperties);

         Assert
               .fail("Une exception DFCEConfigurationParameterNotFoundRuntimeException doit être levée");

      } catch (DFCEConfigurationParameterNotFoundRuntimeException e) {

         Assert.assertEquals(EXPECTED_MESSAGE, "Le paramètre '" + property
               + "' doit être obligatoirement renseigné.", e.getMessage());
      }
   }

   private void assertBadFormat(Properties dfceProperties, String property) {

      try {
         DFCEConnectionValidation.validate(dfceProperties);

         Assert
               .fail("Une exception DFCEConfigurationParameterBadFormatRuntimeException doit être levée");

      } catch (DFCEConfigurationParameterBadFormatRuntimeException e) {

         Assert.assertEquals(EXPECTED_MESSAGE, "Le paramètre '" + property
               + "' n'est pas au bon format.", e.getMessage());
      }
   }
}

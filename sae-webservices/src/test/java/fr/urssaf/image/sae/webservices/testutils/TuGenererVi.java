package fr.urssaf.image.sae.webservices.testutils;

//CHECKSTYLE:OFF

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;

import org.apache.commons.codec.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Element;

import fr.urssaf.image.sae.saml.params.SamlAssertionParams;
import fr.urssaf.image.sae.saml.params.SamlCommonsParams;
import fr.urssaf.image.sae.saml.service.SamlAssertionCreationService;
import fr.urssaf.image.sae.saml.util.ConverterUtils;
import fr.urssaf.image.sae.saml.util.XMLUtils;


/**
 * Générations de VI pour les tests
 */
@SuppressWarnings("PMD")
public class TuGenererVi {
   
   
   private static Logger LOG = LoggerFactory.getLogger(TuGenererVi.class);
   
   private static final String KEYSTORE_PASSWORD = "hiUnk6O3QnRN";
   
   public static final String ISSUER = "issuer_test";
   
   public static final URI SERVICE_VISE = ConverterUtils.uri("http://sae.urssaf.fr");
   
   public static final String ID_UTILISATEUR = "idUtilisateur_test";
   
   public static final List<String> PAGM_OK_PING_SECURE = 
      Arrays.asList("ROLE_TOUS;FULL");
   
   public static final List<String> PAGM_KO_PING_SECURE = 
      Arrays.asList("ROLE_INCONNU;FULL");
   
   
   @Test
   @Ignore
   public void vi_success()
      throws 
         KeyStoreException, 
         NoSuchAlgorithmException, 
         CertificateException, 
         IOException, 
         URISyntaxException {
      
      
      SamlAssertionCreationService service = new SamlAssertionCreationService(); 
      SamlAssertionParams params = new SamlAssertionParams();
      SamlCommonsParams commonsParams = new SamlCommonsParams();
      params.setCommonsParams(commonsParams);
      KeyStore keystore = getKeyStorePortailImage();
      String keystorePublicKeyAlias = keystore.aliases().nextElement();
            
      commonsParams.setId(null);
      commonsParams.setIssueInstant(null);
      commonsParams.setIssuer(ISSUER);
      commonsParams.setNotOnBefore(getDebutAssertion());
      commonsParams.setNotOnOrAfter(getFinAssertion());
      commonsParams.setAudience(SERVICE_VISE);
      commonsParams.setAuthnInstant(null);
      commonsParams.setPagm(PAGM_OK_PING_SECURE);
      
      params.setSubjectFormat2(new URI("urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified"));
      params.setSubjectId2(ID_UTILISATEUR);
      params.setMethodAuthn2(new URI("urn:oasis:names:tc:SAML:2.0:ac:classes:unspecified"));
      params.setRecipient(new URI("urn:URSSAF"));
      
      Element assertion = service.genererAssertion(
            params, keystore, keystorePublicKeyAlias, KEYSTORE_PASSWORD);
      
      debugVi(LOG,assertion);
      
   }
   
   
   @Test
   @Ignore
   public void vi_failure_accessDenied()
      throws 
         KeyStoreException, 
         NoSuchAlgorithmException, 
         CertificateException, 
         IOException, 
         URISyntaxException {
      
      SamlAssertionCreationService service = new SamlAssertionCreationService(); 
      SamlAssertionParams params = new SamlAssertionParams();
      SamlCommonsParams commonsParams = new SamlCommonsParams();
      params.setCommonsParams(commonsParams);
      KeyStore keystore = getKeyStorePortailImage();
      String keystorePublicKeyAlias = keystore.aliases().nextElement();
            
      commonsParams.setId(null);
      commonsParams.setIssueInstant(null);
      commonsParams.setIssuer(ISSUER);
      commonsParams.setNotOnBefore(getDebutAssertion());
      commonsParams.setNotOnOrAfter(getFinAssertion());
      commonsParams.setAudience(SERVICE_VISE);
      commonsParams.setAuthnInstant(null);
      commonsParams.setPagm(PAGM_KO_PING_SECURE);
      
      params.setSubjectFormat2(new URI("urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified"));
      params.setSubjectId2(ID_UTILISATEUR);
      params.setMethodAuthn2(new URI("urn:oasis:names:tc:SAML:2.0:ac:classes:unspecified"));
      params.setRecipient(new URI("urn:URSSAF"));
      
      Element assertion = service.genererAssertion(
            params, keystore, keystorePublicKeyAlias, KEYSTORE_PASSWORD);
      
      debugVi(LOG,assertion);
      
   }
   
   
private Date getDebutAssertion() {
      
      Calendar calendar = Calendar.getInstance();

      calendar.set(Calendar.YEAR,2011);
      calendar.set(Calendar.MONTH,2); // les numéros de mois commencent à 0
      calendar.set(Calendar.DAY_OF_MONTH,1);
      calendar.set(Calendar.HOUR_OF_DAY, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);
         
      Date laDate = calendar.getTime();
      
      return laDate;

   }
   
   private Date getFinAssertion() {
      
      Calendar calendar = Calendar.getInstance();

      calendar.set(Calendar.YEAR,2100);
      calendar.set(Calendar.MONTH,0); // les numéros de mois commencent à 0
      calendar.set(Calendar.DAY_OF_MONTH,1);
      calendar.set(Calendar.HOUR_OF_DAY, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);
         
      Date laDate = calendar.getTime();
      
      return laDate;

   }
   
   private static KeyStore getKeyStorePortailImage()
      throws 
         KeyStoreException, 
         NoSuchAlgorithmException, 
         CertificateException, 
         IOException {
      return createKeystore(
            "src/test/resources/Portail_Image.p12",KEYSTORE_PASSWORD);
   }
   
   
   private static KeyStore createKeystore(String file, String password)
         throws KeyStoreException, NoSuchAlgorithmException,
         CertificateException, IOException {

      KeyStore keystore = KeyStore.getInstance("PKCS12");

      FileInputStream in = new FileInputStream(file);
      try {
         keystore.load(in, password.toCharArray());

      } finally {
         in.close();
      }

      return keystore;

   }
   
   
   
   private static void debugVi(
         Logger logger,
         Element vi) {
      
      Transformer transformer = XMLUtils.initTransformer();
      // transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      // transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
      transformer.setOutputProperty(OutputKeys.INDENT, "no");

      logger.debug("\n" + XMLUtils.print(vi, CharEncoding.UTF_8, transformer));
      
   }

}
//CHECKSTYLE:ON

package fr.urssaf.image.sae.vi.testutils;

//CHECKSTYLE:OFF

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import fr.urssaf.image.sae.saml.params.SamlAssertionParams;
import fr.urssaf.image.sae.saml.params.SamlCommonsParams;
import fr.urssaf.image.sae.saml.service.SamlAssertionCreationService;
import fr.urssaf.image.sae.saml.util.ConverterUtils;
import fr.urssaf.image.sae.saml.util.XMLUtils;
import fr.urssaf.image.sae.vi.testutils.signature.XmlSignature;


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
   
   public static final List<String> PAGM = Arrays.asList(
         "DROIT_APPLICATIF_1;PERIMETRE_DONNEES_1",
         "DROIT_APPLICATIF_2;PERIMETRE_DONNEES_2");
   
   
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
      commonsParams.setPagm(PAGM);
      
      params.setSubjectFormat2(new URI("urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified"));
      params.setSubjectId2(ID_UTILISATEUR);
      params.setMethodAuthn2(new URI("urn:oasis:names:tc:SAML:2.0:ac:classes:unspecified"));
      params.setRecipient(new URI("urn:URSSAF"));
      
      Element assertion = service.genererAssertion(
            params, keystore, keystorePublicKeyAlias, KEYSTORE_PASSWORD);
      
      debugAssertion(LOG,assertion);
      
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
   
   
   
   private static void debugAssertion(
         Logger logger,
         Element assertion) {
      
      Transformer transformer = XMLUtils.initTransformer();
      // transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      // transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
      transformer.setOutputProperty(OutputKeys.INDENT, "no");

      logger.debug("\n" + XMLUtils.print(assertion, CharEncoding.UTF_8, transformer));
      
   }
   
   
   
   
   @Ignore("Ce n'est pas un TU, mais un moyen de générer des VI")
   @Test
   public void vi_success_V2() {
      
      try {
      
         DateTime systemDate = new DateTime();
         
         String assertionId = UUID.randomUUID().toString();
         // String assertionId = "bad id";
         // String assertionId = "pfx5d541dee-4468-74d2-7cbe-03078ef284e7";
                  
         String issuer = ISSUER;
         
         String recipient = "urn:URSSAF";
         
         String audience = SERVICE_VISE.toString();
         
         String authnInstant = systemDate.toString();
         
         String notOnOrAfter = systemDate.plusYears(200).toString();
         
         String notBefore = systemDate.minusHours(2).toString();
         
         List<String> pagm = PAGM ;
         
         String assertionNonSignee = getAssertionNonSignee(
               assertionId,
               issuer,
               recipient,
               audience,
               authnInstant,
               notOnOrAfter,
               notBefore,
               pagm);
         
         String assertionSignee = getAssertionSignee(assertionNonSignee);
         
         System.out.println(assertionSignee);
         
       
      }
      catch (Exception ex) {
         throw new RuntimeException(ex);
      }
      
   }
   
   
   private String getAssertionNonSignee(
         String assertionId,
         String issuer,
         String recipient,
         String audience,
         String authnInstant,
         String notOnOrAfter,
         String notBefore,
         List<String> pagms) throws IOException {
      
      
      // Lecture du modèle d'assertion depuis les ressources de src/test/resources
      
      
      
      // File file = new File("modele_vi/modele_assertion_saml2.xml");
      List<String> lines;
      // FileInputStream fis = new FileInputStream(file);
//      try {
//         lines = IOUtils.readLines(fis);
         lines = IOUtils.readLines(this.getClass().getClassLoader().getResourceAsStream("modele_vi/modele_assertion_saml2.xml"));
//      }
//      finally {
//         if (fis!=null) {
//            fis.close();
//         }
//      }
      String assertion = StringUtils.join(lines, "\r\n");
      
      // Construction de la liste des PAGM
      String pagmsAplat = construitListePagm(pagms);
      
      // Rechercher/Remplacer des valeurs
      assertion = StringUtils.replace(assertion, "[AssertionID]", assertionId);
      assertion = StringUtils.replace(assertion, "[Issuer]", issuer);
      assertion = StringUtils.replace(assertion, "[Recipient]", recipient);
      assertion = StringUtils.replace(assertion, "[Audience]", audience);
      assertion = StringUtils.replace(assertion, "[AuthnInstant]", authnInstant);
      assertion = StringUtils.replace(assertion, "[NotOnOrAfter]", notOnOrAfter);
      assertion = StringUtils.replace(assertion, "[NotBefore]", notBefore);
      assertion = StringUtils.replace(assertion, "[PAGM]", pagmsAplat);
      
      // Renvoi de la valeur de retour
      return assertion;
      
   }
   
   
   private String construitListePagm(List<String> pagm) {
      
      List<String> pagmList = new ArrayList<String>();
      
      for (String unPagm: pagm) {
         pagmList.add("<saml2:AttributeValue>" + unPagm + "</saml2:AttributeValue>");
      }
      
      String result = StringUtils.join(pagmList, "\r\n");
      
      return result;
      
   }
   
   
   private String getAssertionSignee(String assertionNonSignee) {
    
      try {
      
         KeyStore keystore = getKeyStorePortailImage();
         String alias = keystore.aliases().nextElement() ;
         String password = KEYSTORE_PASSWORD;
         
         String assertionSignee = XmlSignature.signeXml(
               IOUtils.toInputStream(assertionNonSignee), 
               keystore,
               alias, 
               password);
         
         return assertionSignee;
      
      } catch (Exception ex) {
         throw new RuntimeException(ex);
      }
      
   }

}
//CHECKSTYLE:ON

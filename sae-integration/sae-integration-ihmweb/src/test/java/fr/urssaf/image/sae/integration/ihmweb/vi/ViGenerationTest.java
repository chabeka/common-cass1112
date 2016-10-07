package fr.urssaf.image.sae.integration.ihmweb.vi;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.signature.XmlSignature;


/**
 * Outils pour les VI
 */
@SuppressWarnings("PMD")
public class ViGenerationTest {

   
   //private static final String PKCS12 = "certificats/PNR_Application_Test.p12";
   //private static final String KEYSTORE_PASSWORD = "QEtDiGuGuEnZ";
   
   private static final String PKCS12 = "certificats/gammeimage.p12";
   private static final String KEYSTORE_PASSWORD = "gammeimage";
   
   /**
    * Génération d'un vecteur d'identification valide,
    * pour le mode application à application.<br>
    * <br>
    * On s'appuie sur les modèles de VI :
    * <ul>
    *    <li>src/test/resources/modele_vi/modele_assertion_saml2.xml</li>
    *    <li>src/test/resources/modele_vi/modele_wsseSecurity.xml</li>
    * </ul>
    * Le modèle "modele_assertion_saml2.xml" est celui de l'assertion SAML 2.0<br>
    * <br>
    * Le modèle "modele_wsseSecurity.xml" est celui de l'en-tête WS-Security dans lequel
    * il faut placer l'assertion SAML 2.0<br>
    * <br>
    * Les valeurs à rechercher/remplacer dans les "modele_assertion_saml2.xml" sont :<br>
    * <ul>
    *    <li>[AssertionID]  => Identifiant unique de l'assertion (UUID)</li>
    *    <li>[Issuer]       => Identifiant de l'application client (chaîne de caractères)</li>
    *    <li>[Recipient]    => Identifiant du fournisseur (URN)</li>
    *    <li>[Audience]     => Identifiant du service visé (URN)</li>
    *    <li>[AuthnInstant] => Timestamp de la génération du VI</li>
    *    <li>[NotOnOrAfter] => Timestamp après lequel le VI doit être considéré comme invalide</li>
    *    <li>[NotBefore]    => Timestamp avant lequel le VI doit être considéré comme invalide</li>
    *    <li>[MethodAuthn2] => Méthode d'authentification de l'utilisateur sur le SI de l'organisme client</li>
    *    <li>[PAGM]         => Les PAGM</li>
    * </ul>
    * 
    */
   @Test
   //@Ignore("Ce n'est pas un TU, mais un moyen de générer des VI")
   public void generationViAA() {
      
      try {
      
         DateTime systemDate = new DateTime();
         
         String assertionId = UUID.randomUUID().toString();
         String issuer = "CS_DEV_TOUTES_ACTIONS_NAT";
         String recipient = "urn:URSSAF";
         String audience = "http://sae.urssaf.fr";
         String authnInstant = systemDate.toString();
         String notOnOrAfter = systemDate.plusYears(200).toString();
         String notBefore = systemDate.minusHours(2).toString();
         String methodAuthn = "urn:oasis:names:tc:SAML:2.0:ac:classes:unspecified";
         String[] pagm = new String[] {"PAGM_TOUTES_ACTIONS_NAT"} ;
         
         String assertionNonSignee = getAssertionNonSignee(
               assertionId,
               issuer,
               recipient,
               audience,
               authnInstant,
               notOnOrAfter,
               notBefore,
               methodAuthn,
               pagm);
         
         String assertionSignee = getAssertionSignee(assertionNonSignee);
         
         String enTestWsSecurity = getEnTestWsSecurity(assertionSignee,assertionId); 
         
         System.out.println(enTestWsSecurity);
         
       
      }
      catch (Exception ex) {
         throw new IntegrationRuntimeException(ex);
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
         String methodAuthn,
         String[] pagms) throws IOException {
      
      ClassPathResource resource = new ClassPathResource("modele_vi/modele_assertion_saml2.xml"); 
      List<String> lines = IOUtils.readLines(resource.getInputStream());
      String assertion = StringUtils.join(lines, "\r\n");
      
      String pagmsAplat = construitListePagm(pagms);
      
      assertion = StringUtils.replace(assertion, "[AssertionID]", assertionId);
      assertion = StringUtils.replace(assertion, "[Issuer]", issuer);
      assertion = StringUtils.replace(assertion, "[Recipient]", recipient);
      assertion = StringUtils.replace(assertion, "[Audience]", audience);
      assertion = StringUtils.replace(assertion, "[AuthnInstant]", authnInstant);
      assertion = StringUtils.replace(assertion, "[NotOnOrAfter]", notOnOrAfter);
      assertion = StringUtils.replace(assertion, "[NotBefore]", notBefore);
      assertion = StringUtils.replace(assertion, "[MethodAuthn2]", methodAuthn);
      assertion = StringUtils.replace(assertion, "[PAGM]", pagmsAplat);
      
      return assertion;
      
   }
   
   
   private String construitListePagm(String[] pagm) {
      
      List<String> pagmList = new ArrayList<String>();
      
      for (String unPagm: pagm) {
         pagmList.add("<saml2:AttributeValue>" + unPagm + "</saml2:AttributeValue>");
      }
      
      String result = StringUtils.join(pagmList, "\r\n");
      
      return result;
      
   }
   
   
   private String getAssertionSignee(String assertionNonSignee) {
    
      try {
      
         KeyStore keystore = chargeKeyStore();
         String alias = trouveAliasClePrivee(keystore) ;
         String password = KEYSTORE_PASSWORD;
         
         String assertionSignee = XmlSignature.signeXml(
               IOUtils.toInputStream(assertionNonSignee), 
               keystore,
               alias, 
               password);
         
         return assertionSignee;
      
      } catch (Exception ex) {
         throw new IntegrationRuntimeException(ex);
      }
      
   }
   
   
   private KeyStore chargeKeyStore()  {
      
      try {
         
         ClassPathResource resource = new ClassPathResource(PKCS12);
         
         KeyStore keystore = KeyStore.getInstance("PKCS12", "SunJSSE");
         
         keystore.load(resource.getInputStream(), KEYSTORE_PASSWORD.toCharArray());
         
         // System.out.println(keystore.aliases().nextElement());
          
         return keystore;
      
      } catch (Exception ex) {
         throw new IntegrationRuntimeException(ex);
      }

   }
   
   
   
   /**
    * Utilisation du modèle src/test/resources/modele_vi/modele_wsseSecurity.xml<br>
    * <br>
    * Les valeurs à rechercher/remplacer dans ce modèle sont :<br>
    * <br>
    * <ul>
    *    <li>[Assertion]   => l'assertion SAML 2.0 signée</li>
    *    <li>[AssertionID] => l'identifiant (UUID) de l'assertion ci-dessus</li>
    * </ul>
    * 
    */
   private String getEnTestWsSecurity(
         String assertionSignee,
         String assertionId) {
      
      try {
      
         ClassPathResource resource = new ClassPathResource("modele_vi/modele_wsseSecurity.xml"); 
         List<String> lines = IOUtils.readLines(resource.getInputStream());
         String enTestWsSecurity = StringUtils.join(lines, "\r\n");
         
         enTestWsSecurity = StringUtils.replace(enTestWsSecurity, "[Assertion]", assertionSignee);
         enTestWsSecurity = StringUtils.replace(enTestWsSecurity, "[AssertionID]", assertionId);
         
         return enTestWsSecurity; 
      
      
      } catch (Exception ex) {
         throw new IntegrationRuntimeException(ex);
      }
      
   }
   
   private String trouveAliasClePrivee(KeyStore keystore) {
      
      List<String> aliases;
      try {
         
         aliases = Collections.list(keystore.aliases());
         
         for (String alias: aliases) {
            if (keystore.isKeyEntry(alias)) {
               return alias;
            }
         }
         
         // Si pas trouvé, on lève une exception
         throw new IntegrationRuntimeException("");
         
      } catch (KeyStoreException e) {
         throw new IntegrationRuntimeException(e);
      }
      
   }
   
}

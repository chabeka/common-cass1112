package fr.urssaf.image.sae.saml.opensaml.signature;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opensaml.saml2.core.Assertion;
import org.w3c.dom.Element;

import fr.urssaf.image.sae.saml.exception.signature.validate.SamlAutoSignedCertificateException;
import fr.urssaf.image.sae.saml.exception.signature.validate.SamlSignatureCryptoException;
import fr.urssaf.image.sae.saml.exception.signature.validate.SamlSignatureKeyInfoException;
import fr.urssaf.image.sae.saml.exception.signature.validate.SamlSignatureNotFoundException;
import fr.urssaf.image.sae.saml.exception.signature.validate.SamlSignatureValidateException;
import fr.urssaf.image.sae.saml.opensaml.SamlConfiguration;
import fr.urssaf.image.sae.saml.params.SamlAssertionParams;
import fr.urssaf.image.sae.saml.params.SamlCommonsParams;
import fr.urssaf.image.sae.saml.service.SamlAssertionCreationService;
import fr.urssaf.image.sae.saml.testutils.TuUtils;


/**
 * Tests unitaires de la classe {@link fr.urssaf.image.sae.saml.opensaml.signature.SamlSignatureSignService}<br>
 * <br>
 * Cas de tests de la vérification de la signature de l'assertion SAML :<br>
 * <ul>
 *    <li>Cas 01 : Pas de signature => KO</li>
 *    <li>Cas 02 : Signature sans KeyInfo => KO</li>
 *    <li>Cas 03 : Signature avec un certificat autosigné => KO</li>
 *    <li>Cas 04 : Signature cryptographiquement incorrecte => KO</li>
 *    <li>Cas 05 : Signature avec un certificat émis par une AC de confiance, et pas dans CRL => OK</li>
 *    <li>Cas 06 : Signature avec un certificat non émis par une AC de confiance => KO</li>
 *    <li>Cas 07 : Signature avec un certificat émis par une AC de confiance, clé publique dans CRL => KO</li>
 *    <li>
 *      Cas 07 : Signature avec un certificat émis par une AC de confiance, 
 *      pas dans CRL, mais plus valide dans le temps => KO
 *    </li>
 * </ul>
 */
@SuppressWarnings({
   "PMD.AvoidDuplicateLiterals",
   "PMD.TooManyMethods",
   "PMD.LongVariable"})
public class SamlSignatureValidateServiceTest {

   
   /**
    * Classe de génération des fichiers de tests 
    */
   @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
   static class GenerateurDesAssertionsDeTest {
      
      private static final Logger LOG = Logger.getLogger(SamlSignatureValidateServiceTest.class);
      
      
      private GenerateurDesAssertionsDeTest() {
         
      }
            
      private static Date getDebutAssertion() {
         
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
      
      
      private static Date getFinAssertion() {
         
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
      
      
      private static KeyStore createKeystoreFromPkcs12(String file, String password)
      {
         try {
            KeyStore keystore;

            keystore = KeyStore.getInstance("PKCS12");

            FileInputStream inputStream = new FileInputStream(file);
            try {
               keystore.load(inputStream, password.toCharArray());

            } finally {
               inputStream.close();
            }

            return keystore;

         } catch (KeyStoreException e) {
            throw new RuntimeException(e);
         } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
         } catch (CertificateException e) {
            throw new RuntimeException(e);
         } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
         } catch (IOException e) {
            throw new RuntimeException(e);
         }

      }
      
      
      private static SamlAssertionParams getAssertionParams() {

         try {

            SamlAssertionParams params = new SamlAssertionParams();
            SamlCommonsParams commonsParams = new SamlCommonsParams();
            params.setCommonsParams(commonsParams);

            commonsParams.setId(null);
            commonsParams.setIssueInstant(null);
            commonsParams.setIssuer("TEST_SERVICES_WEB_SAE");
            commonsParams.setNotOnBefore(getDebutAssertion());
            commonsParams.setNotOnOrAfter(getFinAssertion());

            commonsParams.setAudience(new URI("http://sae.urssaf.fr"));

            commonsParams.setAuthnInstant(null);
            commonsParams.setPagm(Arrays.asList("ROLE_TOUS"));

            params.setSubjectFormat2(new URI("urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified"));
            params.setSubjectId2("NON_RENSEIGNE");
            params.setMethodAuthn2(new URI("urn:oasis:names:tc:SAML:2.0:ac:classes:unspecified"));
            params.setRecipient(new URI("urn:URSSAF"));

            return params;

         } catch (URISyntaxException e) {
            throw new RuntimeException(e);
         }

      }
      
         
      /**
       * Cas 01 = Pas de signature
       */
      protected static void generateAssertionCas01() {
         
         // Récupération d'un jeu de paramètres SAML de test
         SamlAssertionParams params = getAssertionParams();
         
         // Utilisation du P12 Portail_Image.p12 pour la signature
         String keystorePassword = "hiUnk6O3QnRN";
         KeyStore keystore = createKeystoreFromPkcs12(
               "src/test/resources/verif_signature/cas01/Portail_Image.p12",keystorePassword);
         String keystorePublicKeyAlias;
         try {
            keystorePublicKeyAlias = keystore.aliases().nextElement();
         } catch (KeyStoreException e) {
            throw new RuntimeException(e);
         }
         
         // Création de l'assertion
         SamlAssertionCreationService service = new SamlAssertionCreationService();
         Element assertion = service.genererAssertion(
               params, keystore, keystorePublicKeyAlias, keystorePassword);
         
         // Affichage de l'assertion dans les log pour la récupérer et la mettre dans un
         // fichier de test
         // LOG.debug(assertion);
         TuUtils.debugAssertion(LOG, assertion);
         
         // T O D O
         //  - copier l'assertion de la fenêtre de log dans le fichier :
         //       src/test/resources/verif_signature/cas01/assertion_cas01.xml
         //  - retirer la partie <ds:Signature> de l'assertion
         
      }
      
      
      /**
       * Cas 02 : Signature sans KeyInfo => KO
       */
      protected static void generateAssertionCas02() {
         
         // Récupération d'un jeu de paramètres SAML de test
         SamlAssertionParams params = getAssertionParams();
         
         // Utilisation du P12 Portail_Image.p12 pour la signature
         String keystorePassword = "hiUnk6O3QnRN";
         KeyStore keystore = createKeystoreFromPkcs12(
               "src/test/resources/verif_signature/cas02/Portail_Image.p12",keystorePassword);
         String keystorePublicKeyAlias;
         try {
            keystorePublicKeyAlias = keystore.aliases().nextElement();
         } catch (KeyStoreException e) {
            throw new RuntimeException(e);
         }
         
         // Création de l'assertion
         SamlAssertionCreationService service = new SamlAssertionCreationService();
         Element assertion = service.genererAssertion(
               params, keystore, keystorePublicKeyAlias, keystorePassword);
         
         // Affichage de l'assertion dans les log pour la récupérer et la mettre dans un
         // fichier de test
         TuUtils.debugAssertion(LOG, assertion);
         
         // T O D O
         //  - copier l'assertion de la fenêtre de log dans le fichier :
         //       src/test/resources/verif_signature/cas02/assertion_cas02.xml
         //  - retirer la partie <ds:KeyInfo> de la signature
         
      }
      
      
      
      /**
       * Cas 03 : Signature avec un certificat autosigné => KO
       */
      protected static void generateAssertionCas03() {
         
         
         // Récupération d'un jeu de paramètres SAML de test
         SamlAssertionParams params = getAssertionParams();
         
         // Utilisation du P12 Portail_Image.p12 pour la signature
         String keystorePassword = "mdpKeyStore";
         KeyStore keystore = createKeystoreFromPkcs12(
               "src/test/resources/verif_signature/cas03/keystore.p12",keystorePassword);
         String keystorePublicKeyAlias;
         try {
            keystorePublicKeyAlias = keystore.aliases().nextElement();
         } catch (KeyStoreException e) {
            throw new RuntimeException(e);
         }
         
         // Création de l'assertion
         SamlAssertionCreationService service = new SamlAssertionCreationService();
         Element assertion = service.genererAssertion(
               params, keystore, keystorePublicKeyAlias, keystorePassword);
         
         // Affichage de l'assertion dans les log pour la récupérer et la mettre dans un
         // fichier de test
         TuUtils.debugAssertion(LOG, assertion);
         
         // T O D O
         //  - copier l'assertion de la fenêtre de log dans le fichier :
         //       src/test/resources/verif_signature/cas03/assertion_cas03.xml
         
      }
      
      
      /**
       * Cas 04 : Signature cryptographiquement incorrecte => KO
       */
      protected static void generateAssertionCas04() {
         
         // Récupération d'un jeu de paramètres SAML de test
         SamlAssertionParams params = getAssertionParams();
         
         // Utilisation du P12 Portail_Image.p12 pour la signature
         String keystorePassword = "hiUnk6O3QnRN";
         KeyStore keystore = createKeystoreFromPkcs12(
               "src/test/resources/verif_signature/cas04/Portail_Image.p12",keystorePassword);
         String keystorePublicKeyAlias;
         try {
            keystorePublicKeyAlias = keystore.aliases().nextElement();
         } catch (KeyStoreException e) {
            throw new RuntimeException(e);
         }
         
         // Création de l'assertion
         SamlAssertionCreationService service = new SamlAssertionCreationService();
         Element assertion = service.genererAssertion(
               params, keystore, keystorePublicKeyAlias, keystorePassword);
         
         // Affichage de l'assertion dans les log pour la récupérer et la mettre dans un
         // fichier de test
         TuUtils.debugAssertion(LOG, assertion);
         
         // T O D O
         //  - copier l'assertion de la fenêtre de log dans le fichier :
         //       src/test/resources/verif_signature/cas04/assertion_cas04.xml
         //  - modifier la partie <ds:Signature> de l'assertion de manière aléatoire         
         
      }
      
      
      /**
       * Cas 05 : Signature avec un certificat émis par une AC de confiance, et pas dans CRL => OK
       */
      protected static void generateAssertionCas05() {
         
         // Récupération d'un jeu de paramètres SAML de test
         SamlAssertionParams params = getAssertionParams();
         
         // Utilisation du P12 Portail_Image.p12 pour la signature
         String keystorePassword = "hiUnk6O3QnRN";
         KeyStore keystore = createKeystoreFromPkcs12(
               "src/test/resources/verif_signature/cas05/Portail_Image.p12",keystorePassword);
         String keystorePublicKeyAlias;
         try {
            keystorePublicKeyAlias = keystore.aliases().nextElement();
         } catch (KeyStoreException e) {
            throw new RuntimeException(e);
         }
         
         // Création de l'assertion
         SamlAssertionCreationService service = new SamlAssertionCreationService();
         Element assertion = service.genererAssertion(
               params, keystore, keystorePublicKeyAlias, keystorePassword);
         
         // Affichage de l'assertion dans les log pour la récupérer et la mettre dans un
         // fichier de test
         TuUtils.debugAssertion(LOG, assertion);
         
         // T O D O
         //  - copier l'assertion de la fenêtre de log dans le fichier :
         //       src/test/resources/verif_signature/cas05/assertion_cas05.xml
         
      }
      
   }
   
   
   
   private SamlSignatureValidateService signatureValidateService ;
   
   
   @Before
   public void prepare() {
     new SamlConfiguration();
     signatureValidateService = new SamlSignatureValidateService();
   }
   
   
   /**
    * Génération des assertions de test.<br>
    * <br>
    * Il ne s'agit pas d'un test unitaire, mais d'une méthode permettant de générer
    * des assertions de tests pour répondre aux différents cas de tests.<br>
    * <br>
    * Ces assertions sont écrites dans la console avec un LOG.debug<br>
    * <br>
    * Il faut ensuite les mettre dans le fichier XML du cas de test correspondant.<br>
    * <br>
    * Cette procédure est décrite dans chaque méthode de génération d'assertion de test,
    * en effet, pour certaines, il faut faire quelques opérations à la main sur
    * l'assertion générée.
    */
   @Test
   @Ignore("A utiliser uniquement pour générer des fichiers de tests")
   @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
   public void genererAssertionsDeTests() {
      
      // GenerateurDesAssertionsDeTest.generateAssertionCas01();
      // GenerateurDesAssertionsDeTest.generateAssertionCas02();
      // GenerateurDesAssertionsDeTest.generateAssertionCas03();
      // GenerateurDesAssertionsDeTest.generateAssertionCas04();
      // GenerateurDesAssertionsDeTest.generateAssertionCas05();
      
   }
   
   
   /**
    * Cas 01 : Pas de signature => Le test doit échouer
    * 
    * @throws SamlSignatureValidateException 
    */
   @Test(expected = SamlSignatureNotFoundException.class)
   public void cas01() throws SamlSignatureValidateException {
      
      // Chargement de l'assertion de test dans un objet Assertion
      Assertion assertion = TuUtils.getAssertion(
            "src/test/resources/verif_signature/cas01/assertion_cas01.xml");
      
      // Appel de la vérification de la signature
      signatureValidateService.verifierSignature(
            assertion, null, null);
      
   }
   
   
   
   /**
    * Cas 02 : Signature sans KeyInfo => KO
    * 
    * @throws SamlSignatureValidateException 
    */
   @Test(expected = SamlSignatureKeyInfoException.class)
   public void cas02() throws SamlSignatureValidateException {
      
      // Chargement de l'assertion de test dans un objet Assertion
      Assertion assertion = TuUtils.getAssertion(
            "src/test/resources/verif_signature/cas02/assertion_cas02.xml");
      
      // Appel de la vérification de la signature
      signatureValidateService.verifierSignature(assertion, null, null);
      
   }
   
   /**
    * Cas 03 : Signature avec un certificat autosigné => KO
    * 
    * @throws SamlSignatureValidateException 
    */
   @Test(expected=SamlAutoSignedCertificateException.class)
   public void cas03() throws SamlSignatureValidateException {
      
      // Chargement de l'assertion de test dans un objet Assertion
      Assertion assertion = TuUtils.getAssertion(
            "src/test/resources/verif_signature/cas03/assertion_cas03.xml");
      
      // Appel de la vérification de la signature
      signatureValidateService.verifierSignature(assertion, null, null);
      
   }

   
   /**
    * Cas 04 : Signature cryptographiquement incorrecte => KO
    * 
    * @throws SamlSignatureValidateException 
    */
   @Test(expected=SamlSignatureCryptoException.class)
   public void cas04() throws SamlSignatureValidateException {
      
      // Chargement de l'assertion de test dans un objet Assertion
      Assertion assertion = TuUtils.getAssertion(
            "src/test/resources/verif_signature/cas04/assertion_cas04.xml");
      
      // Appel de la vérification de la signature
      signatureValidateService.verifierSignature(assertion, null, null);
      
   }
   
   
   /**
    * Cas 05 : Signature avec un certificat émis par une AC de confiance, et pas dans CRL => OK
    * 
    * @throws SamlSignatureValidateException 
    */
   @Test
   @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
   public void cas05() throws SamlSignatureValidateException {
      
      // Chargement de l'assertion de test dans un objet Assertion
      Assertion assertion = TuUtils.getAssertion(
            "src/test/resources/verif_signature/cas05/assertion_cas05.xml");
      
      // Appel de la vérification de la signature
      signatureValidateService.verifierSignature(assertion, null, null);
      
      // Résultat attendu : pas d'exception levée
      
   }
   
   
   
   
   
}

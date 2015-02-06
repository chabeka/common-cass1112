package fr.urssaf.image.sae.saml.service;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.opensaml.saml2.core.Assertion;
import org.w3c.dom.Element;

import fr.urssaf.image.sae.saml.exception.SamlFormatException;
import fr.urssaf.image.sae.saml.opensaml.SamlXML;
import fr.urssaf.image.sae.saml.opensaml.service.SamlAssertionService;
import fr.urssaf.image.sae.saml.params.SamlAssertionParams;
import fr.urssaf.image.sae.saml.util.SecurityUtil;

/**
 * Génération d'une assertion SAML 2.0 signée électroniquement<br>
 * <br>
 * la classe s'appuie sur le framework <a
 * href="https://spaces.internet2.edu/display/OpenSAML/Home" />OpenSAML</a><br>
 * <br>
 * Le recours à cette classe nécessite une instanciation au préalable de
 * {@link fr.urssaf.image.sae.saml.opensaml.SamlConfiguration}
 * 
 * <pre>
 * exemple pour instancier un keystore à partir d'un P12
 * 
 * KeyStore keystore = KeyStore.getInstance("PKCS12", "SunJSSE");
 * FileInputStream in = new FileInputStream("/mon_certificat.p12");
 * try {
 *    keystore.load(in, "mon_password".toCharArray());
 * } finally {
 *    in.close();
 * }
 * </pre>
 * 
 */
public class SamlAssertionCreationService {

   private final SamlAssertionService assertionService;

   /**
    * Constructeur<br>
    * Initialisation de {@link SamlAssertionService} pour la validation des
    * jetons SAML<br>
    */
   public SamlAssertionCreationService() {

      assertionService = new SamlAssertionService();
   }

   /**
    * Génération d'une assertion SAML 2.0 signée électroniquement, pour être
    * utilisée dans le cadre de l'authentification aux services web du SAE<br>
    * <br>
    * Les paramètres d'entrées sont vérifiés par AOP par la classe
    * {@link fr.urssaf.image.sae.saml.component.aspect.SamlAssertionValidate}<br>
    * <br>
    * <ol>
    * <li>instanciation d'un jeton SAML :
    * {@link SamlAssertionService#write(SamlAssertionParams)}</li>
    * <li>validation du corps du jeton SAML :
    * {@link SamlAssertionService#validate(Assertion)}</li>
    * <li>signature du jeton SAML :
    * {@link SamlAssertionService#sign(Assertion, X509Certificate, PrivateKey, Collection)}
    * </li>
    * <li>impression du jeton SAML sous forme d'une chaine de caractère</li>
    * </ol>
    * 
    * Il est important de noter que le jeton SAML instancié est une chaine de
    * caractères sur une seule ligne et non indentée. Ceci a pour but de ne pas
    * modifier le contenu du jeton, sinon la signature n'est plus valable.<br>
    * <br>
    * 
    * @param assertionParams
    *           Les paramètres de génération de l'assertion SAML
    * @param keyStore
    *           La clé privée, sa clé publique et sa chaîne de certification
    *           pour la signature de l'assertion SAML
    * @param alias
    *           L'alias de la clé privée du KeyStore à utiliser pour la
    *           signature de l'assertion
    * @param password
    *           mot du de la clé privée
    * @return L'assertion SAML 2.0 signée électroniquement
    */
   public final Element genererAssertion(SamlAssertionParams assertionParams,
         KeyStore keyStore, String alias, String password) {

      // GENERATION DU JETON SAML
      Assertion assertion = assertionService.write(assertionParams);

      // VALIDATION DU JETON SAML
      validate(assertion);

      // SIGNATURE DU JETON
      sign(assertion, keyStore, alias, password);

      // IMPRESSION DU JETON
      Element element = SamlXML.marshaller(assertion);

      return element;

   }

   private void sign(Assertion assertion, KeyStore keystore, String alias,
         String password) {

      try {

         PrivateKey privatekey = SecurityUtil.loadPrivateKey(keystore, alias,
               password);
         X509Certificate x509Certificate = SecurityUtil.loadX509Certificate(
               keystore, alias);

         Collection<X509Certificate> certs = loadCertificateChain(keystore,
               alias);

         assertionService.sign(assertion, x509Certificate, privatekey, certs);
      } catch (UnrecoverableKeyException e) {
         throw new IllegalStateException(e);
      } catch (KeyStoreException e) {
         throw new IllegalStateException(e);
      } catch (NoSuchAlgorithmException e) {
         throw new IllegalStateException(e);
      }
   }

   @SuppressWarnings("unchecked")
   private Collection<X509Certificate> loadCertificateChain(KeyStore keystore,
         String alias) throws KeyStoreException {

      Collection<X509Certificate> certs = null;
      if (ArrayUtils.isNotEmpty(keystore.getCertificateChain(alias))) {

         certs = CollectionUtils.typedCollection(Arrays.asList(keystore
               .getCertificateChain(alias)), X509Certificate.class);

      }
      return certs;
   }

   private void validate(Assertion assertion) {

      try {

         assertionService.validate(assertion);

      } catch (SamlFormatException e) {
         throw new IllegalStateException(e);
      }
   }

}

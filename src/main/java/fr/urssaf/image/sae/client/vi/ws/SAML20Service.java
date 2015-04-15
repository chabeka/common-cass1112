package fr.urssaf.image.sae.client.vi.ws;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.client.vi.exception.XmlSignatureException;
import fr.urssaf.image.sae.client.vi.signature.XmlSignature;
import fr.urssaf.image.sae.client.vi.util.ResourceUtils;
import fr.urssaf.image.sae.client.vi.util.StreamUtils;

/**
 * Classe de service pour les jeton SAML 2.0
 * 
 */
public class SAML20Service {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(SAML20Service.class);

   private static final String SAML_20 = "security/saml20.xml";
   private static final String PAGM = "security/pagm.xml";

   /**
    * Instanciation d'un jeton SAML 2.0 sur le format du fichier
    * "security/saml20.xml"<br>
    * <br>
    * Les valeurs entre crochets [] sont remplacées par les valeurs passées en
    * argument de la méthode
    * 
    * @param issuer
    *           valeur de [ISSUER]
    * @param roles
    *           valeurs de [PAGMS]
    * @param notAfter
    *           valeur de [NotOnOrAfter]
    * @param notBefore
    *           valeur de [NotBefore]
    * @param actual
    *           valeur de [AuthnInstant]
    * @param identifiant
    *           valeur de [AssertionID]
    * @return jeton SAML 2.0
    */
   public final String createAssertion20(String issuer, String login, List<String> roles,
         DateTime notAfter, DateTime notBefore, DateTime actual,
         UUID identifiant) {

      LOGGER
            .debug(
                  "Génération d'une assertion SAML avec les paramètres suivants: issuer={}, login={}, roles={}, notAfter={}, notBefore={}, actual={}, identifiant={}",
                  new Object[] { issuer, login, roles, notAfter, notBefore, actual,
                        identifiant });

      String pagms = createPagm(roles);
      LOGGER.debug("PAGM(s) retravaillé(s): {}", pagms);

      InputStream assertionStream = ResourceUtils.loadResource(this, SAML_20);

      String[] searchList = new String[] { "[ISSUER]", "[LOGIN]", "[PAGMS]",
            "[NotOnOrAfter]", "[NotBefore]", "[AssertionID]", "[AuthnInstant]" };

      String[] replacementList = new String[] { issuer, login, pagms,
            notAfter.toString(), notBefore.toString(), identifiant.toString(),
            actual.toString() };

      String assertion = StreamUtils.createObject(assertionStream, searchList,
            replacementList);

      LOGGER.debug("Assertion SAML générée: {}", assertion);
      return assertion;

   }

   /**
    * @param roles
    * @return
    */
   protected final String createPagm(List<String> roles) {

      LOGGER.debug("Début du pré-traitement des PAGMs. Entrée={}", roles);

      InputStream stream = null;
      StringBuffer buffer = new StringBuffer();
      String xmlPagm;
      try {
         for (String role : roles) {
            stream = ResourceUtils.loadResource(this, PAGM);
            xmlPagm = StreamUtils.createObject(stream,
                  new String[] { "[PAGM]" }, new String[] { role });
            buffer.append(xmlPagm);
            buffer.append('\n');
         }

         String pagmsOk = buffer.toString();
         LOGGER.debug("Fin du pré-traitement des PAGMs. Sortie={}", pagmsOk);
         return pagmsOk;

      } finally {

         try {
            if (stream != null) {
               stream.close();
            }

         } catch (IOException exception) {
            exception.printStackTrace();
         }
      }

   }

   /**
    * instanciation d'un jeton SAML 2.0 signé<br>
    * <br>
    * appel de la méthode {@link #createAssertion20(String)} pour instancier le
    * jeton<br>
    * appel de la méthode {@link XmlSignature#signeXml} pour la signature
    * 
    * @param issuer
    *           valeur de [ISSUER]
    * @param roles
    *           valeurs de [PAGMS]
    * @param notAfter
    *           valeur de [NotOnOrAfter]
    * @param notBefore
    *           valeur de [NotBefore]
    * @param actual
    *           valeur de [AuthnInstant]
    * @param identifiant
    *           valeur de [AssertionID]
    * @param keystore
    *           keystore
    * @param alias
    *           alias de la clé privée
    * @param password
    *           mot de passe de la clé privée
    * @return jeton signé SAML 2.0
    * @throws XmlSignatureException
    *            exception lors de la signature
    */
   public final String createAssertion20(String issuer, String login, List<String> roles,
         DateTime notAfter, DateTime notBefore, DateTime actual,
         UUID identifiant, KeyStore keystore, String alias, String password)
         throws XmlSignatureException {

      LOGGER.debug("Création d'une assertion SAML (pas encore signée)");
      String assertion = createAssertion20(issuer, login, roles, notAfter, notBefore,
            actual, identifiant);

      LOGGER.debug("Signature d'une assertion SAML");
      return XmlSignature.signeXml(IOUtils.toInputStream(assertion), keystore,
            alias, password);

   }

}

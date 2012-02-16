package sae.client.demo.webservice.security.ws;

import java.io.InputStream;
import java.security.KeyStore;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;

import sae.client.demo.util.ResourceUtils;
import sae.client.demo.webservice.security.signature.XmlSignature;
import sae.client.demo.webservice.security.signature.exception.XmlSignatureException;
import sae.client.demo.webservice.security.util.StreamUtils;


/**
 * Classe de service pour les jeton SAML 2.0
 * 
 * 
 */
public class SAML20Service {

   private static final String SAML_20 = "security/saml20.xml";

   /**
    * instanciation d'un jeton SAML 2.0 sur le format du fichier
    * "security/saml20.xml"<br>
    * <br>
    * Les valeurs entre [] sont remplacées par les valeurs passées en argument
    * de la méthode
    * 
    * @param issuer
    *           valeur de [Issuer]
    * @param role
    *           valeur de [PAGM]
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
   public final String createAssertion20(String issuer, String role,
         DateTime notAfter, DateTime notBefore, DateTime actual,
         UUID identifiant) {

      InputStream assertionStream = ResourceUtils.loadResource(this, SAML_20);

      String[] searchList = new String[] { "[Issuer]", "[PAGM]",
            "[NotOnOrAfter]", "[NotBefore]", "[AssertionID]", "[AuthnInstant]" };

      String[] replacementList = new String[] { issuer, role,
            notAfter.toString(), notBefore.toString(), identifiant.toString(),
            actual.toString() };

      return StreamUtils.createObject(assertionStream, searchList,
            replacementList);

   }

   /**
    * instanciation d'un jeton SAML 2.0 signé<br>
    * <br>
    * appel de la méthode {@link #createAssertion20(String)} pour instancier le
    * jeton<br>
    * appel de la méthode {@link XmlSignature#signeXml} pour la signature
    * 
    * @param issuer
    *           valeur de [Issuer] 
    * @param role
    *           valeur de [PAGM]
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
   public final String createAssertion20(String issuer, String role,
         DateTime notAfter, DateTime notBefore, DateTime actual,
         UUID identifiant, KeyStore keystore, String alias, String password)
         throws XmlSignatureException {

      String assertion = createAssertion20(issuer, role, notAfter, notBefore,
            actual, identifiant);

      return XmlSignature.signeXml(IOUtils.toInputStream(assertion), keystore,
            alias, password);

   }

}

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
    * @param login
    *           login de l'utilisateur demandeur du service
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
   public final String createAssertion20(final String issuer, final String login, final List<String> roles,
                                         final DateTime notAfter, final DateTime notBefore, final DateTime actual,
                                         final UUID identifiant) {

      LOGGER
            .debug(
                   "Génération d'une assertion SAML avec les paramètres suivants: issuer={}, login={}, roles={}, notAfter={}, notBefore={}, actual={}, identifiant={}",
                   new Object[] {issuer, login, roles, notAfter, notBefore, actual,
                                 identifiant});

      final String pagms = createPagm(roles);
      LOGGER.debug("PAGM(s) retravaillé(s): {}", pagms);

      final InputStream assertionStream = ResourceUtils.loadResource(this, SAML_20);

      final String[] searchList = new String[] {"[ISSUER]", "[LOGIN]", "[PAGMS]",
                                                "[NotOnOrAfter]", "[NotBefore]", "[AssertionID]", "[AuthnInstant]"};

      final String[] replacementList = new String[] {issuer, login, pagms,
                                                     notAfter.toString(), notBefore.toString(), identifiant.toString(),
                                                     actual.toString()};

      final String assertion = StreamUtils.createObject(assertionStream,
                                                        searchList,
                                                        replacementList);

      LOGGER.debug("Assertion SAML générée: {}", assertion);
      return assertion;

   }

   private final String createPagm(final List<String> roles) {

      LOGGER.debug("Début du pré-traitement des PAGMs. Entrée={}", roles);

      InputStream stream = null;
      final StringBuffer buffer = new StringBuffer();
      buffer.append("<saml2:Attribute Name=\"PAGM\">");
      buffer.append('\n');
      String xmlPagm;
      try {
         for (final String role : roles) {
            stream = ResourceUtils.loadResource(this, PAGM);
            xmlPagm = StreamUtils.createObject(stream,
                                               new String[] {"[PAGM]"},
                                               new String[] {role});
            buffer.append(xmlPagm);
         }
         buffer.append("</saml2:Attribute>");
         buffer.append('\n');

         final String pagmsOk = buffer.toString();
         LOGGER.debug("Fin du pré-traitement des PAGMs. Sortie={}", pagmsOk);
         return pagmsOk;

      }
      finally {

         try {
            if (stream != null) {
               stream.close();
            }

         }
         catch (final IOException exception) {
            exception.printStackTrace();
         }
      }

   }

   /**
    * instanciation d'un jeton SAML 2.0 signé<br>
    * <br>
    * appel de la méthode {@link SAML20Service#createAssertion20} pour instancier le
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
   public final String createAssertion20(final String issuer, final String login, final List<String> roles,
                                         final DateTime notAfter, final DateTime notBefore, final DateTime actual,
                                         final UUID identifiant, final KeyStore keystore, final String alias, final String password)
         throws XmlSignatureException {

      LOGGER.debug("Création d'une assertion SAML (pas encore signée)");
      final String assertion = createAssertion20(issuer,
                                                 login,
                                                 roles,
                                                 notAfter,
                                                 notBefore,
                                                 actual,
                                                 identifiant);

      LOGGER.debug("Signature d'une assertion SAML");
      return XmlSignature.signeXml(IOUtils.toInputStream(assertion),
                                   keystore,
                                   alias,
                                   password);

   }

}

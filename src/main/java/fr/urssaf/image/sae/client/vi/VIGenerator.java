package fr.urssaf.image.sae.client.vi;

import java.security.KeyStore;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import fr.urssaf.image.sae.client.vi.exception.ViSignatureException;
import fr.urssaf.image.sae.client.vi.exception.XmlSignatureException;
import fr.urssaf.image.sae.client.vi.util.XMLUtils;
import fr.urssaf.image.sae.client.vi.ws.SAML20Service;
import fr.urssaf.image.sae.client.vi.ws.WSSecurityService;

/**
 * Classe permettant de générer un VI (vecteur d'identification)
 */
public class VIGenerator {

   private static final Logger LOGGER = LoggerFactory.getLogger(VIGenerator.class);

   /**
    * VIGenerator est une classe statique
    */
   private VIGenerator() {
   }

   /**
    * Génération de l'en-tête WSSE
    * 
    * @param issuer
    *           valeur de [ISSUER]
    * @param login
    *           login de l'utilisateur qui accède au service
    * @param pagms
    *           valeurs de [PAGMS]
    * @param keystore
    *           keystore contenant la clé privée qui sera utilisée pour signer
    * @param alias
    *           alias de la clé privée qui sera utilisée pour signer
    * @param password
    *           mot de passe de la clé privée
    * @return l'en-tête à insérer dans le message
    */
   public static final String genererEnTeteWsse(final String issuer, final String login, final List<String> pagms,
                                                final KeyStore keystore, final String alias, final String password) {

      LOGGER.debug("Début génération en-tête wsse");

      // instanciation des paramètres du jeton SAML
      final DateTime systemDate = new DateTime();
      final UUID identifiant = UUID.randomUUID();

      // pour des questions de dérives d'horloges la période de début et de fin
      // de validé du jeton est de 2heures
      final DateTime notAfter = systemDate.plusHours(2);
      final DateTime notBefore = systemDate.minusHours(2);

      // Trace applicative
      LOGGER
            .debug(
                   "Paramètres pour la génération l'assertion: heure système={}, identifiant={}, notAfter={}, notBefore={}",
                   systemDate,
                   identifiant,
                   notAfter,
                   notBefore);

      // Génération du VI
      String assertion;
      try {

         final SAML20Service assertionService = new SAML20Service();

         assertion = assertionService.createAssertion20(issuer,
                                                        login,
                                                        pagms,
                                                        notAfter,
                                                        notBefore,
                                                        systemDate,
                                                        identifiant,
                                                        keystore,
                                                        alias,
                                                        password);

         LOGGER.debug("Assertion générée : {}", assertion);

      }
      catch (final XmlSignatureException exception) {
         throw new ViSignatureException(exception);
      }

      // Génération de l'en-tête WS-Security
      final WSSecurityService wsService = new WSSecurityService();
      final String wsseSecurity = wsService.createWSSEHeader(assertion, identifiant);
      LOGGER.debug("En-tête WS-Security générée : {}", wsseSecurity);

      // Renvoie l'en-tête wsse
      LOGGER.debug("Fin génération en-tête wsse");
      return wsseSecurity;
   }

   /**
    * Génération de l'en-tête WSSE, renvoyée au format Document XML
    * 
    * @param issuer
    *           valeur de [ISSUER]
    * @param login
    *           login de l'utilisateur qui accède au service
    * @param pagms
    *           valeurs de [PAGMS]
    * @param keystore
    *           keystore contenant la clé privée qui sera utilisée pour signer
    * @param alias
    *           alias de la clé privée qui sera utilisée pour signer
    * @param password
    *           mot de passe de la clé privée
    * @return l'en-tête à insérer dans le message
    */
   public static final Document getWsseHeader(final String issuer, final String login, final List<String> pagms,
                                              final KeyStore keystore, final String alias, final String password) {
      final String wsse = VIGenerator.genererEnTeteWsse(issuer, login, pagms, keystore, alias, password);
      return XMLUtils.createXMLDocumentFromString(wsse);
   }

}

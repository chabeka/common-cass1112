package fr.urssaf.image.sae.integration.ihmweb.saeservice.security;

import java.io.IOException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.signature.XmlSignature;

/**
 * Service de génération de VI
 */
@Service
public class ViService {

   private static Logger LOGGER = LoggerFactory.getLogger(ViService.class);
   
   private static final String PATH_MODELE_SAML = "viModeles/modele_assertion_saml2.xml";
   private static final String PATH_MODELE_WSSE = "viModeles/modele_wsseSecurity.xml";
   
   private static final String PATH_PKCS12 = "certificats/PNR_Application_Test.p12";
   private static final String KEYSTORE_PASSWORD = "QEtDiGuGuEnZ";
   private static final String KEYSTORE_ALIAS = "1";
   
   private static final String DEFAULT_RECIPIENT = "urn:URSSAF";
   private static final String DEFAULT_AUDIENCE = "http://sae.urssaf.fr";
   private static final String DEFAULT_ISSUER = "SaeIntegration";
   private static final String[] DEFAULT_PAGM = new String[] {"ACCES_FULL_PAGM"};
   private static final String DEFAULT_METHODAUTHN2 = "urn:oasis:names:tc:SAML:2.0:ac:classes:unspecified";
   
   
   
   private KeyStore keystore;
   
   
   /**
    * Constructeur
    */
   public ViService() {
      
      keystore = chargeKeyStore();
      
   }
   
   /**
    * Génération d'un VI de type tel que demandé en paramètres d'entrée
    * @param viStyle le type de VI requis
    * @return le VI généré
    */
   public String generationVi(ViStyle viStyle) {
      return generationVi(viStyle, null);
   }
   
   /**
    * Génération d'un VI de type tel que demandé en paramètres d'entrée
    * @param viStyle le type de VI requis
    * @param viParams les paramètres éventuels du VI
    * @return le VI généré
    */
   public String generationVi(ViStyle viStyle, ViFormulaire viParams) {
      
      
      // Sans VI
      if (ViStyle.VI_SANS.equals(viStyle)) {
         return StringUtils.EMPTY;
      } 
      
      // VI OK
      else if (ViStyle.VI_OK.equals(viStyle)) {
         return generationVi_Ok(viParams); 
      }
      
      // VI provoquant la Soap Fault wsse:SecurityTokenUnavailable
      else if (ViStyle.VI_SF_wsse_SecurityTokenUnavailable.equals(viStyle)) {
         return generationVi_SoapFault_wsse_SecurityTokenUnavailable();
      }
      
      // VI provoquant la Soap Fault wsse:InvalidSecurityToken
      else if (ViStyle.VI_SF_wsse_InvalidSecurityToken.equals(viStyle)) {
         return generationVi_SoapFault_wsse_InvalidSecurityToken();
      }
      
      // VI provoquant la Soap Fault wsse:FailedCheck
      else if (ViStyle.VI_SF_wsse_FailedCheck.equals(viStyle)) {
         return generationVi_SoapFault_wsse_FailedCheck();
      }
      
      // VI provoquant la Soap Fault vi:InvalidVI
      else if (ViStyle.VI_SF_vi_InvalidVI.equals(viStyle)) {
         return generationVi_SoapFault_vi_InvalidVI();
      }
      
      // VI provoquant la Soap Fault vi:InvalidService
      else if (ViStyle.VI_SF_vi_InvalidService.equals(viStyle)) {
         return generationVi_SoapFault_vi_InvalidService();
      }
      
      // VI provoquant la Soap Fault vi:InvalidAuthLevel
      else if (ViStyle.VI_SF_vi_InvalidAuthLevel.equals(viStyle)) {
         return generationVi_SoapFault_vi_InvalidAuthLevel();
      }
      
      // VI provoquant la Soap Fault sae:DroitsInsuffisants
      else if (ViStyle.VI_SF_sae_DroitsInsuffisants.equals(viStyle)) {
         return generationVi_SoapFault_sae_DroitsInsuffisants();
      }
      
      // Autres VI non implémentés (vérification technique)
      else {
         throw new IntegrationRuntimeException("Le VI de type " + viStyle + " n'est pas implémenté");
      }
   }
   
   
   private String generationVi_Ok(ViFormulaire viParams) {
      
      DateTime systemDate = new DateTime();
      
      String assertionId = UUID.randomUUID().toString();
      String authnInstant = defaultAuthnInstant(systemDate);
      String notOnOrAfter = defaultNotOnOrAfter(systemDate);
      String notBefore = defaultNotBefore(systemDate);
      String methodAuthn2 = DEFAULT_METHODAUTHN2;
      
      String issuer;
      String recipient;
      String audience;
      String[] pagm;
      
      if ((viParams==null) || (StringUtils.isBlank(viParams.getIssuer()))) {
         
         issuer = DEFAULT_ISSUER;
         recipient = DEFAULT_RECIPIENT;
         audience = DEFAULT_AUDIENCE;

         pagm = DEFAULT_PAGM ;
         
      } else {
         
         issuer = viParams.getIssuer();
         recipient = viParams.getRecipient();
         audience = viParams.getAudience();
         
         PagmList pagmList = viParams.getPagms();
         pagm = (String[])viParams.getPagms().toArray(new String[pagmList.size()]);
         
      }
      
      
      return generationVi(
            assertionId,
            issuer,
            recipient,
            audience,
            authnInstant,
            notOnOrAfter,
            notBefore,
            pagm,
            methodAuthn2);
         
   }
   
   
   private String defaultAuthnInstant(DateTime systemDate) {
      return systemDate.toString();
   }
   
   private String defaultNotOnOrAfter(DateTime systemDate) {
      return systemDate.plusHours(2).toString();
   }
   
   private String defaultNotBefore(DateTime systemDate) {
      return systemDate.minusHours(2).toString();
   }
   
   /**
    * Génération d'un vecteur d'identification valide,
    * pour le mode application à application.<br>
    * <br>
    * Inclus l'en-tête WS-Security<br>
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
    *    <li>[PAGM]         => Les PAGM</li>
    *    <li>[MethodAuthn2] => La méthode d'authentification de l'utilisateur sur le SI de l'organisme client</li>
    * </ul>
    * 
    * @return le VI
    * 
    */
   private String generationVi(
         String assertionId,
         String issuer,
         String recipient,
         String audience,
         String authnInstant,
         String notOnOrAfter,
         String notBefore,
         String[] pagm,
         String methodAuthn2) {
      
      try {
      
         String assertionNonSignee = getAssertionNonSignee(
               assertionId,
               issuer,
               recipient,
               audience,
               authnInstant,
               notOnOrAfter,
               notBefore,
               pagm,
               methodAuthn2);
         
         String assertionSignee = getAssertionSignee(assertionNonSignee);
         
         String enTestWsSecurity = getEnTestWsSecurity(assertionSignee,assertionId); 
         
         return enTestWsSecurity;
         
       
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
         String[] pagms,
         String methodAuthn2) throws IOException {
      
      ClassPathResource resource = new ClassPathResource(PATH_MODELE_SAML); 
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
      assertion = StringUtils.replace(assertion, "[PAGM]", pagmsAplat);
      assertion = StringUtils.replace(assertion, "[MethodAuthn2]", methodAuthn2);
      
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
      
         String alias = KEYSTORE_ALIAS ;
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
         
         ClassPathResource resource = new ClassPathResource(PATH_PKCS12);
         
         KeyStore keystore = KeyStore.getInstance("PKCS12");
         
         keystore.load(resource.getInputStream(), KEYSTORE_PASSWORD.toCharArray());
         
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
      
         ClassPathResource resource = new ClassPathResource(PATH_MODELE_WSSE); 
         List<String> lines = IOUtils.readLines(resource.getInputStream());
         String enTestWsSecurity = StringUtils.join(lines, "\r\n");
         
         enTestWsSecurity = StringUtils.replace(enTestWsSecurity, "[Assertion]", assertionSignee);
         enTestWsSecurity = StringUtils.replace(enTestWsSecurity, "[AssertionID]", assertionId);
         
         return enTestWsSecurity; 
      
      
      } catch (Exception ex) {
         throw new IntegrationRuntimeException(ex);
      }
      
   }
   
   
   /**
    * Génération du VI pour provoquer la SoapFault wsse:SecurityTokenUnavailable<br>
    * <br>
    * Pour faire cela, on génère un en-tête WS-Security vide, c'est à dire
    * sans assertion SAML
    * 
    * @return le VI
    */
   private String generationVi_SoapFault_wsse_SecurityTokenUnavailable() {
      
      LOGGER.debug("Début de la génération d'un VI wsse:SecurityTokenUnavailable");
      
      StringBuilder sBuilder = new StringBuilder();
      
      sBuilder.append("<wsse:Security");
      sBuilder.append(" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"");
      sBuilder.append(" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">");
      sBuilder.append("</wsse:Security>");
      
      String vi = sBuilder.toString();
      
      LOGGER.debug("Fin de la génération d'un VI wsse:SecurityTokenUnavailable");
      return vi ;
      
   }
   
   
   /**
    * Génération du VI pour provoquer la SoapFault wsse:InvalidSecurityToken<br>
    * <br>
    * Pour faire cela, on génère un VI OK standard, à l'exception de l'issuer qui
    * est laissé vide.
    * 
    * @return le VI
    */
   private String generationVi_SoapFault_wsse_InvalidSecurityToken() {
      
      LOGGER.debug("Début de la génération d'un VI wsse:InvalidSecurityToken");
      
      DateTime systemDate = new DateTime();
      
      String assertionId = UUID.randomUUID().toString();
      String issuer = StringUtils.EMPTY;
      String recipient = DEFAULT_RECIPIENT;
      String audience = DEFAULT_AUDIENCE;
      String authnInstant = defaultAuthnInstant(systemDate);
      String notOnOrAfter = defaultNotOnOrAfter(systemDate);
      String notBefore = defaultNotBefore(systemDate);
      String[] pagm = DEFAULT_PAGM ;
      String methodAuthn2 = DEFAULT_METHODAUTHN2;
      
      String vi = generationVi(
            assertionId,
            issuer,
            recipient,
            audience,
            authnInstant,
            notOnOrAfter,
            notBefore,
            pagm,
            methodAuthn2);
      
      LOGGER.debug("Fin de la génération d'un VI wsse:InvalidSecurityToken");
      return vi;
      
   }
   
   
   /**
    * Génération du VI pour provoquer la SoapFault wsse:FailedCheck<br>
    * <br>
    * Pour faire cela, on génère un VI OK standard, puis on modifie la valeur 
    * de l'issuer dans le VI
    * 
    * @return le VI
    */
   private String generationVi_SoapFault_wsse_FailedCheck() {
      
      // Log
      LOGGER.debug("Début de la génération d'un VI wsse:FailedCheck");
      
      // Appel de la méthode générant un VI OK
      String vi = generationVi_Ok(null);
      
      // Effectue un rechercher/remplacer
      // On remplace
      //   <saml2:Issuer>SaeIntegration</saml2:Issuer>
      // par
      //   <saml2:Issuer>SaeIntegration2</saml2:Issuer>
      String searchString = "<saml2:Issuer>" + DEFAULT_ISSUER + "</saml2:Issuer>";
      String replacement = "<saml2:Issuer>SaeIntegration2</saml2:Issuer>";
      vi = StringUtils.replaceOnce(vi, searchString, replacement);
      
      // Renvoi du VI avec la valeur de la signature truandée
      LOGGER.debug("Fin de la génération d'un VI wsse:FailedCheck");
      return vi;
      
   }
   
   /**
    * Génération du VI pour provoquer la SoapFault vi:InvalidVI<br>
    * <br>
    * Pour faire cela, on génère un VI qui n'est plus valide dans le temps
    * 
    * @return le VI
    */
   private String generationVi_SoapFault_vi_InvalidVI() {
      
      // Log
      LOGGER.debug("Début de la génération d'un VI vi:InvalidVI");
      
      // Appel de la méthode générant un VI OK
      DateTime systemDate = new DateTime();
      String assertionId = UUID.randomUUID().toString();
      String issuer = DEFAULT_ISSUER;
      String recipient = DEFAULT_RECIPIENT;
      String audience = DEFAULT_AUDIENCE;
      String authnInstant = defaultAuthnInstant(systemDate);
      String notOnOrAfter = systemDate.minusHours(1).toString();
      String notBefore = systemDate.minusHours(2).toString();
      String[] pagm = DEFAULT_PAGM ;
      String methodAuthn2 = DEFAULT_METHODAUTHN2;
      String vi = generationVi(
            assertionId,
            issuer,
            recipient,
            audience,
            authnInstant,
            notOnOrAfter,
            notBefore,
            pagm,
            methodAuthn2);
      
      // Renvoi du VI
      LOGGER.debug("Fin de la génération d'un VI vi:InvalidVI");
      return vi;
      
   }
   
   /**
    * Génération du VI pour provoquer la SoapFault vi:InvalidService<br>
    * <br>
    * Pour faire cela, on génère un VI dont le service visé (Audience) est
    * http://service.inexistant.fr 
    * 
    * @return le VI
    */
   private String generationVi_SoapFault_vi_InvalidService() {
      
      // Log
      LOGGER.debug("Début de la génération d'un VI vi:InvalidService");
      
      // Appel de la méthode générant un VI OK
      DateTime systemDate = new DateTime();
      String assertionId = UUID.randomUUID().toString();
      String issuer = DEFAULT_ISSUER;
      String recipient = DEFAULT_RECIPIENT;
      String audience = "http://service.inexistant.fr"; // Service visé qui n'existe pas
      String authnInstant = defaultAuthnInstant(systemDate);
      String notOnOrAfter = defaultNotOnOrAfter(systemDate);
      String notBefore = defaultNotBefore(systemDate);
      String[] pagm = DEFAULT_PAGM ;
      String methodAuthn2 = DEFAULT_METHODAUTHN2;
      String vi = generationVi(
            assertionId,
            issuer,
            recipient,
            audience,
            authnInstant,
            notOnOrAfter,
            notBefore,
            pagm,
            methodAuthn2);
      
      // Renvoi du VI
      LOGGER.debug("Fin de la génération d'un VI vi:InvalidService");
      return vi;
      
   }
   
   /**
    * Génération du VI pour provoquer la SoapFault vi:InvalidAuthLevel<br>
    * <br>
    * Pour faire cela, on génère un VI dont la méthode d'authentification côté
    * client (MethodAuthn2) est inconnue du SAE
    * 
    * @return le VI
    */
   private String generationVi_SoapFault_vi_InvalidAuthLevel() {
      
      // Log
      LOGGER.debug("Début de la génération d'un VI vi:InvalidAuthLevel");
      
      // Appel de la méthode générant un VI OK
      DateTime systemDate = new DateTime();
      String assertionId = UUID.randomUUID().toString();
      String issuer = DEFAULT_ISSUER;
      String recipient = DEFAULT_RECIPIENT;
      String audience = DEFAULT_AUDIENCE;
      String authnInstant = defaultAuthnInstant(systemDate);
      String notOnOrAfter = defaultNotOnOrAfter(systemDate);
      String notBefore = defaultNotBefore(systemDate);
      String[] pagm = DEFAULT_PAGM ;
      String methodAuthn2 = "urn:methode:inexistante"; // Méthode d'authentification inconnue du SAE
      String vi = generationVi(
            assertionId,
            issuer,
            recipient,
            audience,
            authnInstant,
            notOnOrAfter,
            notBefore,
            pagm,
            methodAuthn2);
      
      // Renvoi du VI
      LOGGER.debug("Fin de la génération d'un VI vi:InvalidAuthLevel");
      return vi;
      
   }
   
   /**
    * Génération du VI pour provoquer la SoapFault sae:DroitsInsuffisants<br>
    * <br>
    * Pour faire cela, on génère un VI dont les PAGM ne permettent pas de faire 
    * l'opération pingSecure
    * 
    * @return le VI
    */
   private String generationVi_SoapFault_sae_DroitsInsuffisants() {
      
      // Log
      LOGGER.debug("Début de la génération d'un VI sae:DroitsInsuffisants");
      
      // Appel de la méthode générant un VI OK
      DateTime systemDate = new DateTime();
      String assertionId = UUID.randomUUID().toString();
      String issuer = DEFAULT_ISSUER;
      String recipient = DEFAULT_RECIPIENT;
      String audience = DEFAULT_AUDIENCE;
      String authnInstant = defaultAuthnInstant(systemDate);
      String notOnOrAfter = defaultNotOnOrAfter(systemDate);
      String notBefore = defaultNotBefore(systemDate);
      String[] pagm = new String[] {"ROLE_INEXISTANT;FULL"}; ;
      String methodAuthn2 = DEFAULT_METHODAUTHN2;
      String vi = generationVi(
            assertionId,
            issuer,
            recipient,
            audience,
            authnInstant,
            notOnOrAfter,
            notBefore,
            pagm,
            methodAuthn2);
      
      // Renvoi du VI
      LOGGER.debug("Fin de la génération d'un VI sae:DroitsInsuffisants");
      return vi;
      
   }
   
}

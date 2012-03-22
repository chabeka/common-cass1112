package fr.urssaf.image.sae.saml.opensaml.service;

import java.net.URI;
import java.security.PrivateKey;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.Subject;
import org.opensaml.xml.security.x509.BasicX509Credential;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.ValidatorSuite;

import fr.urssaf.image.sae.saml.data.SamlAssertionData;
import fr.urssaf.image.sae.saml.exception.SamlExtractionException;
import fr.urssaf.image.sae.saml.exception.SamlFormatException;
import fr.urssaf.image.sae.saml.exception.signature.keyinfo.SamlKeyInfoException;
import fr.urssaf.image.sae.saml.opensaml.SamlConfiguration;
import fr.urssaf.image.sae.saml.opensaml.signature.SamlSignatureSignService;
import fr.urssaf.image.sae.saml.opensaml.signature.SamlSignatureUtils;
import fr.urssaf.image.sae.saml.params.SamlAssertionParams;
import fr.urssaf.image.sae.saml.params.SamlCommonsParams;
import fr.urssaf.image.sae.saml.util.ConverterUtils;

/**
 * Classe de service du jeton SAML 2.0 dans l'application<br>
 * <br>
 * Le recours à cette classe nécessite une instanciation au préalable de
 * {@link fr.urssaf.image.sae.saml.opensaml.SamlConfiguration}
 * 
 */
public class SamlAssertionService {

   private final SamlCoreService coreService;

   private final SamlSignatureSignService signatureService;

   /**
    * instanciation de :
    * <ul>
    * <li>{@link SamlCoreService}: lecture/écriture du jeton SAML</li>
    * <li>{@link SamlSignatureSignService} : écriture/vérification de la signature</li>
    * </ul>
    */
   public SamlAssertionService() {
      new SamlConfiguration();
      coreService = new SamlCoreService();
      signatureService = new SamlSignatureSignService();
   }

   /**
    * Ecriture d'un jeton SAML
    * 
    * @param assertionParams
    *           valeurs du contenu du jeton SAML
    * @return une instance du jeton SAML
    */
   public final Assertion write(SamlAssertionParams assertionParams) {

      DateTime systemDate = new DateTime();

      // ASSERTION
      DateTime issueInstant = assertionParams.getCommonsParams()
            .getIssueInstant() == null ? systemDate : new DateTime(
            assertionParams.getCommonsParams().getIssueInstant());

      UUID identifiant = assertionParams.getCommonsParams().getId() == null ? UUID
            .randomUUID()
            : assertionParams.getCommonsParams().getId();

      Assertion assertion = coreService.createAssertion(identifiant.toString(),
            issueInstant);

      // ISSUER
      Issuer issuer = coreService.createIssuer(assertionParams
            .getCommonsParams().getIssuer());
      assertion.setIssuer(issuer);

      // SUBJECT
      DateTime notOnOrAfter = new DateTime(assertionParams.getCommonsParams()
            .getNotOnOrAfter());

      Subject subject = coreService.createSubject(assertionParams
            .getSubjectId2(), assertionParams.getSubjectFormat2()
            .toASCIIString(), notOnOrAfter, assertionParams.getRecipient()
            .toASCIIString());
      assertion.setSubject(subject);

      // CONDITION

      DateTime notBefore = new DateTime(assertionParams.getCommonsParams()
            .getNotOnBefore());

      Conditions conditions = coreService.createConditions(notBefore,
            notOnOrAfter, assertionParams.getCommonsParams().getAudience()
                  .toASCIIString());
      assertion.setConditions(conditions);

      // AUTHNSTATEMENT

      DateTime authnInstant = assertionParams.getCommonsParams()
            .getIssueInstant() == null ? systemDate : new DateTime(
            assertionParams.getCommonsParams().getAuthnInstant());

      AuthnStatement authnStatement = coreService.createAuthnStatement(
            authnInstant, identifiant.toString(), assertionParams
                  .getMethodAuthn2().toASCIIString());
      assertion.getAuthnStatements().add(authnStatement);

      // ATTRIBUTESTATEMENT

      AttributeStatement attrStatement = coreService
            .createAttributeStatementPAGM(assertionParams.getCommonsParams()
                  .getPagm());
      assertion.getAttributeStatements().add(attrStatement);

      return assertion;

   }

   /**
    * Chargement d'un jeton SAML dans une structure {@link amlAssertionData}
    * 
    * <pre>
    * exemple d'utilisation
    * 
    * // parsing d'un jeton SAML 
    * Element element = XMLUtils.parse(assertionSaml);
    * 
    * //instanciation du jeton SAML
    * Assertion assertion = (Assertion) SamlXML.unmarshaller(element);
    * 
    * //Lecture des valeurs du jeton
    * this.load(assertion);
    * </pre>
    * 
    * @param assertion
    *           jeton SAML
    * @return structure contenant les valeurs du jeton SAML
    */
   public final SamlAssertionData load(Assertion assertion) {

      SamlAssertionData data = new SamlAssertionData();

      // initialisation de SamlAssertionParams
      SamlAssertionParams assertionParams = new SamlAssertionParams();
      data.setAssertionParams(assertionParams);

      // METHOD AUTHN 2
      URI methodAuthn2 = ConverterUtils.uri(coreService
            .loadAuthnContextClassRef(assertion));
      assertionParams.setMethodAuthn2(methodAuthn2);

      // RECIPIENT
      URI recipient = ConverterUtils.uri(coreService.loadRecipient(assertion));
      assertionParams.setRecipient(recipient);

      // SUBJECT FORMAT 2
      URI subjectFormat2 = ConverterUtils.uri(coreService
            .loadSubjectFormat(assertion));
      assertionParams.setSubjectFormat2(subjectFormat2);

      // SUBJECT ID 2
      String subjectId2 = coreService.loadSubjectId(assertion);
      assertionParams.setSubjectId2(subjectId2);

      // initialisation de SamlCommonsParams
      SamlCommonsParams commonsParams = new SamlCommonsParams();
      assertionParams.setCommonsParams(commonsParams);

      // ISSUE INSTANT
      Date issueInstant = ConverterUtils.date(coreService
            .loadIssueInstant(assertion));
      commonsParams.setIssueInstant(issueInstant);

      // ID
      // UUID uuid = ConverterUtils.uuid(coreService.loadID(assertion));
      UUID uuid;
      String assertionId = StringUtils.EMPTY;
      try {
         assertionId = coreService.loadID(assertion);
         uuid = ConverterUtils.uuid(assertionId);
      } catch (IllegalArgumentException ex) {
         throw new SamlExtractionException(
               String
                     .format(
                           "L'ID de l'assertion doit être un UUID correct (ce qui n'est pas le cas de '%s')",
                           assertionId), ex);
      }
      commonsParams.setId(uuid);
      
      // ISSUER
      commonsParams.setIssuer(coreService.loadIssuer(assertion));

      // AUDIENCE
      URI audience = ConverterUtils.uri(coreService.loadAudience(assertion));
      commonsParams.setAudience(audience);

      // AUTHINSTANT
      Date authnInstant = ConverterUtils.date(coreService
            .loadAuthInstant(assertion));
      commonsParams.setAuthnInstant(authnInstant);

      // NOT BEFORE
      Date notOnBefore = ConverterUtils.date(coreService
            .loadNotBefore(assertion));
      commonsParams.setNotOnBefore(notOnBefore);

      // NOT ON OR AFTER
      Date notOnOrAfter = ConverterUtils.date(coreService
            .loadNotOnOrAfter(assertion));
      commonsParams.setNotOnOrAfter(notOnOrAfter);

      // PAGM
      List<String> pagm = coreService.loadPagm(assertion);
      commonsParams.setPagm(pagm);

      // Récupération de la clé publique
      if (assertion.getSignature()==null) {
         throw new SamlExtractionException("La signature est absente de l'assertion SAML");
      }
      java.security.cert.X509Certificate publicKeyNatif;
      try {
         publicKeyNatif = SamlSignatureUtils.getPublicKeyNatif(assertion.getSignature());
         data.setClePublique(publicKeyNatif);
      } catch (SamlKeyInfoException e) {
         throw new SamlExtractionException(e);
      }
      
      return data;
      
   }

   /**
    * Méthode de validation d'un jeton SAML<br>
    * <br>
    * La validation appelle la méthode {@link Configuration#getValidatorSuite}<br>
    * <br>
    * La méthode utilise les suites de validations spécifiés dans le schéma '
    * <code>saml2-core-validation-config-xml</code>' présent dans la librairie
    * OpenSAML
    * <ul>
    * <li><code>saml2-core-schema-validator</code></li>
    * <li><code>saml2-core-spec-validator</code></li>
    * </ul>
    * 
    * @param assertion
    *           jeton SAML
    * @throws SamlFormatException
    *            le jeton n'est pas valide
    */
   public final void validate(Assertion assertion) throws SamlFormatException {

      try {
         validate(assertion, "saml2-core-schema-validator");
      } catch (ValidationException e) {
         throw new SamlFormatException(e);
      }

      try {
         validate(assertion, "saml2-core-spec-validator");
      } catch (ValidationException e) {
         throw new SamlFormatException(e);
      }

   }

   private void validate(Assertion assertion, String suiteId)
         throws ValidationException {

      ValidatorSuite validatorSuite = Configuration.getValidatorSuite(suiteId);

      validatorSuite.validate(assertion);
   }

   /**
    * Méthode de signature du jeton SAML
    * 
    * @param assertion
    *           jeton SAML à signer
    * @param x509Certificate
    *           certificat pour la signature du jeton
    * @param privatekey
    *           clé privé pour la signature du jeton
    * @param certs
    *           chaines des certificats pour la signature du jeton
    */
   public final void sign(
         Assertion assertion, 
         java.security.cert.X509Certificate x509Certificate,
         PrivateKey privatekey, 
         Collection<java.security.cert.X509Certificate> certs) {

      BasicX509Credential x509Credential = new BasicX509Credential();
      x509Credential.setEntityCertificate(x509Certificate);
      x509Credential.setPrivateKey(privatekey);
      x509Credential.setEntityCertificateChain(certs);

      signatureService.sign(assertion, x509Credential);
   }

}

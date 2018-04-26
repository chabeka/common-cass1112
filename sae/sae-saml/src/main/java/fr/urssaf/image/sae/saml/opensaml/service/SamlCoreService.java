package fr.urssaf.image.sae.saml.opensaml.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AttributeValue;
import org.opensaml.saml2.core.Audience;
import org.opensaml.saml2.core.AudienceRestriction;
import org.opensaml.saml2.core.AuthnContext;
import org.opensaml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.SubjectConfirmation;
import org.opensaml.saml2.core.SubjectConfirmationData;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.XMLObjectBuilder;
import org.opensaml.xml.schema.XSAny;

import fr.urssaf.image.sae.saml.opensaml.SamlXML;
import fr.urssaf.image.sae.saml.util.ListUtils;

/**
 * Classe de service du jeton SAML 2.0<br>
 * <br>
 * <ul>
 * <li><code>create*</code> : instancier des balises du jeton SAML</li>
 * <li><code>load*</code> : lecture d'une valeur dans le jeton SAML</li>
 * </ul>
 * 
 * Le recours à cette classe nécessite une instanciation au préalable de
 * {@link fr.urssaf.image.sae.saml.opensaml.SamlConfiguration}
 * 
 * 
 */
@SuppressWarnings("PMD.TooManyMethods")
public class SamlCoreService {

   /**
    * Création de la balise <code>&lt;AttributeStatement></code>
    * 
    * <pre>
    *  &lt;saml:AttributeStatement>
    *         &lt;saml:Attribute Name="PAGM">
    *             &lt;saml:AttributeValue>PAGM_1&lt;/saml:AttributeValue>
    *             &lt;saml:AttributeValue>PAGM_2&lt;/saml:AttributeValue>
    *         &lt;/saml:Attribute>
    * &lt;/saml:AttributeStatement>
    * </pre>
    * 
    * Les pagms non renseignés sont filtrés tels que null, "" et "  "
    * 
    * @param pagms
    *           Liste des PAGM
    * @return balise XML
    */
   @SuppressWarnings("unchecked")
   public final AttributeStatement createAttributeStatementPAGM(
         List<String> pagms) {

      AttributeStatement attrStatement = SamlXML
            .create(AttributeStatement.DEFAULT_ELEMENT_NAME);

      Attribute attribute = SamlXML.create(Attribute.DEFAULT_ELEMENT_NAME);
      attribute.setName("PAGM");

      for (String pagm : ListUtils.filter(pagms)) {

         XMLObjectBuilder<XSAny> builder = Configuration.getBuilderFactory()
               .getBuilder(XSAny.TYPE_NAME);

         XSAny value = builder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME);
         value.setTextContent(pagm);
         attribute.getAttributeValues().add(value);

      }

      attrStatement.getAttributes().add(attribute);

      return attrStatement;
   }

   /**
    * Création de la balise <code>&lt;AuthnStatement></code>
    * 
    * <pre>
    * 
    * &lt;saml:AuthnStatement AuthnInstant="<i><b>[authnInstant]</b></i>" SessionIndex="<i><b>[sessionIndex]</b></i>">
    *         &lt;saml:AuthnContext>
    *             &lt;saml:AuthnContextClassRef><i><b>[classrefValue]</b></i>&lt;/saml:AuthnContextClassRef>
    *         &lt;/saml:AuthnContext>
    * &lt;/saml:AuthnStatement>
    * 
    * </pre>
    * 
    * @param authnInstant
    *           Instant d'authentification de l'usager sur le SI
    * @param sessionIndex
    *           Identifiant unique de l'assertion
    * @param classrefValue
    *           Méthode d'authentification de l'utilisateur sur le SI de
    *           l'organisme client
    * @return balise XML
    */
   public final AuthnStatement createAuthnStatement(DateTime authnInstant,
         String sessionIndex, String classrefValue) {

      AuthnStatement authnStatement = SamlXML
            .create(AuthnStatement.DEFAULT_ELEMENT_NAME);
      authnStatement.setAuthnInstant(authnInstant);
      authnStatement.setSessionIndex(sessionIndex);

      AuthnContext authncontext = SamlXML
            .create(AuthnContext.DEFAULT_ELEMENT_NAME);
      AuthnContextClassRef classref = SamlXML
            .create(AuthnContextClassRef.DEFAULT_ELEMENT_NAME);
      classref.setAuthnContextClassRef(classrefValue);
      authncontext.setAuthnContextClassRef(classref);
      authnStatement.setAuthnContext(authncontext);

      return authnStatement;
   }

   /**
    * Création de la balise <code>&lt;Conditions></code>
    * 
    * <pre>
    * 
    * &lt;saml:Conditions NotBefore="<i><b>[notBefore]</b></i>" NotOnOrAfter="<i><b>[notOnOrAfter]</b></i>">
    *         &lt;saml:AudienceRestriction>
    *             &lt;saml:Audience><i><b>[audienceURI]</b></i>&lt;/saml:Audience>
    *         &lt;/saml:AudienceRestriction>
    * &lt;/saml:Conditions>
    * 
    * </pre>
    * 
    * @param notBefore
    *           Date de début de validité de l'assertion
    * @param notOnOrAfter
    *           Date d'expiration de l'assertion
    * @param audienceURI
    *           URI décrivant le service visé
    * @return balise XML
    */
   public final Conditions createConditions(DateTime notBefore,
         DateTime notOnOrAfter, String audienceURI) {

      Conditions conditions = SamlXML.create(Conditions.DEFAULT_ELEMENT_NAME);
      conditions.setNotBefore(notBefore);
      conditions.setNotOnOrAfter(notOnOrAfter);

      AudienceRestriction restriction = SamlXML
            .create(AudienceRestriction.DEFAULT_ELEMENT_NAME);
      Audience audience = SamlXML.create(Audience.DEFAULT_ELEMENT_NAME);
      audience.setAudienceURI(audienceURI);
      restriction.getAudiences().add(audience);
      conditions.getAudienceRestrictions().add(restriction);

      return conditions;
   }

   /**
    * Création de la balise <code>&lt;Assertion></code>
    * 
    * <pre>
    * 
    * &lt;saml:Assertion xmlns:saml="urn:oasis:names:tc:SAML:2.0:assertion" 
    *                    ID="<i><b>[i]</b></i>" 
    *                    IssueInstant="<i><b>[issueInstant]</b></i>" 
    *                    Version="2.0">
    * 
    * &lt;/saml:Assertion>
    * 
    * </pre>
    * 
    * @param id
    *           Identifiant unique de l'assertion
    * @param issueInstant
    *           Instant de génération de l’assertion
    * @return balise XML
    */
   @SuppressWarnings("PMD.ShortVariable")
   public final Assertion createAssertion(String id, DateTime issueInstant) {

      Assertion assertion = SamlXML.create(Assertion.DEFAULT_ELEMENT_NAME);
      assertion.setIssueInstant(issueInstant);
      assertion.setID(id);

      return assertion;

   }

   /**
    * Création de la balise <code>&lt;Issuer></code>
    * 
    * <pre>
    * 
    * &lt;saml:Issuer><i><b>[value]</b></i>&lt;/saml:Issuer>
    * 
    * </pre>
    * 
    * @param value
    *           Identifiant de l'émetteur de l'assertion
    * @return balise XML
    */
   public final Issuer createIssuer(String value) {

      Issuer issuer = SamlXML.create(Issuer.DEFAULT_ELEMENT_NAME);
      issuer.setValue(value);

      return issuer;
   }

   /**
    * Création de la balise <code>&lt;Subject></code>
    * 
    * <pre>
    * 
    * &lt;saml:Subject>
    *         &lt;saml:NameID Format="<i><b>[subjectFormat2]</b></i>"><i><b>[subjectId2]</b></i>&lt;/saml:NameID>
    *         &lt;saml:SubjectConfirmation Method="urn:oasis:names:tc:SAML:2.0:cm:bearer">
    *             &lt;saml:SubjectConfirmationData NotOnOrAfter="<i><b>[notOnOrAfter]</b></i>" Recipient="<i><b>[recipient]</b></i>"/>
    *         &lt;/saml:SubjectConfirmation>
    * &lt;/saml:Subject>
    * 
    * </pre>
    * 
    * @param subjectId2
    *           Identifiant de l’utilisateur
    * @param subjectFormat2
    *           Identifiant du format de l’identifiant du demandeur
    * @param notOnOrAfter
    *           Date d'expiration de l'assertion
    * @param recipient
    *           Identifiant de l'organisme fournisseur
    * @return balsie XML
    */
   public final Subject createSubject(String subjectId2, String subjectFormat2,
         DateTime notOnOrAfter, String recipient) {
      NameID nameID = SamlXML.create(NameID.DEFAULT_ELEMENT_NAME);
      nameID.setValue(subjectId2);
      nameID.setFormat(subjectFormat2);

      Subject subject = SamlXML.create(Subject.DEFAULT_ELEMENT_NAME);
      subject.setNameID(nameID);

      SubjectConfirmation confirmation = SamlXML
            .create(SubjectConfirmation.DEFAULT_ELEMENT_NAME);
      confirmation.setMethod("urn:oasis:names:tc:SAML:2.0:cm:bearer");

      SubjectConfirmationData data = SamlXML
            .create(SubjectConfirmationData.DEFAULT_ELEMENT_NAME);
      data.setNotOnOrAfter(notOnOrAfter);
      data.setRecipient(recipient);
      confirmation.setSubjectConfirmationData(data);

      subject.getSubjectConfirmations().add(confirmation);

      return subject;
   }

   /**
    * lecture de l'attribut 'IssueInstant' de la balise
    * <code>&lt;Assertion></code>
    * 
    * @param assertion
    *           jeton SAML
    * @return Instant de génération de l'assertion
    */
   public final DateTime loadIssueInstant(Assertion assertion) {

      return assertion.getIssueInstant();
   }

   /**
    * lecture de l'attribut 'ID' de la balise <code>&lt;Assertion></code>
    * 
    * @param assertion
    *           jeton SAML
    * @return Identifiant unique de l'assertion
    */
   public final String loadID(Assertion assertion) {

      return assertion.getID();
   }

   /**
    * lecture de la balise <code>&lt;Issuer></code>
    * 
    * @param assertion
    *           jeton SAML
    * @return Identifiant de l'émetteur de l'assertion
    */
   public final String loadIssuer(Assertion assertion) {

      return assertion.getIssuer() == null ? null : assertion.getIssuer()
            .getValue();
   }

   /**
    * lecture de la balise <code>&lt;Audience></code>
    * 
    * @param assertion
    *           jeton SAML
    * @return URI décrivant le service visé
    */
   public final String loadAudience(Assertion assertion) {

      String audience = null;

      if (assertion.getConditions() != null
            && CollectionUtils.isNotEmpty(assertion.getConditions()
                  .getAudienceRestrictions())
            && CollectionUtils.isNotEmpty(assertion.getConditions()
                  .getAudienceRestrictions().get(0).getAudiences())) {

         audience = assertion.getConditions().getAudienceRestrictions().get(0)
               .getAudiences().get(0).getAudienceURI();

      }

      return audience;
   }

   /**
    * lecture de la l'attribut 'AuthnInstant' de la balise
    * <code>&lt;AuthnStatement></code>
    * 
    * @param assertion
    *           jeton SAML
    * @return Instant d'authentification de l'usager sur le SI
    */
   public final DateTime loadAuthInstant(Assertion assertion) {

      DateTime authInstant = null;

      if (CollectionUtils.isNotEmpty(assertion.getAuthnStatements())) {

         authInstant = assertion.getAuthnStatements().get(0).getAuthnInstant();
      }

      return authInstant;
   }

   /**
    * lecture de l'attribut 'NotBefore' de la balise <code>&lt;Conditions><code>
    * 
    * @param assertion
    *           jeton SAML
    * @return Date de début de validité de l'assertion
    */
   public final DateTime loadNotBefore(Assertion assertion) {

      DateTime notBefore = null;

      if (assertion.getConditions() != null) {

         notBefore = assertion.getConditions().getNotBefore();
      }

      return notBefore;
   }

   /**
    * lecture de l'attribut 'NotOnOrAfter' de la balise
    * <code>&lt;Conditions><code>
    * 
    * @param assertion
    *           jeton SAML
    * @return Date d'expiration de l'assertion
    */
   public final DateTime loadNotOnOrAfter(Assertion assertion) {

      DateTime notOnOrAfter = null;

      if (assertion.getConditions() != null) {

         notOnOrAfter = assertion.getConditions().getNotOnOrAfter();
      }

      return notOnOrAfter;
   }

   /**
    * lecture des balises PAGM dans <code>&lt;AttributeStatement><code>
    * 
    * @param assertion
    *           jeton SAML
    * @return Liste des PAGM
    */
   @SuppressWarnings("PMD.AvoidDeeplyNestedIfStmts")
   public final List<String> loadPagm(Assertion assertion) {

      List<String> pagm = new ArrayList<String>();

      if (CollectionUtils.isNotEmpty(assertion.getAttributeStatements())) {

         for (AttributeStatement attributStatement : assertion
               .getAttributeStatements()) {

            if (CollectionUtils.isNotEmpty(attributStatement.getAttributes())) {

               for (Attribute attribute : attributStatement.getAttributes()) {

                  if ("PAGM".equals(attribute.getName())) {

                     for (XMLObject value : attribute.getAttributeValues()) {

                        pagm.add(value.getDOM().getTextContent());
                     }
                  }
               }

            }

         }
      }

      return pagm;
   }

   /**
    * lecture de la balise <code>&lt;AuthnContextClassRef></code>
    * 
    * @param assertion
    *           jeton SAML
    * @return Méthode d'authentification de l'utilisateur sur le SI de
    *         l'organisme client
    */
   public final String loadAuthnContextClassRef(Assertion assertion) {

      String classRef = null;

      if (CollectionUtils.isNotEmpty(assertion.getAuthnStatements())
            && assertion.getAuthnStatements().get(0).getAuthnContext() != null) {

         classRef = assertion.getAuthnStatements().get(0).getAuthnContext()
               .getAuthnContextClassRef().getAuthnContextClassRef();
      }

      return classRef;
   }

   /**
    * lecture de l'attribut 'Recipient' de la balise
    * <code>&lt;SubjectConfirmationData></code>
    * 
    * @param assertion
    *           jeton SAML
    * @return Identifiant de l'organisme fournisseur
    */
   public final String loadRecipient(Assertion assertion) {

      String recipient = null;

      if (assertion.getSubject() != null
            && CollectionUtils.isNotEmpty(assertion.getSubject()
                  .getSubjectConfirmations())
            && assertion.getSubject().getSubjectConfirmations().get(0)
                  .getSubjectConfirmationData() != null) {

         recipient = assertion.getSubject().getSubjectConfirmations().get(0)
               .getSubjectConfirmationData().getRecipient();
      }

      return recipient;
   }

   /**
    * lecture de l'attribut 'Format' de la balise <code>&lt;Subject><code>
    * 
    * @param assertion
    *           jeton SAML
    * @return Identifiant du format de l'identifiant du demandeur
    */
   public final String loadSubjectFormat(Assertion assertion) {

      String subjectFormat = null;

      if (assertion.getSubject() != null
            && assertion.getSubject().getNameID() != null) {

         subjectFormat = assertion.getSubject().getNameID().getFormat();
      }

      return subjectFormat;
   }

   /**
    * lecture de la balise <code>&lt;NameID><code>
    * 
    * @param assertion
    *           jeton SAML
    * @return Identifiant de l'utilisateur
    */
   public final String loadSubjectId(Assertion assertion) {

      String subjectId = null;

      if (assertion.getSubject() != null
            && assertion.getSubject().getNameID() != null) {

         subjectId = assertion.getSubject().getNameID().getValue();
      }

      return subjectId;
   }

}

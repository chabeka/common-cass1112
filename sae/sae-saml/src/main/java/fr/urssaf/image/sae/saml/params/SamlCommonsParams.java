package fr.urssaf.image.sae.saml.params;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.ObjectUtils;

/**
 * Paramètres communs aux formats SAML 1.1 et SAML 2.0 (assertion SAML 1.1,
 * assertion SAML 2.0, réponse SAML 2.0)
 * 
 * 
 */
public class SamlCommonsParams {

   @SuppressWarnings("PMD.ShortVariable")
   private UUID id;

   private Date issueInstant;

   private String issuer;

   private Date notOnOrAfter;

   private Date notOnBefore;

   private URI audience;

   private Date authnInstant;

   private List<String> pagm;

   /**
    * @return Identifiant unique de l'assertion
    */
   public final UUID getId() {
      return id;
   }

   /**
    * 
    * @param id
    *           Identifiant unique de l'assertion
    */
   @SuppressWarnings("PMD.ShortVariable")
   public final void setId(UUID id) {
      this.id = id;
   }

   /**
    * 
    * @return Instant de génération de l'assertion
    */
   public final Date getIssueInstant() {
      return (Date) ObjectUtils.clone(issueInstant);
   }

   /**
    * 
    * @param issueInstant
    *           Instant de génération de l'assertion
    */
   public final void setIssueInstant(Date issueInstant) {
      this.issueInstant = (Date) ObjectUtils.clone(issueInstant);
   }

   /**
    * 
    * @return Identifiant de l'émetteur de l'assertion
    */
   public final String getIssuer() {
      return issuer;
   }

   /**
    * 
    * @param issuer
    *           Identifiant de l'émetteur de l'assertion
    */
   public final void setIssuer(String issuer) {
      this.issuer = issuer;
   }

   /**
    * 
    * @return Date d'expiration de l'assertion
    */
   public final Date getNotOnOrAfter() {
      return (Date) ObjectUtils.clone(notOnOrAfter);
   }

   /**
    * 
    * @param notOnOrAfter
    *           Date d'expiration de l'assertion
    */
   public final void setNotOnOrAfter(Date notOnOrAfter) {
      this.notOnOrAfter = (Date) ObjectUtils.clone(notOnOrAfter);
   }

   /**
    * 
    * @return Date de début de validité de l'assertion
    */
   public final Date getNotOnBefore() {
      return (Date) ObjectUtils.clone(notOnBefore);
   }

   /**
    * 
    * @param notOnBefore
    *           Date de début de validité de l'assertion
    */
   public final void setNotOnBefore(Date notOnBefore) {
      this.notOnBefore = (Date) ObjectUtils.clone(notOnBefore);
   }

   /**
    * 
    * @return URI décrivant le service visé
    */
   public final URI getAudience() {
      return audience;
   }

   /**
    * 
    * @param audience
    *           URI décrivant le service visé
    */
   public final void setAudience(URI audience) {
      this.audience = audience;
   }

   /**
    * 
    * @return Instant d'authentification de l'usager sur le SI.
    */
   public final Date getAuthnInstant() {
      return (Date) ObjectUtils.clone(authnInstant);
   }

   /**
    * 
    * @param authnInstant
    *           Instant d'authentification de l'usager sur le SI.
    */
   public final void setAuthnInstant(Date authnInstant) {
      this.authnInstant = (Date) ObjectUtils.clone(authnInstant);
   }

   /**
    * 
    * @return Liste des PAGM
    */
   public final List<String> getPagm() {
      return pagm;
   }

   /**
    * 
    * @param pagm
    *           Liste des PAGM
    */
   public final void setPagm(List<String> pagm) {
      this.pagm = pagm;
   }

}

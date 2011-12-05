package fr.urssaf.image.sae.saml.params;

import java.net.URI;

/**
 * Propriétés d'une assertion SAML 2.0
 * 
 * 
 */
public class SamlAssertionParams {

   private SamlCommonsParams commonsParams;

   private URI subjectFormat2;

   private String subjectId2;

   private URI methodAuthn2;

   private URI recipient;

   /**
    * 
    * @return Propriétés générales
    */
   public final SamlCommonsParams getCommonsParams() {
      return commonsParams;
   }

   /**
    * 
    * @param commonsParams
    *           Propriétés générales
    */
   public final void setCommonsParams(SamlCommonsParams commonsParams) {
      this.commonsParams = commonsParams;
   }

   /**
    * 
    * @return Identifiant du format de l'identifiant du demandeur
    */
   public final URI getSubjectFormat2() {
      return subjectFormat2;
   }

   /**
    * 
    * @param subjectFormat2
    *           Identifiant du format de l'identifiant du demandeur
    */
   public final void setSubjectFormat2(URI subjectFormat2) {
      this.subjectFormat2 = subjectFormat2;
   }

   /**
    * 
    * @return Identifiant de l'utilisateur
    */
   public final String getSubjectId2() {
      return subjectId2;
   }

   /**
    * 
    * @param subjectId2
    *           Identifiant de l'utilisateur
    */
   public final void setSubjectId2(String subjectId2) {
      this.subjectId2 = subjectId2;
   }

   /**
    * 
    * @return Méthode d'authentification de l'utilisateur sur le SI de
    *         l'organisme client
    */
   public final URI getMethodAuthn2() {
      return methodAuthn2;
   }

   /**
    * 
    * @param methodAuthn2
    *           Méthode d'authentification de l'utilisateur sur le SI de
    *           l'organisme client
    */
   public final void setMethodAuthn2(URI methodAuthn2) {
      this.methodAuthn2 = methodAuthn2;
   }

   /**
    * 
    * @return Identifiant de l'organisme fournisseur
    */
   public final URI getRecipient() {
      return recipient;
   }

   /**
    * 
    * @param recipient
    *           Identifiant de l'organisme fournisseur
    */
   public final void setRecipient(URI recipient) {
      this.recipient = recipient;
   }

}

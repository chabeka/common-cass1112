package fr.urssaf.image.sae.integration.ihmweb.formulaire;

import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;

/**
 * Formulaire associé au tag vi.tag
 */
public class ViFormulaire {

   private String issuer;
   private String recipient;
   private String audience;
   private PagmList pagms;
   
   /**
    * L'identifiant du contrat de service
    * @return L'identifiant du contrat de service
    */
   public final String getIssuer() {
      return issuer;
   }
   
   /**
    * L'identifiant du contrat de service
    * @param issuer L'identifiant du contrat de service
    */
   public final void setIssuer(String issuer) {
      this.issuer = issuer;
   }
   
   /**
    * L'identifiant de l'organisme fournisseur du service
    * @return L'identifiant de l'organisme fournisseur du service
    */
   public final String getRecipient() {
      return recipient;
   }
   
   /**
    * L'identifiant de l'organisme fournisseur du service
    * @param recipient L'identifiant de l'organisme fournisseur du service
    */
   public final void setRecipient(String recipient) {
      this.recipient = recipient;
   }
   
   /**
    * L'identifiant du service visé
    * @return L'identifiant du service visé
    */
   public final String getAudience() {
      return audience;
   }
   
   /**
    * L'identifiant du service visé
    * @param audience L'identifiant du service visé
    */
   public final void setAudience(String audience) {
      this.audience = audience;
   }
   
   /**
    * La liste des PAGM
    * @return La liste des PAGM
    */
   public final PagmList getPagms() {
      return pagms;
   }
   
   /**
    * La liste des PAGM
    * @param pagms La liste des PAGM
    */
   public final void setPagms(PagmList pagms) {
      this.pagms = pagms;
   }
   
}

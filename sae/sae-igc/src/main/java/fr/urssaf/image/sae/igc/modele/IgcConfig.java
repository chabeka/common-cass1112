package fr.urssaf.image.sae.igc.modele;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Configuration des éléments de l'IGC
 * 
 * 
 */
@XStreamAlias("IgcConfig")
public class IgcConfig {

   @XStreamAlias("id")
   private String pkiIdent;

   @XStreamAlias("certifACRacine")
   private String acRacine;

   @XStreamAlias("repertoireCRL")
   private String crlsRep;

   @XStreamAlias("URLTelechargementCRL")
   private URLList urlList;
   
   @XStreamAlias("issuers")
   private IssuerList issuerList;

   /**
    * @return the pkiIdent
    */
   public final String getPkiIdent() {
      return pkiIdent;
   }

   /**
    * @param pkiIdent the pkiIdent to set
    */
   public final void setPkiIdent(String pkiIdent) {
      this.pkiIdent = pkiIdent;
   }

   /**
    * @return the acRacine
    */
   public final String getAcRacine() {
      return acRacine;
   }

   /**
    * @param acRacine the acRacine to set
    */
   public final void setAcRacine(String acRacine) {
      this.acRacine = acRacine;
   }

   /**
    * @return the crlsRep
    */
   public final String getCrlsRep() {
      return crlsRep;
   }

   /**
    * @param crlsRep the crlsRep to set
    */
   public final void setCrlsRep(String crlsRep) {
      this.crlsRep = crlsRep;
   }

   /**
    * @return the urlList
    */
   public final URLList getUrlList() {
      return urlList;
   }

   /**
    * @param urlList the urlList to set
    */
   public final void setUrlList(URLList urlList) {
      this.urlList = urlList;
   }

   /**
    * @return the issuerList
    */
   public final IssuerList getIssuerList() {
      return issuerList;
   }

   /**
    * @param issuerList the issuerList to set
    */
   public final void setIssuerList(IssuerList issuerList) {
      this.issuerList = issuerList;
   }

}

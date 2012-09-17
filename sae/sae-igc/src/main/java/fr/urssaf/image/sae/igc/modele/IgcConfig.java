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

   @XStreamAlias("activerTelechargementCRL")
   private boolean dlActivated;

   /**
    * @return l'identifiant de la PKI
    */
   public final String getPkiIdent() {
      return pkiIdent;
   }

   /**
    * @param pkiIdent
    *           l'identifiant de la PKI
    */
   public final void setPkiIdent(String pkiIdent) {
      this.pkiIdent = pkiIdent;
   }

   /**
    * @return le chemin complet vers l'AC racine
    */
   public final String getAcRacine() {
      return acRacine;
   }

   /**
    * @param acRacine
    *           le chemin complet vers l'AC racine
    */
   public final void setAcRacine(String acRacine) {
      this.acRacine = acRacine;
   }

   /**
    * @return le chemin complet vers le répertoire des CRL
    */
   public final String getCrlsRep() {
      return crlsRep;
   }

   /**
    * @param crlsRep
    *           le chemin complet vers le répertoire des CRL
    */
   public final void setCrlsRep(String crlsRep) {
      this.crlsRep = crlsRep;
   }

   /**
    * @return la liste des URL de téléchargement des CRL
    */
   public final URLList getUrlList() {
      return urlList;
   }

   /**
    * @param urlList
    *           la liste des URL de téléchargement des CRL
    */
   public final void setUrlList(URLList urlList) {
      this.urlList = urlList;
   }

   /**
    * @return la liste des issuers autorisés
    */
   public final IssuerList getIssuerList() {
      return issuerList;
   }

   /**
    * @param issuerList
    *           la liste des issuers autorisés
    */
   public final void setIssuerList(IssuerList issuerList) {
      this.issuerList = issuerList;
   }

   /**
    * @return le flag indiquant si oui ou non les CRL doivent être téléchargés
    */
   public final boolean isDlActivated() {
      return dlActivated;
   }

   /**
    * @param dlActivated
    *           le flag indiquant si oui ou non les CRL doivent être téléchargés
    */
   public final void setDlActivated(boolean dlActivated) {
      this.dlActivated = dlActivated;
   }

}

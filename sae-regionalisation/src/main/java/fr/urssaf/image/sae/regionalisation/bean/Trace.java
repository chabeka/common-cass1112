package fr.urssaf.image.sae.regionalisation.bean;

import java.util.UUID;

/**
 * Classe représentant un objet de trace de mise à jour
 */
public class Trace {

   private UUID idDocument;

   private int lineNumber;
   
   private boolean modeMiseAjour;
   
   private boolean nciIsRenum;
   
   private String nciAncienneValeur;
   
   private String nciNouvelleValeurSiRenum;
   
   private boolean nceIsRenum;
   
   private String nceAncienneValeur;
   
   private String nceNouvelleValeurSiRenum;
   
   private boolean npeIsRenum;
   
   private String npeAncienneValeur;
   
   private String npeNouvelleValeurSiRenum;
   
   private boolean cogIsRenum;
   
   private String cogAncienneValeur;
   
   private String cogNouvelleValeurSiRenum;
   
   private boolean copIsRenum;
   
   private String copAncienneValeur;
   
   private String copNouvelleValeurSiRenum;
   

   /**
    * @return identifiant du document modifié
    */
   public final UUID getIdDocument() {
      return idDocument;
   }

   /**
    * @param idDocument
    *           identifiant du document modifié
    */
   public final void setIdDocument(UUID idDocument) {
      this.idDocument = idDocument;
   }

   /**
    * @return the lineNumber
    */
   public final int getLineNumber() {
      return lineNumber;
   }

   /**
    * @param lineNumber
    *           the lineNumber to set
    */
   public final void setLineNumber(int lineNumber) {
      this.lineNumber = lineNumber;
   }

   public boolean isNciIsRenum() {
      return nciIsRenum;
   }

   public void setNciIsRenum(boolean nciIsRenum) {
      this.nciIsRenum = nciIsRenum;
   }

   public String getNciAncienneValeur() {
      return nciAncienneValeur;
   }

   public void setNciAncienneValeur(String nciAncienneValeur) {
      this.nciAncienneValeur = nciAncienneValeur;
   }

   public String getNciNouvelleValeurSiRenum() {
      return nciNouvelleValeurSiRenum;
   }

   public void setNciNouvelleValeurSiRenum(String nciNouvelleValeurSiRenum) {
      this.nciNouvelleValeurSiRenum = nciNouvelleValeurSiRenum;
   }

   public boolean isNceIsRenum() {
      return nceIsRenum;
   }

   public void setNceIsRenum(boolean nceIsRenum) {
      this.nceIsRenum = nceIsRenum;
   }

   public String getNceAncienneValeur() {
      return nceAncienneValeur;
   }

   public void setNceAncienneValeur(String nceAncienneValeur) {
      this.nceAncienneValeur = nceAncienneValeur;
   }

   public String getNceNouvelleValeurSiRenum() {
      return nceNouvelleValeurSiRenum;
   }

   public void setNceNouvelleValeurSiRenum(String nceNouvelleValeurSiRenum) {
      this.nceNouvelleValeurSiRenum = nceNouvelleValeurSiRenum;
   }

   public boolean isNpeIsRenum() {
      return npeIsRenum;
   }

   public void setNpeIsRenum(boolean npeIsRenum) {
      this.npeIsRenum = npeIsRenum;
   }

   public String getNpeAncienneValeur() {
      return npeAncienneValeur;
   }

   public void setNpeAncienneValeur(String npeAncienneValeur) {
      this.npeAncienneValeur = npeAncienneValeur;
   }

   public String getNpeNouvelleValeurSiRenum() {
      return npeNouvelleValeurSiRenum;
   }

   public void setNpeNouvelleValeurSiRenum(String npeNouvelleValeurSiRenum) {
      this.npeNouvelleValeurSiRenum = npeNouvelleValeurSiRenum;
   }

   public boolean isCogIsRenum() {
      return cogIsRenum;
   }

   public void setCogIsRenum(boolean cogIsRenum) {
      this.cogIsRenum = cogIsRenum;
   }

   public String getCogAncienneValeur() {
      return cogAncienneValeur;
   }

   public void setCogAncienneValeur(String cogAncienneValeur) {
      this.cogAncienneValeur = cogAncienneValeur;
   }

   public String getCogNouvelleValeurSiRenum() {
      return cogNouvelleValeurSiRenum;
   }

   public void setCogNouvelleValeurSiRenum(String cogNouvelleValeurSiRenum) {
      this.cogNouvelleValeurSiRenum = cogNouvelleValeurSiRenum;
   }

   public boolean isCopIsRenum() {
      return copIsRenum;
   }

   public void setCopIsRenum(boolean copIsRenum) {
      this.copIsRenum = copIsRenum;
   }

   public String getCopAncienneValeur() {
      return copAncienneValeur;
   }

   public void setCopAncienneValeur(String copAncienneValeur) {
      this.copAncienneValeur = copAncienneValeur;
   }

   public String getCopNouvelleValeurSiRenum() {
      return copNouvelleValeurSiRenum;
   }

   public void setCopNouvelleValeurSiRenum(String copNouvelleValeurSiRenum) {
      this.copNouvelleValeurSiRenum = copNouvelleValeurSiRenum;
   }

   public boolean isModeMiseAjour() {
      return modeMiseAjour;
   }

   public void setModeMiseAjour(boolean modeMiseAjour) {
      this.modeMiseAjour = modeMiseAjour;
   }

}

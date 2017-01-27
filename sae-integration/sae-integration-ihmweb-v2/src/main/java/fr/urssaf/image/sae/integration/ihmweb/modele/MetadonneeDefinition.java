package fr.urssaf.image.sae.integration.ihmweb.modele;


/**
 * Propriétés d'une métadonnée
 */
public class MetadonneeDefinition {

   
   private String codeLong;
   private String codeCourt;
   private boolean archivablePossible;
   private boolean archivableObligatoire;
   private boolean consulteeParDefaut;
   private boolean consultable;
   private boolean critereRecherche;
   private boolean client;
   private boolean obligatoireAuStockage;
   private String typeDfce;
   
   
   /**
    * cf. document référentiel métadonnées
    * @return cf. document référentiel métadonnées
    */
   public final String getCodeLong() {
      return codeLong;
   }
   
   
   /**
    * cf. document référentiel métadonnées
    * @param codeLong cf. document référentiel métadonnées
    */
   public final void setCodeLong(String codeLong) {
      this.codeLong = codeLong;
   }
   
   /**
    * cf. document référentiel métadonnées
    * @return cf. document référentiel métadonnées
    */
   public final String getCodeCourt() {
      return codeCourt;
   }
   
   
   /**
    * cf. document référentiel métadonnées
    * @param codeCourt cf. document référentiel métadonnées
    */
   public final void setCodeCourt(String codeCourt) {
      this.codeCourt = codeCourt;
   }
   
   
   /**
    * cf. document référentiel métadonnées
    * @return cf. document référentiel métadonnées
    */
   public final boolean isArchivablePossible() {
      return archivablePossible;
   }
   
   
   /**
    * cf. document référentiel métadonnées
    * @param archivablePossible cf. document référentiel métadonnées
    */
   public final void setArchivablePossible(boolean archivablePossible) {
      this.archivablePossible = archivablePossible;
   }
   
   
   /**
    * cf. document référentiel métadonnées
    * @return cf. document référentiel métadonnées
    */
   public final boolean isArchivableObligatoire() {
      return archivableObligatoire;
   }
   
   
   /**
    * cf. document référentiel métadonnées
    * @param archivableObligatoire cf. document référentiel métadonnées
    */
   public final void setArchivableObligatoire(boolean archivableObligatoire) {
      this.archivableObligatoire = archivableObligatoire;
   }
   
   
   /**
    * cf. document référentiel métadonnées
    * @return cf. document référentiel métadonnées
    */
   public final boolean isConsulteeParDefaut() {
      return consulteeParDefaut;
   }
   
   
   /**
    * cf. document référentiel métadonnées
    * @param consulteeParDefaut cf. document référentiel métadonnées
    */
   public final void setConsulteeParDefaut(boolean consulteeParDefaut) {
      this.consulteeParDefaut = consulteeParDefaut;
   }
   
   
   /**
    * cf. document référentiel métadonnées
    * @return cf. document référentiel métadonnées
    */
   public final boolean isConsultable() {
      return consultable;
   }
   
   
   /**
    * cf. document référentiel métadonnées
    * @param consultable cf. document référentiel métadonnées
    */
   public final void setConsultable(boolean consultable) {
      this.consultable = consultable;
   }
   
   
   /**
    * cf. document référentiel métadonnées
    * @return cf. document référentiel métadonnées
    */
   public final boolean isCritereRecherche() {
      return critereRecherche;
   }
   
   
   /**
    * cf. document référentiel métadonnées
    * @param critereRecherche cf. document référentiel métadonnées
    */
   public final void setCritereRecherche(boolean critereRecherche) {
      this.critereRecherche = critereRecherche;
   }
   
   
   /**
    * cf. document référentiel métadonnées
    * @return cf. document référentiel métadonnées
    */
   public final boolean isClient() {
      return client;
   }
   
   
   /**
    * cf. document référentiel métadonnées
    * @param client cf. document référentiel métadonnées
    */
   public final void setClient(boolean client) {
      this.client = client;
   }
   
   
   /**
    * cf. document référentiel métadonnées
    * @return cf. document référentiel métadonnées
    */
   public final boolean isObligatoireAuStockage() {
      return obligatoireAuStockage;
   }
   
   
   /**
    * cf. document référentiel métadonnées
    * @param obligatoireAuStockage cf. document référentiel métadonnées
    */
   public final void setObligatoireAuStockage(boolean obligatoireAuStockage) {
      this.obligatoireAuStockage = obligatoireAuStockage;
   }


   /**
    * cf. document référentiel métadonnées
    * @param typeDfce cf. document référentiel métadonnées
    */
   public void setTypeDfce(String typeDfce) {
      this.typeDfce = typeDfce;
   }


   /**
    * cf. document référentiel métadonnées
    * @return cf. document référentiel métadonnées
    */
   public String getTypeDfce() {
      return typeDfce;
   }
   
   
}

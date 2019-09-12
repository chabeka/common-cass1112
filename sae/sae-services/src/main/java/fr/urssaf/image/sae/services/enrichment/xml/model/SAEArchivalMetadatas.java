package fr.urssaf.image.sae.services.enrichment.xml.model;

/**
 * Énumération contenant la listes des codes long des métadonnées à enrichir.
 */
public enum SAEArchivalMetadatas {
   // Code RND
   CODE_RND("CodeRND","SM_DOCUMENT_TYPE"),
   // Code fonction
   CODE_FONCTION("CodeFonction","dom"),
   // CodeActivite
   CODE_ACTIVITE("CodeActivite","act"),
   // DateArchivage
   DATE_ARCHIVAGE("DateArchivage" ,"SM_ARCHIVAGE_DATE"),
   // DateDebutConservation
   DATE_DEBUT_CONSERVATION("DateDebutConservation", "SM_LIFE_CYCLE_REFERENCE_DATE"),
   // DateFinConservation
   DATE_FIN_CONSERVATION("DateFinConservation", "dfc"),
   // ContratDeService
   CONTRAT_DE_SERVICE("ContratDeService","cse"),
   // pas de valeur
   NOVALUE("",""),
   // Version RND
   VERSION_RND("VersionRND","vrn"),
   // Hash
   HASH_CODE("Hash", "SM_DIGEST"),
   //TypeHash
   TYPE_HASH("TypeHash", "SM_DIGEST_ALGORITHM"),
   //APPLICATIONPRODUCTRICE
   APPLICATION_PRODUCTRICE("ApplicationProductrice", "apr"),
   //NBPAGES
   NB_PAGES("NbPages", "nbp"),
   //NomFichier
   NOM_FICHIER("NomFichier", "nfi"),
   //DocumentVirtuel
   DOCUMENT_VIRTUEL("DocumentVirtuel","SM_VIRTUAL"),
   //NumeroIdArchivage
   NUMERO_ID_ARCHIVAGE("NumeroIdArchivage","nid"),
   //DocumentArchivable
   DOCUMENT_ARCHIVABLE("DocumentArchivable","dar");
   

   // Le code long de la métadonnée.
   private String longCode;
   
   // Le code court de la métadonnée.
   private String shortCode;

   /**
    * Construtor
    * @param longCode Le code long
    * @param shortCode Le code court
    */
   SAEArchivalMetadatas(final String longCode, final String shortCode) {
      this.longCode = longCode;
      this.shortCode = shortCode;
   }

   /**
    * @param longCode
    *           : Le code long de la métadonnée.
    */
   public void setLongCode(final String longCode) {
      this.longCode = longCode;
   }

   /**
    * @return : Le code long de la métadonnée.
    */
   public String getLongCode() {
      return longCode;
   }

  /**
   * @return the shortCode
   */
  public String getShortCode() {
    return shortCode;
  }

  /**
   * @param shortCode the shortCode to set
   */
  public void setShortCode(String shortCode) {
    this.shortCode = shortCode;
  }

}

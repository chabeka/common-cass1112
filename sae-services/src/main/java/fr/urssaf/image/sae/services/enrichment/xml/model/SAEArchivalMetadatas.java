package fr.urssaf.image.sae.services.enrichment.xml.model;

/**
 * Énumération contenant la listes des codes long des métadonnées à enrichir.<br/>
 */
public enum SAEArchivalMetadatas {
   // Code RND
   CODE_RND("CodeRND"),
   // Code fonction
   CODE_FONCTION("CodeFonction"),
   // CodeActivite
   CODE_ACTIVITE("CodeActivite"),
   // DateArchivage
   DATE_ARCHIVAGE("DateArchivage"),
   // DateDebutConservation
   DATE_DEBUT_CONSERVATION("DateDebutConservation"),
   // DateFinConservation
   DATE_FIN_CONSERVATION("DateFinConservation"),
   // ContratDeService
   CONTRAT_DE_SERVICE("ContratDeService"),
   // pas de valeur
   NOVALUE(""),
   // Version RND
   VERSION_RND("VersionRND"),
   // Hash
   HASH_CODE("Hash"),
   //TypeHash
   TYPE_HASH("TypeHash"),
   //APPLICATIONPRODUCTRICE
   APPLICATION_PRODUCTRICE("ApplicationProductrice"),
   //NBPAGES
   NB_PAGES("NbPages"),
   //NomFichier
   NOM_FICHIER("NomFichier"),
   //DocumentVirtuel
   DOCUMENT_VIRTUEL("DocumentVirtuel"),
   //NumeroIdArchivage
   NUMERO_ID_ARCHIVAGE("NumeroIdArchivage"),
   //DocumentArchivable
   DOCUMENT_ARCHIVABLE("DocumentArchivable");
   

   // Le code long de la métadonnée.
   private String longCode;

   /**
    * 
    * @param shortCode
    *           . Le code court
    */
   SAEArchivalMetadatas(final String longCode) {
      this.longCode = longCode;
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

}

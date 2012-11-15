package fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.cf;

import java.util.UUID;

import com.netflix.astyanax.annotations.Component;

/**
 * Classe représentant la famille de colonne TermInfoRangeString
 * 
 * 
 */
public class TermInfoRangeUuidColumn {

   @Component(ordinal = 0)
   private String categoryValue;
   @Component(ordinal = 1)
   private UUID documentUUID;
   @Component(ordinal = 2)
   private String documentVersion;

   /**
    * 
    * @param categoryValue
    *           la catégorie
    */
   public final void setCategoryValue(String categoryValue) {
      this.categoryValue = categoryValue;
   }

   /**
    * 
    * @return la catégorie
    */
   public final String getCategoryValue() {
      return categoryValue;
   }

   /**
    * 
    * @param documentUUID
    *           l'identifiant unique du document
    */
   public final void setDocumentUUID(UUID documentUUID) {
      this.documentUUID = documentUUID;
   }

   /**
    * 
    * @return l'identifiant unique du document
    */
   public final UUID getDocumentUUID() {
      return documentUUID;
   }

   /**
    * 
    * @param documentVersion
    *           la version du document
    */
   public final void setDocumentVersion(String documentVersion) {
      this.documentVersion = documentVersion;
   }

   /**
    * 
    * @return la version du document
    */
   public final String getDocumentVersion() {
      return documentVersion;
   }

}

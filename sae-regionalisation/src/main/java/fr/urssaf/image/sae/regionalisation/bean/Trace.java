package fr.urssaf.image.sae.regionalisation.bean;

import java.math.BigDecimal;
import java.util.UUID;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Classe représentant un objet de trace de mise à jour
 * 
 * 
 */
public class Trace {

   private BigDecimal idSearch;

   private UUID idDocument;

   private String metaName;

   private String oldValue;

   private String newValue;

   /**
    * @return identifiant du critère de recherche
    */
   public final BigDecimal getIdSearch() {
      return idSearch;
   }

   /**
    * @param idSearch
    *           identifiant du critère de recherche
    */
   public final void setIdSearch(BigDecimal idSearch) {
      this.idSearch = idSearch;
   }

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
    * @return nom de la métadonnée modifié
    */
   public final String getMetaName() {
      return metaName;
   }

   /**
    * @param metaName
    *           nom de la métadonnée modifié
    */
   public final void setMetaName(String metaName) {
      this.metaName = metaName;
   }

   /**
    * @return ancienne valeur
    */
   public final String getOldValue() {
      return oldValue;
   }

   /**
    * @param oldValue
    *           ancienne valeur
    */
   public final void setOldValue(String oldValue) {
      this.oldValue = oldValue;
   }

   /**
    * @return nouvelle valeur
    */
   public final String getNewValue() {
      return newValue;
   }

   /**
    * @param newValue
    *           nouvelle valeur
    */
   public final void setNewValue(String newValue) {
      this.newValue = newValue;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final int hashCode() {
      return new HashCodeBuilder().append(this.idDocument)
            .append(this.idSearch).append(this.metaName).append(this.newValue)
            .append(this.oldValue).hashCode();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (!(obj instanceof Trace))
         return false;

      Trace other = (Trace) obj;

      return new EqualsBuilder().append(this.idDocument, other.idDocument)
            .append(this.idSearch, other.idSearch).append(this.metaName,
                  other.metaName).append(this.newValue, other.newValue).append(
                  this.oldValue, other.oldValue).isEquals();
   }

}

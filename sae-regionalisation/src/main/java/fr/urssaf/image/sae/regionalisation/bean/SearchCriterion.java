package fr.urssaf.image.sae.regionalisation.bean;

import java.math.BigDecimal;

/**
 * Classe représentant un objet de critères de recherche.
 * 
 * 
 */
public class SearchCriterion {

   private BigDecimal identifiant;

   private String lucene;

   private boolean updated;

   /**
    * @return identifiant
    */
   public final BigDecimal getId() {
      return identifiant;
   }

   /**
    * @param identifiant
    *           identifiant
    */
   public final void setId(BigDecimal identifiant) {
      this.identifiant = identifiant;
   }

   /**
    * @return requête lucène
    */
   public final String getLucene() {
      return lucene;
   }

   /**
    * @param lucene
    *           requête lucène
    */
   public final void setLucene(String lucene) {
      this.lucene = lucene;
   }

   /**
    * @return flag de traitement
    */
   public final boolean isUpdated() {
      return updated;
   }

   /**
    * @param updated
    *           flag de traitement
    */
   public final void setUpdated(boolean updated) {
      this.updated = updated;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("SearchCriterion [identifiant=").append(identifiant)
            .append(", lucene=").append(lucene).append(", updated=").append(
                  updated).append("]");
      return builder.toString();
   }

}

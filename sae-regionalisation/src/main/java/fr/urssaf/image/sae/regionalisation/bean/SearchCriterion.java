package fr.urssaf.image.sae.regionalisation.bean;


/**
 * Classe représentant un objet de critères de recherche.
 * 
 * 
 */
public class SearchCriterion {

   private int identifiant;

   private String lucene;

   /**
    * @return identifiant
    */
   public final int getId() {
      return identifiant;
   }

   /**
    * @param identifiant
    *           identifiant
    */
   public final void setId(int identifiant) {
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
    * {@inheritDoc}
    */
   @Override
   public final String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("SearchCriterion [identifiant=").append(identifiant)
            .append(", lucene=").append(lucene).append("]");
      return builder.toString();
   }

}

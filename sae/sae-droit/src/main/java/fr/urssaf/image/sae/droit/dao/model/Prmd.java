/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.model;

/**
 * Classe de modèle d'un PRMD
 * 
 */
public class Prmd {

   /** identifiant unique du PRMD */
   private String code;

   /** description du PRMD */
   private String description;

   /** requête LUCENE pour le filtrage de la recherche */
   private String lucene;

   /**
    * @return l'identifiant unique du PRMD
    */
   public final String getCode() {
      return code;
   }

   /**
    * @param code
    *           identifiant unique du PRMD
    */
   public final void setCode(String code) {
      this.code = code;
   }

   /**
    * @return la description du PRMD
    */
   public final String getDescription() {
      return description;
   }

   /**
    * @param description
    *           description du PRMD
    */
   public final void setDescription(String description) {
      this.description = description;
   }

   /**
    * @return la requête LUCENE pour le filtrage de la recherche
    */
   public final String getLucene() {
      return lucene;
   }

   /**
    * @param lucene
    *           requête LUCENE pour le filtrage de la recherche
    */
   public final void setLucene(String lucene) {
      this.lucene = lucene;
   }

}

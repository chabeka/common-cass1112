/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.model;

import java.util.List;
import java.util.Map;

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

   /** liste de clé/valeur pour un PRMD */
   private Map<String, List<String>> metadata;

   /**
    * Nom du qualifier de la classe d'implémentation du bean de vérification de
    * l'appartenance à un PRMD
    */
   private String bean;

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

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean equals(Object obj) {

      boolean areEquals = false;

      if (obj instanceof Prmd) {
         Prmd prmd = (Prmd) obj;

         areEquals = code.equals(prmd.getCode())
               && description.equals(prmd.getDescription())
               && lucene.equals(prmd.getLucene())
               && bean.equals(prmd.getBean())
               && metadata.keySet().size() == prmd.getMetadata().keySet()
                     .size()
               && metadata.keySet().containsAll(prmd.getMetadata().keySet());
      }

      return areEquals;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final int hashCode() {
      return super.hashCode();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String toString() {

      StringBuffer buffer = new StringBuffer("\nliste des metadonnees :");
      for (String key : metadata.keySet()) {
         buffer.append("\nclé = " + key + " / valeur = " + metadata.get(key));
      }

      return "code : " + code + "\ndescription : " + description
            + "\nlucene : " + lucene + "\nbean : " + bean + buffer.toString();
   }

   /**
    * @return la liste de clé/valeur pour un PRMD
    */
   public final Map<String, List<String>> getMetadata() {
      return metadata;
   }

   /**
    * @param metadata
    *           liste de clé/valeur pour un PRMD
    */
   public final void setMetadata(Map<String, List<String>> metadata) {
      this.metadata = metadata;
   }

   /**
    * @return le Nom du qualifier de la classe d'implémentation du bean de
    *         vérification de l'appartenance à un PRMD
    */
   public final String getBean() {
      return bean;
   }

   /**
    * @param bean
    *           Nom du qualifier de la classe d'implémentation du bean de
    *           vérification de l'appartenance à un PRMD
    */
   public final void setBean(String bean) {
      this.bean = bean;
   }

}

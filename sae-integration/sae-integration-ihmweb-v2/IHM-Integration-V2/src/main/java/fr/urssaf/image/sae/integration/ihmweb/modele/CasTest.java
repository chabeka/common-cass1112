package fr.urssaf.image.sae.integration.ihmweb.modele;

import java.util.List;

/**
 * Description d'un cas de test
 */
public class CasTest {

   private String identifiant;
   private String code;
   private String categorie;
   private String description;
   private String luceneExemple;
   private List<String> luceneExempleList;

   
   /**
    * Identifiant unique
    * 
    * @return Identifiant unique
    */
   public String getIdentifiant() {
      return identifiant;
   }
   
   
   /**
    * Identifiant unique
    * 
    * @param identifiant Identifiant unique
    */
   public void setIdentifiant(String identifiant) {
      this.identifiant = identifiant;
   }
   
   
   /**
    * Code (correspondance avec le code du plan de test)
    * 
    * @return Code (correspondance avec le code du plan de test)
    */
   public String getCode() {
      return code;
   }
   
   
   /**
    * Code (correspondance avec le code du plan de test)
    * 
    * @param code Code (correspondance avec le code du plan de test)
    */
   public void setCode(String code) {
      this.code = code;
   }
   
   
   /**
    * Catégorie
    * 
    * @return Catégorie
    */
   public String getCategorie() {
      return categorie;
   }
   
   
   /**
    * Catégorie
    * 
    * @param categorie Catégorie
    */
   public void setCategorie(String categorie) {
      this.categorie = categorie;
   }
   
   
   /**
    * Description
    * 
    * @return Description
    */
   public String getDescription() {
      return description;
   }
   
   
   /**
    * Description
    * 
    * @param description Description
    */
   public void setDescription(String description) {
      this.description = description;
   }


   /**
    * Requête LUCENE associée au cas de test (facultatif)
    * 
    * @return Requête LUCENE associée au cas de test (facultatif)
    */
   public final String getLuceneExemple() {
      return luceneExemple;
   }


   /**
    * Requête LUCENE associée au cas de test (facultatif)
    * 
    * @param luceneExemple Requête LUCENE associée au cas de test (facultatif)
    */
   public final void setLuceneExemple(String luceneExemple) {
      this.luceneExemple = luceneExemple;
   }
   

   /**
    * Requête LUCENE associée au cas de test (facultatif)
    * 
    * @return Requête LUCENE associée au cas de test (facultatif)
    */
   public final List<String> getLuceneExempleList() {
      return luceneExempleList;
   }


   /**
    * Requête LUCENE associée au cas de test (facultatif)
    * 
    * @param luceneExemple Requête LUCENE associée au cas de test (facultatif)
    */
   public final void setLuceneExempleList(List<String> luceneExempleList) {
      this.luceneExempleList = luceneExempleList;
   }

   
   
}

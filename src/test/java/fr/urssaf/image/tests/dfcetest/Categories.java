package fr.urssaf.image.tests.dfcetest;

/**
 * Enumération des 8 catégories de la base de test
 *
 */
public enum Categories {
   TITRE("Titre"),
   TYPE_DOC("Type de document"),  
   APPLI_SOURCE("_aasource"), 
   DATE("Date"),  
   DATETIME("Datetime"), 
   INTEGER("Entier"), 
   DOUBLE("Double"), 
   BOOLEAN("Booléen");

   /**
    * Nom de la catégorie
    */
   private String name;

   Categories(String catName) {
      this.name = catName;
   }
   
   public String toString() {
      return this.name;
   }
}
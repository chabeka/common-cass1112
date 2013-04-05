package fr.urssaf.image.sae.metadata.referential.model;

import java.util.List;

public class Dictionary {

   
   private String id;
   
   private List<String> entries;

   
   /**
    * Constructeur de la classe Dictionary
    * @param id identifiant du dictionnaire
    * @param entries Liste des valeurs autorisées
    */
   public Dictionary(String id, List<String> entries){
      this.id = id;
      this.entries = entries;
   }
   
   /**
    * 
    * @return l'identifiant du dictionnaire
    */
   public String getId() {
      return id;
   }

   /**
    * 
    * @param id identifiant du dictionnaire
    */
   public void setId(String id) {
      this.id = id;
   }

   /**
    * 
    * @return Liste des valeurs autorisées
    */
   public List<String> getEntries() {
      return entries;
   }

   /**
    * 
    * @param entries Liste des valeurs autorisées
    */
   public void setEntries(List<String> entries) {
      this.entries = entries;
   }
   
   
}

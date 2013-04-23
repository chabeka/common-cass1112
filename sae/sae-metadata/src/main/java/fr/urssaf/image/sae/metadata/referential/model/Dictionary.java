package fr.urssaf.image.sae.metadata.referential.model;

import java.util.List;

public class Dictionary {

   
   private String identifiant;
   
   private List<String> entries;

   
   /**
    * Constructeur de la classe Dictionary
    * @param id identifiant du dictionnaire
    * @param entries Liste des valeurs autorisées
    */
   public Dictionary(String identifiant, List<String> entries){
      this.identifiant = identifiant;
      this.entries = entries;
   }
   
   /**
    * 
    * @return l'identifiant du dictionnaire
    */
   public String getId() {
      return identifiant;
   }

   /**
    * 
    * @param id identifiant du dictionnaire
    */
   public void setId(String identifiant) {
      this.identifiant = identifiant;
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

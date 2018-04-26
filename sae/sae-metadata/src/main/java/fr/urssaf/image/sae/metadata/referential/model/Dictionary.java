package fr.urssaf.image.sae.metadata.referential.model;

import java.util.List;

/**
 * Classe métier représentant un dictionnaire
 * 
 * 
 */
public class Dictionary {

   private String identifiant;

   private List<String> entries;

   /**
    * Constructeur de la classe Dictionary
    * 
    * @param identifiant
    *           identifiant du dictionnaire
    * @param entries
    *           Liste des valeurs autorisées
    */
   public Dictionary(String identifiant, List<String> entries) {
      this.identifiant = identifiant;
      this.entries = entries;
   }

   /**
    * 
    * @return l'identifiant du dictionnaire
    */
   public final String getId() {
      return identifiant;
   }

   /**
    * 
    * @param identifiant
    *           identifiant du dictionnaire
    */
   public final void setId(String identifiant) {
      this.identifiant = identifiant;
   }

   /**
    * 
    * @return Liste des valeurs autorisées
    */
   public final List<String> getEntries() {
      return entries;
   }

   /**
    * 
    * @param entries
    *           Liste des valeurs autorisées
    */
   public final void setEntries(List<String> entries) {
      this.entries = entries;
   }

}

package fr.urssaf.image.sae.integration.ihmweb.modele;


/**
 * Propriétés d'une note
 */
public class Note {
      
      private String contenu;
      private String dateCreation;
      private String auteur;
      
      /**
       * @return le contenu de la note
       */
      public String getContenu() {
         return contenu;
      }
      
      /**
       * @param contenu Affecte le contenu de la note
       */ 
      public void setContenu(String contenu) {
         this.contenu = contenu;
      }
      
      /**
       * @return la date de création de la note
       */      
      public String getDateCreation() {
         return dateCreation;
      }
      
      /**
       * @param dateCreation Affecte la date de création de la note
       */ 
      public void setDateCreation(String dateCreation) {
         this.dateCreation = dateCreation;
      }
      
      /**
       * @return l'auteur de la note
       */
      public String getAuteur() {
         return auteur;
      }

      /**
       * @param auteur Affecte l'auteur de la note
       */ 
      public void setAuteur(String auteur) {
         this.auteur = auteur;
      }
      
      /**
       * @return la note au format texte
       */
      @Override
      public String toString() {
         return "Note [contenu=" + contenu + ", dateCreation=" + dateCreation
         + ", auteur=" + auteur + "]";
      }

}

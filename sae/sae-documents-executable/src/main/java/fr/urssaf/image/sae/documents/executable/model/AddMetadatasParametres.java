package fr.urssaf.image.sae.documents.executable.model;

import java.util.Map;

/**
 * Objet permettant de stocker les paramètres concernant 
 * l’ajout de métadonnées à un ensemble de documents
 */
public class AddMetadatasParametres extends AbstractParametres{

   /**
    * Map de clés/valeurs des métadonnées à ajouter
    */
   private Map<String, String> metadonnees;
   
   /**
    * Chemin du fichier csv.
    */
   private String cheminFichier;

   /**
    * Permet de récupérer la liste des métadonnées à consulter.
    * 
    * @return List<String>
    */
   public final Map<String, String> getMetadonnees() {
      return metadonnees;
   }

   /**
    * Permet de modifier la liste des métadonnées à consulter.
    * 
    * @param metadonnees : liste des métadonnées à ajouuter
    */
   public final void setMetadonnees(final Map<String, String> metadonnees) {
      this.metadonnees = metadonnees;
   }

   /**
    * Permet de récupérer le chemin du fichier csv.
    * 
    * @return String
    */
   public final String getCheminFichier() {
      return cheminFichier;
   }

   /**
    * Permet de modifier le chemin du fichier cvs.
    * 
    * @param cheminFichier chemin du fichier cvs
    */
   public final void setCheminFichier(final String cheminFichier) {
      this.cheminFichier = cheminFichier;
   }
}

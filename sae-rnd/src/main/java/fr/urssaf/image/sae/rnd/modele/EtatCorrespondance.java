package fr.urssaf.image.sae.rnd.modele;

/**
 * Etat possible des mises à jour des correspondances
 * 
 *
 */
public enum EtatCorrespondance {
   CREATED("Correspondance crée en base"), 
   STARTING("Mise à jour des documents concernés démarrée"),
   SUCCES("Mise à jour des documents concernés réussie"),
   FAILURE("Echec mise à jour des documents concernés");

   private String description;

   EtatCorrespondance(String description) {
      this.description = description;
   }

   /**
    * 
    * @return le nom
    */
   public String getValue() {
      return name();
   }


   /**
    * 
    * @return le libellé
    */
   public String getDescription() {
      return description;
   }

   /**
    * 
    * @param description
    *           Le libellé
    */
   public void setDescription(String description) {
      this.description = description;
   }
}

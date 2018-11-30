package fr.urssaf.image.sae.integration.ihmweb.modele;


/**
 * Représente un comptage<br>
 * <br>
 * Soit l'attribut erreur est renseigné, et cela signifie qu'une erreur s'est produite
 * lors du comptage. Sinon, on peut lire l'attribut nombre.
 */
public class Comptage {

   private Long comptage;
   private String erreur;
   
   /**
    * Le comptage
    * @return Le comptage
    */
   public final Long getComptage() {
      return comptage;
   }
   
   
   /***
    * Le comptage
    * @param comptage Le comptage
    */
   public final void setComptage(Long comptage) {
      this.comptage = comptage;
   }
   
   
   /**
    * L'éventuelle erreur
    * @return L'éventuelle erreur
    */
   public final String getErreur() {
      return erreur;
   }
   
   
   /**
    * L'éventuelle erreur
    * @param erreur L'éventuelle erreur
    */
   public final void setErreur(String erreur) {
      this.erreur = erreur;
   }
 
}
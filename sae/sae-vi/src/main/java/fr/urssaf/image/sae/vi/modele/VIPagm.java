package fr.urssaf.image.sae.vi.modele;


/**
 * Le PAGM est un droit SAE véhiculé par le Vecteur d'Identification.<br>
 * Le terme PAGM est issu d'Interops et signifie "Profil Applicatif Générique Métier".<br>
 * Un PAGM est constitué :<br>
 * <ul>
 *    <li>d'un droit applicatif : il s'agit d'un code sous forme d'une chaîne de caractères</li>
 *    <li>d'un périmètre de données : il s'agit également d'un code sous forme d'une chaîne de caractères</li>
 * </ul>
 */
@Deprecated
public class VIPagm {
   
   
   private String droitApplicatif;
   
   private String perimetreDonnees;

   
   /**
    * Constructeur par défaut
    */
   public VIPagm() {
      // rien à faire
   }
   
   
   /**
    * Constructeur
    * 
    * @param droitApplicatif le droit applicatif 
    * @param perimetreDonnees le périmètre de données
    */
   public VIPagm(String droitApplicatif, String perimetreDonnees) {
      this.droitApplicatif = droitApplicatif;
      this.perimetreDonnees = perimetreDonnees;
   }
   
   
   /**
    * Le droit applicatif
    * 
    * @return Le droit applicatif
    */
   public final String getDroitApplicatif() {
      return droitApplicatif;
   }

   /**
    * Le droit applicatif
    * 
    * @param droitApplicatif Le droit applicatif
    */
   public final void setDroitApplicatif(String droitApplicatif) {
      this.droitApplicatif = droitApplicatif;
   }

   /**
    * Le périmètre de données
    * 
    * @return Le périmètre de données
    */
   public final String getPerimetreDonnees() {
      return perimetreDonnees;
   }

   /**
    * Le périmètre de données
    * 
    * @param perimetreDonnees Le périmètre de données
    */
   public final void setPerimetreDonnees(String perimetreDonnees) {
      this.perimetreDonnees = perimetreDonnees;
   }

}

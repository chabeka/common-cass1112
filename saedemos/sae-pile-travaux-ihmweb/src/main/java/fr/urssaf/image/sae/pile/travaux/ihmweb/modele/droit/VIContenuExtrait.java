package fr.urssaf.image.sae.pile.travaux.ihmweb.modele.droit;

/**
 * Résultats de la vérification d'un vecteur d’identification.<br>
 * <br>
 * Contient des informations qui peuvent être utilisées pour mettre en place un
 * contexte de sécurité basé sur l'authentification.
 * 
 * 
 */
public class VIContenuExtrait {

   private String codeAppli;

   private String idUtilisateur;

   private SaeDroits saeDroits;

   /**
    * 
    * @return Le code de l'application consommatrice
    */
   public final String getCodeAppli() {
      return codeAppli;
   }

   /**
    * 
    * @param codeAppli
    *           Le code de l'application consommatrice
    */
   public final void setCodeAppli(String codeAppli) {
      this.codeAppli = codeAppli;
   }

   /**
    * 
    * @return L'identifiant de l'utilisateur authentifié dans l'application
    *         consommatrice
    */
   public final String getIdUtilisateur() {
      return idUtilisateur;
   }

   /**
    * 
    * @param idUtilisateur
    *           L'identifiant de l'utilisateur authentifié dans l'application
    *           consommatrice
    */
   public final void setIdUtilisateur(String idUtilisateur) {
      this.idUtilisateur = idUtilisateur;
   }

   /**
    * @return la liste des droits du SAE
    */
   public final SaeDroits getSaeDroits() {
      return saeDroits;
   }

   /**
    * @param saeDroits
    *           la liste des droits du SAE
    */
   public final void setSaeDroits(SaeDroits saeDroits) {
      this.saeDroits = saeDroits;
   }

}

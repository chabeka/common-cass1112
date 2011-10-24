package fr.urssaf.image.sae.vi.modele;

import java.util.List;

/**
 * Résultats de la vérification d'un vecteur d’identification.<br>
 * <br>
 * Contient des informations qui peuvent être utilisées pour mettre en place un
 * contexte de sécurité basé sur l'authentification.
 * 
 * 
 */
public class VIContenuExtrait {

   private List<VIPagm> pagm;

   private String codeAppli;

   private String idUtilisateur;

   /**
    * 
    * @return La liste des PAGM
    */
   public final List<VIPagm> getPagm() {
      return pagm;
   }

   /**
    * 
    * @param pagm
    *           La liste des PAGM
    */
   public final void setPagm(List<VIPagm> pagm) {
      this.pagm = pagm;
   }

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

}

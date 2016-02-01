package fr.urssaf.image.sae.vi.modele;

import java.util.ArrayList;
import java.util.List;

import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.droit.model.SaeDroits;

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

   private List<String> pagms = new ArrayList<String>();
   
   // Note : Utilisation d'un Set et non d'une liste pour éviter les doublons.
   private List<FormatControlProfil> listControlProfil = new ArrayList<FormatControlProfil>();

   /**
    * @return the listControlProfil
    */
   public List<FormatControlProfil> getListControlProfil() {
      return listControlProfil;
   }

   /**
    * @param listControlProfil
    *           the listControlProfil to set
    */
   public void setListControlProfil(
         List<FormatControlProfil> listControlProfil) {
      this.listControlProfil = listControlProfil;
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

   /**
    * Le ou les PAGM
    * 
    * @return Le ou les PAGM
    */
   public List<String> getPagms() {
      return pagms;
   }

   /**
    * Le ou les PAGM
    * 
    * @param pagms
    *           Le ou les PAGM
    */
   public void setPagms(List<String> pagms) {
      this.pagms = pagms;
   }

}

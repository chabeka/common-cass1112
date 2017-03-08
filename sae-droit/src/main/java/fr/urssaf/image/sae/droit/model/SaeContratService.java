package fr.urssaf.image.sae.droit.model;

import java.util.List;

import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;

/**
 * Classe représentant un contrat de service complet
 * 
 * 
 */
public class SaeContratService extends ServiceContract {

   /**
    * Liste des PAGM du contrat de service
    */
   private List<SaePagm> saePagms;

   /**
    * Liste des Prmd rattachés au contrat de service
    */
   private List<SaePrmd> saePrmds;

   /**
    * Liste des profils de contrôle du format rattachés au contrat de service
    */
   private List<FormatControlProfil> formatControlProfils;

   /**
    * @return la liste des pagms
    */
   public final List<SaePagm> getSaePagms() {
      return saePagms;
   }

   /**
    * @param saePagms
    *           la liste des pagms
    */
   public final void setSaePagms(List<SaePagm> saePagms) {
      this.saePagms = saePagms;
   }

   /**
    * @return la liste des prmds
    */
   public final List<SaePrmd> getSaePrmds() {
      return saePrmds;
   }

   /**
    * @param saePrmds
    *           la liste des prmds
    */
   public final void setSaePrmds(List<SaePrmd> saePrmds) {
      this.saePrmds = saePrmds;
   }

   /**
    * @return the formatControlProfils
    */
   public final List<FormatControlProfil> getFormatControlProfils() {
      return formatControlProfils;
   }

   /**
    * @param formatControlProfils the formatControlProfils to set
    */
   public final void setFormatControlProfils(
         List<FormatControlProfil> formatCtrlProfils) {
      this.formatControlProfils = formatCtrlProfils;
   }

}

package fr.urssaf.image.sae.droit.model;

import java.util.ArrayList;
import java.util.List;

import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;

/**
 * objet métier contenant l'ensemble des droits et des formats
 * 
 * 
 */
public class SaeDroitsEtFormat {

   private SaeDroits saeDroits;
   private List<FormatControlProfil> listFormatControlProfil = new ArrayList<FormatControlProfil>();

   /**
    * @return the saeDroits
    */
   public SaeDroits getSaeDroits() {
      return saeDroits;
   }

   /**
    * @param saeDroits
    *           the saeDroits to set
    */
   public void setSaeDroits(SaeDroits saeDroits) {
      this.saeDroits = saeDroits;
   }

   /**
    * @return the listFormatControlProfil
    */
   public List<FormatControlProfil> getListFormatControlProfil() {
      return listFormatControlProfil;
   }

   /**
    * @param listFormatControlProfil
    *           the listFormatControlProfil to set
    */
   public void setListFormatControlProfil(
         List<FormatControlProfil> listFormatControlProfil) {
      this.listFormatControlProfil = listFormatControlProfil;
   }

}

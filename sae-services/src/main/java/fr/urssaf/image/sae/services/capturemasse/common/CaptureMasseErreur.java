/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.common;

import java.util.List;

/**
 * Objet représentant les erreurs rencontrées lors du traitement.<br />
 * Contient les éléments suivants : <br />
 * <ul>
 * <li>la liste des index des documents en erreur</li>
 * <li>la liste des codes erreurs</li>
 * <li>la liste des exceptions</li>
 * </ul>
 * 
 * 
 */
public class CaptureMasseErreur {

   /**
    * Liste des codes erreur
    */
   private List<String> listCodes;

   /**
    * Liste des index
    */
   private List<Integer> listIndex;

   /**
    * Liste des index des fichiers de référence
    */
   private List<Integer> listRefIndex;

   /**
    * Liste des exceptions
    */
   private List<Exception> listException;

   /**
    * @return the listCodes
    */
   public final List<String> getListCodes() {
      return listCodes;
   }

   /**
    * @param listCodes
    *           the listCodes to set
    */
   public final void setListCodes(List<String> listCodes) {
      this.listCodes = listCodes;
   }

   /**
    * @return the listIndex
    */
   public final List<Integer> getListIndex() {
      return listIndex;
   }

   /**
    * @param listIndex
    *           the listIndex to set
    */
   public final void setListIndex(List<Integer> listIndex) {
      this.listIndex = listIndex;
   }

   /**
    * @return the listException
    */
   public final List<Exception> getListException() {
      return listException;
   }

   /**
    * @param listException
    *           the listException to set
    */
   public final void setListException(List<Exception> listException) {
      this.listException = listException;
   }

   /**
    * @return the listRefIndex
    */
   public final List<Integer> getListRefIndex() {
      return listRefIndex;
   }

   /**
    * @param listRefIndex
    *           the listRefIndex to set
    */
   public final void setListRefIndex(List<Integer> listRefIndex) {
      this.listRefIndex = listRefIndex;
   }

}

/**
 * 
 */
package fr.urssaf.image.sae.integration.ihmweb.controller.components;

import fr.urssaf.image.sae.integration.ihmweb.modele.ecde.EcdeTest;

/**
 * 
 * 
 */
public class EcdeTestDisplayed extends EcdeTest {

   private boolean checked;

   private String statusTraitement;

   private String erreur;

   /**
    * Constructeur
    * 
    * @param ecdeTest
    *           test métier
    */
   public EcdeTestDisplayed(EcdeTest ecdeTest) {
      super(ecdeTest.getName(), ecdeTest.getUrl());
   }

   /**
    * constructeur par défaut
    */
   public EcdeTestDisplayed() {
      super();
   }

   /**
    * @return the checked
    */
   public final boolean getChecked() {
      return checked;
   }

   /**
    * @param checked
    *           the checked to set
    */
   public final void setChecked(boolean checked) {
      this.checked = checked;
   }

   /**
    * @return the statusTraitement
    */
   public final String getStatusTraitement() {
      return statusTraitement;
   }

   /**
    * @param statusTraitement
    *           the statusTraitement to set
    */
   public final void setStatusTraitement(String statusTraitement) {
      this.statusTraitement = statusTraitement;
   }

   /**
    * @return the erreur
    */
   public final String getErreur() {
      return erreur;
   }

   /**
    * @param erreur
    *           the erreur to set
    */
   public final void setErreur(String erreur) {
      this.erreur = erreur;
   }

}

/**
 * 
 */
package fr.urssaf.image.sae.integration.ihmweb.formulaire;

import java.util.List;

import fr.urssaf.image.sae.integration.ihmweb.controller.components.EcdeTestDisplayed;
import fr.urssaf.image.sae.integration.ihmweb.modele.ecde.EcdeTest;

/**
 * 
 * 
 */
public class TestInjectionFormulaire {

   private List<EcdeTestDisplayed> listEcdeDisplayed;

   /**
    * Liste des URLS à traiter
    */
   private List<String> treatmentList;

   /**
    * Cas de test en mode saisie
    */
   private EcdeTest test;
   
   /**
    * URL du WS
    */
   private String url;

   /**
    * @return the treatmentList
    */
   public final List<String> getTreatmentList() {
      return treatmentList;
   }

   /**
    * @param treatmentList
    *           the treatmentList to set
    */
   public final void setTreatmentList(List<String> treatmentList) {
      this.treatmentList = treatmentList;
   }

   /**
    * @return the test
    */
   public final EcdeTest getTest() {
      return test;
   }

   /**
    * @param test
    *           the test to set
    */
   public final void setTest(EcdeTest test) {
      this.test = test;
   }

   /**
    * @return the listEcdeDisplayed
    */
   public final List<EcdeTestDisplayed> getListEcdeDisplayed() {
      return listEcdeDisplayed;
   }

   /**
    * @param listEcdeDisplayed
    *           the listEcdeDisplayed to set
    */
   public final void setListEcdeDisplayed(List<EcdeTestDisplayed> listEcdeDisplayed) {
      this.listEcdeDisplayed = listEcdeDisplayed;
   }

   /**
    * @return the url
    */
   public final String getUrl() {
      return url;
   }

   /**
    * @param url the url to set
    */
   public final void setUrl(String url) {
      this.url = url;
   }

}

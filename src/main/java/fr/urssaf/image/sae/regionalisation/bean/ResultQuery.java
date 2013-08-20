/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.bean;

import java.util.List;

/**
 * 
 * 
 */
public class ResultQuery {

   private List<String> results;
   
   private String currentCode;

   
   
   /**
    * Constructeur
    * @param results liste des résultats
    * @param currentCode dernier code utilisé
    */
   public ResultQuery(List<String> results, String currentCode) {
      this.results = results;
      this.currentCode = currentCode;
   }

   /**
    * @return the results
    */
   public final List<String> getResults() {
      return results;
   }

   /**
    * @return the currentCode
    */
   public final String getCurrentCode() {
      return currentCode;
   }
   
   
   
}

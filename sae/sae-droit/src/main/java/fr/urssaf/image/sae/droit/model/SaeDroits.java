/**
 * 
 */
package fr.urssaf.image.sae.droit.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 */
public class SaeDroits extends HashMap<String, List<SaePrmd>> implements
      Map<String, List<SaePrmd>> {

   private static final long serialVersionUID = -5913141404840013369L;

   /** code de l'action unitaire d'un droit du SAE */
   private String key;
   
   /** Liste des PRMD d'un droit du SAE */
   private List<SaePrmd> values;

   /**
    * @return le code de l'action unitaire d'un droit du SAE
    */
   public final String getKey() {
      return key;
   }

   /**
    * @param key code de l'action unitaire d'un droit du SAE
    */
   public final void setKey(String key) {
      this.key = key;
   }

   /**
    * @return la liste des PRMD d'un droit du SAE
    */
   public final List<SaePrmd> getValues() {
      return values;
   }

   /**
    * @param values liste des PRMD d'un droit du SAE
    */
   public final void setValues(List<SaePrmd> values) {
      this.values = values;
   }
   
   
   
}

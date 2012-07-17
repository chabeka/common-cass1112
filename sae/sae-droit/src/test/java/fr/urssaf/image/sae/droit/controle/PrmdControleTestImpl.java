/**
 * 
 */
package fr.urssaf.image.sae.droit.controle;

import java.util.List;
import java.util.Map;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;

/**
 * 
 * 
 */
public class PrmdControleTestImpl implements PrmdControle {

   /*
    * (non-Javadoc)
    * 
    * @see
    * fr.urssaf.image.sae.droit.controle.PrmdControle#createLucene(java.util
    * .Map)
    */
   @Override
   public final String createLucene(Map<String, List<String>> parametres) {
      // TODO Auto-generated method stub
      return null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * fr.urssaf.image.sae.droit.controle.PrmdControle#isPermitted(java.util.
    * List, java.util.Map)
    */
   @Override
   public final boolean isPermitted(List<UntypedMetadata> metadatas,
         Map<String, String> values) {
      return true;
   }

}

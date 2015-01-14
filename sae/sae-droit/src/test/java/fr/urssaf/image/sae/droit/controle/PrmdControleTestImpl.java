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

   /**
    * {@inheritDoc}
    */
   @Override
   public final String createLucene(Map<String, String> parametres) {
      return "prmd1:valeur1";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean isPermitted(List<UntypedMetadata> metadatas,
         Map<String, String> values) {
      return true;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void addDomaine(List<UntypedMetadata> metadatas,
         Map<String, String> values) {
   }

}

/**
 * 
 */
package fr.urssaf.image.sae.security;

import java.util.List;
import java.util.Map;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.controle.PrmdControle;

/**
 * 
 * 
 */
public class PrmdControlePermitNothing implements PrmdControle {

   /**
    * {@inheritDoc}
    */
   @Override
   public final String createLucene(Map<String, String> parametres) {
      return null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean isPermitted(List<UntypedMetadata> metadatas,
         Map<String, String> values) {
      return false;
   }

}

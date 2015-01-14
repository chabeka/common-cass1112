/**
 * 
 */
package fr.urssaf.image.sae.droit.controle.impl.skip;

import java.util.List;
import java.util.Map;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.controle.PrmdControle;

/**
 * Implémentation de {@link PrmdControle} qui donne tous les droits
 * 
 */
public class PrmdControlePermitAll implements PrmdControle {

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
      return true;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void addDomaine(List<UntypedMetadata> metadatas,
         Map<String, String> values) {
      
      //-- On vérifie que la métadonnée « DomaineCotisant » n’est pas présente
      boolean isDomaineCotisantFound = false;
      for (UntypedMetadata meta : metadatas) {
         if(meta.getLongCode().equals("DomaineCotisant")){
            isDomaineCotisantFound = true;
            break;
         }
      }
      
      if(!isDomaineCotisantFound){
         metadatas.add(new UntypedMetadata("DomaineCotisant", "1"));
      }
   }

}

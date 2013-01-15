package fr.urssaf.image.sae.integration.ihmweb.comparator;

import java.util.Comparator;
import java.util.Map;

import fr.urssaf.image.sae.droit.dao.model.Prmd;

public class PrmdComparator implements Comparator<Map> {

   @Override
   public int compare(Map o1, Map o2) {
      
      String codePrmd1 = ((Prmd) o1.get("prmd")).getCode();
      String codePrmd2 = ((Prmd) o2.get("prmd")).getCode();
      
      return codePrmd1.compareTo(codePrmd2);
      
   }

}

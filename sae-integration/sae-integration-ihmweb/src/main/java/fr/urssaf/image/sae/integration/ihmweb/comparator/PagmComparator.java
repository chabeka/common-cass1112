package fr.urssaf.image.sae.integration.ihmweb.comparator;

import java.util.Comparator;

import fr.urssaf.image.sae.droit.dao.model.Pagm;

public class PagmComparator implements Comparator<Pagm> {

   @Override
   public int compare(Pagm o1, Pagm o2) {
      
      return o1.getCode().compareTo(o2.getCode());
      
   }

}

package fr.urssaf.image.sae.integration.ihmweb.comparator;

import java.util.Comparator;

import fr.urssaf.image.sae.droit.dao.model.ServiceContractDatas;

public class ServiceContractDatasComparator implements Comparator<ServiceContractDatas> {

   @Override
   public int compare(ServiceContractDatas o1, ServiceContractDatas o2) {
      
      return o1.getCodeClient().compareTo(o2.getCodeClient());
      
   }

}

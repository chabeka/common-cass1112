package fr.urssaf.image.sae.integration.ihmweb.modele.piletravaux;

import java.util.Comparator;

public class JobRequestComparator implements Comparator<JobRequest> {

   @Override
   public int compare(JobRequest o1, JobRequest o2) {
      
      if ((o1.getCreationDate()!=null) && (o2.getCreationDate()!=null)) {
         
         return o1.getCreationDate().compareTo(o2.getCreationDate()) * -1;
         
      } else {
         
         return 0;
         
      }
      
   }

}

package fr.urssaf.image.sae.pile.travaux.ihmweb.modele;

import java.util.Comparator;

public class JobRequestComparator implements Comparator<JobRequest> {

   @Override
   @SuppressWarnings("PMD.ConfusingTernary")
   public int compare(JobRequest job1, JobRequest job2) {
      
      if ((job1.getCreationDate()!=null) && (job2.getCreationDate()!=null)) {
         
         return job1.getCreationDate().compareTo(job2.getCreationDate()) * -1;
         
      } else if ((job1.getCreationDate()==null) && (job2.getCreationDate()!=null)) {
         
         return 1;
      
      }  else if ((job1.getCreationDate()!=null) && (job2.getCreationDate()==null)) {
         
         return -1;
      }
      else {
         
         return 0;
         
      }
      
   }

}

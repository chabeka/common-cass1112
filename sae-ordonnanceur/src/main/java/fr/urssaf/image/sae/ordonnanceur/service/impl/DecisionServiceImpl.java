package fr.urssaf.image.sae.ordonnanceur.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.ordonnanceur.exception.AucunJobALancerException;
import fr.urssaf.image.sae.ordonnanceur.service.DecisionService;
import fr.urssaf.image.sae.ordonnanceur.support.CaptureMasseSupport;

/**
 * Implémentation du sevice {@link DecisionService}
 * 
 * 
 */
@Service
public class DecisionServiceImpl implements DecisionService {

   @Autowired
   private CaptureMasseSupport captureMasseSupport;

   /**
    * {@inheritDoc}
    */
   @Override
   public final JobInstance trouverJobALancer(List<JobInstance> jobsEnAttente,
         Collection<JobExecution> jobsEnCours) throws AucunJobALancerException {

      // pour l'instant la partie décisionnelle ne prend actuellement en compte
      // que les traitements d'archivage de masse.
      // si un nouveau type de traitement est ajouté, l'algo sera modifié.

      // vérification que des traitements de capture en masse sont à lancer
      if (CollectionUtils.isEmpty(jobsEnAttente)) {
         throw new AucunJobALancerException();
      }

      // filtrage des capture en masse sur l'ECDE local
      List<JobInstance> jobInstances = captureMasseSupport
            .filtrerJobInstanceLocal(jobsEnAttente);

      // vérification que des traitements de capture en masse sur l'ECDE local
      // sont à lancer
      if (CollectionUtils.isEmpty(jobInstances)) {
         throw new AucunJobALancerException();
      }

      // filtrage des traitements en cours sur les capture en masse sur le
      // serveur courant
      if (!CollectionUtils.isEmpty(jobsEnCours)) {
         Collection<JobExecution> traitementsEnCours = captureMasseSupport
               .filtrerJobExecutionLocal(jobsEnCours);

         // si un traitement de capture en masse est en cours alors aucun
         // traitement n'est à lancer sur le serveur
         if (!traitementsEnCours.isEmpty()) {
            throw new AucunJobALancerException();
         }

      }

      // on renvoie la capture en masse enn attente avec le plus petit
      // identifiant
      // on trie les traitements en attente en fonction de leur identifiant
      Collections.sort(jobInstances, new JobInstanceComparator());

      return jobInstances.get(0);
   }

   private static class JobInstanceComparator implements
         Comparator<JobInstance>, Serializable {

      private static final long serialVersionUID = 1L;

      @Override
      public int compare(JobInstance job1, JobInstance job2) {

         return job1.getId().compareTo(job2.getId());
      }
   }

}

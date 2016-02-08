package fr.urssaf.image.sae.ordonnanceur.service.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.ordonnanceur.exception.AucunJobALancerException;
import fr.urssaf.image.sae.ordonnanceur.service.DecisionService;
import fr.urssaf.image.sae.ordonnanceur.service.JobFailureService;
import fr.urssaf.image.sae.ordonnanceur.support.DFCESupport;
import fr.urssaf.image.sae.ordonnanceur.support.TraceOrdoSupport;
import fr.urssaf.image.sae.ordonnanceur.support.TraitementMasseSupport;
import fr.urssaf.image.sae.ordonnanceur.util.ListeUtils;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;

/**
 * Implémentation du sevice {@link DecisionService}
 * 
 * 
 */
@Service
public class DecisionServiceImpl implements DecisionService {

   private final TraitementMasseSupport traitementMasseSupport;

   private final DFCESupport dfceSuppport;

   private final JobFailureService jobFailureService;

   private final TraceOrdoSupport traceOrdoSupport;

   private static final Logger LOG = LoggerFactory
         .getLogger(DecisionServiceImpl.class);

   private static final String PREFIX_LOG = "ordonnanceur()";

   /**
    * Liste des URL ECDE qui ont été tracées comme indisponibles
    */
   private final List<URI> listeUrlCaptureMasseIndispo = new ArrayList<URI>();

   /**
    * Nombre maximal d'éléments à stocker dans la liste
    * listeUrlCaptureMasseIndispo
    */
   private static final int NB_MAX_CAPTURE_MASSE_INDISPO = 500;

   /**
    * 
    * @param captureMasseSupport
    *           support pour les traitements de capture en masse
    * @param traitementMasseSupport
    *           support pour les traitements de masse
    * @param dfceSuppport
    *           service de DFCE
    * @param jobFailureService
    *           service pour le traitement en échec
    * @param traceOrdoSupport
    *           support pour l'écriture de la traçabilité
    */
   @Autowired
   public DecisionServiceImpl(TraitementMasseSupport traitementMasseSupport,
         DFCESupport dfceSuppport, JobFailureService jobFailureService,
         TraceOrdoSupport traceOrdoSupport) {

      this.traitementMasseSupport = traitementMasseSupport;
      this.dfceSuppport = dfceSuppport;
      this.jobFailureService = jobFailureService;
      this.traceOrdoSupport = traceOrdoSupport;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final JobQueue trouverJobALancer(List<JobQueue> jobsEnAttente,
         List<JobRequest> jobsEnCours) throws AucunJobALancerException {

      // pour l'instant la partie décisionnelle ne prend actuellement en compte
      // que les traitements d'archivage de masse.
      // si un nouveau type de traitement est ajouté, l'algo sera modifié.

      // vérification que des traitements de masse sont à lancer
      if (CollectionUtils.isEmpty(jobsEnAttente)) {
         throw new AucunJobALancerException();
      }

      // filtrage des traitements de masse :
      // - capture en masse sur l'ECDE local
      // - suppression en masse sur tous les CNP
      // - restore en masse sur tous les CNP
      List<JobQueue> jobInstances = traitementMasseSupport
            .filtrerTraitementMasse(jobsEnAttente);

      // vérification que des traitements de masse
      if (CollectionUtils.isEmpty(jobInstances)) {
         throw new AucunJobALancerException();
      }

      // filtrage des traitements en cours sur les capture en masse, suppression
      // et restore
      if (!CollectionUtils.isEmpty(jobsEnCours)) {
         Collection<JobRequest> enCours = traitementMasseSupport
               .filtrerTraitementMasse(jobsEnCours);

         // si un traitement en masse est en cours alors aucun
         // traitement n'est à lancer sur le serveur
         if (!enCours.isEmpty()) {
            throw new AucunJobALancerException();
         }

      }

      // filtrage des traitements de masse à lancer des traitements ayant
      // soulevé un nombre anormal d'anomalies
      jobInstances = filtrerTraitementMasseFailure(jobInstances);
      if (CollectionUtils.isEmpty(jobInstances)) {
         throw new AucunJobALancerException();
      }

      // vérification que le serveur DFCE est Up!
      if (!dfceSuppport.isDfceUp()) {
         LOG.debug("{} - DFCE n'est pas accessible avec la configuration",
               PREFIX_LOG);
         throw new AucunJobALancerException();
      }

      // Si on arrive jusque là, on sélectionne le job à lancer,
      // c'est à dire le plus ancien dans la pile
      JobQueue jobAlancer = jobInstances.get(0);

      // Si le job à lancer est une capture de masse, on
      // vérifie que l'ECDE est accessible
      controleDispoEcdeCaptureMasse(jobAlancer);

      // renvoie du job à lancer
      return jobAlancer;

   }

   private List<JobQueue> filtrerTraitementMasseFailure(
         Collection<JobQueue> jobRequests) {

      final Set<UUID> jobsFailure = jobFailureService.findJobEchec();

      @SuppressWarnings("unchecked")
      List<JobQueue> jobMasse = (List<JobQueue>) CollectionUtils.select(
            jobRequests, new Predicate() {

               @Override
               public boolean evaluate(Object object) {

                  JobQueue jobRequest = (JobQueue) object;

                  // on filtre les traitements ayant trop d'échec
                  boolean isJobFailure = !jobsFailure.contains(jobRequest
                        .getIdJob());

                  return isJobFailure;
               }

            });

      return jobMasse;
   }

   private void controleDispoEcdeCaptureMasse(JobQueue jobAlancer)
         throws AucunJobALancerException {

      if ((traitementMasseSupport.isCaptureMasse(jobAlancer))
            && (!traitementMasseSupport.isEcdeUpJobCaptureMasse(jobAlancer))) {

         URI urlEcde = traitementMasseSupport.extractUrlEcde(jobAlancer);

         // On écrit la trace qu'une seule fois pour éviter une
         // redondance inutile
         if (!listeUrlCaptureMasseIndispo.contains(urlEcde)) {

            // Maintien une liste de moins de n éléments
            // pour éviter une occupation mémoire inutile
            ListeUtils.nettoieListeSiBesoin(listeUrlCaptureMasseIndispo,
                  NB_MAX_CAPTURE_MASSE_INDISPO);

            // Trace applicative sur le serveur dans un fichier log
            LOG.error("L'URL ECDE pointée par le job de capture de masse "
                  + jobAlancer.getIdJob() + " n'est pas disponible : "
                  + urlEcde.toString());

            // Trace générale au niveau de la plateforme
            traceOrdoSupport.ecritTraceUrlEcdeNonDispo(jobAlancer.getIdJob(),
                  urlEcde);

            // Ajout de l'URL à la liste de celle déjà tracée en erreur
            listeUrlCaptureMasseIndispo.add(urlEcde);

         }

         // Lève l'exception indiquant à l'appelant qu'il n'y a aucun traitement
         // à lancer
         throw new AucunJobALancerException();

      }

   }

}

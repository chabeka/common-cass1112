package fr.urssaf.image.sae.ordonnanceur.support.impl;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.ordonnanceur.exception.OrdonnanceurRuntimeException;
import fr.urssaf.image.sae.ordonnanceur.support.EcdeSupport;
import fr.urssaf.image.sae.ordonnanceur.support.TraitementMasseSupport;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;

/**
 * Support pour les traitements en masse
 * 
 * 
 */
@Component
public class TraitementMasseSupportImpl implements TraitementMasseSupport {

   /**
    * Nom du job d'un traitement de capture en masse
    */
   public static final String CAPTURE_MASSE_JN = "capture_masse";
   
   /**
    * Nom du job d'un traitement de suppression de masse
    */
   public static final String SUPPRESSION_MASSE_JN = "suppression_masse";
   
   /**
    * Nom du job d'un traitement de restore de masse
    */
   public static final String RESTORE_MASSE_JN = "restore_masse";
   
   /**
    * Nom du job d'un traitement de modification en masse
    */
   public static final String MODIFICATION_MASSE_JN = "modification_masse";
   
   /**
    * Nom du job d'un traitement de transfert en masse
    */
   public static final String TRANSFERT_MASSE_JN = "transfert_masse";

   private final EcdeSupport ecdeSupport;

   private static final String ECDE_URL = "ecdeUrl";

   public static final List<String> TRAITEMENT_MASSE_AVEC_ECDE = Arrays.asList(
         CAPTURE_MASSE_JN, MODIFICATION_MASSE_JN);

   /**
    * 
    * @param ecdeSupport
    *           service sur l'ECDE
    * 
    */
   @Autowired
   public TraitementMasseSupportImpl(EcdeSupport ecdeSupport) {

      this.ecdeSupport = ecdeSupport;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public final List<JobQueue> filtrerTraitementMasse(
         List<JobQueue> jobRequests) {

      @SuppressWarnings("unchecked")
      List<JobQueue> jobTraitementMasse = (List<JobQueue>) CollectionUtils.select(
            jobRequests, new Predicate() {

               @Override
               public boolean evaluate(Object object) {

                  JobQueue jobRequest = (JobQueue) object;

                  boolean isCaptureMasse = isCaptureMasse(jobRequest);
                  
                  boolean isSuppressionMasse = isSuppressionMasse(jobRequest);
                  
                  boolean isRestoreMasse = isRestoreMasse(jobRequest);

                  boolean isModificationMasse = isModificationMasse(jobRequest);
                  
                  boolean isTransfertMasse = isTransfertMasse(jobRequest);

                  boolean isLocal = isLocal(jobRequest);

                  return isRestoreMasse || isSuppressionMasse
                        || (isCaptureMasse && isLocal) || isModificationMasse || isTransfertMasse;
               }

            });

      return jobTraitementMasse;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<JobRequest> filtrerTraitementMasse(Collection<JobRequest> jobRequests) {

      @SuppressWarnings("unchecked")
      List<JobRequest> jobCaptureMasse = (List<JobRequest>) CollectionUtils
            .select(jobRequests, new Predicate() {

               @Override
               public boolean evaluate(Object object) {

                  JobRequest jobRequest = (JobRequest) object;

                  boolean isCaptureMasse = isCaptureMasseJobRequest(jobRequest);
                  
                  boolean isSuppressionMasse = isSuppressionMasseJobRequest(jobRequest);
                  
                  boolean isRestoreMasse = isRestoreMasseJobRequest(jobRequest);
                  
                  boolean isModificationMasse = isModificationMasseJobRequest(jobRequest);
                  
                  boolean isTransfertMasse = isTransfertMasseJobRequest(jobRequest);

                  return isCaptureMasse || isSuppressionMasse || isRestoreMasse || isModificationMasse || isTransfertMasse;
               }

            });

      return jobCaptureMasse;
   }

   /**
    * Indique si le job passé en paramètre est un job de type "capture de masse"
    * 
    * @param jobRequest
    *           le job
    * @return true si le job est une capture de masse, false dans le cas
    *         contraire
    */
   public final boolean isCaptureMasse(JobQueue jobRequest) {

      boolean isCaptureMasse = CAPTURE_MASSE_JN.equals(jobRequest.getType());

      return isCaptureMasse;
   }
   
   /**
    * Indique si le job passé en paramètre est un job de type "suppression de masse"
    * 
    * @param jobRequest
    *           le job
    * @return true si le job est une suppression de masse, false dans le cas
    *         contraire
    */
   public final boolean isSuppressionMasse(JobQueue jobRequest) {

      boolean isSuppressionMasse = SUPPRESSION_MASSE_JN.equals(jobRequest.getType());

      return isSuppressionMasse;
   }

   /**
    * Indique si le job passé en paramètre est un job de type "restore de masse"
    * 
    * @param jobRequest
    *           le job
    * @return true si le job est une restore de masse, false dans le cas
    *         contraire
    */
   public final boolean isRestoreMasse(JobQueue jobRequest) {

      boolean isRestoreMasse = RESTORE_MASSE_JN.equals(jobRequest.getType());

      return isRestoreMasse;
   }
   
   /**
    * Indique si le job passé en paramètre est un job de type
    * "modification de masse"
    * 
    * @param jobRequest
    *           le job
    * @return true si le job est une restore de masse, false dans le cas
    *         contraire
    */
   public final boolean isModificationMasse(JobQueue jobRequest) {

      boolean isModificationMasse = MODIFICATION_MASSE_JN.equals(jobRequest
            .getType());

      return isModificationMasse;
   }
   
   private boolean isCaptureMasseJobRequest(JobRequest jobRequest) {

      boolean isCaptureMasse = CAPTURE_MASSE_JN.equals(jobRequest.getType());

      return isCaptureMasse;
   }
   
   private boolean isSuppressionMasseJobRequest(JobRequest jobRequest) {

      boolean isSuppressionMasse = SUPPRESSION_MASSE_JN.equals(jobRequest.getType());

      return isSuppressionMasse;
   }
   
   private boolean isRestoreMasseJobRequest(JobRequest jobRequest) {

      boolean isRestoreMasse = RESTORE_MASSE_JN.equals(jobRequest.getType());

      return isRestoreMasse;
   }

   private boolean isModificationMasseJobRequest(JobRequest jobRequest) {

      boolean isCaptureMasse = MODIFICATION_MASSE_JN.equals(jobRequest
            .getType());

      return isCaptureMasse;
   }
   
   /**
    * Indique si le job passé en paramètre est un job de type "transfert de masse"
    * 
    * @param jobRequest
    *           le job
    * @return true si le job est un transfert de masse, false dans le cas
    *         contraire
    */
   public final boolean isTransfertMasse(JobQueue jobRequest) {

      boolean isTransfertMasse = TRANSFERT_MASSE_JN.equals(jobRequest.getType());

      return isTransfertMasse;
   }
   
   private boolean isTransfertMasseJobRequest(JobRequest jobRequest) {

      boolean isTransfertMasse = TRANSFERT_MASSE_JN.equals(jobRequest.getType());

      return isTransfertMasse;
   }

   private boolean isLocal(JobQueue jobQueue) {

      // Dans le cadre d'une capture de masse seule l'url ECDE du sommaire est
      // sérializée dans les paramètres
      URI ecdeUrl = extractUrlEcde(jobQueue);

      boolean isLocal = false;

      if (ecdeUrl != null) {

         try {

            isLocal = ecdeSupport.isLocal(ecdeUrl);

         } catch (IllegalArgumentException e) {

            // cas où ecdeParameter ne respecte pas RFC 2396
            // (Cf. http://www.ietf.org/rfc/rfc2396.txt)

            isLocal = false;

         }

      }

      return isLocal;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public final URI extractUrlEcde(JobQueue jobQueue) {

      String ecdeUrl = StringUtils.EMPTY;

      if (StringUtils.isNotBlank(jobQueue.getParameters())) {
         ecdeUrl = jobQueue.getParameters();
      } else {
         if (MapUtils.isNotEmpty(jobQueue.getJobParameters())) {
            ecdeUrl = jobQueue.getJobParameters().get(ECDE_URL);
         }
      }

      URI ecdeUri = null;
      if (ecdeUrl != null) {
         ecdeUri = URI.create(ecdeUrl);
      }

      return ecdeUri;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean isEcdeUpJobTraitementMasse(JobQueue jobQueue) {

      // Vérifie que le job est bien un job de capture de masse
      if (!isCaptureMasse(jobQueue) && !isModificationMasse(jobQueue)
            && !isTransfertMasse(jobQueue)) {
         throw new OrdonnanceurRuntimeException(
               "Mauvaise utilisation du contrôle de la disponibilité de l'ECDE: le job contrôlé n'est pas un job de traitement de masse devant utilisé l'ecde (id: "
                     + jobQueue.getIdJob() + ")");
      }

      // Extrait l'URL ECDE des paramètres du job
      URI ecdeUrl = extractUrlEcde(jobQueue);

      // Vérifie si l'ECDE est disponble
      return ecdeSupport.isEcdeDisponible(ecdeUrl);

   }


   /**
    * {@inheritDoc}
    */
   @Override
   public List<JobQueue> filtrerTraitementMasseFailure(
         final Set<UUID> jobsFailure, Collection<JobQueue> jobRequests) {
      @SuppressWarnings("unchecked")
      List<JobQueue> jobMasse = (List<JobQueue>) CollectionUtils.select(
            jobRequests, new Predicate() {

               @Override
               public boolean evaluate(Object object) {

                  JobQueue jobRequest = (JobQueue) object;

                  // on filtre les traitements ayant trop d'échec
                  boolean isJobFailure = !jobsFailure.contains(jobRequest
                        .getIdJob());

                  // Pour la modification de masse uniquement, on garde les jobs en erreur
                  // pour le controle du code traitement.
                  return isJobFailure
                        || (!isJobFailure && isModificationMasse(jobRequest));
               }

            });

      return jobMasse;
   }

}

package fr.urssaf.image.sae.ordonnanceur.support.impl;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.commons.utils.Constantes;
import fr.urssaf.image.sae.ordonnanceur.exception.JobRuntimeException;
import fr.urssaf.image.sae.ordonnanceur.exception.OrdonnanceurRuntimeException;
import fr.urssaf.image.sae.ordonnanceur.service.JobService;
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
   
   private static final String ECDE_URL = "ecdeUrl";

   public static final List<String> TRAITEMENT_MASSE_AVEC_ECDE = Arrays.asList(
         CAPTURE_MASSE_JN, MODIFICATION_MASSE_JN);

   private final EcdeSupport ecdeSupport;

   private final JobService jobService;

   /**
    * Constructeur
    * 
    * @param ecdeSupport
    *           service sur l'ECDE
    * @param jobService
    *           job service
    * 
    */
   @Autowired
   public TraitementMasseSupportImpl(EcdeSupport ecdeSupport,
         JobService jobService) {
      this.ecdeSupport = ecdeSupport;
      this.jobService = jobService;
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

                  JobQueue jobQueue = (JobQueue) object;

                  boolean isCaptureMasse = isCaptureMasse(jobQueue);
                  
                  boolean isSuppressionMasse = isSuppressionMasse(jobQueue);
                  
                  boolean isRestoreMasse = isRestoreMasse(jobQueue);

                  boolean isModificationMasse = isModificationMasse(jobQueue);

                  boolean isTransfertMasse = isTransfertMasse(jobQueue);
                  
                  boolean isRepriseMasse = isRepriseMasse(jobQueue);
                  
                  JobRequest jobAReprendre = retrieveJobAReprendre(jobQueue);
                  boolean isRepriseCaptureMasse = false;
                  boolean isRepriseLocal = false;
                  if (jobAReprendre != null) {
                     isRepriseCaptureMasse = isRepriseCaptureMasse(
                           jobAReprendre, jobQueue);
                     isRepriseLocal = isLocal(jobAReprendre);
                  }
                  boolean isLocal = isLocal(jobQueue);

                  return isRestoreMasse || isSuppressionMasse
                        || (isCaptureMasse && isLocal) || isModificationMasse
                        || isTransfertMasse
                        || (isRepriseMasse && !isRepriseCaptureMasse)
                        || (isRepriseCaptureMasse && isRepriseLocal);
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
                  
                  boolean isRepriseMasse = isRepriseMasseJobRequest(jobRequest);

                  return isCaptureMasse || isSuppressionMasse || isRestoreMasse 
                        || isModificationMasse || isTransfertMasse
                        || isRepriseMasse;
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

      boolean isModificationMasse = MODIFICATION_MASSE_JN.equals(jobRequest
            .getType());

      return isModificationMasse;
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
   
   /**
    * Détermine si le jobRequest passé en paramètre est un job de reprise de masse
    * @param jobRequest
    * @return true si le jobRequest est une reprsie de masse
    */
   private boolean isRepriseMasseJobRequest(JobRequest jobRequest) {

      boolean isRepriseMasse = Constantes.REPRISE_MASSE_JN.equals(jobRequest.getType());

      return isRepriseMasse;
   }
   
   public final boolean isRepriseMasse(JobQueue jobRequest) {

      boolean isRepriseMasse = Constantes.REPRISE_MASSE_JN.equals(jobRequest.getType());

      return isRepriseMasse;
   }
   
   /**
    * Controle si le job est un job de reprise de la capture de masse.
    * 
    * @param jobAReprendre
    *           job à reprendre {@link JobRequest}
    * @param jobQueue
    *           job {@link jobQueue}
    * @return True si le job est un job de reprise de la capture de masse.
    */
   public final boolean isRepriseCaptureMasse(JobRequest jobAReprendre,
         JobQueue jobQueue) {
      return jobAReprendre != null
            && Constantes.REPRISE_MASSE_JN.equals(jobQueue.getType())
            && jobAReprendre.getType() != null
            && isCaptureMasseJobRequest(jobAReprendre);
   }

   /**
    * Contrôle que l'url ECDE du traitement de masse est une URL local.
    * 
    * @param jobQueue
    *           Job {@link JobQueue}
    * @return l'Url ECDE du traitement de masse
    */
   private boolean isLocal(JobQueue jobQueue) {
      // Dans le cadre d'une capture de masse seule l'url ECDE du sommaire est
      // sérializée dans les paramètres
      URI ecdeUrl = extractUrlEcde(jobQueue);

      return isUrlLocal(ecdeUrl);
   }

   /**
    * 
    * @param jobQueue
    * @return
    */
   private boolean isLocal(JobRequest jobQueue) {
      // Dans le cadre d'une capture de masse seule l'url ECDE du sommaire est
      // sérializée dans les paramètres
      if (jobQueue == null) {
         return false;
      }

      URI ecdeUrl = extractUrlEcde(jobQueue);

      return isUrlLocal(ecdeUrl);
   }

   /**
    * 
    * @param ecdeUrl
    * @return
    */
   private boolean isUrlLocal(URI ecdeUrl) {
      boolean isLocal = false;
      if (ecdeUrl != null) {
         try {
            isLocal = ecdeSupport.isLocal(ecdeUrl);
         } catch (IllegalArgumentException e) {
         }
      }

      return isLocal;
   }

   /**
    * Récupére le job à reprendre d'un job de reprise de traitement de masse.
    * 
    * @param jobQueue
    *           job {@link JobQueue}
    * @return le job à reprendre présent dans les paramètres d'un job à
    *         reprendre
    */
   private JobRequest retrieveJobAReprendre(JobQueue jobQueue) {
      if (Constantes.REPRISE_MASSE_JN.equals(jobQueue.getType())) {
         String idJobAReprendre = null;
         if (jobQueue.getJobParameters() != null
               && !jobQueue.getJobParameters().isEmpty()) {
            Map<String, String> jobParameters = jobQueue.getJobParameters();
            idJobAReprendre = jobParameters
                  .get(Constantes.ID_TRAITEMENT_A_REPRENDRE);
         }

         if (idJobAReprendre != null && !idJobAReprendre.isEmpty()) {
            UUID idJob = UUID.fromString(idJobAReprendre);
            return jobService.getJobRequest(idJob);

         } else {
            throw new JobRuntimeException(
                  jobQueue,
                  new Exception(
                        "Le job de reprise n'a pas d'UUID de job a reprendre en paramètre - Job rejeté"));
         }
      }
      return null;
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

      return this.getEcdeURI(ecdeUrl);
   }


   /**
    * Extrait l'URL ECDE pointé par un job de traitement de masse
    * 
    * @param JobRequest
    *           le job de traitement de masse
    * @return l'URL ECDE pointé par le job de traitement de masse
    */
   public final URI extractUrlEcde(JobRequest jobQueue) {
      String ecdeUrl = StringUtils.EMPTY;

      if (StringUtils.isNotBlank(jobQueue.getParameters())) {
         ecdeUrl = jobQueue.getParameters();
      } else {
         if (MapUtils.isNotEmpty(jobQueue.getJobParameters())) {
            ecdeUrl = jobQueue.getJobParameters().get(ECDE_URL);
         }
      }

      return this.getEcdeURI(ecdeUrl);
   }

   /**
    * Création de l'URL à partir d'une chaine de caractere.
    * 
    * @param ecdeUrl
    *           Url en chaine de caractere.
    * @return URL ecde.
    */
   private URI getEcdeURI(String ecdeUrl) {
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
      // Extrait l'URL ECDE des paramètres du job
      URI ecdeUrl = extractUrlEcde(jobQueue);

      // Si on a pas d'URL ECDE, on a pas à controler la dispo de ce dernier.
      if (ecdeUrl == null) {
         return true;
      }

      // Vérifie que le job est bien un job de qui peut utiliser l'ECDE
      if (!isCaptureMasse(jobQueue) && !isModificationMasse(jobQueue)
            && !isTransfertMasse(jobQueue)) {
         throw new OrdonnanceurRuntimeException(
               "Mauvaise utilisation du contrôle de la disponibilité de l'ECDE: le job contrôlé n'est pas un job de traitement de masse devant utiliser l'ecde (id: "
                     + jobQueue.getIdJob() + ")");
      }

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
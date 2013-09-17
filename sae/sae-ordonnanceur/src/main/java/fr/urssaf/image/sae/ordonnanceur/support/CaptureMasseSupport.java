package fr.urssaf.image.sae.ordonnanceur.support;

import java.net.URI;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.ordonnanceur.exception.OrdonnanceurRuntimeException;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;

/**
 * Support pour les traitements de capture en masse
 * 
 * 
 */
@Component
public class CaptureMasseSupport {

   /**
    * Nom du job d'un traitement de capture en masse
    */
   public static final String CAPTURE_MASSE_JN = "capture_masse";

   private final EcdeSupport ecdeSupport;

   private static final String ECDE_URL = "ecdeUrl";

   /**
    * 
    * @param ecdeSupport
    *           service sur l'ECDE
    * 
    */
   @Autowired
   public CaptureMasseSupport(EcdeSupport ecdeSupport) {

      this.ecdeSupport = ecdeSupport;
   }

   /**
    * Filtre les traitements de masse pour ne récupérer que ceux concernant les
    * capture en masse pour l'ECDE local.<br>
    * <br>
    * Les traitements de capture en masse sont indiqués par la propriété
    * <code>jobName</code> de l'instance {@link JobRequest}.<br>
    * Si il indique '{@value #CAPTURE_MASSE_JN}' alors il s'agit d'une capture
    * en masse.<br>
    * <br>
    * Un traitement de capture en masse indique dans ses paramètres l'URL ECDE
    * du fichier sommaire.xml.<br>
    * on s'appuie sur {@link EcdeSupport#isLocal(URI)} pour savoir si il s'agit
    * d'une URL ECDE local ou non.
    * 
    * @param jobRequests
    *           traitements de masse
    * @return traitements de capture en masse filtrés
    */
   public final List<JobQueue> filtrerCaptureMasseLocal(
         List<JobQueue> jobRequests) {

      @SuppressWarnings("unchecked")
      List<JobQueue> jobCaptureMasse = (List<JobQueue>) CollectionUtils.select(
            jobRequests, new Predicate() {

               @Override
               public boolean evaluate(Object object) {

                  JobQueue jobRequest = (JobQueue) object;

                  boolean isCaptureMasse = isCaptureMasse(jobRequest);

                  boolean isLocal = isLocal(jobRequest);

                  return isCaptureMasse && isLocal;
               }

            });

      return jobCaptureMasse;
   }

   /**
    * Filtre les traitements de masse pour ne récupérer que ceux concernant les
    * capture en masse.<br>
    * <br>
    * Les traitements de capture en masse sont indiqués par la propriété
    * <code>jobName</code> de l'instance {@link JobRequest}.<br>
    * Si il indique '{@value #CAPTURE_MASSE_JN}' alors il s'agit d'une capture
    * en masse.<br>
    * <br>
    * 
    * @param jobRequests
    *           traitements de masse
    * @return traitements de capture en masse filtrés
    */
   public final List<JobRequest> filtrerCaptureMasse(
         Collection<JobRequest> jobRequests) {

      @SuppressWarnings("unchecked")
      List<JobRequest> jobCaptureMasse = (List<JobRequest>) CollectionUtils
            .select(jobRequests, new Predicate() {

               @Override
               public boolean evaluate(Object object) {

                  JobRequest jobRequest = (JobRequest) object;

                  boolean isCaptureMasse = isCaptureMasseJobRequest(jobRequest);

                  return isCaptureMasse;
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

   private boolean isCaptureMasseJobRequest(JobRequest jobRequest) {

      boolean isCaptureMasse = CAPTURE_MASSE_JN.equals(jobRequest.getType());

      return isCaptureMasse;
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
    * Extrait l'URL ECDE pointé par un job de capture de masse
    * 
    * @param jobQueue
    *           le job de capture de masse
    * @return l'URL ECDE pointé par le job de capture de masse
    */
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
    * Vérifie que l'ECDE pointé par le job de capture de masse transmis en
    * paramètre soit disponible.<br>
    * <br>
    * La méthode lève une exception runtime si le job passé en paramètre n'est
    * pas un job de capture de masse.
    * 
    * @param jobQueue
    *           un job de capture de masse
    * @return true si l'ECDE est disponible, false dans le cas contraire
    */
   public final boolean isEcdeUpJobCaptureMasse(JobQueue jobQueue) {

      // Vérifie que le job est bien un job de capture de masse
      if (!isCaptureMasse(jobQueue)) {
         throw new OrdonnanceurRuntimeException(
               "Mauvaise utilisation du contrôle de la disponibilité de l'ECDE: le job contrôlé n'est pas un job de capture de masse (id: "
                     + jobQueue.getIdJob() + ")");
      }

      // Extrait l'URL ECDE des paramètres du job
      URI ecdeUrl = extractUrlEcde(jobQueue);

      // Vérifie si l'ECDE est disponble
      return ecdeSupport.isEcdeDisponible(ecdeUrl);

   }

}

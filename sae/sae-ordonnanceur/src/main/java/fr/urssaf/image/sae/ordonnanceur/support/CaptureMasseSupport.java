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
   
   private final String ECDE_URL= "ecdeUrl";

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

   private boolean isCaptureMasse(JobQueue jobRequest) {

      boolean isCaptureMasse = CAPTURE_MASSE_JN.equals(jobRequest.getType());

      return isCaptureMasse;
   }

   private boolean isCaptureMasseJobRequest(JobRequest jobRequest) {

      boolean isCaptureMasse = CAPTURE_MASSE_JN.equals(jobRequest.getType());

      return isCaptureMasse;
   }

   private boolean isLocal(JobQueue jobRequest) {

      // Dans le cadre d'une capture de masse seule l'url ECDE du sommaire est
      // sérializée dans les paramètres
      String ecdeUrl = StringUtils.EMPTY;
      if (StringUtils.isNotBlank(jobRequest.getParameters())) {
         ecdeUrl = jobRequest.getParameters();
      } else {
         if (MapUtils.isNotEmpty(jobRequest.getJobParameters())) {
            ecdeUrl = jobRequest.getJobParameters().get(ECDE_URL);
         }
      }

      boolean isLocal = false;

      if (ecdeUrl != null) {

         try {
            URI ecdeURL = URI.create(ecdeUrl);

            isLocal = ecdeSupport.isLocal(ecdeURL);

         } catch (IllegalArgumentException e) {

            // cas où ecdeParameter ne respecte pas RFC 2396
            // (Cf. http://www.ietf.org/rfc/rfc2396.txt)

            isLocal = false;

         }

      }

      return isLocal;
   }

}

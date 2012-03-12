package fr.urssaf.image.sae.ordonnanceur.support;

import java.net.URI;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.ordonnanceur.exception.OrdonnanceurRuntimeException;
import fr.urssaf.image.sae.ordonnanceur.util.HostUtils;

/**
 * Support pour les traitements de capture en masse
 * 
 * 
 */
@Component
public class CaptureMasseSupport {

   /**
    * Paramètre indiquant l'URL ECDE du sommaire.xml pour un traitement de
    * capture en masse
    */
   public static final String CAPTURE_MASSE_ECDE = "capture.masse.sommaire";

   /**
    * Paramètre indiquant l'identifiant du traitement de la capture en masse
    */
   public static final String CAPTURE_MASSE_ID = "capture.masse.idtraitement";

   /**
    * Nom du job d'un traitement de capture en masse
    */
   public static final String CAPTURE_MASSE_JN = "capture_masse";

   /**
    * Clé indiquant dans le contexte d'exécution du job le DNS du serveur où est
    * exécuté le traitement
    */
   public static final String CONTEXT_SERVEUR = "serveur";

   private final EcdeSupport ecdeSupport;

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
    * Filtre les instances des traitements de masse pour ne récupérer que ceux
    * concernant les capture en masse pour l'ECDE local.<br>
    * <br>
    * Les traitements de capture en masse sont indiqués par la propriété
    * <code>jobName</code> de l'instance {@link JobInstance}.<br>
    * Si il indique '{@value #CAPTURE_MASSE_JN}' alors il s'agit d'une capture
    * en masse.<br>
    * <br>
    * Un traitement de capture en masse indique dans ses
    * {@link JobInstance#getJobParameters()} le paramètre '
    * {@value #CAPTURE_MASSE_ECDE}' pour l'URL ECDE du fichier sommaire.xml.<br>
    * on s'appuie sur {@link EcdeSupport#isLocal(URI)} pour savoir si il s'agit
    * d'une URL ECDE local ou non.
    * 
    * @param jobInstances
    *           traitements de masse
    * @return traitements de capture en masse filtrés
    */
   public final List<JobInstance> filtrerJobInstanceLocal(
         List<JobInstance> jobInstances) {

      @SuppressWarnings("unchecked")
      List<JobInstance> jobCaptureMasse = (List<JobInstance>) CollectionUtils
            .select(jobInstances, new Predicate() {

               @Override
               public boolean evaluate(Object object) {

                  JobInstance jobInstance = (JobInstance) object;

                  boolean isCaptureMasse = isCaptureMasse(jobInstance);

                  boolean isLocal = isLocal(jobInstance);

                  return isCaptureMasse && isLocal;
               }

            });

      return jobCaptureMasse;
   }

   /**
    * Filtre les exécutions de traitements de masse pour ne récupérer que ceux
    * concernant les capture en masse sur le serveur courant.<br>
    * <br>
    * Les traitements de capture en masse sont indiqués par la propriété
    * <code>jobName</code> de l'instance {@link JobInstance}.<br>
    * Si il indique '{@value #CAPTURE_MASSE_JN}' alors il s'agit d'une capture
    * en masse.<br>
    * <br>
    * Pour filtrer les traitements de capture en masse sur le serveur courant,
    * on récupère dans l'instance {@link ExecutionContext} de l'instance
    * {@link JobExecution} le DNS du serveur courant indiqué par '
    * {@value #CONTEXT_SERVEUR}'.<br>
    * Si la valeur est indique au hostname du serveur courant alors il s'agit
    * d'un traitement local.
    * 
    * @param jobExecutions
    *           traitements de masse
    * @return traitements de capture en masse filtrés
    */
   public final List<JobExecution> filtrerJobExecutionLocal(
         Collection<JobExecution> jobExecutions) {

      @SuppressWarnings("unchecked")
      List<JobExecution> jobCaptureMasse = (List<JobExecution>) CollectionUtils
            .select(jobExecutions, new Predicate() {

               @Override
               public boolean evaluate(Object object) {

                  JobExecution jobExecution = (JobExecution) object;

                  JobInstance jobInstance = jobExecution.getJobInstance();

                  boolean isCaptureMasse = isCaptureMasse(jobInstance);

                  boolean isLocal = isLocal(jobExecution);

                  return isCaptureMasse && isLocal;
               }

            });

      return jobCaptureMasse;
   }

   private boolean isLocal(JobExecution jobExecution) {

      ExecutionContext executionContext = jobExecution.getExecutionContext();

      String hostname = executionContext.getString(CONTEXT_SERVEUR, null);

      String serverName;
      try {

         serverName = HostUtils.getLocalHostName();

      } catch (UnknownHostException e) {

         throw new OrdonnanceurRuntimeException(e);
      }

      boolean isLocal = serverName.equals(hostname);

      return isLocal;
   }

   private boolean isCaptureMasse(JobInstance jobInstance) {

      boolean isCaptureMasse = CAPTURE_MASSE_JN
            .equals(jobInstance.getJobName());

      return isCaptureMasse;
   }

   private boolean isLocal(JobInstance jobInstance) {

      String ecdeUrl = jobInstance.getJobParameters().getString(
            CAPTURE_MASSE_ECDE);

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

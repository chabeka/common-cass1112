package fr.urssaf.image.sae.services.document.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.ecde.service.EcdeServices;
import fr.urssaf.image.sae.services.batch.BulkCaptureJob;
import fr.urssaf.image.sae.services.batch.BulkCaptureJobWrapper;
import fr.urssaf.image.sae.services.controles.SAEControlesCaptureService;
import fr.urssaf.image.sae.services.document.SAEBulkCaptureService;
import fr.urssaf.image.sae.services.exception.capture.CaptureBadEcdeUrlEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeUrlFileNotFoundEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeWriteFileEx;
import fr.urssaf.image.sae.services.exception.capture.ServerBusyEx;
import fr.urssaf.image.sae.storage.model.jmx.JmxIndicator;

/**
 * Fournit l'implémentation des services pour la capture.<BR />
 * 
 * @author rhofir.
 */
@Service
@Qualifier("saeBulkCaptureService")
public class SAEBulkCaptureServiceImpl implements SAEBulkCaptureService {
   private static final Logger LOG = LoggerFactory
         .getLogger(SAEBulkCaptureServiceImpl.class);
   private ThreadPoolTaskExecutor taskExecutor;
   private EcdeServices ecdeServices;
   private BulkCaptureJob bulkCaptureJob;
   private BulkCaptureJobWrapper jobWrapper;
  
   @Autowired
   @Qualifier("saeControlesCaptureService")
   private SAEControlesCaptureService controlesCaptureService;  

   /**
    * 
    * @param taskExecutor
    *           : un objet de type {@link ThreadPoolTaskExecutor}
    */
   public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
      this.taskExecutor = taskExecutor;
   }

   /**
    * 
    * @param taskExecutor
    *           : un objet de type {@link ThreadPoolTaskExecutor}
    */
   public ThreadPoolTaskExecutor getTaskExecutor() {
      return taskExecutor;
   }

   /**
    * {@inheritDoc} puisqu'on est dans une thread séparée
    * @throws CaptureEcdeWriteFileEx {@link CaptureEcdeWriteFileEx}
    * @throws CaptureEcdeUrlFileNotFoundEx {@link CaptureEcdeUrlFileNotFoundEx}
    * @throws CaptureBadEcdeUrlEx {@link CaptureBadEcdeUrlEx}
    */
   public final void bulkCapture(String urlEcde) throws CaptureBadEcdeUrlEx,
         CaptureEcdeUrlFileNotFoundEx, CaptureEcdeWriteFileEx {
      BulkCaptureJobWrapper bulkWrapper = new BulkCaptureJobWrapper(urlEcde,
            ecdeServices, bulkCaptureJob);
      bulkWrapper.setLogContexteUUID((String) MDC.get("log_contexte_uuid"));
      String prefixeTrc = "bulkCapture()";
      LOG.debug("{} - Début", prefixeTrc);
      jobWrapper = bulkWrapper;
      bulkWrapper.setIndicator(new JmxIndicator());
      // Ici on regarde si le thread a fini sa tâche avant d'en lancer une
      // autre
      checkBulkCaptureEcdeUrl(urlEcde);
      if (taskExecutor.getActiveCount() == 0) {
         LOG
               .debug(
                     "{} - Lancement du thread pour l'exécution de la capture de masse",
                     prefixeTrc);
         taskExecutor.execute(bulkWrapper);

      } else {
         // sinon on lève une exception
         throw new ServerBusyEx();
      }
      LOG.debug("{} - Sortie", prefixeTrc);
      // Fin des traces debug - sortie méthode
   }

   /**
    * Construit un objet de type {@link SAEBulkCaptureService}
    * 
    * @param ecdeServices
    *           : Le service Ecde
    * @param bulkCaptureJob
    *           : Le job de l'archivage en masse.
    * @param taskExecutor
    *           : Le pool de threads.
    */
   @Autowired
   public SAEBulkCaptureServiceImpl(
         @Qualifier("ecdeServices") final EcdeServices ecdeServices,
         @Qualifier("bulkCaptureJob") final BulkCaptureJob bulkCaptureJob,
         final ThreadPoolTaskExecutor taskExecutor) {
      this.ecdeServices = ecdeServices;
      this.bulkCaptureJob = bulkCaptureJob;
      this.taskExecutor = taskExecutor;

   }

   /**
    * {@inheritDoc}
    */
   public final JmxIndicator retrieveJmxSAEBulkCaptureIndicator() {
      return jobWrapper.retrieveJmxBulkCaptureJobWrapperIndicator();
   }

   /**
    * {@inheritDoc}
    */
   public boolean isActive() {
      return taskExecutor.getActiveCount() != 0;

   }

   @Override
   public void checkBulkCaptureEcdeUrl(String urlEcde)
         throws CaptureBadEcdeUrlEx, CaptureEcdeUrlFileNotFoundEx,
         CaptureEcdeWriteFileEx {
      controlesCaptureService.checkBulkCaptureEcdeUrl(urlEcde);
      
   }

}

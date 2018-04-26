package fr.urssaf.image.sae.webservice.client.demo.service;

import java.util.concurrent.ConcurrentLinkedQueue;

import me.prettyprint.hector.api.Keyspace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.webservice.client.demo.component.PropertiesLoader;
import fr.urssaf.image.sae.webservice.client.demo.service.multithreading.SupervisionPoolThreadExecutor;
import fr.urssaf.image.sae.webservice.client.demo.service.multithreading.SupervisionRunnable;


public class SupervisionJobService {

   private static final Logger LOG = LoggerFactory
         .getLogger(SupervisionJobService.class);

   private static final String TRC_SUPERVISION = "SupervisionJobService.main()";

   /**
    * Pool executor
    */
   private static SupervisionPoolThreadExecutor poolExecutor;

   public SupervisionJobService() {
   }

   /**
    * @param args
    */
   @SuppressWarnings("unchecked")
   public static void main(Object[] args) {
      ConcurrentLinkedQueue<String> listUUIDJobs;
      Keyspace keyspc;
      Object obj = args[0];
      Object obj2 = args[1];

      if (obj instanceof ConcurrentLinkedQueue<?>) {
         listUUIDJobs = (ConcurrentLinkedQueue<String>) obj;
      } else {
         LOG.debug("La liste d'UUID de job n'est pas une liste.");
         return;
      }

      if (obj2 instanceof Keyspace) {
         keyspc = (Keyspace) obj2;
      } else {
         LOG.debug("L'argument keyspace n'est pas valide.");
         return;
      }
      Long timeDelaySupervision = PropertiesLoader.getInstance()
            .getTimeDelaySupervision();
      poolExecutor = new SupervisionPoolThreadExecutor();
      SupervisionRunnable runnable = new SupervisionRunnable(listUUIDJobs,
            timeDelaySupervision, keyspc);

      poolExecutor.execute(runnable);

      LOG.debug("{} - nombre de documents en attente dans le pool : {}",
            TRC_SUPERVISION, poolExecutor.getQueue().size());

   }

   public static void waitFinish() {
      if (poolExecutor != null) {
         poolExecutor.waitFinishSupervision();
      }
   }

}

package fr.urssaf.image.parser_opencsv.application.service.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import fr.urssaf.image.parser_opencsv.application.JobLauncher;

@Service
public class AsynchronousService {

   @Autowired
   @Qualifier("bnd_thread_pool")
   private ThreadPoolTaskExecutor taskExecutor;

   @Autowired
   private ApplicationContext applicationContext;

   private int nbreDeWorker;

   private static final Logger LOGGER = LoggerFactory.getLogger(AsynchronousService.class);

   /**
    * Execute Async Task
    * 
    * @param runnable
    * @return
    */
   @Async
   public Future<?> executeAsync(final Callable<?> runnable) {
      return taskExecutor.submit(runnable);
   }

   @Async
   public void executeAsyncTask(final Runnable runnable) {
      taskExecutor.execute(runnable);
   }

   /**
    * Permet d'arrêter le pool de Threads si tous les traitements concurrents sont terminés
    * Aussi de fermer le context
    */
   public void stopPool() {
      if (nbreDeWorker == JobLauncher.nombreTraitementsTerminee.incrementAndGet()) {
         LOGGER.info("Tous les Traitement sont terminés, arrêter le Pool de Threads");
         taskExecutor.shutdown();
         final GenericApplicationContext context = (GenericApplicationContext) applicationContext;
         context.close();
         context.registerShutdownHook();
      }
   }

   /**
    * @return the nbreDeWorker
    */
   public int getNbreDeWorker() {
      return nbreDeWorker;
   }

   /**
    * @param nbreDeWorker
    *           the nbreDeWorker to set
    */
   public void setNbreDeWorker(final int nbreDeWorker) {
      this.nbreDeWorker = nbreDeWorker;
   }

}

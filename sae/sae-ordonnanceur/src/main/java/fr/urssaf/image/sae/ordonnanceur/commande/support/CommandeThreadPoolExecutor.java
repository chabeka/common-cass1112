package fr.urssaf.image.sae.ordonnanceur.commande.support;

import java.util.concurrent.Callable;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.ordonnanceur.commande.LancementTraitement;
import fr.urssaf.image.sae.ordonnanceur.model.OrdonnanceurConfiguration;
import fr.urssaf.image.sae.ordonnanceur.util.RandomUtils;

/**
 * Thread Pool pour exécuter les commandes de l'ordonnanceur.<br>
 * <br>
 * Il est possible d'exécuter une commande en appelant les méthodes :
 * <ul>
 * <li><code>execute(Runnable runnable)</code></li>
 * <li><code>schedule(Callable callable, long delay, TimeUnit unit)</code></li>
 * <li><code>schedule(Runnable runnable, long delay, TimeUnit unit)</code></li>
 * <li>
 * <code>scheduleAtFixedRate(Runnable command , long initialDelay, long delay, TimeUnit unit)</code>
 * </li>
 * <li>
 * <code>scheduleWithFixedDelay(Runnable command, long initialDelay, long period, TimeUnit unit)</code>
 * </li>
 * </ul>
 * 
 * Seules les traitements de type {@link LancementTraitement} peuvent être
 * exécutés
 * 
 */
@Component
public class CommandeThreadPoolExecutor extends ScheduledThreadPoolExecutor {

   /**
    * Le nombre de commandes exécutables en parallèle
    */
   protected static final int CORE_POOL_SIZE = 10;

   private static final Logger LOG = LoggerFactory
         .getLogger(CommandeThreadPoolExecutor.class);

   private static final String PREFIX_LOG = "ordonnanceur()";

   private final int intervalle;

   private final ApplicationContext context;

  final Lock lock = new ReentrantLock();

  final Condition waitCondition = lock.newCondition();

   /**
    * Le nombre de commande exécutable en parallèle est fixé à
    * {@value #CORE_POOL_SIZE}
    * 
    * @param configuration
    *           configuration de l'ordonnanceur
    * @param context
    *           contexte de l'application
    */
   @Autowired
   public CommandeThreadPoolExecutor(OrdonnanceurConfiguration configuration,
         ApplicationContext context) {
      super(CORE_POOL_SIZE);

      Assert.notNull(configuration, "'configuration' is required");
      Assert.notNull(context, "'context' is required");

      this.intervalle = configuration.getIntervalle();
      this.context = context;

      Assert.state(intervalle >= 1,
            "'intervalle' must be greater than or equal to 1.");
   }

   /**
    * Surcharge de la méthode {@link ScheduledThreadPoolExecutor#terminated()}<br>
    * Il s'agit d'arrêter proprement les traitements en cours.
    */
   @Override
  protected final void terminated() {
    lock.lock();
    super.terminated();
    try {
      waitCondition.signalAll();
    }
    finally {
      lock.unlock();
    }
  }

   /**
    * Surcharge de la méthode {@link ScheduledThreadPoolExecutor#shutdown()}<br>
    * Il s'agit d'arrêter proprement les traitements en cours.
    */
   @Override
   public final void shutdown() {
      LOG.debug("{} - le pool des commandes est en train de s'arrêter",
            "shutdown()");

      // on vide la file d'attente
      super.getQueue().clear();
      super.shutdown();

      LOG.debug("{} - le pool des commandes est à l'état shut down",
            "shutdown()");

   }

   /**
    * Méthode pour attendre la fin de l'exécution du traitement en cours
    */
  public final void waitFinish() {
    while (!this.isTerminated()) {
      lock.lock();
      try {
        waitCondition.await();
      }
      catch (InterruptedException e) {

        throw new IllegalStateException(e);
      }
      finally {
        lock.unlock();
      }
    }

    LOG.debug("{} - le pool des commandes est à l'état terminé",
              "waitFinish()");
  }

   /**
    * Surcharge de la méthode
    * {@link ScheduledThreadPoolExecutor#decorateTask(Runnable, RunnableScheduledFuture)}
    * <br>
    * Il s'agit de vérifier que seules des traitements de type
    * {@link LancementTraitement} sont exécutés dans la pool de Threads
    */
   @Override
   protected final <V> RunnableScheduledFuture<V> decorateTask(
         Runnable runnable, RunnableScheduledFuture<V> task) {

      if (runnable instanceof LancementTraitement) {

         return super.decorateTask(runnable, task);

      } else {

         throw new IllegalArgumentException(
               "Impossible d'exécuter autre chose que des traitements pour le lancement des traitements de masse");
      }
   }

   /**
    * Surcharge de la méthode
    * {@link ScheduledThreadPoolExecutor#decorateTask(Callable, RunnableScheduledFuture)}
    * <br>
    * Il s'agit de vérifier que seules des traitements de type
    * {@link LancementTraitement} sont exécutés dans la pool de Threads
    */
   @Override
   protected final <V> RunnableScheduledFuture<V> decorateTask(
         Callable<V> callable, RunnableScheduledFuture<V> task) {

      if (callable instanceof LancementTraitement) {

         return super.decorateTask(callable, task);

      } else {

         throw new IllegalArgumentException(
               "Impossible d'exécuter autre chose que des traitements pour le lancement des traitements de masse");
      }
   }

   /**
    * Surcharge de la méthode
    * {@link ScheduledThreadPoolExecutor#afterExecute(Runnable,Throwable)}<br>
    * Il s'agit d'effectuer des traitements à la fin de l'exécution d'un
    * traitement de l'ordonnanceur.<br>
    * <br>
    * exemple : relancer le traitement de lancement des traitements de masse
    */
   @Override
   protected final void afterExecute(Runnable runnable, Throwable throwable) {

      super.afterExecute(runnable, throwable);

      if (!this.isShutdown()) {

         // si le pool n'est pas en train de s'arrêter on programme un
         // nouveau traitement
         // le lancement du traitement est ici aléatoire
         int min = intervalle;
         int max = min * 2;
         int waitTime = RandomUtils.random(min, max);

         LOG
               .debug(
                     "{} - prochaine tentative de lancement d'un traitement dans {} secondes",
                     PREFIX_LOG, waitTime);

         LancementTraitement traitement = new LancementTraitement(context);

         this.schedule(traitement, waitTime, TimeUnit.SECONDS);

      }

   }

}

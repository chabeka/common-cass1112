package fr.urssaf.image.sae.ordonnanceur.commande.support;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ordonnanceur.commande.LancementTraitementMock;
import fr.urssaf.image.sae.ordonnanceur.model.OrdonnanceurConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-ordonnanceur-commande-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class CommandeThreadPoolExecutorTest {

   private CommandeThreadPoolExecutor poolExecutor;

   private LancementTraitementMock command;

   @Autowired
   private ApplicationContext context;

   @Before
   public void before() {

      OrdonnanceurConfiguration configuration = new OrdonnanceurConfiguration();
      configuration.setIntervalle(10);

      poolExecutor = new CommandeThreadPoolExecutor(configuration, context);

      command = new LancementTraitementMock(context);
   }

   @Test
   public void execute_success_shutdown_sans_commande() {

      poolExecutor.execute(command);

      // on attend qu'il n'y ait plus tâche dans la queue
      synchronized (poolExecutor) {

         while (poolExecutor.getCompletedTaskCount() != 1) {

            try {

               poolExecutor.wait(1000);

            } catch (InterruptedException e) {

               throw new IllegalStateException(e);
            }
         }
      }

      Assert.assertEquals("Il ne doit avoir aucun traitement en cours", 0,
            poolExecutor.getActiveCount());

      poolExecutor.shutdown();
      
      poolExecutor.waitFinish();
   }

   @Test
   public void execute_failure_noLancementTraitement_runnable() {

      Runnable command = new Runnable() {

         @Override
         public void run() {

            // pas d'implémentation
         }

      };

      try {
         poolExecutor.execute(command);

         Assert
               .fail("la méthode execute doit lever une IllegalArgumentException");
      } catch (IllegalArgumentException e) {

         Assert
               .assertEquals(
                     "le message de l'exception est inattendu",
                     "Impossible d'exécuter autre chose que des traitements pour le lancement des traitements de masse",
                     e.getMessage());
      }

   }

   @Test
   public void execute_failure_noLancementTraitement_callable() {

      Callable<String> command = new Callable<String>() {

         @Override
         public String call() {

            return null;
         }

      };

      try {
         poolExecutor.schedule(command, 0, TimeUnit.SECONDS);

         Assert
               .fail("la méthode execute doit lever une IllegalArgumentException");
      } catch (IllegalArgumentException e) {

         Assert
               .assertEquals(
                     "le message de l'exception est inattendu",
                     "Impossible d'exécuter autre chose que des traitements pour le lancement des traitements de masse",
                     e.getMessage());
      }

   }

}

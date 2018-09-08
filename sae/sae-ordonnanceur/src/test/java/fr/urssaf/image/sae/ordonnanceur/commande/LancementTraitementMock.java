package fr.urssaf.image.sae.ordonnanceur.commande;

import java.util.UUID;

import org.springframework.context.ApplicationContext;

/**
 * Mock d'un traitement de masse
 * 
 * 
 */
public class LancementTraitementMock extends LancementTraitement implements
      Runnable {

   /**
    * 
    * @param context
    *           context de l'application
    */
   public LancementTraitementMock(ApplicationContext context) {
      super(context);
   }

   private static final long TIME = 1000;

   @Override
   public final void run() {

      try {
         Thread.sleep(TIME);
      } catch (InterruptedException e) {

         throw new IllegalStateException(e);
      }

   }

   @Override
   public final UUID call() {

      run();

      return UUID.randomUUID();
   }

}

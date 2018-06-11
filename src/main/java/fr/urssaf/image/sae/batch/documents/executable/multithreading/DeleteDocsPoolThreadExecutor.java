package fr.urssaf.image.sae.batch.documents.executable.multithreading;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import fr.urssaf.image.sae.batch.documents.executable.model.DeleteDocsParametres;

/**
 * Objet permettant de réaliser des exécutions parallèles de suppression de documents
 */
public class DeleteDocsPoolThreadExecutor extends ThreadPoolExecutor {

   /**
    * Logger de la classe.
    */
   private static final Logger LOGGER = LoggerFactory
         .getLogger(DeleteDocsPoolThreadExecutor.class);

   /**
    * Nombre de documents traités.
    */
   private int nombreTraites;

   /**
    * Pas d'exécution (nombre de documents à traités pour avoir une trace
    * applicative).
    */
   private int pasExecution;

   /**
    * Construteur.
    * 
    * @param parametres
    *           parametres
    */
   public DeleteDocsPoolThreadExecutor(final DeleteDocsParametres parametres) {
      super(parametres.getTaillePool(), parametres.getTaillePool(), 1,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(parametres
                  .getTailleQueue()), new DiscardPolicy());
      setPasExecution(parametres.getTaillePasExecution());
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void afterExecute(final Runnable runnable,
         final Throwable throwable) {
      super.afterExecute(runnable, throwable);
        
      //-- On incrémenter le compteur d’éléments traités
      nombreTraites++;
      
      if (getNombreTraites() % getPasExecution() == 0) {
         LOGGER.info("{} éléments traités", getNombreTraites());
      }
   }

   /**
    * Attend que l'ensemble des threads aient bien terminé leur travail.
    */
   public final void waitFinishDelete() {

      synchronized (this) {
         while (!this.isTerminated()) {
            try {
               this.wait();
            } catch (InterruptedException e) {
               throw new IllegalStateException(e);
            }
         }
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void terminated() {
      super.terminated();
      synchronized (this) {
         this.notifyAll();
      }
   }

   /**
    * Permet de récupérer le nombre de documents traités.
    * 
    * @return int
    */
   public final int getNombreTraites() {
      return nombreTraites;
   }

   /**
    * Permet de modifier le nombre de documents traités.
    * 
    * @param nombreTraites
    *           nombre de documents traités
    */
   public final void setNombreTraites(final int nombreTraites) {
      this.nombreTraites = nombreTraites;
   }

   /**
    * Permet de récupérer le pas d'exécution (nombre de documents à traités pour
    * avoir une trace applicative).
    * 
    * @return int
    */
   public final int getPasExecution() {
      return pasExecution;
   }

   /**
    * Permet de modifier le pas d'exécution (nombre de documents à traités pour
    * avoir une trace applicative).
    * 
    * @param pasExecution
    *           pas d'exécution (nombre de documents à traités pour avoir une
    *           trace applicative)
    */
   public final void setPasExecution(final int pasExecution) {
      this.pasExecution = pasExecution;
   }
}

package fr.urssaf.image.sae.documents.executable.multithreading;

import java.io.File;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.documents.executable.model.FormatValidationParametres;
import fr.urssaf.image.sae.documents.executable.utils.MetadataUtils;

/**
 * Objet permettant de réaliser des exécutions parallèles de validation de
 * format de fichier.
 */
public class FormatValidationPoolThreadExecutor extends ThreadPoolExecutor {

   /**
    * Logger de la classe.
    */
   private static final Logger LOGGER = LoggerFactory
         .getLogger(FormatValidationPoolThreadExecutor.class);

   /**
    * Liste des métadonnées à consulter.
    */
   private final List<String> metadonnees;

   /**
    * Nombre de documents traités en erreur.
    */
   private int nombreDocsErreur;

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
   public FormatValidationPoolThreadExecutor(
         final FormatValidationParametres parametres) {
      super(parametres.getTaillePool(), parametres.getTaillePool(), 1,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(parametres
                  .getTailleQueue()), new DiscardPolicy());
      setPasExecution(parametres.getTaillePasExecution());
      this.metadonnees = parametres.getMetadonnees();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void afterExecute(final Runnable runnable,
         final Throwable throwable) {
      super.afterExecute(runnable, throwable);
      
      synchronized (this) {
         final FormatRunnable formatRunnable = (FormatRunnable) runnable;
         final String idDocument = formatRunnable.getDocument().getUuid()
               .toString();
         final String metaToLog = MetadataUtils.getMetadatasForLog(formatRunnable
               .getDocument(), metadonnees);
   
         if ((throwable == null) && (!formatRunnable.getResultat().isValid())) {
            nombreDocsErreur++;
            final String resultatDetail = formatResultatDetails(formatRunnable);
            LOGGER.warn("{} ; {} ; {} ; {}", new Object[] { "VALID", idDocument, metaToLog,
                  resultatDetail });
         } else if (throwable != null) {
            nombreDocsErreur++;
   
            LOGGER.error("{} ; {} ; {} ; {}", new Object[] { "VALID", idDocument, metaToLog,
                  throwable.getMessage() });
         }
         nombreTraites++;
         if (getNombreTraites() % getPasExecution() == 0) {
            LOGGER.info("{} documents validés", getNombreTraites());
         }
   
         // supprime le fichier temporaire
         if (formatRunnable.getFile() != null) {
            File file = formatRunnable.getFile();
            LOGGER.debug("Suppression du fichier temporaire {}", file
                  .getAbsolutePath());
            if (!file.delete()) {
               LOGGER.error("Impossible de supprimer le fichier temporaire {}",
                     file.getAbsolutePath());
            }
         }
      }
   }

   /**
    * Methode permettant de formatter le détail du résultat.
    * 
    * @param formatRunnable
    *           runnable
    * @return String
    */
   private String formatResultatDetails(final FormatRunnable formatRunnable) {
      final StringBuffer buffer = new StringBuffer();
      boolean first = true;
      for (String detail : formatRunnable.getResultat().getDetails()) {
         if (!first) {
            buffer.append(", ");
         }
         buffer.append(detail);
         first = false;
      }
      return buffer.toString();
   }

   /**
    * Attend que l'ensemble des threads aient bien terminé leur travail.
    */
   public final void waitFinishValidation() {

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
    * Permet de récupérer le nombre de documents traités en erreur.
    * 
    * @return int
    */
   public final int getNombreDocsErreur() {
      return nombreDocsErreur;
   }

   /**
    * Permet de modifier le nombre de documents traités en erreur.
    * 
    * @param nombreDocsErreur
    *           nombre de documents traités en erreur
    */
   public final void setNombreDocsErreur(final int nombreDocsErreur) {
      this.nombreDocsErreur = nombreDocsErreur;
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

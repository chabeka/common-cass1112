/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.batch;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.springframework.batch.core.ExitStatus;

import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.listener.AbstractListener;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.storage.model.storagedocument.AbstractStorageDocument;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

/**
 * Classe mère implémentant les méthodes :
 * <ul>
 * <li>@beforeStep : ouverture de la connexion DFCE</li>
 * <li>@afterStep : fermeture de la connexion DFCE</li>
 * </ul>
 * 
 */
public abstract class AbstractDocumentWriterListener extends AbstractListener {

   /**
    * {@inheritDoc}
    * <ul>
    * <li>ouverture de la connexion à DFCE</li>
    * </ul>
    */
   @Override
   protected void specificInitOperations() {

      String trcPrefix = "specificInitOperations()";

      try {
         getServiceProvider().openConnexion();

         /* nous sommes obligés de récupérer les throwable pour les erreurs DFCE */
      } catch (Throwable e) {
         getLogger().warn("{} - erreur de connexion à DFCE", trcPrefix, e);

         getCodesErreurListe().add(Constantes.ERR_BUL001);
         getIndexErreurListe().add(0);
         getExceptionErreurListe().add(new Exception(e.getMessage()));

         getStepExecution().setExitStatus(new ExitStatus("FAILED_NO_ROLLBACK"));

         throw new CaptureMasseRuntimeException(e);
      }

      getLogger().debug("{} - ouverture de la connexion DFCE", trcPrefix);
   }

   /**
    * {@inheritDoc}
    * <ul>
    * <li></li>
    * </ul>
    */
   @Override
   protected ExitStatus specificAfterStepOperations() {
      // pour l'instant nous avons fait le choix de propager l'erreur
      // pour ne pas la cacher et attérir dans un état en erreur

      String trcPrefix = "specificAfterStepOperations()";
      ExitStatus exitStatus = getStepExecution().getExitStatus();

      try {
         getServiceProvider().closeConnexion();

         /* nous sommes obligés de récupérer les throwable pour les erreurs DFCE */
      } catch (Throwable e) {
         getLogger().warn(
               "{} - erreur lors de la fermeture de la base de données",
               trcPrefix, e);

         getCodesErreurListe().add(Constantes.ERR_BUL001);
         getIndexErreurListe().add(0);
         getExceptionErreurListe().add(new Exception(e.getMessage()));

         if (!isModePartielBatch()) {
            exitStatus = ExitStatus.FAILED;
         }
      }

      getLogger().debug("{} - fermeture de la connexion DFCE", trcPrefix);

      if (isModePartielBatch()) {
         // En mode PARTIEL, on regarde s'il y a une erreur déclarée dans la
         // liste des erreurs. Si c'est le cas, on est en echec et non en
         // success. Il faut donc remonter cet état.
         if (!getIndexErreurListe().isEmpty()) {
            exitStatus = ExitStatus.FAILED;
         }
      }

      return exitStatus;
   }

   /**
    * Vérifie que le document est dans liste des document en erreur ou déjà
    * traité (Reprise). Renvoi True si c'est le cas, false sinon.
    * 
    * @param index
    *           index du document traité
    * @return True si le document est dans liste des document en erreur ou déjà
    *         traité (Reprise), false sinon.
    */
   protected boolean isDocumentATraite(int index) {
      boolean isdocumentATraite = true;
      if (isModePartielBatch() || isRepriseActifBatch()) {
         isdocumentATraite = isDocumentATraiteByListIndex(
               getIndexErreurListe(), index)
               && isDocumentATraiteByListIndex(getIndexRepriseDoneListe(),
                     index);
      }

      return isdocumentATraite;
   }

   /**
    * Vérifie que le document a déjà été traité par le traitement nominal lors
    * de la reprise.
    * 
    * @param index
    *           index du document
    * @return True si le document a déjà été traité, false sinon.
    */
   protected boolean isDocumentDejaTraite(int index) {
      boolean isdocumentDejaTraite = false;
      if (isRepriseActifBatch()) {
         isdocumentDejaTraite = !isDocumentATraiteByListIndex(
               getIndexRepriseDoneListe(), index);
      }

      return isdocumentDejaTraite;
   }

   /**
    * Méthode permettant de voir si un index d'un document est présent dans une
    * liste d'index de type {@link ConcurrentLinkedQueue}
    * 
    * @param indexListe
    *           liste d'index
    * @param index
    *           index à trouver
    * @return True si l'index du document est present dans la liste, false
    *         sinon.
    */
   private boolean isDocumentATraiteByListIndex(
         ConcurrentLinkedQueue<Integer> indexListe, int index) {
      // En mode PARTIEL, on regarde s'il y a une erreur de déclarer pour
      // l'item. Si c'est le cas, on ne le traite pas.
      for (Integer indexDocError : indexListe) {
         if (indexDocError == (getStepExecution().getReadCount() + index)) {
            return false;
         }
      }
      return true;
   }

   /**
    * Lancement du traitement du service concerné.
    * @param storageDocument Document à traiter.
    * @return l'identifiant du document traité.
    */
   public abstract UUID launchTraitement(final AbstractStorageDocument storageDocument, final int index) throws Exception;

   
   /**
    * @return le serviceProvider
    */
   protected abstract StorageServiceProvider getServiceProvider();

   /**
    * @return le logger
    */
   protected abstract Logger getLogger();

}

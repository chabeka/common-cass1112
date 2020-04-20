/**
 *
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.batch;

import java.util.UUID;

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

      final String trcPrefix = "specificInitOperations()";

      try {
         getServiceProvider().openConnexion();

         /* nous sommes obligés de récupérer les throwable pour les erreurs DFCE */
      }
      catch (final Exception e) {
         getLogger().warn("{} - erreur de connexion à DFCE", trcPrefix, e);
         getCodesErreurListe().add(Constantes.ERR_BUL001);
         getIndexErreurListe().add(0);
         getErrorMessageList().add(e.getMessage());
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
      // pour ne pas la cacher et atterrir dans un état en erreur

      final String trcPrefix = "specificAfterStepOperations()";
      ExitStatus exitStatus = getStepExecution().getExitStatus();

      try {
         getServiceProvider().closeConnexion();

         /* nous sommes obligés de récupérer les throwable pour les erreurs DFCE */
      }
      catch (final Exception e) {
         getLogger().warn(
               "{} - erreur lors de la fermeture de la base de données",
               trcPrefix,
               e);
         getCodesErreurListe().add(Constantes.ERR_BUL001);
         getIndexErreurListe().add(0);
         getErrorMessageList().add(e.getMessage());

         if (!isModePartielBatch()) {
            exitStatus = ExitStatus.FAILED;
         }
      }

      getLogger().debug("{} - fermeture de la connexion DFCE", trcPrefix);

      if (isModePartielBatch()) {
         // En mode PARTIEL, on regarde s'il y a une erreur déclarée dans la
         // liste des erreurs. Si c'est le cas, on est en échec et non en
         // success. Il faut donc remonter cet état.
         if (!getIndexErreurListe().isEmpty()) {
            exitStatus = ExitStatus.FAILED;
         }
      }

      return exitStatus;
   }

   /**
    * Regarde si le document est à traiter.
    * Il est à traiter s'il n'est pas dans la liste des éléments déjà traités (utilisée en mode reprise)
    * et s'il n'est pas dans la liste des documents en erreur
    *
    * @param docIndex
    *           index du document dans le sommaire
    * @return True est à traiter, false sinon
    */
   protected boolean isDocumentATraite(final int docIndex) {
      boolean isdocumentATraite = true;
      if (isModePartielBatch() || isRepriseActifBatch()) {
         isdocumentATraite = !getIndexErreurListe().contains(docIndex) && !getIndexRepriseDoneListe().contains(docIndex);
      }

      return isdocumentATraite;
   }

   /**
    * Vérifie que le document a déjà été traité par le traitement nominal lors
    * de la reprise.
    *
    * @param docIndex
    *           index du document dans le sommaire
    * @return True si le document a déjà été traité, false sinon.
    */
   protected boolean isDocumentDejaTraite(final int docIndex) {
      boolean isdocumentDejaTraite = false;
      if (isRepriseActifBatch()) {
         isdocumentDejaTraite = getIndexRepriseDoneListe().contains(docIndex);
      }

      return isdocumentDejaTraite;
   }

   /**
    * Lancement du traitement du service concerné.
    * 
    * @param storageDocument
    *           Document à traiter.
    * @param docIndex
    *           index du document dans le sommaire
    * @return l'identifiant du document traité.
    */
   public abstract UUID launchTraitement(final AbstractStorageDocument storageDocument, final int docIndex) throws Exception;

   /**
    * @return le serviceProvider
    */
   protected abstract StorageServiceProvider getServiceProvider();

   /**
    * @return le logger
    */
   protected abstract Logger getLogger();

}

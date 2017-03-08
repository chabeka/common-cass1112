/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.batch;

import org.slf4j.Logger;
import org.springframework.batch.core.ExitStatus;

import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.listener.AbstractListener;
import fr.urssaf.image.sae.services.batch.common.Constantes;
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
   protected final void specificInitOperations() {

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
   protected final ExitStatus specificAfterStepOperations() {
      // pour l'instant nous avons fait le choix de propager l'erreur
      // pour ne pas la cacher et attérir dans un état en erreur

      String trcPrefix = "afterStep()";
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

         exitStatus = ExitStatus.FAILED;
      }

      return exitStatus;
   }

   /**
    * @return le serviceProvider
    */
   protected abstract StorageServiceProvider getServiceProvider();

   /**
    * @return le logger
    */
   protected abstract Logger getLogger();

}
